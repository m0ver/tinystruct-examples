package tinystruct.examples;

import org.tinystruct.AbstractApplication;
import org.tinystruct.Application;
import org.tinystruct.ApplicationException;
import org.tinystruct.system.ApplicationManager;
import org.tinystruct.system.ClassFileLoader;
import org.tinystruct.system.Configuration;
import org.tinystruct.system.Settings;

import java.lang.reflect.InvocationTargetException;

public class hello extends AbstractApplication {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		this.setAction("say", "say");
		this.setAction("smile", "smile");
		this.setAction("render", "render");
	}

	@Override
	public String version() {
		return "tinystruct version 2.0.1";
	}
	
	public String say(String words){
		return words;
	}
	
	public String smile() {
		return ":)";
	}
	
	public hello render() {
		return this;
	}
	
	/**
	 * @param args
	 * @throws ApplicationException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) throws ApplicationException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
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
		
		// to run nothing
		ApplicationManager.call("smile", null);	// Looks nothing

		// What will be happened?
		System.out.println(ApplicationManager.call("smile", null));	// Will render the default template
		ApplicationManager.call("render", null);	// Will render the default template


		// Use ClassFileLoader to load Java class
		ClassFileLoader loader = ClassFileLoader.getInstance();
		
		Configuration<String> config = new Settings("/application.properties");
		config.set("default.apps.path", "WEB-INF/classes");
		config.set("default.apps.package", "tinystruct.examples");

		Class<?> clz = loader.findClass("hello");
		if(clz!=null && clz.getSuperclass().equals(AbstractApplication.class)) {
			ApplicationManager.install((Application) (clz.getDeclaredConstructor().newInstance()));
			ApplicationManager.call("say/Merry Christmas!", null);
		}
	}
	
}
