package tinystruct.examples;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.tinystruct.AbstractApplication;
import org.tinystruct.ApplicationException;

public class redirectChecker extends AbstractApplication {

  @Override
  public void init() {
    // TODO Auto-generated method stub
    this.setAction("check", "start");
  }

  /**
    HTTP/1.1 301 Moved Permanently
    Server:[nginx]
    X-Request-ID:[v-34c8ab26-3140-11e6-8d1f-22000aab1671]
    X-Content-Type-Options:[nosniff]
    X-Age:[158593]
    Connection:[keep-alive]
    Date:[Wed, 15 Jun 2016 04:27:24 GMT]
    X-Varnish:[2128902194 2128190039]
    Cache-Control:[max-age=1209600]
    Content-Encoding:[gzip]
    Vary:[Accept-Encoding]
    Expires:[Mon, 27 Jun 2016 08:24:11 GMT]
    Content-Length:[217]
    X-Cache-Hits:[168]
    Location:[http://domain.com/path]
    Content-Type:[text/html; charset=iso-8859-1]
   * @throws ApplicationException
   */
  public void start() throws ApplicationException{
    try 
    {
      FileInputStream fileInput = new FileInputStream("url-rules.txt");
      InputStreamReader reader = new InputStreamReader(fileInput);
      BufferedReader bufferReader = new BufferedReader(reader);
      
      String line, destinationURL, currentURL;
      String[] parts;
      URL from;
      while((line = bufferReader.readLine()) != null)
      {
        parts = line.split("\\t|\\s");
        destinationURL = parts[1];
        from = new URL(parts[0]);
        
        boolean follow = false, redirected = false;
        do {
          HttpURLConnection connection = (HttpURLConnection) from.openConnection();
          connection.setInstanceFollowRedirects(follow);
          connection.connect();
          currentURL = connection.getHeaderField("Location").trim();
          
          int responseCode = connection.getResponseCode();
          
          redirected = (responseCode == 302);
          
          if(!destinationURL.trim().equalsIgnoreCase(currentURL)) {
            Map<String, List<String>> list = connection.getHeaderFields();
            Set<Entry<String, List<String>>> set = list.entrySet();
            Iterator<Entry<String, List<String>>> iterator = set.iterator();
            Entry<String, List<String>> entity;
            while(iterator.hasNext()) {
              entity = iterator.next();
              System.out.println(entity.getKey()+":"+entity.getValue());
            }
          }
          if(redirected) from = new URL(currentURL); 
          System.out.println("Status:\t"+ connection.getHeaderField(0) +"\t"+ (destinationURL.trim().equalsIgnoreCase(currentURL)?"OK":"Failure"));
          System.out.println("From:\t"+from+"\r\nTo:\t"+currentURL+"\r\n");
        } while (redirected);
        
        System.out.println();
      }
      bufferReader.close();
      reader.close();
      fileInput.close();
    } catch (MalformedURLException e) {
      throw new ApplicationException(e.getMessage(), e.getCause());
    } catch (IOException e) {
      throw new ApplicationException(e.getMessage(), e.getCause());
    }
  }

  @Override
  public String version() {
    // TODO Auto-generated method stub
    return null;
  }

}
