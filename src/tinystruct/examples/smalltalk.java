package tinystruct.examples;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tinystruct.AbstractApplication;
import org.tinystruct.system.util.StringUtilities;

public class smalltalk extends AbstractApplication {

	private final Map<String, String> map = Collections.synchronizedMap(new HashMap<String, String>());
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		this.setAction("talk", "index");
		this.setAction("talk/start", "start");
		this.setAction("talk/update", "update");
		this.setAction("talk/save", "save");
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

	public void update() throws InterruptedException, IOException {
		HttpServletResponse response = (HttpServletResponse) this.context.getAttribute("HTTP_RESPONSE");
		
		synchronized(this.map) {
			while (true) {
				this.map.wait();
				
				if(this.map.containsKey("textvalue")) {
					System.out.println(this.getVariable("browser").getValue().toString()+":"+this.map.get("textvalue"));
					response.getWriter().println("<script charset=\"utf-8\"> var message = '" + new StringUtilities(this.map.get("textvalue")).replace('\n', "\\n") + "';parent.update(message);</script>");
					response.getWriter().flush();
				}
			}
		}
	}

	public boolean save() {
		HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
		
		synchronized(this.map){
			String[] agent = request.getHeader("User-Agent").split(" ");
			this.setVariable("browser", agent[agent.length-1]);
			
			if(this.map.containsKey("textvalue")) this.map.remove("textvalue");
			this.map.put("textvalue", request.getSession().getAttribute("user") + ":" + request.getParameter("text"));
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
