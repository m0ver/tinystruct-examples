package tinystruct.examples;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.tinystruct.AbstractApplication;
import org.tinystruct.system.annotation.Action;

public class startup extends AbstractApplication {

	private static boolean stop;

	@Override
	public void init() {
		// TODO Auto-generated method stub
	}

	@Action("startup")
	public startup index() {
		return this;
	}
	
	public void start() {
		stop=false;
	}

	public void update() throws IOException, InterruptedException {

		HttpServletResponse response = (HttpServletResponse) getContext()
				.getAttribute("HTTP_RESPONSE");
		while(true)
			if (!stop) {
				response.getWriter().println(
						"<script> parent.update('" + new Date()
								+ "');</script>");
				response.getWriter().flush();

				Thread.sleep(1000);
			}else {
				break;
			}
	}

	public String stop() {
		stop = true;
		return "stopped!";
	}

	@Override
	public String version() {
		// TODO Auto-generated method stub
		return null;
	}

}
