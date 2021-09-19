package custom.objects;

import org.tinystruct.data.component.AbstractData;
import org.tinystruct.data.component.Row;

public class Account extends AbstractData {

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String username;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    @Override
    public void setData(Row row) {
        if(row.getFieldInfo("id")!=null)	this.setId(row.getFieldInfo("id").stringValue());
        if(row.getFieldInfo("email")!=null)	this.setEmail(row.getFieldInfo("email").stringValue());
        if(row.getFieldInfo("username")!=null)	this.setUsername(row.getFieldInfo("username").stringValue());
        if(row.getFieldInfo("password")!=null)	this.setPassword(row.getFieldInfo("password").stringValue());
    }

    @Override
    public String toString() {
        StringBuffer buffer=new StringBuffer();
        buffer.append("{");
        buffer.append("\"Id\":\""+this.getId()+"\"");
        buffer.append(",\"email\":\""+this.getEmail()+"\"");
        buffer.append(",\"username\":\""+this.getUsername()+"\"");
        buffer.append(",\"password\":\""+this.getPassword()+"\"");
        buffer.append("}");
        return buffer.toString();
    }
}
