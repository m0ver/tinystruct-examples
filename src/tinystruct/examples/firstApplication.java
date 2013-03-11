package tinystruct.examples;
import org.tinystruct.AbstractApplication;
import org.tinystruct.Application;
import org.tinystruct.ApplicationException;
import org.tinystruct.system.ApplicationManager;

public class firstApplication extends AbstractApplication {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		this.setAction("praise", "praise");
		this.setAction("say", "say");
		this.setAction("youhappy", "happy");
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
		return null;
	}
	
	public static void main(String[]args) throws ApplicationException {
		ApplicationManager.install(new firstApplication());
		
		System.out.println(ApplicationManager.call("praise", null));
		System.out.println("I am "+ApplicationManager.call("youhappy", null)+"ly happy");
		
		ApplicationManager.call("say/Hello World", null);
		
		Application app=ApplicationManager.get( firstApplication.class.getName());
		app.invoke("say", new Object[]{"<h1>Hello, World!</h1>"});
		app.invoke("say", new Object[]{"<h2>Bye!</h2>"});
	}

}

