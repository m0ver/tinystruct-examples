package tinystruct.examples;

import java.awt.image.BufferedImage;
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
import org.tinystruct.system.util.Matrix;

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
		this.setAction("talk/topic", "topic");
		this.setAction("talk/matrix", "matrix");
		this.setAction("talk/version", "version");
		
		this.setVariable("message","");
		this.setVariable("topic", "");
	}
	
	public smalltalk index() {
		HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
		Object code=request.getSession().getAttribute("meeting_code");

		if(code==null) {
			String key = java.util.UUID.randomUUID().toString();
			request.getSession(true).setAttribute("meeting_code", key);
			
			this.list = new ConcurrentLinkedQueue<Builder>();
			this.map.put(key, this.list); 
			this.setVariable("meeting_code", key);

			System.out.println("New meeting generated:" + key);
		}
		else {
			this.setVariable("meeting_code", code.toString());
			if(this.getVariable(code.toString())!=null) {
				this.setVariable("topic", this.getVariable(code.toString()).getValue().toString().replaceAll("[\r\n]", "<br />"), true);
			}
		}
		
		return this;
	}
	
	public String matrix() throws ApplicationException {
		HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");

		if(request.getParameter("meeting_code")!=null) {
			BufferedImage qrImage = Matrix.toQRImage(this.getLink("talk/join") +"/" + request.getParameter("meeting_code"), 100, 100);
			
			return "data:image/png;base64," + Matrix.getBase64Image(qrImage);
		}

		return "";
	}
	
	public String join(String meeting_code) throws ApplicationException{
		
		if(map.containsKey(meeting_code)) {
			HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
			HttpServletResponse response = (HttpServletResponse) this.context.getAttribute("HTTP_RESPONSE");

			request.getSession(true).setAttribute("meeting_code", meeting_code);
			
			this.setVariable("meeting_code", meeting_code);
			
			Reforward reforward = new Reforward(request, response);
			reforward.setDefault("/?q=talk");
			reforward.forward();
		}
		else {
			return "Invalid meeting code.";
		}
		
		return "Please start the conversation with your name: " + this.config.get("default.base_url") + "talk/start/YOUR NAME";
	}
	
	public String start(String name) throws ApplicationException{
		HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
		HttpServletResponse response = (HttpServletResponse) this.context.getAttribute("HTTP_RESPONSE");

		if(request.getSession().getAttribute("meeting_code")==null) {
			Reforward reforward = new Reforward(request, response);
			reforward.setDefault("/?q=talk");
			reforward.forward();
		}
		else {
			this.setVariable("meeting_code", request.getSession(true).getAttribute("meeting_code").toString());
		}
		request.getSession(true).setAttribute("user", name);

		return name;
	}
	
	public String update() throws ApplicationException {
		HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
		if(request.getSession().getAttribute("meeting_code")!=null) {
			this.checkup(request);
			synchronized(this.list) {
				try {
					this.list.wait();
				} catch (InterruptedException e) {
					throw new ApplicationException(e.getMessage(),e);
				}
				
				if(!this.list.isEmpty()) {
					String message = this.list.peek().toString();
					System.out.println("[" + request.getSession(true).getAttribute("meeting_code") + "]:"+message);
					return message.trim();
				}
			}
		}
		
		return "";
	}

	public boolean save() {
		HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d h:m:s");
		if(request.getSession().getAttribute("meeting_code")!=null) {
			if(!request.getParameter("text").isEmpty()) {
				this.checkup(request);
				synchronized(this.list){
					String[] agent = request.getHeader("User-Agent").split(" ");
					this.setVariable("browser", agent[agent.length-1]);
					
					Builder builder = this.list.poll();
					builder.remove("cmd");
					builder.put("user", request.getSession(true).getAttribute("user"));
					builder.put("time", format.format(new Date()));
					builder.put("message", filter(request.getParameter("text")));
					
					this.list.add(builder);
					this.list.notifyAll();
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean command() {
		HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
		if(request.getSession().getAttribute("meeting_code")!=null) {
			this.checkup(request);
			synchronized(this.list){
				
				Builder builder = this.list.poll();
				builder.remove("message");
				builder.remove("time");
				builder.put("user", request.getSession(true).getAttribute("user"));
				builder.put("cmd", request.getParameter("cmd"));
				
				this.list.add(builder);
				this.list.notifyAll();
				return true;
			}
		}
		
		return false;
	}
	
	public boolean topic() {
		HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
		if(request.getSession().getAttribute("meeting_code")!=null) {
			String key = request.getSession(true).getAttribute("meeting_code").toString();

			this.setVariable(key, filter(request.getParameter("topic")));
			return true;
		}
		
		return false;
	}
	
	public smalltalk exit(String meeting_code) {
		HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
		request.getSession(true).removeAttribute("meeting_code");
		
		return this;
	}
	
	private void checkup(HttpServletRequest request){
		String key = request.getSession(true).getAttribute("meeting_code").toString();
		if((this.list = map.get(key))==null){
			this.list = new ConcurrentLinkedQueue<Builder>();
			this.map.put(key, this.list); 
			this.setVariable("meeting_code", key);
		}
	}
	
	private String filter(String text) {
		text = text.replaceAll("<script(.*)>(.*)<\\/script>", "");
		return text;
	}
	
	@Override
	public String version() {
		return "Welcome to use tinystruct 2.0";
	}

}
