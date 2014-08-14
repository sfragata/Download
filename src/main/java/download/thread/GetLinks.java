package download.thread;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;

import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.ElementIterator;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

import download.StatusLink;
import download.Utils;
import download.listener.MensagemListener;

/**
 * Classe que a partir de um link, obtem outros links
 * 
 * @author Silvio Fragata da Siva
 */
public class GetLinks implements Runnable {
	private String link;

	private MensagemListener listener;

	private List filters;

	/**
	 * Construtor
	 * 
	 * @param link
	 *            O link
	 * @param filters
	 *            TODO
	 * @param listener
	 *            O listener para atualizar as mensagens
	 * @throws Exception
	 *             Exception
	 */
	public GetLinks(String link, List filters) {
		this.link = link;
		this.filters = filters;
	}

	/**
	 * Método que adiciona o listener de mensagme
	 * 
	 * @param listener
	 */
	public void addListener(MensagemListener listener) {
		this.listener = listener;
	}

	private void setText(String texto) {
		if (listener != null)
			listener.setTexto(texto);
	}

	private int getSize() {
		if (listener != null)
			return listener.getSizeList();
		return 0;
	}

	private void addItemLista(StatusLink d) {
		if (listener != null)
			listener.addLista(d);
	}

	private void log(String msg) {
		if (listener != null)
			listener.log(msg);
	}

	private void fatal(Exception exception) {
		if (listener != null)
			listener.fatal(exception);
	}

	// Returns a reader on the HTML data. If 'uri' begins
	// with "http:", it's treated as a URL; otherwise,
	// it's assumed to be a local filename.
	/**
	 * Gets the reader attribute of the GetLinks object
	 * 
	 * @param uri
	 *            Description of the Parameter
	 * @return The reader value
	 * @exception IOException
	 *                Description of the Exception
	 */
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
						href = new StringBuffer(href).insert(
								0,
								link.substring(link.indexOf("//") + 2, link
										.indexOf("/", link.indexOf("//") + 2)))
								.insert(
										0,
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
				StatusLink d = new StatusLink(href, StatusLink.NAO_BAIXADO);
				addItemLista(d);

			}
		}
	}

	private boolean testFilters(String href) {
		if (filters.isEmpty())
			return true;
		for (Iterator iter = filters.iterator(); iter.hasNext();) {
			String filtro = (String) iter.next();
			if (href.endsWith(filtro))
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
//				SimpleAttributeSet img = (SimpleAttributeSet) elem
//						.getAttributes().getAttribute(HTML.Tag.IMG);
//				if (img != null) {
//					String href = (String) s.getAttribute(HTML.Attribute.SRC);
//					getLink(href);
//				}
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