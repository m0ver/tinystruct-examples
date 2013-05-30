package tinystruct.examples;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tinystruct.AbstractApplication;

public class tinyeditor extends AbstractApplication {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		this.setAction("tinyeditor", "index");
		this.setAction("tinyeditor/update", "update");
		this.setAction("tinyeditor/save", "save");
	}
	
	public tinyeditor index(){
		return this;
	}

	public void update() throws InterruptedException, IOException {
		HttpServletResponse response = (HttpServletResponse) this.context
				.getAttribute("HTTP_RESPONSE");
		while (true) {
			
			if(this.getVariable("textvalue")!=null) {
				response.getWriter().println(
						"<script> parent.update('" + this.getVariable("textvalue").getValue()
								+ "');</script>");
				response.getWriter().flush();
			}

				Thread.sleep(1000);
		}
	}

	public boolean save() {
		HttpServletRequest request = (HttpServletRequest) this.context
		.getAttribute("HTTP_REQUEST");
		
		this.setVariable("textvalue", request.getParameter("text"));
		
		return true;
	}

	@Override
	public String version() {
		// TODO Auto-generated method stub
		return null;
	}

}