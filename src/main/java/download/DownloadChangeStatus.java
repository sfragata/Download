package download;

import java.awt.event.*;

import javax.swing.*;

/**
 * Caixa de Diálogo serve para alteração de status dos links
 * 
 * @author: Silvio Fragata da Silva
 */

public class DownloadChangeStatus extends DownloadDialogBase {
	private static final long serialVersionUID = -281842941600353092L;

	private JPanel jPanelTotal = new JPanel();

	private JPanel jPanelUrl = new JPanel();

	private JPanel jPanelBotao = new JPanel();

	private JButton jButtonFechar = new JButton();

	private JButton jButtonOk = new JButton();

	private JRadioButton[] jRadioButtonArray;

	private StatusLink[] dadosArray = null;

	private int status = 1;

	/**
	 * Construtor
	 * 
	 * @param frame
	 *            Frame proprietário
	 * @param dadosArray
	 *            Array de status de links
	 * @param titulo
	 *            Título
	 */
	public DownloadChangeStatus(JFrame frame, StatusLink[] dadosArray,
			String titulo) {
		super(frame, titulo);
		this.dadosArray = dadosArray;
		if (dadosArray.length == 1) {
			status = dadosArray[0].getStatus();
		}
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retornar o status do link
	 * 
	 * @return o status do link
	 */
	public int getStatus() {
		return status;
	}

	private void jbInit() throws Exception {
		getJRadioButton(status).setSelected(true);
		Utils.center(this, 600, 100);
		setVisible(true);
	}

	/**
	 * Ação do botão de OK
	 * 
	 * @param e
	 *            Description of the Parameter
	 */
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		int size = dadosArray.length;
		status = getJRadioButtonSelected();
		for (int i = 0; i < size; i++) {
			dadosArray[i].setStatus(status);
		}
		this_windowClosing(null);
	}
}