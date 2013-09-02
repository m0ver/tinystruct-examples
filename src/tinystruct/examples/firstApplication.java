package tinystruct.examples;
import org.tinystruct.AbstractApplication;
import org.tinystruct.Application;
import org.tinystruct.ApplicationContext;
import org.tinystruct.ApplicationException;
import org.tinystruct.handle.Report;
import org.tinystruct.system.ApplicationManager;

public class firstApplication extends AbstractApplication {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		this.setAction("praise", "praise");
		this.setAction("say", "say");
		this.setAction("youhappy", "happy");
		
		this.setAction("version", "version", "GET");
		this.setAction("version", "setVersion","POST");
	}
	
	public String praise(){
		return "Praise to the Lord!";
	}
	
	public boolean happy(){
		return true;
	}
	
	public String say(String words){
		System.out.println(words);
		return "<h1>"+words+"</h1>";
	}

	@Override
	public String version() {
		// TODO Auto-generated method stub
		return this.context.getAttribute("name") + this.context.getAttribute("number").toString();
	}
	
	public void setVersion(float number){
		this.context.setAttribute("number", number);
	}
	
	/**
	 * The following example code only for you to reference. 
	 * @param args
	 * @throws ApplicationException
	 */
	public static void main(String[]args) throws ApplicationException {
		ApplicationManager.install(new firstApplication());
		
		System.out.println(ApplicationManager.call("praise", null)); // Praise to the Lord!
		System.out.println("I am "+ApplicationManager.call("youhappy", null)+"ly happy"); // I am truely happy
		
		ApplicationManager.call("say/Hello World", null); 			// Hello World
		
		Application app=ApplicationManager.get( firstApplication.class.getName()); 
		app.invoke("say", new Object[]{"<h1>Hello, World!</h1>"});	// <h1>Hello, World!</h1>
		app.invoke("say", new Object[]{"<h2>Bye!</h2>"});			// <h2>Bye!</h2>
		
		ApplicationContext context=new ApplicationContext();
		context.setAttribute("name", "struct");
		context.setAttribute("number", 2.0);
		
		context.setAttribute("METHOD", "GET");
		System.out.println("Current version: "+ApplicationManager.call("version", context)); // Current version: struct2.0
		
		context.setAttribute("METHOD", "POST");
		ApplicationManager.call("version/3", context);
		
		context.setAttribute("METHOD", "GET");
		System.out.println("Current version: "+ApplicationManager.call("version", context)); // Current version: struct3.0

	}
}