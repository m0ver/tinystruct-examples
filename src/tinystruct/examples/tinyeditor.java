package tinystruct.examples;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tinystruct.AbstractApplication;
import org.tinystruct.system.util.StringUtilities;

public class tinyeditor extends AbstractApplication {

	private final Map<String, String> map = Collections.synchronizedMap(new HashMap<String, String>());
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		this.setAction("tinyeditor", "index");
		this.setAction("tinyeditor/update", "update");
		this.setAction("tinyeditor/save", "save");
		this.setAction("tinyeditor/version", "version");
	}
	
	public tinyeditor index(){
		return this;
	}

	public void update() throws InterruptedException, IOException {
		HttpServletResponse response = (HttpServletResponse) this.context
				.getAttribute("HTTP_RESPONSE");
		
		while (true) {
			
			synchronized(this.map) {
				this.map.wait();
				
				if(this.map.containsKey("textvalue")) {
					
					System.out.println(this.getVariable("browser").getValue().toString()+":"+this.map.get("textvalue"));
					response.getWriter().println(
							"<script charset=\"utf-8\"> var message = '" + new StringUtilities(this.map.get("textvalue")).replace('\n', "\\n")
									+ "';parent.update(message);</script>");
					response.getWriter().flush();
					
					this.map.remove("textvalue");
				}
			}
		}
	}

	public boolean save() {
		HttpServletRequest request = (HttpServletRequest) this.context
		.getAttribute("HTTP_REQUEST");
		
		synchronized(this.map){
			String[] agent = request.getHeader("User-Agent").split(" ");
			
			this.map.put("textvalue", request.getParameter("text"));
			this.setVariable("browser", agent[agent.length-1]);
			
			System.out.println("It's ready now!");
			this.map.notify();
		}
		return true;
	}

	@Override
	public String version() {
		// TODO Auto-generated method stub
		return "Welcome to use tinystruct 2.0";
	}

}
