package tinystruct.examples;

import org.tinystruct.AbstractApplication;
import org.tinystruct.Application;
import org.tinystruct.ApplicationException;
import org.tinystruct.system.ApplicationManager;
import org.tinystruct.system.ClassFileLoader;
import org.tinystruct.system.Configuration;
import org.tinystruct.system.Settings;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class hello extends AbstractApplication {

    private static final String AUTH_HEADER_KEY = "Authorization";
    private static final String AUTH_HEADER_VALUE_PREFIX = "Bearer "; // with trailing space to separate token
    private static final int STATUS_CODE_UNAUTHORIZED = 401;

    @Override
    public void init() {
        // TODO Auto-generated method stub
        this.setAction("say", "say");
        this.setAction("smile", "smile");
        this.setAction("render", "render");
        this.setAction("login", "login");
    }

    @Override
    public String version() {
        System.out.println("tinystruct version 2.0.1");
        return null;
    }

    public String say() {
        if(null != this.context.getAttribute("words"))
        return this.context.getAttribute("words").toString();

        return "Invalid parameters.";
    }

    public String say(String words) {
        return words;
    }

    public String smile() throws ApplicationException {
        return ":)";
    }

    public String login() throws ServletException {
        HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
        HttpServletResponse response = (HttpServletResponse) this.context.getAttribute("HTTP_RESPONSE");

        String bearer = this.getBearerToken(request);

        if (null != bearer) {
			request.login("James", "0123456");
        }
        else {
            response.setContentLength( 0 );
            response.setStatus( STATUS_CODE_UNAUTHORIZED );
        }

        return bearer;
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
    public static void main(String[] args) throws ApplicationException, InstantiationException, IllegalAccessException {
        // Praise to the Lord!
        ApplicationManager.install(new hello());

        // to print 'Hello World'
        ApplicationManager.call("say/Hello World", null);            // Hello World

        // or...
        Application app = ApplicationManager.get(hello.class.getName());
        app.invoke("say", new Object[]{"<h1>Hello, World!</h1>"});    // <h1>Hello, World!</h1>
        app.invoke("say", new Object[]{"<h2>Bye!</h2>"});            // <h2>Bye!</h2>

        // or...
        // http://localhost:8080/?q=say/Hello World

        // to run nothing
        ApplicationManager.call("smile", null);    // Looks nothing

        // What will be happened?
        System.out.println(ApplicationManager.call("smile", null));    // Will render the default template

        // Use ClassFileLoader to load Java class
        ClassFileLoader loader = ClassFileLoader.getInstance();

        Configuration<String> config = new Settings("/application.properties");
        config.set("default.apps.path", "WEB-INF/classes");
        config.set("default.apps.package", "tinystruct.examples");

        Class<?> clz = loader.findClass("hello");
        if (clz != null && clz.getSuperclass().equals(AbstractApplication.class)) {
            ApplicationManager.install((Application) clz.newInstance());
            ApplicationManager.call("say/Merry Christmas!", null);
        }
    }

    /**
     * Get the bearer token from the HTTP request.
     * The token is in the HTTP request "Authorization" header in the form of: "Bearer [token]"
     */
    private String getBearerToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER_KEY);
        if (authHeader != null && authHeader.startsWith(AUTH_HEADER_VALUE_PREFIX)) {
            return authHeader.substring(AUTH_HEADER_VALUE_PREFIX.length());
        }
        return null;
    }

}
