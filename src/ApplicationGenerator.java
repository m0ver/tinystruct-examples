
import org.tinystruct.ApplicationException;
import org.tinystruct.data.tools.Generator;
import org.tinystruct.data.tools.MySQLGenerator;

public class ApplicationGenerator {
	public static void main(String[]arguments)
	{
		System.out.println("Please type the command like this:ApplicationGenerator className tableName");
		
		try {
			String[] list=new String[]{"book"};
//			String[] list=new String[]{"Member","User","Rights","Role"};
			for(String className:list)
			{
				Generator generator=new MySQLGenerator();
				generator.setFileName("src/custom/objects/");
				generator.setPackageName("custom.objects");
				generator.importPackages("java.util.Date");
				generator.create(className,className);
				System.out.println("class:"+className+" table:"+className);
			}
		} catch (ApplicationException e) {
			
			e.printStackTrace();
		}
		
		System.out.println("OK!");
		System.out.close();
	}
}