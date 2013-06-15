package tinystruct.examples;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tinystruct.AbstractApplication;
import org.tinystruct.handle.Report;
import org.tinystruct.system.util.StringUtilities;

public class tinyeditor extends AbstractApplication {

	Map<String, String> map = Collections.synchronizedMap(new HashMap<String, String>());
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		this.setAction("tinyeditor", "index");
		this.setAction("tinyeditor/update", "update");
		this.setAction("tinyeditor/save", "save");
		this.setAction("tinyeditor/version", "version");
		
		System.out.println("Tinyeditor:Thread["+Thread.currentThread().getId()+"]"+Thread.currentThread().getName());
	}
	
	public tinyeditor index(){
		return this;
	}

	public synchronized void update() throws InterruptedException, IOException {
		HttpServletResponse response = (HttpServletResponse) this.context
				.getAttribute("HTTP_RESPONSE");
		
		while (true) {
			
			wait();
			
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

	public synchronized boolean save() {
		HttpServletRequest request = (HttpServletRequest) this.context
		.getAttribute("HTTP_REQUEST");
		
		String[] agent = request.getHeader("User-Agent").split(" ");
		
		this.map.put("textvalue", request.getParameter("text"));
		this.setVariable("browser", agent[agent.length-1]);
		
		System.out.println("It's ready now!");
		notifyAll();
		return true;
	}

	@Override
	public String version() {
		// TODO Auto-generated method stub
		String message = "Welcome to use tinystruct 2.0";
		Report report = Report.getInstance();
		report.println(message);
		return message;
	}

}