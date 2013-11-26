package custom.objects;

import java.util.Date;

import org.tinystruct.data.component.Row;
import org.tinystruct.data.component.AbstractData;

public class User extends AbstractData {
	private String email;
	private String username;
	private String password;
	private String nickname;
	private String introduce;
	private int gender;
	private String firstName;
	private String lastName;
	private String country;
	private String city;
	private String telephone;
	private String postcode;
	private String lastloginIP;
	private Date lastloginTime;
	private Date registrationTime;
	private String logo;
	private boolean status;

	public String getId()
	{
		return String.valueOf(this.Id);
	}

	public void setEmail(String email)
	{
		this.email=this.setFieldAsString("email",email);
	}

	public String getEmail()
	{
		return this.email;
	}

	public void setUsername(String username)
	{
		this.username=this.setFieldAsString("username",username);
	}

	public String getUsername()
	{
		return this.username;
	}

	public void setPassword(String password)
	{
		this.password=this.setFieldAsString("password",password);
	}

	public String getPassword()
	{
		return this.password;
	}

	public void setNickname(String nickname)
	{
		this.nickname=this.setFieldAsString("nickname",nickname);
	}

	public String getNickname()
	{
		return this.nickname;
	}

	public void setIntroduce(String introduce)
	{
		this.introduce=this.setFieldAsString("introduce",introduce);
	}

	public String getIntroduce()
	{
		return this.introduce;
	}

	public void setGender(int gender)
	{
		this.gender=this.setFieldAsInt("gender",gender);
	}

	public int getGender()
	{
		return this.gender;
	}

	public void setFirstName(String firstName)
	{
		this.firstName=this.setFieldAsString("firstName",firstName);
	}

	public String getFirstName()
	{
		return this.firstName;
	}

	public void setLastName(String lastName)
	{
		this.lastName=this.setFieldAsString("lastName",lastName);
	}

	public String getLastName()
	{
		return this.lastName;
	}

	public void setCountry(String country)
	{
		this.country=this.setFieldAsString("country",country);
	}

	public String getCountry()
	{
		return this.country;
	}

	public void setCity(String city)
	{
		this.city=this.setFieldAsString("city",city);
	}

	public String getCity()
	{
		return this.city;
	}

	public void setTelephone(String telephone)
	{
		this.telephone=this.setFieldAsString("telephone",telephone);
	}

	public String getTelephone()
	{
		return this.telephone;
	}

	public void setPostcode(String postcode)
	{
		this.postcode=this.setFieldAsString("postcode",postcode);
	}

	public String getPostcode()
	{
		return this.postcode;
	}

	public void setLastloginIP(String lastloginIP)
	{
		this.lastloginIP=this.setFieldAsString("lastloginIP",lastloginIP);
	}

	public String getLastloginIP()
	{
		return this.lastloginIP;
	}

	public void setLastloginTime(Date lastloginTime)
	{
		this.lastloginTime=this.setFieldAsDate("lastloginTime",lastloginTime);
	}

	public Date getLastloginTime()
	{
		return this.lastloginTime;
	}

	public void setRegistrationTime(Date registrationTime)
	{
		this.registrationTime=this.setFieldAsDate("registrationTime",registrationTime);
	}

	public Date getRegistrationTime()
	{
		return this.registrationTime;
	}

	public void setLogo(String logo)
	{
		this.logo=this.setFieldAsString("logo",logo);
	}

	public String getLogo()
	{
		return this.logo;
	}

	public void setStatus(boolean status)
	{
		this.status=this.setFieldAsBoolean("status",status);
	}

	public boolean getStatus()
	{
		return this.status;
	}


	@Override
	public void setData(Row row) {
		if(row.getFieldInfo("id")!=null)	this.setId(row.getFieldInfo("id").stringValue());
		if(row.getFieldInfo("email")!=null)	this.setEmail(row.getFieldInfo("email").stringValue());
		if(row.getFieldInfo("username")!=null)	this.setUsername(row.getFieldInfo("username").stringValue());
		if(row.getFieldInfo("password")!=null)	this.setPassword(row.getFieldInfo("password").stringValue());
		if(row.getFieldInfo("nickname")!=null)	this.setNickname(row.getFieldInfo("nickname").stringValue());
		if(row.getFieldInfo("introduce")!=null)	this.setIntroduce(row.getFieldInfo("introduce").stringValue());
		if(row.getFieldInfo("gender")!=null)	this.setGender(row.getFieldInfo("gender").intValue());
		if(row.getFieldInfo("first_name")!=null)	this.setFirstName(row.getFieldInfo("first_name").stringValue());
		if(row.getFieldInfo("last_name")!=null)	this.setLastName(row.getFieldInfo("last_name").stringValue());
		if(row.getFieldInfo("country")!=null)	this.setCountry(row.getFieldInfo("country").stringValue());
		if(row.getFieldInfo("city")!=null)	this.setCity(row.getFieldInfo("city").stringValue());
		if(row.getFieldInfo("telephone")!=null)	this.setTelephone(row.getFieldInfo("telephone").stringValue());
		if(row.getFieldInfo("postcode")!=null)	this.setPostcode(row.getFieldInfo("postcode").stringValue());
		if(row.getFieldInfo("lastlogin_IP")!=null)	this.setLastloginIP(row.getFieldInfo("lastlogin_IP").stringValue());
		if(row.getFieldInfo("lastlogin_time")!=null)	this.setLastloginTime(row.getFieldInfo("lastlogin_time").dateValue());
		if(row.getFieldInfo("registration_time")!=null)	this.setRegistrationTime(row.getFieldInfo("registration_time").dateValue());
		if(row.getFieldInfo("logo")!=null)	this.setLogo(row.getFieldInfo("logo").stringValue());
		if(row.getFieldInfo("status")!=null)	this.setStatus(row.getFieldInfo("status").booleanValue());
	}

	@Override
	public String toString() {
		StringBuffer buffer=new StringBuffer();
		buffer.append("{");
		buffer.append("\"Id\":\""+this.getId()+"\"");
		buffer.append(",\"email\":\""+this.getEmail()+"\"");
		buffer.append(",\"username\":\""+this.getUsername()+"\"");
		buffer.append(",\"password\":\""+this.getPassword()+"\"");
		buffer.append(",\"nickname\":\""+this.getNickname()+"\"");
		buffer.append(",\"introduce\":\""+this.getIntroduce()+"\"");
		buffer.append(",\"gender\":"+this.getGender());
		buffer.append(",\"firstName\":\""+this.getFirstName()+"\"");
		buffer.append(",\"lastName\":\""+this.getLastName()+"\"");
		buffer.append(",\"country\":\""+this.getCountry()+"\"");
		buffer.append(",\"city\":\""+this.getCity()+"\"");
		buffer.append(",\"telephone\":\""+this.getTelephone()+"\"");
		buffer.append(",\"postcode\":\""+this.getPostcode()+"\"");
		buffer.append(",\"lastloginIP\":\""+this.getLastloginIP()+"\"");
		buffer.append(",\"lastloginTime\":\""+this.getLastloginTime()+"\"");
		buffer.append(",\"registrationTime\":\""+this.getRegistrationTime()+"\"");
		buffer.append(",\"logo\":\""+this.getLogo()+"\"");
		buffer.append(",\"status\":"+this.getStatus());
		buffer.append("}");
		return buffer.toString();
	}

}