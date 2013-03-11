package tinystruct.examples;
import org.tinystruct.AbstractApplication;
import org.tinystruct.Application;
import org.tinystruct.ApplicationException;
import org.tinystruct.system.ApplicationManager;

public class firstApplication extends AbstractApplication {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		this.setAction("hello", "sayHello");
		this.setAction("say", "say");
		this.setAction("youhappy", "happy");
	}
	
	public String sayHello(){
		return "Hello, World!";
	}
	
	public boolean happy(){
		return true;
	}
	
	public void say(String words){
		System.out.println(words);
	}

	@Override
	public String version() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[]args) throws ApplicationException {
		ApplicationManager.install(new firstApplication());
		
		System.out.println(ApplicationManager.call("hello", null));
		
		System.out.println(ApplicationManager.call("say/hello world", null));
		
		System.out.println("I am "+ApplicationManager.call("youhappy", null)+"ly happy");

		Application app=ApplicationManager.get( firstApplication.class.getName());
		app.invoke("say", new Object[]{"Praise to the Lord!"});
		app.invoke("say", new Object[]{"Amen!"});
	}

}

