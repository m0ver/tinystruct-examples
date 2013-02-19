import org.tinystruct.AbstractApplication;

public class keywords extends AbstractApplication {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		this.setAction("hello", "sayHello");
	}
	
	public String sayHello(){
		return "<h1>Hello, World!</h1>";
		
	}

	@Override
	public String version() {
		// TODO Auto-generated method stub
		return null;
	}

}