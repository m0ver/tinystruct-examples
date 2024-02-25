package tinystruct.examples;


import org.tinystruct.AbstractApplication;
import org.tinystruct.ApplicationException;
import org.tinystruct.dom.Element;
import org.tinystruct.handler.Reforward;
import org.tinystruct.http.Request;
import org.tinystruct.http.Response;
import org.tinystruct.http.Session;
import org.tinystruct.system.annotation.Action;

public class error extends AbstractApplication {

    private Request request;
    private Response response;
    private Reforward reforward;

    @Override
    public void init() {
        // TODO Auto-generated method stub
    }

    @Override
    public String version() {
        // TODO Auto-generated method stub
        return null;
    }

    @Action("error")
    public Object process() throws ApplicationException {
        this.request = (Request) this.context.getAttribute("HTTP_REQUEST");
        this.response = (Response) this.context.getAttribute("HTTP_RESPONSE");

        this.reforward = new Reforward(this.request, this.response);

        this.setVariable("from", this.reforward.getFromURL());

        Session session = this.request.getSession();

        if (session.getAttribute("error") != null) {
            ApplicationException exception = (ApplicationException) session.getAttribute("error");

            String message = exception.getRootCause().getMessage();
            if (message != null) this.setVariable("exception.message", message);
            else this.setVariable("exception.message", "Unknown error");

            this.setVariable("exception.details", this.getDetail(exception).toString());
            return this.getVariable("exception.message").getValue().toString() + this.getVariable("exception.details").getValue();
        } else {
            this.reforward.forward();
        }

        return "This request is forbidden!";
    }

    private Element getDetail(ApplicationException exception) {
        Element errors = new Element("ul");
        int i = 0;

        Throwable ex = exception.getRootCause();

        StackTraceElement[] trace = ex.getStackTrace();

        while (i < trace.length) {
            Element element = new Element("li");
            element.setData(trace[i++].toString());
            errors.addElement(element);
        }

        return errors;
    }

}
