package tinystruct.examples;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.tinystruct.AbstractApplication;
import org.tinystruct.ApplicationException;

public class image2 extends AbstractApplication {

	private String imagePath;
	@Override
	public void init() {
		// TODO Auto-generated method stub
		this.setAction("image2base64", "image2base64");
	}

	@Override
	public String version() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String image2base64() throws ApplicationException  {
		if(this.context.getAttribute("--image-path")==null || this.context.getAttribute("--image-path").toString().trim().length()==0) {
			throw new ApplicationException("Invalid file");
		}
		
		this.imagePath = this.context.getAttribute("--image-path").toString();
		return this.image2base64(this.imagePath);
	}
	
	public String image2base64(String f)  {
		ByteArrayOutputStream ous = null;
		FileInputStream fs = null;
		byte[] data = new byte[]{};
		try {
			byte[] buffer = new byte[4096];
			ous = new ByteArrayOutputStream();
			int read = 0;
			fs = new FileInputStream(f);
			while ((read = fs.read(buffer)) != -1) {
				ous.write(buffer, 0, read);
			}
			
			data = ous.toByteArray();
			if(data.length > 0)
				return "data:image/png;base64," + org.tinystruct.system.util.Base64.encode(data);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ous != null)
					ous.close();

				if (fs != null)
					fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}


}
