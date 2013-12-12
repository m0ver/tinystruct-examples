package tinystruct.examples;

import org.tinystruct.AbstractApplication;
import org.tinystruct.Application;
import org.tinystruct.ApplicationException;
import org.tinystruct.system.ApplicationManager;

public class hello extends AbstractApplication {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		this.setAction("say", "say");
	}

	@Override
	public String version() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String say(String words){
		System.out.println(words);
		return "<h1>"+words+"</h1>";
	}

	/**
	 * @param args
	 * @throws ApplicationException 
	 */
	public static void main(String[] args) throws ApplicationException {
		// TODO Auto-generated method stub
		// Praise to the Lord!
		ApplicationManager.install(new hello());
		
		// to print 'Hello World'
		ApplicationManager.call("say/Hello World", null); 			// Hello World
		
		// or...
		Application app=ApplicationManager.get( hello.class.getName()); 
		app.invoke("say", new Object[]{"<h1>Hello, World!</h1>"});	// <h1>Hello, World!</h1>
		app.invoke("say", new Object[]{"<h2>Bye!</h2>"});			// <h2>Bye!</h2>
		
		// or...
		// http://localhost:8080/?q=say/Hello World
	}

}
