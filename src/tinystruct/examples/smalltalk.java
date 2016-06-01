package tinystruct.examples;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.tinystruct.AbstractApplication;
import org.tinystruct.ApplicationException;
import org.tinystruct.data.component.Builder;
import org.tinystruct.data.component.Builders;
import org.tinystruct.datatype.Variable;
import org.tinystruct.handle.Reforward;
import org.tinystruct.system.util.Matrix;
import org.tinystruct.system.util.StringUtilities;
import org.tinystruct.transfer.http.upload.ContentDisposition;
import org.tinystruct.transfer.http.upload.MultipartFormData;

public class smalltalk extends AbstractApplication implements HttpSessionListener {

  private static final long TIMEOUT = 1000;
  private final Map<String, Map<String, Queue<Builder>>> groups = new ConcurrentHashMap<String, Map<String, Queue<Builder>>>();

  @Override
  public void init() {
    this.setAction("talk", "index");
    this.setAction("talk/join", "join");
    this.setAction("talk/start", "start");
    this.setAction("talk/update", "update");
    this.setAction("talk/save", "save");
    this.setAction("talk/upload", "upload");
    this.setAction("talk/command", "command");
    this.setAction("talk/topic", "topic");
    this.setAction("talk/matrix", "matrix");
    this.setAction("talk/version", "version");

    this.setVariable("message", "");
    this.setVariable("topic", "");
  }

  public smalltalk index() {
    final HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
    Object meetingCode = request.getSession().getAttribute("meeting_code");

    if ( meetingCode == null ) {
      meetingCode = java.util.UUID.randomUUID().toString();
      request.getSession().setAttribute("meeting_code", meetingCode);

      System.out.println("New meeting generated:" + meetingCode);
    }

    Map<String, Queue<Builder>> sessions;
    synchronized (this.groups) {
      if ((sessions = this.groups.get(meetingCode)) == null) {
        this.groups.put(meetingCode.toString(), sessions = new HashMap<String, Queue<Builder>>());
      }

      final String sessionId = request.getSession().getId();
      if (sessions.get(sessionId) == null) {
        sessions.put(sessionId, new ArrayDeque<Builder>());
      }

      this.groups.notifyAll();
    }

    this.setVariable("meeting_code", meetingCode.toString());
    Variable<?> topic;
    if ((topic = this.getVariable(meetingCode.toString())) != null) {
      this.setVariable("topic", topic.getValue().toString().replaceAll("[\r\n]", "<br />"), true);
    }

    return this;
  }

  public String matrix() throws ApplicationException {
    final HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");

    if (request.getParameter("meeting_code") != null) {
      BufferedImage qrImage = Matrix.toQRImage(this.getLink("talk/join") + "/" + request.getParameter("meeting_code"), 100, 100);
      return "data:image/png;base64," + Matrix.getBase64Image(qrImage);
    }

    return "";
  }

  public String join(String meetingCode) throws ApplicationException {
    if (groups.containsKey(meetingCode)) {
      final HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
      final HttpServletResponse response = (HttpServletResponse) this.context.getAttribute("HTTP_RESPONSE");
      request.getSession().setAttribute("meeting_code", meetingCode);

      this.setVariable("meeting_code", meetingCode);

      Reforward reforward = new Reforward(request, response);
      reforward.setDefault("/?q=talk");
      reforward.forward();
    } else {
      return "Invalid meeting code.";
    }

    return "Please start the conversation with your name: "
        + this.config.get("default.base_url") + "talk/start/YOUR NAME";
  }

  public String start(String name) throws ApplicationException {
    final HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
    final HttpServletResponse response = (HttpServletResponse) this.context.getAttribute("HTTP_RESPONSE");

    Object meetingCode = request.getSession().getAttribute("meeting_code");
    if (meetingCode == null) {
      Reforward reforward = new Reforward(request, response);
      reforward.setDefault("/?q=talk");
      reforward.forward();
    } else {
      this.setVariable("meeting_code", meetingCode.toString());
    }
    request.getSession().setAttribute("user", name);

    return name;
  }

  public String command() {
    final HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
    final HttpServletResponse response = (HttpServletResponse) this.context.getAttribute("HTTP_RESPONSE");
    response.setContentType("application/json");

    final Object meetingCode = request.getSession().getAttribute("meeting_code");
    final String sessionId = request.getSession().getId();

    if ( meetingCode != null ) {
      if (request.getSession().getAttribute("user") == null) {
        return "{ \"error\": \"missing user\" }";
      }

      Builder builder = new Builder();
      builder.put("user", request.getSession().getAttribute("user"));
      builder.put("cmd", request.getParameter("cmd"));

      return this.save(sessionId, meetingCode, builder);
    }

    return "{ \"error\": \"expired\" }";
  }

  public String save() {
    final HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
    final HttpServletResponse response = (HttpServletResponse) this.context.getAttribute("HTTP_RESPONSE");
    response.setContentType("application/json");

    final Object meetingCode = request.getSession().getAttribute("meeting_code");
    if ( meetingCode != null ) {
      String message;
      if ((message = request.getParameter("text")) != null && !message.isEmpty()) {
        String[] agent = request.getHeader("User-Agent").split(" ");
        this.setVariable("browser", agent[agent.length - 1]);

        final SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d h:m:s");

        final Builder builder = new Builder();
        final String sessionId = request.getSession().getId();
        builder.put("user", request.getSession().getAttribute("user"));
        builder.put("time", format.format(new Date()));
        builder.put("message", filter(message));
        builder.put("sessionId", sessionId);

        return this.save(sessionId, meetingCode, builder);
      }
    }

    return "{}";
  }

