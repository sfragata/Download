package download;

import javax.swing.*;
import java.awt.BorderLayout;

/**
 * Classe base das Caixas de Diálogos com o Radios Buttons para os status
 * @author Silvio Fragata da Silva
 * @version 1.0
 */
public abstract class DownloadDialogBase extends DialogBase
{
	protected JPanel jPanelTotal = new JPanel();
	private JRadioButton[] jRadioButtonArray;

	/**
	 * Construtor
	 * @param frame Frame proprietário
	 * @param titulo Título
	 */
	public DownloadDialogBase(JFrame frame, String titulo)
	{
		super(frame, titulo, true);
		try
		{
			jbInit();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		jPanelTotal.setLayout(new BorderLayout());

		this.setLocationRelativeTo(null);
		JPanel panel2 = new JPanel();
		String[] status = StatusLink.getAllStatus();
		int size = status.length;
		ButtonGroup group = new ButtonGroup();
		jRadioButtonArray = new JRadioButton[size];
		for (int i = 0; i < size; i++)
		{
			JRadioButton jRadioButton = new JRadioButton(status[i]);
			jRadioButtonArray[i] = jRadioButton;
			panel2.add(jRadioButton);
			jRadioButton.setMnemonic(status[i].charAt(0));
			jRadioButton.setIcon(DownloadGUI.IMAGES[i]);
            jRadioButton.setSelectedIcon( DownloadGUI.IMAGES_PRESSED[i] );
			jRadioButton.setToolTipText("Link - " + status[i]);
			group.add(jRadioButton);
		}
		jPanelTotal.add(panel2, BorderLayout.SOUTH);
		this.getContentPane().add(jPanelTotal, BorderLayout.CENTER);
	}

	/**
	 * Retorna o Radio Button na posição passada
	 * @param pos posiçao
	 * @return o Radio Button
	 */
	protected JRadioButton getJRadioButton(int pos)
	{
		return jRadioButtonArray[pos];
	}

	/**
	 * Retorna o Radio Button selecionado
	 * @return o Radio Button selecionado
	 */
	protected int getJRadioButtonSelected()
	{
		for (int i = 0; i < jRadioButtonArray.length; i++)
		{
			if (jRadioButtonArray[i].isSelected())
			{
				return i;
			}
		}
		return 0;
	}
}