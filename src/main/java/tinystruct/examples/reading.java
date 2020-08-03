/*******************************************************************************
 * Copyright  (c) 2013 Mover Zhou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package tinystruct.examples;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.tinystruct.AbstractApplication;
import org.tinystruct.ApplicationException;
import org.tinystruct.data.component.Field;
import org.tinystruct.data.component.Row;
import org.tinystruct.data.component.Table;
import org.tinystruct.datatype.DataVariable;
import org.tinystruct.dom.Element;
import org.tinystruct.system.security.Authentication;
import org.tinystruct.system.security.Credential;
import org.tinystruct.system.util.Base64;
import org.tinystruct.system.util.URLResourceLoader;

import custom.objects.User;
import custom.objects.article;
import custom.objects.book;

public class reading extends AbstractApplication {
	private String bookId;
	private int chapterId;
	private int sectionId;

	private int maxChapter = 0;
	private int lastChapterId;
	private int nextChapterId;
	private HttpServletResponse response;
	private book book;
	private HttpServletRequest request;
	private User usr;

	@Override
	public void init() {
		this.setAction("book", "read");
		this.setAction("book/api", "api");
		this.setAction("book/feed", "feed");

		this.setText("holy.book.forward");
		this.setText("holy.book.previous");
		this.setText("holy.book.next");

		this.book = new book();

		this.setVariable("TEMPLATES_DIR", "/themes", false);
		this.setVariable("metas", "");

		String username = "";
		if (this.getVariable("username") != null) {
			username = String.valueOf(this.getVariable("username").getValue());
		}

		this.setText("page.welcome.hello", (username == null || username.trim().length() == 0) ? "" : username + "，");
	}

	@Override
	public String version() {
		return null;
	}

	public Object read() throws ApplicationException {
		throw new ApplicationException("Invalid book Id");
	}

	public Object read(String bookId) throws ApplicationException {
		return this.read(bookId, 1);
	}

	public Object read(String bookId, int chapterId) throws ApplicationException {
		return this.read(bookId, chapterId, 0);
	}

	public Object read(String bookId, int chapterId, int sectionId) throws ApplicationException {
		if (bookId == null)
			throw new ApplicationException("Invalid book Id");

		if (chapterId == 0)
			chapterId = 1;

		this.request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
		this.setVariable("action",
				this.config.get("default.base_url") + this.context.getAttribute("REQUEST_ACTION").toString());
		this.setVariable("base_url", String.valueOf(this.context.getAttribute("HTTP_HOST")));

		HttpSession session = this.request.getSession();
		if (session.getAttribute("usr") != null) {
			this.usr = (User) session.getAttribute("usr");

			this.setVariable("user.status", "");
			this.setVariable("user.profile",
					"<a href=\"javascript:void(0)\" onmousedown=\"profileMenu.show(event,'1')\">" + this.usr.getEmail()
							+ "</a>");
			this.setVariable("scripts", "$.ajax({url:\"" + this.getLink("services/getwords")
					+ "\",dataType:\"xml\",type:'GET'}).success(function(data){data=wordsXML(data);ldialog.show(data);});");
		} else {
			this.setVariable("user.status", "<a href=\"" + this.getLink("user/login") + "\">"
					+ this.getProperty("page.login.caption") + "</a>");
			this.setVariable("user.profile", "");
			this.setVariable("scripts", "");
		}

		this.bookId = bookId;
		this.chapterId = chapterId;
		this.sectionId = sectionId;

		book = book == null ? new book() : this.book;
		try {
			String lang = this.getLocale().toString();
			if (lang.equalsIgnoreCase("en_GB")) {
				lang = "en_US";
			}

			Table table = book.findWith("WHERE id=? and language=?", new Object[] { this.bookId, lang });

			if (table.size() > 0) {
				Row row = table.get(0);
				book.setData(row);
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		this.setVariable(new DataVariable("book", book), true);
		this.setText("book.info", new Object[] { book.getAuthor(), book.getPublishDate(), book.getPublisher() });

		String condition = "book_id=? and chapter_id=?";

		String where = "WHERE " + condition + " order by section_id";

		article article = new article();

		Table vtable = article.findWith(where, new Object[] { this.bookId, this.chapterId });

		this.maxChapter = article.setRequestFields("max(chapter_id) as max_chapter")
				.findWith("WHERE book_id=?", new Object[] { this.bookId }).get(0).get(0).get("max_chapter").intValue();
		this.lastChapterId = this.chapterId - 1 <= 0 ? 1 : this.chapterId - 1;
		this.nextChapterId = this.chapterId + 1 > this.maxChapter ? this.maxChapter : this.chapterId + 1;

		this.setVariable("chapterId", String.valueOf(this.chapterId));
		this.setVariable("sectionId", String.valueOf(this.sectionId));
		this.setVariable("maxchapter", String.valueOf(this.maxChapter));
		this.setVariable("lastchapter", String.valueOf(this.lastChapterId));
		this.setVariable("nextchapter", String.valueOf(this.nextChapterId));

		this.setText("holy.book.info", book.getName(), this.chapterId, this.maxChapter);

		this.setVariable(new DataVariable("book", book), true);

		int count = vtable.size();
		StringBuffer content = new StringBuffer();
		String line;

		if (count > 0) {
			int i;

			content.append("<div>");

			for (i = 0; i < count; i++) {
				Row item = vtable.get(i);
				article.setData(item);

				line = article.getContent();

				if (line != null) {
					if (i == 0 && line.trim().length() > 0 && line.charAt(0) != '<')
						line = "<span class='firstletter'>" + line.substring(0, 1) + "</span>"
								+ line.substring(1, line.length());
					if (line.contains("div")) {
						System.err.println(article.getId());
					}
					line = line.replaceAll("<br (.+?)(/?)>", "<br />").replaceAll("\n\n", "<br />");
					content.append("<span" + (this.sectionId == article.getSectionId() ? " class=\"selected\"" : "")
							+ ">" + line + "</span><p></p>");
				}

			}

			content.append("</div>");
		} else {
			content.append("暂时没有任何内容");
		}

		this.setVariable("content", content.toString());

		return this;
	}

	public String feed() throws ApplicationException {
		throw new ApplicationException("Invalid book Id");
	}

	public String feed(String bookId, int chapterId) throws ApplicationException {
		Element element = new Element();

		Element root = (Element) element.clone();
		root.setName("rss");
		root.setAttribute("version", "2.0");
		root.setAttribute("xmlns:content", "http://purl.org/rss/1.0/modules/content/");
		root.setAttribute("xmlns:wfw", "http://wellformedweb.org/CommentAPI/");
		root.setAttribute("xmlns:dc", "http://purl.org/dc/elements/1.1/");
		root.setAttribute("xmlns:atom", "http://www.w3.org/2005/Atom");
		root.setAttribute("xmlns:sy", "http://purl.org/rss/1.0/modules/syndication/");
		root.setAttribute("xmlns:slash", "http://purl.org/rss/1.0/modules/slash/");

		Element channel = (Element) element.clone();
		channel.setName("channel");

		Element title = (Element) element.clone();
		title.setName("title");
		title.setData(this.getProperty("application.title"));
		channel.addElement(title);

		Element atom_link = (Element) element.clone();
		atom_link.setName("atom:link");
		atom_link.setAttribute("href", this.getLink("bible").replaceAll("&", "&amp;"));
		atom_link.setAttribute("rel", "self");
		atom_link.setAttribute("type", "application/rss+xml");
		channel.addElement(atom_link);

		Element link = (Element) element.clone();
		link.setName("link");
		link.setData(this.getLink("bible").replaceAll("&", "&amp;"));
		channel.addElement(link);

		Element description = (Element) element.clone();
		description.setName("description");
		description.setData("<![CDATA[ " + this.getProperty("application.description") + " ]]>");
		channel.addElement(description);

		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("MM/dd"), full_format = new SimpleDateFormat("yyyy-MM-dd");
		Element lastBuildDate = (Element) element.clone();
		lastBuildDate.setName("lastBuildDate");

		lastBuildDate.setData(full_format.format(date));
		channel.addElement(lastBuildDate);

		Element generator = (Element) element.clone();
		generator.setName("generator");
		generator.setData("g");
		channel.addElement(generator);

		Element language = (Element) element.clone();
		language.setName("language");
		language.setData(this.getLocale().toString());
		channel.addElement(language);

		Element sy_updatePeriod = (Element) element.clone();
		sy_updatePeriod.setName("sy:updatePeriod");
		sy_updatePeriod.setData("daily");
		channel.addElement(sy_updatePeriod);

		Element sy_updateFrequency = (Element) element.clone();
		sy_updateFrequency.setName("sy:updateFrequency");
		sy_updateFrequency.setData("1");
		channel.addElement(sy_updateFrequency);

		Table vtable = this.load(bookId, chapterId, 0);

		Element item = new Element("item");
		Element item_title = new Element("title");
		item_title.setData("<![CDATA[" + this.book.getName() + " " + this.chapterId + "]]>");
		item.addElement(item_title);

		Element item_link = new Element("link");
		item_link.setData(this.getLink("bible").replace("&", "&amp;") + "/" + format.format(new Date()));
		item.addElement(item_link);

		Element item_comments = new Element("comments");
		item_comments.setData("");
		item.addElement(item_comments);

		Element item_pubDate = new Element("pubDate");
		item_pubDate.setData(full_format.format(date));
		item.addElement(item_pubDate);

		Element dc_creator = new Element("dc:creator");
		dc_creator.setData("<![CDATA[" + "Bible System" + "]]>");
		item.addElement(dc_creator);

		Element category = (Element) element.clone();
		category.setName("category");
		category.setData(format.format(date));
		item.addElement(category);

		Element guid = (Element) element.clone();
		guid.setName("guid");
		guid.setAttribute("isPermaLink", "true");
		guid.setData(this.getLink("feed").replace("&", "&amp;") + "/" + format.format(new Date()));
		item.addElement(guid);

		// start
		StringBuffer buffer = new StringBuffer();
		String finded;
		buffer.append("<ol style=\"list-style-type: none;\">");
		int count = vtable.size();
		if (count > 0) {
			Field fields;
			for (Enumeration<Row> table = vtable.elements(); table.hasMoreElements();) {
				Row row = table.nextElement();
				Iterator<Field> iterator = row.iterator();

				while (iterator.hasNext()) {
					fields = iterator.next();
					finded = fields.get("content").value().toString();
					buffer.append("<li"
							+ (this.sectionId == fields.get("section_id").intValue() ? " class=\"selected\"" : "")
							+ "><a class=\"sup\">" + fields.get("section_id").intValue() + "</a>" + finded + "</li>");
				}
			}
			buffer.append("</ol>");
		} else {
			buffer.append("暂时没有任何内容");
		}

		Element item_description = (Element) element.clone();
		item_description.setName("description");
		item_description.setData("<![CDATA[" + buffer + "]]>");

		item.addElement(item_description);

		Element content_encoded = (Element) element.clone();
		content_encoded.setName("content:encoded");
		content_encoded.setData("<![CDATA[" + buffer.toString() + "]]>");
		item.addElement(content_encoded);

		Element wfw_commentRss = (Element) element.clone();
		wfw_commentRss.setName("wfw:commentRss");
		wfw_commentRss.setData("");
		item.addElement(wfw_commentRss);

		Element slash_comments = (Element) element.clone();
		slash_comments.setName("slash:comments");
		slash_comments.setData("");
		item.addElement(slash_comments);

		channel.addElement(item);

		root.addElement(channel);
		// end

		this.response = (HttpServletResponse) this.context.getAttribute("HTTP_RESPONSE");

		this.response.setContentType("text/xml;charset=" + this.config.get("charset"));

		StringBuffer xbuffer = new StringBuffer();
		xbuffer.append("<?xml version=\"1.0\" encoding=\"" + this.config.get("charset") + "\"?>\r\n");
		xbuffer.append(root);

		return xbuffer.toString();
	}

	public Object book() throws ApplicationException {
		throw new ApplicationException("Invalid book Id");
	}

	public Object book(String bookId) throws ApplicationException {
		return this.book(bookId, 1);
	}

	public Object book(String bookId, int chapterId) throws ApplicationException {
		return this.book(bookId, chapterId, 0);
	}

	public Object book(String bookId, int chapterId, int sectionId) throws ApplicationException {
		StringBuffer xml = new StringBuffer();
		String finded = "";

		Table vtable = this.load(bookId, chapterId, sectionId);

		xml.append("<?xml version=\"1.0\" encoding=\"" + this.context.getAttribute("charset") + "\"?>");
		xml.append("<book id=\"book\" name=\"book\" bookId=\"" + this.bookId + "\" bookname=\"" + this.book.getName()
				+ "\" chapterId=\"" + this.chapterId + "\" maxchapter=\"" + this.maxChapter + "\" lastchapter=\""
				+ this.lastChapterId + "\" nextchapter=\"" + this.nextChapterId + "\">\r\n");
		Field fields;
		for (Enumeration<Row> table = vtable.elements(); table.hasMoreElements();) {
			Row row = table.nextElement();
			Iterator<Field> iterator = row.iterator();

			while (iterator.hasNext()) {
				fields = iterator.next();
				finded = fields.get("content").value().toString();
				if (this.sectionId == Integer.parseInt(fields.get("section_id").value().toString())) {
					xml.append("<item uid=\"" + fields.get("id").value().toString() + "\" id=\""
							+ fields.get("section_id").value().toString() + "\" selected=\"true\">" + finded
							+ "</item>");
				} else {
					xml.append("<item uid=\"" + fields.get("id").value().toString() + "\" id=\""
							+ fields.get("section_id").value().toString() + "\" selected=\"false\">" + finded
							+ "</item>");
				}
			}
		}
		xml.append("</book>");

		return xml.toString();
	}

	private Table load(String bookId, int chapterId, int sectionId) throws ApplicationException {

		this.bookId = bookId;
		this.chapterId = chapterId;
		this.sectionId = sectionId;

		book = new book();
		try {
			String lang = this.getLocale().toString();
			if (this.getLocale().toString().equalsIgnoreCase("en_GB")) {
				lang = "en_US";
			}

			Table table = book.findWith("WHERE id=? and language=?", new Object[] { this.bookId, lang });

			if (table.size() > 0) {
				Row row = table.get(0);
				book.setData(row);
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		String condition = "";

		if (this.chapterId == 0) {
			condition = "book_id=" + this.bookId;
		} else {
			condition = "book_id=" + this.bookId + " and chapter_id=" + this.chapterId;
		}

		String where = "WHERE " + condition + " order by section_id";

		article article = new article();
		Table vtable = article.findWith(where, new Object[] {});

		this.maxChapter = article.setRequestFields("max(chapter_id) as max_chapter")
				.findWith("WHERE book_id=?", new Object[] { this.bookId }).get(0).get(0).get("max_chapter").intValue();
		this.lastChapterId = this.chapterId - 1 <= 0 ? 1 : this.chapterId - 1;
		this.nextChapterId = this.chapterId + 1 > this.maxChapter ? this.maxChapter : this.chapterId + 1;

		this.setText("holy.book.info", book.getName(), this.chapterId, this.maxChapter);

		return vtable;
	}

	public Object api() throws ApplicationException {
		boolean valid = false;

		HttpServletRequest request = (HttpServletRequest) this.context.getAttribute("HTTP_REQUEST");
		HttpServletResponse response = (HttpServletResponse) this.context.getAttribute("HTTP_RESPONSE");

		String s = "Basic realm=\"Login for Book API\"";
		response.setHeader("WWW-Authenticate", s);

		// Get the Authorization header, if one was supplied
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null) {
			StringTokenizer st = new StringTokenizer(authHeader);
			if (st.hasMoreTokens()) {
				String basic = st.nextToken();

				// We only handle HTTP Basic authentication

				if (basic.equalsIgnoreCase("Basic")) {
					String credentials = st.nextToken();

					// This example uses sun.misc.* classes.
					// You will need to provide your own
					// if you are not comfortable with that.

					String userPass = new String(Base64.decode(credentials));
					// The decoded string is in the form
					// "userID:password".

					int p = userPass.indexOf(":");
					if (p != -1) {
						final String userID = userPass.substring(0, p);
						final String password = userPass.substring(p + 1);

						// Validate user ID and password
						// and set valid true true if valid.
						// In this example, we simply check
						// that neither field is blank

						if ((!userID.trim().equals("")) && (!password.trim().equals(""))) {
							Authentication context = new Authentication() {
								private boolean status;

								@Override
								public boolean approved() {
									return status & grant() != null;
								}

								@Override
								public Object grant() {
									return "Ok";
								}

								@Override
								public void identify(Credential credential, Map<String, Object> parameters) throws ApplicationException {
									status = userID.equals("Mover") && password.equals("ingod.asia");
								}
							};
							valid = context.approved();
						}
					}
				}
			}
		}

		// If the user was not validated, fail with a
		// 401 status code (UNAUTHORIZED) and
		// pass back a WWW-Authenticate header for
		// this servlet.
		//
		// Note that this is the normal situation the
		// first time you access the page. The client
		// web browser will prompt for userID and password
		// and cache them so that it doesn't have to
		// prompt you again.

		if (!valid) {
			response.setHeader("WWW-Authenticate", "Basic realm=\"Login for Bible API\"");
			response.setStatus(401);

			return "Forbidden! Authentication failed!";
		}

		return this.feed();
	}

	public static void main(String[] args) throws ApplicationException, IOException {
		book book = new book();
		book.setId("3104de7d-811e-4f6e-8ed6-0790ca948c06");
		book.findOneById();
		int p = 47, chapterId = 1;
		Pattern patt = Pattern.compile("^第(.+?)章");
		String s = "<div id=\"postmessage_(.+?)\" class=\"t_msgfont\">(.+?)(</div>)"; // <div id=\"postmessage_(.*)\"
																						// class=\"t_msgfont\">(.*)</\1>(.*)
		Pattern pattern = Pattern.compile(s, Pattern.DOTALL);
		int sectionId = 1;

		while (p <= 47) {
			URL url = new URL("http://www.old-gospel.net/viewthread.php?tid=446&extra=page%3D1&page=" + (p++));
			URLResourceLoader file = new URLResourceLoader(url);
			file.setCharset("gbk");
			String content = file.getContent().toString();
			Matcher m = pattern.matcher(content);
			article article = new article();
			article.setBookId(book.getId());

			while (m.find()) {
				content = m.group(2);
				article.setContent(content);

				article.setChapterId(chapterId);
				if (patt.matcher(content).find()) {
					sectionId = 1;
					article.setChapterId(chapterId++);
				} else {
					sectionId++;
					article.setChapterId(chapterId - 1);
				}

				article.setSectionId(sectionId);
				article.append();

				System.out.println(content);

			}
		}
	}
}