package tinystruct.examples;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tinystruct.AbstractApplication;
import org.tinystruct.ApplicationException;
import org.tinystruct.data.component.Builder;
import org.tinystruct.handle.Reforward;
import org.tinystruct.system.util.StringUtilities;

public class smalltalk extends AbstractApplication {

	private final Map<String, Queue<Builder>> map = Collections.synchronizedMap(new HashMap<String, Queue<Builder>>());
	private Queue<Builder> list;
	
	@Override
	public void init() {
		this.setAction("talk", "index");
		this.setAction("talk/join", "join");
		this.setAction("talk/start", "start");
		this.setAction("talk/update", "update");
		this.setAction("talk/save", "save");
		this.setAction("talk/command", "command");
		this.setAction("talk/version", "version");
		
		this.setVariable("message","");
	}
	
	public String index(){
		HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
		if(request.getSession().getAttribute("meeting_code")==null) {
			request.getSession(true).setAttribute("meeting_code", java.util.UUID.randomUUID());
			
			String key = request.getSession(true).getAttribute("meeting_code").toString();
			this.list = new ConcurrentLinkedQueue<Builder>();
			this.map.put(key, this.list); 
			
			System.out.println("New meeting generated:" + key);
		}

		this.setVariable("meeting_code", request.getSession(true).getAttribute("meeting_code").toString());
		
		return "Please start the conversation with your name: " + this.config.get("default.base_url") + "talk/start/+YOUR-NAME";
	}
	
	public String join(String meeting_code){
		
		if(map.containsKey(meeting_code)) {
			HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
			request.getSession(true).setAttribute("meeting_code", meeting_code);
			
			this.setVariable("meeting_code", meeting_code);
		}
		else {
			return "Invalid meeting code.";
		}
		
		return "Please start the conversation with your name: " + this.config.get("default.base_url") + "talk/start/+YOUR-NAME";
	}
	
	public smalltalk start(String name) throws ApplicationException{
		HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
		HttpServletResponse response = (HttpServletResponse) this.context.getAttribute("HTTP_RESPONSE");

		if(request.getSession().getAttribute("meeting_code")==null) {
			Reforward reforward = new Reforward(request, response);
			reforward.setDefault("/?q=talk");
			reforward.forward();
		}
		
		request.getSession(true).setAttribute("user", name);
		
		return this;
	}
	
	public String update() throws ApplicationException {
		HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
		if(request.getSession().getAttribute("meeting_code")!=null) {
			this.list = map.get(request.getSession(true).getAttribute("meeting_code").toString());
			
			synchronized(this.list) {
				try {
					this.list.wait();
				} catch (InterruptedException e) {
					throw new ApplicationException(e.getMessage(),e);
				}
				
				if(!this.list.isEmpty()) {
					String message = this.list.peek().toString();
					System.out.println(message);
					return new StringUtilities(message.trim()).replace('\n', "");
				}
				
			}
		}
		
		return "";
	}

	public boolean save() {
		HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d h:m:s");
		if(request.getSession().getAttribute("meeting_code")!=null) {
			this.list = map.get(request.getSession(true).getAttribute("meeting_code").toString());
			
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
		
		return false;
	}
	
	public boolean command() {
		HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
		if(request.getSession().getAttribute("meeting_code")!=null) {
			this.list = map.get(request.getSession(true).getAttribute("meeting_code").toString());
			
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
		
		return false;
	}

	public smalltalk exit(String meeting_code){
		HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
		request.getSession(true).removeAttribute("meeting_code");
		
		return this;
	}
	
	@Override
	public String version() {
		return "Welcome to use tinystruct 2.0";
	}
	
	public static void main(String[] args) {
		final Map<Object, Queue<Builder>> map = Collections.synchronizedMap(new HashMap<Object, Queue<Builder>>());
		Queue<Builder> list = new ConcurrentLinkedQueue<Builder>();

		map.put("a", list);
		
		System.out.println(map.get("a").size());
		
		list.add(new Builder());
		
		System.out.println(map.get("a").size());
	}

}
