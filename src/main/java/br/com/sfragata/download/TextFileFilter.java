package br.com.sfragata.download;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TextFileFilter extends javax.swing.filechooser.FileFilter {
	private Map<String, TextFileFilter> filters = new HashMap<String, TextFileFilter>();

	private String description = null;

	private String fullDescription = null;

	private boolean useExtensionsInDescription = true;

	public TextFileFilter() {
	}

	public TextFileFilter(String extension) {
		this(extension, null);
	}

	public TextFileFilter(String extension, String description) {
		this();
		if (extension != null) {
			addExtension(extension);
		}
		if (description != null) {
			setDescription(description);
		}
	}

	public TextFileFilter(String[] filters) {
		this(filters, null);
	}

	public TextFileFilter(String[] filters, String description) {
		this();
		for (int i = 0; i < filters.length; i++) {
			// add filters one by one
			addExtension(filters[i]);
		}
		if (description != null) {
			setDescription(description);
		}
	}

	public boolean accept(File f) {
		if ((f != null) && !f.isHidden()) {
			if (f.isDirectory()) {
				return true;
			}

			String extension = getExtension(f);

			if ((extension != null) && (filters.get(getExtension(f)) != null)) {
				return true;
			}
		}
		return false;
	}

	public String getExtension(File f) {
		if (f != null) {
			String filename = f.getName();
			int i = filename.lastIndexOf('.');

			if ((i > 0) && (i < (filename.length() - 1))) {
				return filename.substring(i + 1).toLowerCase();
			}
		}
		return null;
	}

	public void addExtension(String extension) {
		filters.put(extension.toLowerCase(), this);
		fullDescription = null;
	}

	public String getDescription() {
		if (fullDescription == null) {
			if ((description == null) || isExtensionListInDescription()) {
				fullDescription = (description == null) ? "(" : description
						+ " (";

				// build the description from the extension list
				Set<String> extensions = filters.keySet();
				boolean firstTime = false;
				for (String ext : extensions) {
					if (!firstTime) {
						fullDescription += "." + ext;
						firstTime = true;
					} else {
						fullDescription += ", " + ext;
					}
				}
				fullDescription += ")";
			} else {
				fullDescription = description;
			}
		}
		return fullDescription;
	}

	public void setDescription(String description) {
		this.description = description;
		fullDescription = null;
	}

	public void setExtensionListInDescription(boolean b) {
		useExtensionsInDescription = b;
		fullDescription = null;
	}

	public boolean isExtensionListInDescription() {
		return useExtensionsInDescription;
	}
}