package tinystruct.examples;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.tinystruct.AbstractApplication;
import org.tinystruct.system.annotation.Action;

public class time extends AbstractApplication {

	private static boolean stop;

	@Override
	public void init() {
		// TODO Auto-generated method stub
	}

	@Action("time")
	public time index() {
		return this;
	}

	@Action("time/start")
	public void start() {
		stop=false;
	}

	@Action("time/update")
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

	@Action("time/stop")
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
