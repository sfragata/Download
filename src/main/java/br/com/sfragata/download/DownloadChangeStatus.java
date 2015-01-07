package br.com.sfragata.download;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;

/**
 * @author: Silvio Fragata da Silva
 */

public class DownloadChangeStatus extends DownloadDialogBase {
	private static final long serialVersionUID = -281842941600353092L;

	private StatusLink[] dadosArray = null;

	private int status = 1;

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

	public int getStatus() {
		return status;
	}

	private void jbInit() throws Exception {
		getJRadioButton(status).setSelected(true);
		Utils.center(this, 600, 100);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		int size = dadosArray.length;
		status = getJRadioButtonSelected();
		for (int i = 0; i < size; i++) {
			dadosArray[i].setStatus(status);
		}
		windowClosing(null);
	}
}