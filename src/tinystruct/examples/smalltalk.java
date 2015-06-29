package tinystruct.examples;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tinystruct.AbstractApplication;
import org.tinystruct.ApplicationException;
import org.tinystruct.data.component.Builder;
import org.tinystruct.system.util.StringUtilities;

public class smalltalk extends AbstractApplication {

	private final Map<String, Object> map = Collections.synchronizedMap(new HashMap<String, Object>());
	
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

	public void update() throws ApplicationException {
		HttpServletResponse response = (HttpServletResponse) this.context.getAttribute("HTTP_RESPONSE");

		synchronized(this.map) {
			try {
				this.map.wait();
			} catch (InterruptedException e) {
				throw new ApplicationException(e.getMessage(),e);
			}
			
			if(this.map.containsKey("textvalue")) {
				System.out.println(this.getVariable("browser").getValue().toString()+":"+this.map.get("textvalue"));
				try {
					response.getWriter().println(new StringUtilities(this.map.get("textvalue").toString().trim()).replace('\n', ""));
					response.getWriter().flush();
					response.getWriter().close();
				} catch (IOException e) {
					throw new ApplicationException(e.getMessage(),e);
				}
			}
			
			if(this.map.containsKey("command")) {
				System.out.println(this.map.get("command"));
				try {
					response.getWriter().println(new StringUtilities(this.map.get("command").toString().trim()).replace('\n', ""));
					response.getWriter().flush();
					response.getWriter().close();
				} catch (IOException e) {
					throw new ApplicationException(e.getMessage(),e);
				}
			}
		}
	}

	public boolean save() {
		HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
		SimpleDateFormat format = new SimpleDateFormat("Y-M-d h:m:s");
		
		synchronized(this.map){
			String[] agent = request.getHeader("User-Agent").split(" ");
			this.setVariable("browser", agent[agent.length-1]);
			if(this.map.containsKey("textvalue")) this.map.remove("textvalue");
			
			Builder builder = new Builder();
			builder.put("user", request.getSession().getAttribute("user"));
			builder.put("time", format.format(new Date()));
			builder.put("message", request.getParameter("text"));
			
			this.map.put("textvalue", builder);
			this.map.notifyAll();
			return true;
		}
	}
	
	public boolean command() {
		HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
		
		synchronized(this.map){
			if(this.map.containsKey("command")) this.map.remove("command");
			if(this.map.containsKey("textvalue")) this.map.remove("textvalue");
			
			Builder builder = new Builder();
			builder.put("user", request.getSession().getAttribute("user"));
			builder.put("cmd", request.getParameter("cmd"));
			
			this.map.put("command", builder);
			this.map.notifyAll();
			return true;
		}
	}

	@Override
	public String version() {
		// TODO Auto-generated method stub
		return "Welcome to use tinystruct 2.0";
	}

}
