package custom.objects;

import java.util.Date;

import org.tinystruct.data.component.Row;
import org.tinystruct.data.component.AbstractData;

public class book extends AbstractData {
	private String name;
	private String cover;
	private String author;
	private String translator;
	private String publisher;
	private Date publishDate;
	private String language;

	public String getId()
	{
		return String.valueOf(this.Id);
	}

	public void setName(String name)
	{
		this.name=this.setFieldAsString("name",name);
	}

	public String getName()
	{
		return this.name;
	}

	public void setCover(String cover)
	{
		this.cover=this.setFieldAsString("cover",cover);
	}

	public String getCover()
	{
		return this.cover;
	}

	public void setAuthor(String author)
	{
		this.author=this.setFieldAsString("author",author);
	}

	public String getAuthor()
	{
		return this.author;
	}

	public void setTranslator(String translator)
	{
		this.translator=this.setFieldAsString("translator",translator);
	}

	public String getTranslator()
	{
		return this.translator;
	}

	public void setPublisher(String publisher)
	{
		this.publisher=this.setFieldAsString("publisher",publisher);
	}

	public String getPublisher()
	{
		return this.publisher;
	}

	public void setPublishDate(Date publishDate)
	{
		this.publishDate=this.setFieldAsDate("publishDate",publishDate);
	}

	public Date getPublishDate()
	{
		return this.publishDate;
	}

	public void setLanguage(String language)
	{
		this.language=this.setFieldAsString("language",language);
	}

	public String getLanguage()
	{
		return this.language;
	}


	@Override
	public void setData(Row row) {
		if(row.getFieldInfo("id")!=null)	this.setId(row.getFieldInfo("id").stringValue());
		if(row.getFieldInfo("name")!=null)	this.setName(row.getFieldInfo("name").stringValue());
		if(row.getFieldInfo("cover")!=null)	this.setCover(row.getFieldInfo("cover").stringValue());
		if(row.getFieldInfo("author")!=null)	this.setAuthor(row.getFieldInfo("author").stringValue());
		if(row.getFieldInfo("translator")!=null)	this.setTranslator(row.getFieldInfo("translator").stringValue());
		if(row.getFieldInfo("publisher")!=null)	this.setPublisher(row.getFieldInfo("publisher").stringValue());
		if(row.getFieldInfo("publish_date")!=null)	this.setPublishDate(row.getFieldInfo("publish_date").dateValue());
		if(row.getFieldInfo("language")!=null)	this.setLanguage(row.getFieldInfo("language").stringValue());
	}

	@Override
	public String toString() {
		StringBuffer buffer=new StringBuffer();
		buffer.append("{");
		buffer.append("\"Id\":\""+this.getId()+"\"");
		buffer.append(",\"name\":\""+this.getName()+"\"");
		buffer.append(",\"cover\":\""+this.getCover()+"\"");
		buffer.append(",\"author\":\""+this.getAuthor()+"\"");
		buffer.append(",\"translator\":\""+this.getTranslator()+"\"");
		buffer.append(",\"publisher\":\""+this.getPublisher()+"\"");
		buffer.append(",\"publishDate\":\""+this.getPublishDate()+"\"");
		buffer.append(",\"language\":\""+this.getLanguage()+"\"");
		buffer.append("}");
		return buffer.toString();
	}

}