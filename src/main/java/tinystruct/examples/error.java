package tinystruct.examples;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.tinystruct.AbstractApplication;
import org.tinystruct.ApplicationException;
import org.tinystruct.dom.Element;
import org.tinystruct.handler.Reforward;
import org.tinystruct.system.ApplicationManager;

public class error extends AbstractApplication {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private Reforward reforward;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		this.setAction("error", "process");
		this.setAction("info", "info");
	}
	
	@Override
	public String version() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object process()
			throws ApplicationException {
		this.request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
		this.response = (HttpServletResponse) this.context.getAttribute("HTTP_RESPONSE");

		this.reforward=new Reforward(this.request,this.response);
		
		this.setVariable("from", this.reforward.getFromURL());
		
		HttpSession session=this.request.getSession();	
		
		if(session.getAttribute("error")!=null)
		{
			ApplicationException exception=(ApplicationException)session.getAttribute("error");
			
			String message=exception.getRootCause().getMessage();
			if(message!=null)		this.setVariable("exception.message", message);
			else 		this.setVariable("exception.message", "Unknown error");
			
			this.setVariable("exception.details", this.getDetail(exception).toString());
			return this.getVariable("exception.message").getValue().toString()+this.getVariable("exception.details").getValue();
		}
		else
		{
			this.reforward.forward();
		}
		
		return "This request is forbidden!";
	}
	
	private Element getDetail(ApplicationException exception)
	{
		Element errors=new Element("ul");
		int i=0;
		
		Throwable ex=exception.getRootCause();
		
		StackTraceElement[] trace=ex.getStackTrace();

		while(i<trace.length)
		{
			Element element=new Element("li");
			element.setData(trace[i++].toString());
			errors.addElement(element);
		}
		
		return errors;
	}
	
	public StringBuffer info()
	{
		StringBuffer buffer=new StringBuffer();
		
		buffer.append("Protocol: " + this.request.getProtocol()+"\r\n");
		buffer.append("Scheme: " + this.request.getScheme()+"\r\n");
		buffer.append("Server Name: " + this.request.getServerName()+"\r\n");
		buffer.append("Server Port: " + this.request.getServerPort()+"\r\n");
		buffer.append("Protocol: " + this.request.getProtocol()+"\r\n");
//		buffer.append("Server Info: " + getServletConfig().getServletContext().getServerInfo()+"\r\n");
		buffer.append("Remote Addr: " + this.request.getRemoteAddr()+"\r\n");
		buffer.append("Remote Host: " + this.request.getRemoteHost()+"\r\n");
		buffer.append("Character Encoding: " + this.request.getCharacterEncoding()+"\r\n");
		buffer.append("Content Length: " + this.request.getContentLength()+"\r\n");
		buffer.append("Content Type: "+ this.request.getContentType()+"\r\n");
		buffer.append("Auth Type: " + this.request.getAuthType()+"\r\n");
		buffer.append("HTTP Method: " + this.request.getMethod()+"\r\n");
		buffer.append("Path Info: " + this.request.getPathInfo()+"\r\n");
		buffer.append("Path Trans: " + this.request.getPathTranslated()+"\r\n");
		buffer.append("Query String: " + this.request.getQueryString()+"\r\n");
		buffer.append("Remote User: " + this.request.getRemoteUser()+"\r\n");
		buffer.append("Session Id: " + this.request.getRequestedSessionId()+"\r\n");
		buffer.append("Request URI: " + this.request.getRequestURI()+"\r\n");
		buffer.append("Servlet Path: " + this.request.getServletPath()+"\r\n");
		buffer.append("Accept: " + this.request.getHeader("Accept")+"\r\n");
		buffer.append("Host: " + this.request.getHeader("Host")+"\r\n");
		buffer.append("Referer : " + this.request.getHeader("Referer")+"\r\n");
		buffer.append("Accept-Language : " + this.request.getHeader("Accept-Language")+"\r\n");
		buffer.append("Accept-Encoding : " + this.request.getHeader("Accept-Encoding")+"\r\n");
		buffer.append("User-Agent : " + this.request.getHeader("User-Agent")+"\r\n");
		buffer.append("Connection : " + this.request.getHeader("Connection")+"\r\n");
		buffer.append("Cookie : " + this.request.getHeader("Cookie")+"\r\n");
		
		return buffer;
	}

		ApplicationManager.init();

}
