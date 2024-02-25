package tinystruct.examples;
import org.tinystruct.AbstractApplication;
import org.tinystruct.Application;
import org.tinystruct.ApplicationContext;
import org.tinystruct.ApplicationException;
import org.tinystruct.data.component.Builder;
import org.tinystruct.data.component.Table;
import org.tinystruct.data.tools.Generator;
import org.tinystruct.data.tools.MySQLGenerator;
import org.tinystruct.system.ApplicationManager;

import custom.objects.User;
import org.tinystruct.system.Dispatcher;
import org.tinystruct.system.annotation.Action;
import org.tinystruct.system.annotation.Argument;

public class firstApplication extends AbstractApplication {

	@Override
	public void init() {
		// TODO Auto-generated method stub
	}

	@Action("praise")
	public String praise(){
		return "Praise to the Lord!";
	}

	@Action("youhappy")
	public boolean happy(){
		return true;
	}

	@Action("read")
	public Object read(String json,String name){
		Builder builder = new Builder();
		try {
			builder.parse(json);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return builder.get(name);
	}

	@Action("say")
	public String say() throws ApplicationException {
		if(null != this.context.getAttribute("words"))
			return this.context.getAttribute("words").toString();

		throw new ApplicationException("Could not find the parameter <i>words</i>.");
	}

	@Action("say")
	public String say(String words){
		return words;
	}

	@Action("version")
	@Override
	public String version() {
		// TODO Auto-generated method stub
		return this.context.getAttribute("name") + this.context.getAttribute("number").toString();
	}

	@Action(value = "version", options = {
			@Argument(key = "POST", description = "POST method"),
	})
	public void setVersion(float number){
		this.context.setAttribute("name", "struct");
		this.context.setAttribute("number", number);
		Dispatcher d;
	}

	@Action("generate")
	public void generate_with_user_table(){
		try {
			String[] list=new String[]{"User"};
			Generator generator=new MySQLGenerator();
			for(String className:list)
			{
				generator.setPath("src/custom/objects/");
				generator.setPackageName("custom.objects");
				generator.importPackages("java.util.Date");
				generator.create(className,className);
				System.out.println("class:"+className+" table:"+className);
				System.out.println("Done!");
			}
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Action("user")
	public User findUser(Object userId) throws ApplicationException{
		
		if(userId!=null){
			User user = new User();
			user.setId(userId);
			user.findOneById();
			
			return user;
		}
		
		return null;
	}

	@Action("users")
	public Table findUsers() throws ApplicationException{
		return new User().findAll();
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
	
		Object name = app.invoke("read", new Object[]{"{\"name\":\"Mover\",\"age\":30}","name"});
		System.out.println(name);
		
		ApplicationManager.call("generate", null);
		
		System.out.println(ApplicationManager.call("user/1", null)); 	// http://localhost:8080/user/1
		System.out.println(ApplicationManager.call("users", null));		// http://localhost:8080/users
	}
}