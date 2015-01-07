package br.com.sfragata.download;

import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * @author Silvio Fragata da Silva
 */
public class CellRenderer extends JLabel implements ListCellRenderer {
	private static final long serialVersionUID = -4786453613783747261L;

	final ImageIcon DIR_ICON = new ImageIcon(
			Utils.getResource("resources/folder.gif"));
	final ImageIcon DIR_ICON_PRESSED = new ImageIcon(getClass()
			.getClassLoader().getResource("resources/folder_pressed.gif"));

	final ImageIcon BROKEN_ICON = new ImageIcon(
			Utils.getResource("resources/linkquebrado.gif"));
	final ImageIcon BROKEN_ICON_PRESSED = new ImageIcon(getClass()
			.getClassLoader().getResource("resources/linkquebrado_pressed.gif"));

	final ImageIcon DOWNLOAD_ICON = new ImageIcon(
			Utils.getResource("resources/ok.gif"));
	final ImageIcon DOWNLOAD_ICON_PRESSED = new ImageIcon(getClass()
			.getClassLoader().getResource("resources/ok_pressed.gif"));

	final ImageIcon N_DOWNLOAD_ICON = new ImageIcon(
			Utils.getResource("resources/notget.gif"));
	final ImageIcon N_DOWNLOAD_ICON_PRESSED = new ImageIcon(getClass()
			.getClassLoader().getResource("resources/notget_pressed.gif"));

	final ImageIcon IN_USE_ICON = new ImageIcon(
			Utils.getResource("resources/inuse.gif"));

	/**
	 * Construtor
	 */
	public CellRenderer() {
		setOpaque(true);
		setFont(new java.awt.Font("Monospaced", Font.BOLD, 12));
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		StatusLink dados = (StatusLink) value;
		setText(dados.getUrl());
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		switch (dados.getStatus()) {
		case StatusLink.NOT_DOWNLOADED:
			setIcon(N_DOWNLOAD_ICON);
			setToolTipText(Utils.getMessages("notget"));
			break;
		case StatusLink.BROKEN:
			setIcon(BROKEN_ICON);
			setToolTipText(Utils.getMessages("broken"));
			break;
		case StatusLink.DOWNLOADED:
			setIcon(DOWNLOAD_ICON);
			setToolTipText(Utils.getMessages("get"));
			break;
		case StatusLink.FOLDER:
			setIcon(DIR_ICON);
			setToolTipText(Utils.getMessages("dir"));
			break;
		case StatusLink.IN_USE:
			setIcon(IN_USE_ICON);
			setToolTipText(Utils.getMessages("inuse"));
			break;
		}
		return this;
	}
}