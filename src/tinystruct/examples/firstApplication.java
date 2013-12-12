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

public class firstApplication extends AbstractApplication {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		this.setAction("praise", "praise");
		this.setAction("say", "say");
		this.setAction("youhappy", "happy");
		
		this.setAction("user", "findUser");
		this.setAction("users", "findUsers");
		
		this.setAction("version", "version", "GET");
		this.setAction("version", "setVersion","POST");
		
		this.setAction("read", "read");
		
		this.setAction("generate", "generate_with_user_table");
	}
	
	public String praise(){
		return "Praise to the Lord!";
	}
	
	public boolean happy(){
		return true;
	}
	
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
		this.context.setAttribute("name", "struct");
		this.context.setAttribute("number", number);
	}
	
	public void generate_with_user_table(){
		try {
			String[] list=new String[]{"User"};
			Generator generator=new MySQLGenerator();
			for(String className:list)
			{
				generator.setFileName("src/custom/objects/");
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
	
	public User findUser(Object userId) throws ApplicationException{
		User user = new User();
		if(userId!=null){
			user.setId(userId);
			user.findOneById();
		}
		
		return user;
	}
	
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