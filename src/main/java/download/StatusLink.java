/**
 * $Id: StatusLink.java,v 1.3 2006/03/11 16:34:52 sfragata Exp $
 */

package download;

/**
 * Classe que representa os possíveis status de um link
 * 
 * @author Silvio Fragata da Silva
 * @version $Revision: 1.3 $
 */
public class StatusLink {
	private String url;

	private int status;

	public final static int BAIXADO = 0;

	public final static int NAO_BAIXADO = 1;

	public final static int QUEBRADO = 2;

	public final static int DIRETORIO = 3;

	public final static int EM_USO = 4;

	private final static String[] STATUS_ARRAY = { "Baixado", "Não Baixado",
			"Quebrado", "Diretório" };

	/**
	 * Retorna todos os status possíveis
	 * 
	 * @return Retorna todos os status possíveis
	 */
	public static String[] getAllStatus() {
		return STATUS_ARRAY;
	}

	public boolean equals(Object other) {
		if (other instanceof StatusLink) {
			StatusLink obj = (StatusLink) other;
			return obj.getUrl().equalsIgnoreCase(url);
			// && obj.getStatus() == status;
		}
		return false;
	}

	/**
	 * Constructor
	 * 
	 * @param url
	 *            URL
	 * @param status
	 *            Status
	 */
	public StatusLink(String url, int status) {
		this.url = url;
		this.status = status;
	}

	/**
	 * Altera o status
	 * 
	 * @param status
	 *            o novo status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Altera a URL
	 * 
	 * @param url
	 *            nava url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Retorna o status do link
	 * 
	 * @return O status do link
	 */
	public int getStatus() {
		return this.status;
	}

	/**
	 * Retorna a url
	 * 
	 * @return a url
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * Retorna o nome do arquivo
	 * 
	 * @return o nome do arquivo
	 */
	public String getNomeArquivo() {
		String separator = "";
		if (this.url.startsWith("http") || this.url.startsWith("https") || this.url.startsWith("ftp")) {
			separator = "/";
		} else {
			separator = java.io.File.separator;
		}
		int index = this.url.lastIndexOf(separator) + 1;
		return this.url.substring(index);
	}

	public String toString() {
		String text = "StatusLink:{" + getUrl();
		if (status != EM_USO) {
			text += " - " + STATUS_ARRAY[status] + "}";
		}
		return text;
	}

	private String getProtocol() {
		String protocol = "";
		if (url.startsWith("http://"))
			protocol = "http://";
		else if (url.startsWith("https://"))
			protocol = "https://";
		else if (url.startsWith("ftp://"))
			protocol = "ftp://";
		return protocol;
	}

	private String getParentLink() {
		String protocol = getProtocol();
		String link = url;
		int index = url.indexOf("/", url.indexOf(protocol)
				+ protocol.length());
		if (index != -1)
			link = url.substring(0, index);
		return link.substring( protocol.length() );
	}
	
	public String getDestFile(){
		return getParentLink() + "_" + getNomeArquivo();
	}

}