package custom.objects;

import org.tinystruct.data.component.AbstractData;
import org.tinystruct.data.component.Row;

public class article extends AbstractData {
	private String bookId;
	private int chapterId;
	private int sectionId;
	private String content;

	public Integer getId()
	{
		return Integer.parseInt(this.Id.toString());
	}

	public void setBookId(String bookId)
	{
		this.bookId=this.setFieldAsString("bookId",bookId);
	}

	public String getBookId()
	{
		return this.bookId;
	}

	public void setChapterId(int chapterId)
	{
		this.chapterId=this.setFieldAsInt("chapterId",chapterId);
	}

	public int getChapterId()
	{
		return this.chapterId;
	}

	public void setSectionId(int sectionId)
	{
		this.sectionId=this.setFieldAsInt("sectionId",sectionId);
	}

	public int getSectionId()
	{
		return this.sectionId;
	}

	public void setContent(String content)
	{
		this.content=this.setFieldAsString("content",content);
	}

	public String getContent()
	{
		return this.content;
	}


	@Override
	public void setData(Row row) {
		if(row.getFieldInfo("id")!=null)	this.setId(row.getFieldInfo("id").intValue());
		if(row.getFieldInfo("book_id")!=null)	this.setBookId(row.getFieldInfo("book_id").stringValue());
		if(row.getFieldInfo("chapter_id")!=null)	this.setChapterId(row.getFieldInfo("chapter_id").intValue());
		if(row.getFieldInfo("section_id")!=null)	this.setSectionId(row.getFieldInfo("section_id").intValue());
		if(row.getFieldInfo("content")!=null)	this.setContent(row.getFieldInfo("content").stringValue());
	}

	@Override
	public String toString() {
		StringBuffer buffer=new StringBuffer();
		buffer.append("{");
		buffer.append("\"Id\":"+this.getId());
		buffer.append(",\"bookId\":\""+this.getBookId()+"\"");
		buffer.append(",\"chapterId\":"+this.getChapterId());
		buffer.append(",\"sectionId\":"+this.getSectionId());
		buffer.append(",\"content\":\""+this.getContent()+"\"");
		buffer.append("}");
		return buffer.toString();
	}

}