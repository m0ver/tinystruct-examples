package tinystruct.examples;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

import org.tinystruct.AbstractApplication;
import org.tinystruct.ApplicationContext;
import org.tinystruct.ApplicationException;
import org.tinystruct.application.Context;
import org.tinystruct.system.ApplicationManager;
import org.tinystruct.system.security.Credential;
import org.tinystruct.system.security.oauth2.OAuth2Client;
import org.tinystruct.system.security.oauth2.UserCredential;

public class redis extends AbstractApplication {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		this.setAction("operations", "callback");
	}

	@Override
	public String version() {
		// TODO Auto-generated method stub
		return null;
	}

	public String callback()
			throws ApplicationException, MalformedURLException, URISyntaxException, UnsupportedEncodingException {
		OAuth2Client client = new OAuth2Client();
		Credential credential = new UserCredential();
		HashMap<String, Object> parameters = new HashMap<String, Object>();
//		parameters.put("grant_type", "client_credentials"); // disabled for applicationId
		
		parameters.put("username", "James");
		parameters.put("password", "0123456");
		
//		parameters.put("username", "james");
//		parameters.put("password", "rinnazamcpjF9_msg7Fd");
		
		parameters.put("grant_type", "password");
		client.identify(credential, parameters);
		if (client.approved() && client.grant() != null) {
			URL uri = new URL("http://localhost:8000/api/redis/list/pop/languages");
//			client.requestResource(uri, null);
			uri = new URL("http://localhost:8000/api/redis/list/languages");
			
			return new String(client.requestResource(uri, null), "utf-8");
		}

		return null;
	}

	public static void main(String[] args) throws ApplicationException {
		Context context = new ApplicationContext();
		ApplicationManager.install(new redis());
		System.out.println(ApplicationManager.call("operations", context));
	}

}
