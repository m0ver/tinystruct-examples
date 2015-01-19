package tinystruct.examples;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.tinystruct.AbstractApplication;
import org.tinystruct.Application;
import org.tinystruct.ApplicationException;
import org.tinystruct.application.Action;
import org.tinystruct.application.Actions;
import org.tinystruct.data.component.Builder;
import org.tinystruct.system.ClassFileLoader;
import org.tinystruct.system.Configuration;
import org.tinystruct.system.Settings;
import org.tinystruct.system.util.StringUtilities;
import org.tinystruct.system.util.TextFileLoader;

public class test {

	/**
	 * @param args
	 * @throws ApplicationException 
	 */
	public static void main(String[] args) throws ApplicationException {
		// TODO Auto-generated method stub
		// Use ClassFileLoader to load Java class
		ClassFileLoader loader = ClassFileLoader.getInstance();
		
        Configuration config = new Settings("/application.properties");
        config.set("default.apps.path", "WEB-INF/classes");
        config.set("default.apps.package", "tinystruct.examples");

		Builder builder = new Builder();
        File fi = new File(".lock");
        StringBuffer lock=new StringBuffer();
        if(fi.exists()) {
    		TextFileLoader f = new TextFileLoader(".lock");
    		lock = f.getContent();
        }
    	
        if(lock.length() == 0){
    		builder.parse("{\"installed\":{},\"updated\":true }");
        }
		else
			builder.parse(lock.toString());
			
		Builder build = (Builder) builder.get("installed");
		
		String apps_package = config.get("default.apps.package").toString();
        String apps_package_dir = config.get("default.apps.path").toString()+"/"+apps_package.replaceAll("\\.", "/");

		File files = new File(apps_package_dir);
		File[] list = files.listFiles();
		
		String path;
		for(int i=0;i<list.length;i++) {
			if(list[i].getName().indexOf('$')==-1) {
				Class<?> clz = loader.findClass(list[i].getName());
				
				if(clz!=null && clz.getSuperclass().equals(AbstractApplication.class)) {
					Class<Application> clzz = (Class<Application>) clz;
					try {
						Application app = clzz.newInstance();
						Actions actions = app.actions();
						
						Collection<Action> a = actions.list();
						System.out.println(a.size());
						Iterator<Action> la = a.iterator();
						while(la.hasNext()) {
							Action act = la.next();
							path = act.getPath();
							if(path.equalsIgnoreCase("say")){
								System.out.println(path+":"+build.get(path));
							}
							
							if(build.get(path)==null || !( build.get(path).toString().equals(act.getApplicationName()))) {
								build.put(path,act.getApplicationName());
								builder.put("updated", false);
							}
						}
					}catch (Exception ex) {
						ex.printStackTrace();
						continue;
					}
				}
			}
		}
		
		Actions acts = Actions.getInstance();
		
		int size = ((Builder) (builder.get("installed"))).size();
		System.out.println(size+":"+acts.list().size());
		builder.put("updated", acts.list().size() == size);
		if(builder.get("updated").toString().equalsIgnoreCase("false")) {
			File file=new File(".lock");
			
			builder.put("updated", true);
			builder.saveAsFile(file);
			
			System.out.println("Updated lock.");
		}
		
	}

}
