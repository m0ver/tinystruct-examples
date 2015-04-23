package tinystruct.examples;

import org.tinystruct.AbstractApplication;
import org.tinystruct.Application;
import org.tinystruct.ApplicationException;
import org.tinystruct.system.ApplicationManager;
import org.tinystruct.system.ClassFileLoader;
import org.tinystruct.system.Configuration;
import org.tinystruct.system.Settings;

public class hello extends AbstractApplication {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		this.setAction("say", "say");
		this.setAction("smile", "smile");
		this.setAction("--logo", "logo");
		this.setAction("--version", "version");
	}

	@Override
	public String version() {
		System.out.println("tinystruct version 2.0.1");
		return null;
	}
	
	public String say(String words){
		System.out.println(words);
		return "<h1>"+words+"</h1>";
	}
	
	public void smile() throws ApplicationException{
		System.out.println(":)");
	}
	
	public void logo() {
		String logo ="\n"+
		"  _/  '         _ _/  _     _ _/   \n"+
		"  /  /  /) (/ _)  /  /  (/ (  /  "+this.color(2.0, FORECOLOR.green)+"  \n"+
		"           /                       \n";
		System.out.println(logo);
	}
	
	public String color(Object s, int color){
		return (char)27+"["+color+"m"+s+(char)27 + "[0m";
	}

	/**
	 * @param args
	 * @throws ApplicationException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) throws ApplicationException, InstantiationException, IllegalAccessException {
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
		
		// Use ClassFileLoader to load Java class
		ClassFileLoader loader = ClassFileLoader.getInstance();
		
        Configuration config = new Settings("/application.properties");
        config.set("default.apps.path", "WEB-INF/classes");
        config.set("default.apps.package", "tinystruct.examples");

		Class<?> clz = loader.findClass("hello");
		if(clz!=null && clz.getSuperclass().equals(AbstractApplication.class)) {
			ApplicationManager.install((Application) clz.newInstance());
			ApplicationManager.call("say/Merry Christmas!", null);
		}
	}
	
	public class FORECOLOR {
		public static final int black = 30;
		public static final int red = 31;
		
		public static final int green = 32;
		public static final int yellow = 33;
		
		public static final int blue = 34;
		public static final int magenta = 35;
		public static final int cyan = 36;
		public static final int white = 37;
	}
	
	public class BACKGROUND_COLOR {
		public static final int black = 40;
		public static final int red = 41;
		public static final int green = 42;
		public static final int yellow = 43;
		public static final int blue = 44;
		public static final int magenta = 45;
		public static final int cyan = 46;
		public static final int white = 47;
	}

}
