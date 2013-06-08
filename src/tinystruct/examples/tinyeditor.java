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

	Map<String, String> map = Collections.synchronizedMap(new HashMap<String, String>());
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		this.setAction("tinyeditor", "index");
		this.setAction("tinyeditor/update", "update");
		this.setAction("tinyeditor/save", "save");
		
		System.out.println("Tinyeditor:Thread["+Thread.currentThread().getId()+"]"+Thread.currentThread().getName());
	}
	
	public tinyeditor index(){
		return this;
	}

	public void update() throws InterruptedException, IOException {
		HttpServletResponse response = (HttpServletResponse) this.context
				.getAttribute("HTTP_RESPONSE");
		
		while (true) {
			
			if(this.map.containsKey("textvalue")) {
				response.getWriter().println(
						"<script charset=\"utf-8\"> var message = '" + new StringUtilities(this.map.get("textvalue")).replace('\n', "\\n")
								+ "';parent.update(message);</script>");
				response.getWriter().flush();
				
				this.map.remove("textvalue");
			}

			Thread.sleep(1000);
		}
	}

	public boolean save() {
		HttpServletRequest request = (HttpServletRequest) this.context
		.getAttribute("HTTP_REQUEST");
		
		this.map.put("textvalue", request.getParameter("text"));
		
		return true;
	}

	@Override
	public String version() {
		// TODO Auto-generated method stub
		return null;
	}

}