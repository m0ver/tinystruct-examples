package tinystruct.examples;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.tinystruct.AbstractApplication;

public class time extends AbstractApplication {

	private static boolean stop;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		this.setAction("time", "index");
		this.setAction("time/start", "start");
		this.setAction("time/stop", "stop");
	}

	public time index() {
		return this;
	}

	public void start() throws IOException, InterruptedException {

		HttpServletResponse response = (HttpServletResponse) this.context
				.getAttribute("HTTP_RESPONSE");
			stop = false;
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
