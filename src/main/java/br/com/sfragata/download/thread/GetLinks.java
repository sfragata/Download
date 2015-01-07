package br.com.sfragata.download.thread;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.ElementIterator;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

import br.com.sfragata.download.StatusLink;
import br.com.sfragata.download.Utils;
import br.com.sfragata.download.listener.MensagemListener;


/**
 * @author Silvio Fragata da Siva
 */
public class GetLinks implements Runnable {
	private String link;

	private MensagemListener listener;

	private List<String> filters;

	public GetLinks(String link, List<String> filters) {
		this.link = link;
		this.filters = filters;
	}

	public void addListener(MensagemListener listener) {
		this.listener = listener;
	}

	private void setText(String texto) {
		if (listener != null)
			listener.setText(texto);
	}

	private int getSize() {
		if (listener != null)
			return listener.getSizeList();
		return 0;
	}

	private void addItemLista(StatusLink d) {
		if (listener != null)
			listener.addList(d);
	}

	private void log(String msg) {
		if (listener != null)
			listener.log(msg);
	}

	private void fatal(Exception exception) {
		if (listener != null)
			listener.fatal(exception);
	}

	private Reader getReader(String uri) throws IOException {
		if (uri.startsWith("http:")) {
			// Retrieve from Internet.
			URLConnection conn = new URL(uri).openConnection();
			return new InputStreamReader(conn.getInputStream());
		} else {
			// Retrieve from file.
			return new FileReader(uri);
		}
	}

	private void getLink(String href) {
		if (href != null && !href.equalsIgnoreCase("") && testFilters(href)) {
			log(Utils.getMessages("url.found") + href);
			if (!(href.startsWith("mailto:") || href.startsWith("javascript:"))) {
				if (!(href.startsWith("http://") || href.startsWith("ftp://"))) {
					if (href.startsWith("www.")) {
						href = "http://" + href;
					} else if (href.startsWith("/")) {
						href = new StringBuffer(href)
								.insert(0,
										link.substring(
												link.indexOf("//") + 2,
												link.indexOf("/",
														link.indexOf("//") + 2)))
								.insert(0,
										link.substring(0,
												link.indexOf("//") + 2))
								.toString();
					} else if (!href.startsWith(".")) {
						href = new StringBuffer(href).insert(0,
								link.substring(0, link.lastIndexOf("/") + 1))
								.toString();
					}
				}
				href = href.replaceAll("&amp;", "&");
				StatusLink d = new StatusLink(href, StatusLink.NOT_DOWNLOADED);
				addItemLista(d);

			}
		}
	}

	private boolean testFilters(String href) {
		if (filters.isEmpty())
			return true;
		for (String filter : filters) {
			if (href.endsWith(filter))
				return true;
		}
		return false;
	}

	/**
	 * Main processing method for the GetLinks object
	 */
	public void run() {
		try {
			setText(Utils.getMessages("getlinksmessage"));
			EditorKit kit = new HTMLEditorKit();
			Document doc = kit.createDefaultDocument();
			if (!(link.endsWith(".htm") || link.endsWith(".html"))) {
				link = link.concat("/");
			}
			int listaInicial = getSize();
			// The Document class does not yet handle charset's properly.
			doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
			// Create a reader on the HTML content.
			Reader rd = getReader(link);
			// Parse the HTML.
			kit.read(rd, doc, 0);
			// Iterate through the elements of the HTML document.
			ElementIterator it = new ElementIterator(doc);
			javax.swing.text.Element elem;
			while ((elem = it.next()) != null) {
				SimpleAttributeSet s = (SimpleAttributeSet) elem
						.getAttributes().getAttribute(HTML.Tag.A);
				if (s != null) {
					String href = (String) s.getAttribute(HTML.Attribute.HREF);
					getLink(href);
				}
			}
			if (getSize() > listaInicial) {
				setText(Utils.getMessages("linkscreated"));
			} else {
				setText(Utils.getMessages("nolinks"));
				log(Utils.getMessages("nolinks"));
			}
		} catch (ConnectException ex) {
			setText(Utils.getMessages("connectionerror"));
			fatal(ex);
		} catch (Exception ex) {
			setText(ex.getMessage());
			fatal(ex);
		}
	}
}