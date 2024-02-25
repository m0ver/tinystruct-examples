package tinystruct.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.tinystruct.AbstractApplication;
import org.tinystruct.system.annotation.Action;

public class dateConverter extends AbstractApplication {

	@Override
	public void init() {
		// TODO Auto-generated method stub
	}

	@Override
	public String version() {
		// TODO Auto-generated method stub
		return null;
	}

	@Action("convert")
	public String convert() throws IOException, ParseException {
		String from = this.context.getAttribute("from").toString();
		String to = this.context.getAttribute("to").toString();
		return this.convert(from, to);
	}
	/**
	 	26/01/2018 11:09:51
		26/01/2018 15:09:10
		29/01/2018 09:14:44
		30/01/2018 09:14:53
		30/01/2018 13:11:53
		01/02/2018 10:38:44
		01/02/2018 11:18:31
		01/02/2018 12:38:54
	 *	
	 * @param date_list
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public String convert(String from, String to) throws IOException, ParseException {
		StringBuffer list = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter some date list or press enter to quit:");
	
		DateFormat t = new SimpleDateFormat(to);
		DateFormat f = new SimpleDateFormat(from);
		String input;
		while (true) {
			 input = br.readLine().trim();
			 if (input.length() == 0) {
				 break;
			 }
			 list.append(t.format(f.parse(input))).append("\n");
		}
	    		
		return list.toString();
	}
	
	public static void main(String[]args) throws IOException, ParseException {
		System.out.println(new dateConverter().convert("dd/MM/yyyy HH:mm", "yyyy/MM/dd HH:mm:ss"));
	}
}
