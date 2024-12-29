package tinystruct.examples;


import org.tinystruct.AbstractApplication;
import org.tinystruct.ApplicationException;

public class example extends AbstractApplication {

    @Override
    public void init() {
        // TODO Auto-generated method stub
        this.setAction("praise", "praise");
        this.setAction("say", "say");
        this.setAction("smile", "smile");
    }

    @Override
    public String version() {
        return "1.0";
    }

    public String praise() {
        return "Praise to the Lord!";
    }

    public String say() throws ApplicationException {
        if (null != getContext().getAttribute("--words"))
            return getContext().getAttribute("--words").toString();

        throw new ApplicationException("Could not find the parameter <i>words</i>.");
    }

    public String say(String words) {
        return words;
    }

    public String smile() {
        return ":)";
    }

}
