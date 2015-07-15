package tinystruct.examples;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.http.HttpServletRequest;

import org.tinystruct.AbstractApplication;
import org.tinystruct.ApplicationException;
import org.tinystruct.data.component.Builder;
import org.tinystruct.system.util.StringUtilities;

public class smalltalk extends AbstractApplication {

	private final Queue<Builder> list = new ConcurrentLinkedQueue<Builder>();
	
	@Override
	public void init() {
		this.setAction("talk", "index");
		this.setAction("talk/start", "start");
		this.setAction("talk/update", "update");
		this.setAction("talk/save", "save");
		this.setAction("talk/command", "command");
		this.setAction("talk/version", "version");
	}
	
	public smalltalk index(){
		return this;
	}
	
	public smalltalk start(String name){
		HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
		request.getSession().setAttribute("user", name);
		
		return this;
	}
	
	public String update() throws ApplicationException {
		String message;
		synchronized(this.list) {
			try {
				this.list.wait();
			} catch (InterruptedException e) {
				throw new ApplicationException(e.getMessage(),e);
			}
			
			if(!this.list.isEmpty()) {
				message = this.list.peek().toString();
				System.out.println(message);
				return new StringUtilities(message.trim()).replace('\n', "");
			}
			
		}
		
		return "";
	}

	public boolean save() {
		HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d h:m:s");
		
		synchronized(this.list){
			String[] agent = request.getHeader("User-Agent").split(" ");
			this.setVariable("browser", agent[agent.length-1]);
			this.list.poll();
			
			Builder builder = new Builder();
			builder.put("user", request.getSession(true).getAttribute("user"));
			builder.put("time", format.format(new Date()));
			builder.put("message", request.getParameter("text"));
			
			this.list.add(builder);
			this.list.notifyAll();
			return true;
		}
	}
	
	public boolean command() {
		HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
		
		synchronized(this.list){
			this.list.poll();
			
			Builder builder = new Builder();
			builder.put("user", request.getSession(true).getAttribute("user"));
			builder.put("cmd", request.getParameter("cmd"));
			
			this.list.add(builder);
			this.list.notifyAll();
			return true;
		}
	}

	@Override
	public String version() {
		return "Welcome to use tinystruct 2.0";
	}

}
