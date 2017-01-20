package tinystruct.examples;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.tinystruct.AbstractApplication;
import org.tinystruct.ApplicationException;
import org.tinystruct.data.component.Builder;
import org.tinystruct.system.ApplicationManager;

public class talk extends AbstractApplication {

  private static final long TIMEOUT = 200;
  protected final Map<String, Queue<Builder>> list = new ConcurrentHashMap<String, Queue<Builder>>();
  protected final Map<String, Queue<Builder>> meetings = new ConcurrentHashMap<String, Queue<Builder>>();
  protected final Map<String, List<String>> sessions = new ConcurrentHashMap<String, List<String>>();
  private final ExecutorService service = Executors.newFixedThreadPool(3);

  @Override
  public void init() {
    this.setAction("talk/update", "update");
    this.setAction("talk/save", "save");
    this.setAction("talk/version", "version");
    this.setAction("talk/testing", "testing");
    
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
        @Override
        public void run() {
            service.shutdown();
            while (true) {
                try {
                    System.out.println("Waiting for the service to terminate...");
                    if (service.awaitTermination(5, TimeUnit.SECONDS)) {
                      System.out.println("Service will be terminated soon.");
                        break;
                    }
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
            }
        }
    }));
  }

  /**
   * To be used for testing.
   * @param meetingCode
   * @param sessionId
   * @param message
   * @return
   */
  public String save(Object meetingCode, String sessionId, String message) {
    if ( meetingCode != null ) {
      if (message != null && !message.isEmpty()) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d h:m:s");
        final Builder builder = new Builder();
        builder.put("user", "user_"+sessionId);
        builder.put("time", format.format(new Date()));
        builder.put("message", filter(message));
        builder.put("session_id", sessionId);

        return this.save(meetingCode, builder);
      }
    }

    return "{}";
  }

  /**
   * Save message and create a thread for copying it to message list of each session.
   * @param meetingCode
   * @param builder
   * @return builder
   */
  public final String save(final Object meetingCode, final Builder builder) {
    final Queue<Builder> messages;
    synchronized (this.meetings) {
      if (this.meetings.get(meetingCode) == null) {
        this.meetings.put(meetingCode.toString(), new ConcurrentLinkedQueue<Builder>());
      }

      messages = this.meetings.get(meetingCode);
      messages.add(builder);
      this.meetings.notifyAll();
    }

    service.execute(new Runnable(){
      @Override
      public void run() {
        synchronized(talk.this.meetings) {
          Builder message;
          do {
            try {
              talk.this.meetings.wait(TIMEOUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
          } while(talk.this.meetings.get(meetingCode) == null || (message = talk.this.meetings.get(meetingCode).poll()) == null);
          
          talk.this.copy(meetingCode, message);
        }
      }
    });
    return builder.toString();
  }

  /**
   * Poll message from the messages of the session specified sessionId.
   * @param sessionId
   * @return message
   * @throws ApplicationException
   * @throws IOException
   */
  public final String update(final String sessionId) throws ApplicationException, IOException {
    Builder message;
    Queue<Builder> messages;
    synchronized (this.list) {
      messages = this.list.get(sessionId);
      while((message = messages.poll()) == null) {
        try {
          this.list.wait(TIMEOUT);
        } catch (InterruptedException e) {
          throw new ApplicationException(e.getMessage(), e);
        }
      }

      return message.toString();
    }
  }

  /**
   * This function can be override.
   * @param text
   * @return
   */
  protected String filter(String text) {
    return text;
  }

  /**
   * Copy message to the list of each session.
   * @param meetingCode 
   * @param builder
   */
  private final void copy(Object meetingCode, Builder builder) {
    synchronized(this.list) {
      final Collection<Entry<String, Queue<Builder>>> set = list.entrySet();
      final Iterator<Entry<String, Queue<Builder>>> iterator = set.iterator();
      final List<String> meeting_session;
      if((meeting_session = this.sessions.get(meetingCode)) != null) {
        while(iterator.hasNext()) {
          Entry<String, Queue<Builder>> e = iterator.next();
          if(meeting_session.contains(e.getKey())) {
            e.getValue().add(builder);
            this.list.notifyAll();
          }
        }
      }
      else
      this.list.notifyAll();
    }
  }

  @Override
  public String version() {
    return "Welcome to use tinystruct 2.0";
  }

  /**
   * This is a testing. It can be executed with the command:
   * $ bin/dispatcher --import-applications=tinystruct.examples.talk talk/testing/100
   * 
   * @param n
   * @return
   * @throws ApplicationException
   */
  public boolean testing(final int n) throws ApplicationException {
    this.meetings.put("[M001]", new ConcurrentLinkedQueue<Builder>());
    this.list.put("{A}", new ConcurrentLinkedQueue<Builder>());
    this.list.put("{B}", new ConcurrentLinkedQueue<Builder>());
    
    List<String> sess = new ArrayList<String>();
    sess.add("{A}");
    sess.add("{B}");
    this.sessions.put("[M001]", sess);
    
    service.execute(new Runnable(){
      @Override
      public void run() {
        int i=0;
        while(i++<n)
        try {
          ApplicationManager.call("talk/save/[M001]/{A}/A post "+i, null);
          Thread.sleep(1);
        } catch (ApplicationException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    });

    service.execute(new Runnable(){
      @Override
      public void run() {
        int i=0;
        while(i++<n)
        try {
          ApplicationManager.call("talk/save/[M001]/{B}/B post "+i, null);
          Thread.sleep(1);
        } catch (ApplicationException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    });

    service.execute(new Runnable(){
      @Override
      public void run() {
        // TODO Auto-generated method stub
        System.out.println("[A] is started...");
        while(true)
        try {
          System.out.println("**A**:"+ApplicationManager.call("talk/update/{A}", null));
          Thread.sleep(1);
        } catch (ApplicationException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    });

    service.execute(new Runnable(){
      @Override
      public void run() {
        // TODO Auto-generated method stub
        System.out.println("[B] is started...");
        while(true)
        try {
          System.out.println("**B**:"+ApplicationManager.call("talk/update/{B}", null));
          Thread.sleep(1);
        } catch (ApplicationException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    });
    
    return true;
  }

}
