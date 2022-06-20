package tinystruct.examples;

import java.net.MalformedURLException;
import java.net.URL;

import org.tinystruct.AbstractApplication;
import org.tinystruct.ApplicationContext;
import org.tinystruct.ApplicationException;
import org.tinystruct.ApplicationRuntimeException;
import org.tinystruct.system.ApplicationManager;
import org.tinystruct.system.Dispatcher;
import org.tinystruct.system.util.URLResourceLoader;

public class bible extends AbstractApplication {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		this.setAction("bible", "open");
	}

	@Override
	public String version() {
		return "v1.0";
	}

	public String open(String pattern) throws ApplicationException {
		pattern = pattern.replace('.', '/'); // To avoid the conflict with the path query
		URL uri;
		try {
			uri = new URL("http://rcuv.hkbs.org.hk/bb/info/CUNPs_1/"+pattern+"/");
			URLResourceLoader loader = new URLResourceLoader(uri );
			StringBuilder buffer = loader.getContent();
			
			return this.preprocess(buffer.toString());
		} catch (MalformedURLException e) {
			throw new ApplicationException(e.getMessage(), e.getCause());
		}
	}

	protected String preprocess(String text) {
		return text.split("[\r\n]")[4].replace('「', '“').replace('『', '‘').replace('」', '”').replace('』', '’');
	}

	public static void main(String[] args) throws ApplicationException {
		ApplicationContext ctx = new ApplicationContext();
		ctx.setAttribute("--http.proxyHost", "135.245.48.34");
		ctx.setAttribute("--http.proxyPort", "8000");

		ApplicationManager.install(new Dispatcher());
		ApplicationManager.install(new bible());
		
		ApplicationManager.call("set/--http.proxyHost", ctx);
		ApplicationManager.call("set/--http.proxyPort", ctx);

		System.out.println(ApplicationManager.call("bible/GEN.3", ctx));
	}

}
