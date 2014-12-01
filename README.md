tinystruct2.0
=========

This is an example project based on tinystruct2.0, it can work for both C/S application and B/S web application. 

Please check the example source code to get more.

If you've installed this code in Tomcat 6.0+, You can access the below urls:

<a href="http://localhost:8080/?q=say/Praise%20to%20the%20Lord!">http://localhost:8080/?q=say/Praise%20to%20the%20Lord! </a><br />
<a href="http://localhost:8080/?q=praise">http://localhost:8080/?q=praise </a><br />
<a href="http://localhost:8080/?q=say/Hello%20World">http://localhost:8080/?q=say/Hello%20World </a><br />
<a href="http://localhost:8080/?q=youhappy">http://localhost:8080/?q=youhappy</a><br />
<a href="http://localhost:8080/?q=say/%E4%BD%A0%E7%9F%A5%E9%81%93%E5%85%A8%E4%B8%96%E7%95%8C%E6%9C%80%E7%95%85%E9%94%80%E7%9A%84%E4%B9%A6%E6%98%AF%E5%93%AA%E4%B8%80%E6%9C%AC%E4%B9%A6%E5%90%97%EF%BC%9F">http://localhost:8080/?q=say/%E4%BD%A0%E7%9F%A5%E9%81%93%E5%85%A8%E4%B8%96%E7%95%8C%E6%9C%80%E7%95%85%E9%94%80%E7%9A%84%E4%B9%A6%E6%98%AF%E5%93%AA%E4%B8%80%E6%9C%AC%E4%B9%A6%E5%90%97%EF%BC%9F</a>

The following example is funny, because it supports more than one people to edit a textarea in the same time.
so the functionality can be used for <strong>chat</strong>, or <strong>online collaboration</strong>. Just try it for a second.

<a href="http://localhost:8080/?q=tinyeditor">http://localhost:8080/?q=tinyeditor</a><br />

-------------------------
```java
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
```
-------------------------

You will see them in your browser.

<blockquote>
<h1>Praise to the Lord!</h1>
Praise to the Lord! 
<h1>Hello World</h1>
<i>true</i>
<h1>你知道全世界最畅销的书是哪一本书吗？</h1>
</blockquote>

-------------------------

Please check the example in hello.java to get how does the template to be rendered?
------------------
```javascript
<javascript>

for(var i=1;i<10;i++) {
	println((i>5?Array(i).join('  '): Array(10 - i).join('  ')) + String.fromCharCode(96+i));
}
</javascript>
```
------------------
Result:
------------------
                a
              b
            c
          d
        e
          f
            g
              h
                i
------------------

<pre>
<strong>
                                _       __  
_/  '         _ _/  _     _ _/  _)     /  ) 
/  /  /) (/ _)  /  /  (/ (  /  /__ .  (__/  
         /                                  
</strong>
</pre>
---

Also please see this project: 
https://github.com/m0ver/mobile1.0
http://ingod.asia

