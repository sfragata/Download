package br.com.sfragata.download;

/**
 * @author Silvio Fragata da Silva
 */
public class StatusLink {

	private String url;
	private int status;

	public final static int DOWNLOADED = 0;

	public final static int NOT_DOWNLOADED = 1;

	public final static int BROKEN = 2;

	public final static int FOLDER = 3;

	public final static int IN_USE = 4;

	private final static String[] STATUS_ARRAY = {
			Utils.getMessages("status.downloaded"),
			Utils.getMessages("status.notdownloaded"),
			Utils.getMessages("status.broken"),
			Utils.getMessages("status.folder") };

	public static String[] getAllStatus() {
		return STATUS_ARRAY;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof StatusLink) {
			StatusLink obj = (StatusLink) other;
			return obj.getUrl().equalsIgnoreCase(url);
		}
		return false;
	}

	public StatusLink(String url, int status) {
		this.url = url;
		this.status = status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getStatus() {
		return this.status;
	}

	public String getUrl() {
		return this.url;
	}

	public String getFilename() {
		String separator = "";
		if (this.url.startsWith("http") || this.url.startsWith("https")
				|| this.url.startsWith("ftp")) {
			separator = "/";
		} else {
			separator = java.io.File.separator;
		}
		int index = this.url.lastIndexOf(separator) + 1;
		return this.url.substring(index);
	}

	@Override
	public String toString() {
		String text = "StatusLink:{" + getUrl();
		if (status != IN_USE) {
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
		int index = url.indexOf("/", url.indexOf(protocol) + protocol.length());
		if (index != -1)
			link = url.substring(0, index);
		return link.substring(protocol.length());
	}

	public String getTargetFile() {
		return getParentLink() + "_" + getFilename();
	}

}