  public String update() throws ApplicationException, IOException {
    final HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
    final Object meetingCode = request.getSession().getAttribute("meeting_code");
    final String sessionId = request.getSession().getId();
    if ( meetingCode != null ) {
      return update(sessionId, meetingCode);
    }
    return "";
  }

  public String save(final String sessionId, final Object meetingCode, final Builder builder) {
    Map<String, Queue<Builder>> sessions;
    synchronized (this.groups) {
      if ((sessions = this.groups.get(meetingCode)) == null) {
        this.groups.put(meetingCode.toString(), sessions = new HashMap<String, Queue<Builder>>());
      }

      final Collection<Queue<Builder>> set = sessions.values();
      final Iterator<Queue<Builder>> iterator = set.iterator();
      while(true) {
        if(!iterator.hasNext()) break;
        iterator.next().add(builder);
        this.groups.notifyAll();
      }

      if (sessions.get(sessionId) == null) {
        sessions.put(sessionId, new ArrayDeque<Builder>());
        sessions.get(sessionId).add(builder);
        this.groups.notifyAll();
      }

      return builder.toString();
    }
  }

  public String update(final String sessionId, final Object meetingCode) throws ApplicationException, IOException {
    Builder message;

    Map<String, Queue<Builder>> sessions;
    synchronized (this.groups) {
      sessions = this.groups.get(meetingCode);

      do {
        try {
          this.groups.wait(TIMEOUT);
        } catch (InterruptedException e) {
          throw new ApplicationException(e.getMessage(), e);
        }
      } while(sessions == null || sessions.get(sessionId) == null || (message = sessions.get(sessionId).poll()) == null);

      return message.toString();
    }
  }

  public String upload() throws ApplicationException {
    final HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
    final HttpServletResponse response = (HttpServletResponse) this.context.getAttribute("HTTP_RESPONSE");
    response.setContentType("text/html;charset=UTF-8");

    // Create path components to save the file
    final String path = this.config.get("system.directory") != null ? this.config.get("system.directory").toString() + "/files" : "files";

    final Builders builders = new Builders();
    try {
      final MultipartFormData iter = new MultipartFormData(request);
      ContentDisposition e = null;
      int read = 0;
      while ((e = iter.getNextPart()) != null) {
        final String fileName = e.getFileName();
        final Builder builder = new Builder();
        builder.put("type", StringUtilities.implode(";", Arrays.asList(e.getContentType())));
        builder.put("file", new StringBuffer().append(this.context.getAttribute("HTTP_SCHEME")).append("://").append(this.context.getAttribute("HTTP_SERVER")).append(":"+ request.getServerPort()).append( "/files/").append(fileName));
        final File f = new File(path + File.separator + fileName);
        if (!f.exists()) {
          if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
          }
        }

        final OutputStream out = new FileOutputStream(f);
        final BufferedOutputStream bout= new BufferedOutputStream(out);
        final ByteArrayInputStream is = new ByteArrayInputStream(e.getData());
        final BufferedInputStream bs = new BufferedInputStream(is);
        final byte[] bytes = new byte[8192];
        while ((read = bs.read(bytes)) != -1) {
           bout.write(bytes, 0, read);
        }
        bout.close();
        bs.close();

        builders.add(builder);
        System.out.println(String.format("File %s being uploaded to %s", new Object[] { fileName, path }));
      }
    } catch (IOException e) {
      throw new ApplicationException(e.getMessage(), e);
    } catch (ServletException e) {
      throw new ApplicationException(e.getMessage(), e);
    }

    return builders.toString();
  }

  public boolean topic() {
    final HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
    final Object meeting_code = request.getSession().getAttribute("meeting_code");

    if ( meeting_code != null ) {
      this.setVariable(meeting_code.toString(), filter(request.getParameter("topic")));
      return true;
    }

    return false;
  }

  protected smalltalk exit() {
    final HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
    request.getSession().removeAttribute("meeting_code");
    return this;
  }

  protected String filter(String text) {
    text = text.replaceAll("<script(.*)>(.*)<\\/script>", "");
    return text;
  }

  @Override
  public String version() {
    return "Welcome to use tinystruct 2.0";
  }

  @Override
  public void sessionCreated(HttpSessionEvent arg0) {
    Object meetingCode = arg0.getSession().getAttribute("meeting_code");
    if ( meetingCode == null ) {
      meetingCode = java.util.UUID.randomUUID().toString();
      arg0.getSession().setAttribute("meeting_code", meetingCode);

      System.out.println("New meeting generated by HttpSessionListener:" + meetingCode);
    }

    Map<String, Queue<Builder>> sessions;
    synchronized (groups) {
      if ((sessions = groups.get(meetingCode)) == null) {
        groups.put(meetingCode.toString(), sessions = new HashMap<String, Queue<Builder>>());
      }

      final String sessionId = arg0.getSession().getId();
      if (sessions.get(sessionId) == null) {
        sessions.put(sessionId, new ArrayDeque<Builder>());
      }

      groups.notifyAll();
    }
  }

  @Override
  public void sessionDestroyed(HttpSessionEvent arg0) {
    Object meetingCode = arg0.getSession().getAttribute("meeting_code");
    String sessionId = arg0.getSession().getId();
    if ( meetingCode != null ) {
      Map<String, Queue<Builder>> sessions;
      synchronized (groups) {
        if ((sessions = groups.get(meetingCode)) != null) {
          sessions.remove(sessionId);
          groups.notifyAll();
        }
      }
    }
  }
}
