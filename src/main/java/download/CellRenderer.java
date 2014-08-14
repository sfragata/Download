/**
 * $Id: CellRenderer.java,v 1.2 2006/03/03 23:06:47 sfragata Exp $
 */

package download;

import java.awt.*;

import javax.swing.*;

/**
 * Classe para formatar o Jlist
 * @author Silvio Fragata da Silva
 * @version $Revision: 1.2 $
 */
public class CellRenderer extends JLabel implements ListCellRenderer
{
	private static final long serialVersionUID = -4786453613783747261L;
	
	final ImageIcon DIR_ICON =	new ImageIcon(getClass().getClassLoader().getResource("resources/folder.gif"));
    final ImageIcon DIR_ICON_PRESSED =	new ImageIcon(getClass().getClassLoader().getResource("resources/folder_pressed.gif"));

	final ImageIcon BROKEN_ICON = new ImageIcon(getClass().getClassLoader().getResource("resources/linkquebrado.gif"));
    final ImageIcon BROKEN_ICON_PRESSED = new ImageIcon(getClass().getClassLoader().getResource("resources/linkquebrado_pressed.gif"));

	final ImageIcon DOWNLOAD_ICON =	new ImageIcon(getClass().getClassLoader().getResource("resources/ok.gif"));
	final ImageIcon DOWNLOAD_ICON_PRESSED =	new ImageIcon(getClass().getClassLoader().getResource("resources/ok_pressed.gif"));

	final ImageIcon N_DOWNLOAD_ICON = new ImageIcon(getClass().getClassLoader().getResource("resources/notget.gif"));
    final ImageIcon N_DOWNLOAD_ICON_PRESSED = new ImageIcon(getClass().getClassLoader().getResource("resources/notget_pressed.gif"));

	final ImageIcon IN_USE_ICON = new ImageIcon(getClass().getClassLoader().getResource("resources/inuse.gif"));

	/**
	 *  Construtor */
	public CellRenderer()
	{
		setOpaque(true);
		setFont(new java.awt.Font("Monospaced", Font.BOLD, 12));
	}

	/**
	 * Gets the listCellRendererComponent attribute of the CellRenderer object
	 *
	 * @param list JList
	 * @param value Valor o JList selecionado
	 * @param index Indice do valor selecionado
	 * @param isSelected Se está selecionado ou não
	 * @param cellHasFocus Se a celula está fom o foco
	 * @return The Component
	 */
	public Component getListCellRendererComponent(
		JList list,
		Object value,
		int index,
		boolean isSelected,
		boolean cellHasFocus)
	{
		StatusLink dados = (StatusLink) value;
		setText(dados.getUrl());
		if (isSelected)
		{
			setBackground( list.getSelectionBackground() );
			setForeground( list.getSelectionForeground() );
		} 
		else
		{
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		switch (dados.getStatus())
		{
			case StatusLink.NAO_BAIXADO :
				setIcon(N_DOWNLOAD_ICON);
				setToolTipText(Utils.getMessages("notget"));
				break;
			case StatusLink.QUEBRADO :
				setIcon(BROKEN_ICON);
				setToolTipText(Utils.getMessages("broken"));
				break;
			case StatusLink.BAIXADO :
				setIcon(DOWNLOAD_ICON);
				setToolTipText(Utils.getMessages("get"));
				break;
			case StatusLink.DIRETORIO :
				setIcon(DIR_ICON);
				setToolTipText(Utils.getMessages("dir"));
				break;
			case StatusLink.EM_USO :
				setIcon(IN_USE_ICON);
				setToolTipText(Utils.getMessages("inuse"));
				break;
		}
		return this;
	}
}