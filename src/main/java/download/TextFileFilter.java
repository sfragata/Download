/**
 * $Id: TextFileFilter.java,v 1.2 2006/03/03 23:14:03 sfragata Exp $
 */

package download;

import java.io.*;
import java.util.*;

/**
 * @author Silvio Fragata da Silva
 * @version $Revision: 1.2 $
 */
public class TextFileFilter extends javax.swing.filechooser.FileFilter {
	private String TYPE_UNKNOWN = "Type Unknown";

	private String HIDDEN_FILE = "Hidden File";

	private Hashtable filters = null;

	private String description = null;

	private String fullDescription = null;

	private boolean useExtensionsInDescription = true;

	/**
	 * Creates a file filter. If no filters are added, then all files are
	 * accepted.
	 * 
	 * @see #addExtension
	 */
	public TextFileFilter() {
		this.filters = new Hashtable();
	}

	/**
	 * Creates a file filter that accepts files with the given extension.
	 * Example: new ExampleFileFilter("jpg");
	 * 
	 * @param extension
	 * @see #addExtension
	 */
	public TextFileFilter(String extension) {
		this(extension, null);
	}

	/**
	 * Creates a file filter that accepts the given file type. Example: new
	 * ExampleFileFilter("jpg", "JPEG Image Images"); Note that the "." before
	 * the extension is not needed. If provided, it will be ignored.
	 * 
	 * @param extension
	 * @param description
	 * @see #addExtension
	 */
	public TextFileFilter(String extension, String description) {
		this();
		if (extension != null) {
			addExtension(extension);
		}
		if (description != null) {
			setDescription(description);
		}
	}

	/**
	 * Creates a file filter from the given string array. Note that the "."
	 * before the extension is not needed adn will be ignored.
	 * 
	 * @param filters
	 * @see #addExtension
	 */
	public TextFileFilter(String[] filters) {
		this(filters, null);
	}

	/**
	 * Creates a file filter from the given string array and description. Note
	 * that the "." before the extension is not needed and will be ignored.
	 * 
	 * @param filters
	 * @param description
	 * @see #addExtension
	 */
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

	/**
	 * Return true if this file should be shown in the directory pane, false if
	 * it shouldn't. changed for Silvio: If file is hidden, it´s not show Files
	 * that begin with "." are ignored.
	 * 
	 * @param f
	 *            DOCUMENT ME!
	 * @return DOCUMENT ME!
	 * @see #getExtension
	 * @see FileFilter#accepts
	 */
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

	/**
	 * Return the extension portion of the file's name .
	 * 
	 * @param f
	 *            DOCUMENT ME!
	 * @return DOCUMENT ME!
	 * @see #getExtension
	 * @see FileFilter#accept
	 */
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

	/**
	 * Adds a filetype "dot" extension to filter against. For example: the
	 * following code will create a filter that filters out all files except
	 * those that end in ".jpg" and ".tif": ExampleFileFilter filter = new
	 * ExampleFileFilter(); filter.addExtension("jpg");
	 * filter.addExtension("tif"); Note that the "." before the extension is not
	 * needed and will be ignored.
	 * 
	 * @param extension
	 *            DOCUMENT ME!
	 */
	public void addExtension(String extension) {
		if (filters == null) {
			filters = new Hashtable(5);
		}
		filters.put(extension.toLowerCase(), this);
		fullDescription = null;
	}

	/**
	 * Returns the human readable description of this filter. For example: "JPEG
	 * and GIF Image Files (.jpg, .gif)"
	 * 
	 * @return DOCUMENT ME!
	 * @see setDescription
	 * @see setExtensionListInDescription
	 * @see isExtensionListInDescription
	 * @see FileFilter#getDescription
	 * @uml.property name="description"
	 */
	public String getDescription() {
		if (fullDescription == null) {
			if ((description == null) || isExtensionListInDescription()) {
				fullDescription = (description == null) ? "(" : description
						+ " (";

				// build the description from the extension list
				Enumeration extensions = filters.keys();
				if (extensions != null && extensions.hasMoreElements()) {
					fullDescription += ("." + (String) extensions.nextElement());
					while (extensions.hasMoreElements()) {
						fullDescription += (", " + (String) extensions
								.nextElement());
					}
				}
				fullDescription += ")";
			} else {
				fullDescription = description;
			}
		}
		return fullDescription;
	}

	/**
	 * Sets the human readable description of this filter. For example:
	 * filter.setDescription("Gif and JPG Images");
	 * 
	 * @param description
	 *            DOCUMENT ME!
	 * @see setDescription
	 * @see setExtensionListInDescription
	 * @see isExtensionListInDescription
	 * @uml.property name="description"
	 */
	public void setDescription(String description) {
		this.description = description;
		fullDescription = null;
	}

	/**
	 * Determines whether the extension list (.jpg, .gif, etc) should show up in
	 * the human readable description. Only relevent if a description was
	 * provided in the constructor or using setDescription();
	 * 
	 * @param b
	 *            DOCUMENT ME!
	 * @see getDescription
	 * @see setDescription
	 * @see isExtensionListInDescription
	 */
	public void setExtensionListInDescription(boolean b) {
		useExtensionsInDescription = b;
		fullDescription = null;
	}

	/**
	 * Returns whether the extension list (.jpg, .gif, etc) should show up in
	 * the human readable description. Only relevent if a description was
	 * provided in the constructor or using setDescription();
	 * 
	 * @return DOCUMENT ME!
	 * @see getDescription
	 * @see setDescription
	 * @see setExtensionListInDescription
	 */
	public boolean isExtensionListInDescription() {
		return useExtensionsInDescription;
	}
}