package br.com.sfragata.download;

import java.awt.BorderLayout;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * @author Silvio Fragata da Silva
 */
public abstract class DownloadDialogBase extends DialogBase {
	private static final long serialVersionUID = 1541053110258051374L;

	protected JPanel jPanelTotal = new JPanel();

	private JRadioButton[] jRadioButtonArray;

	public DownloadDialogBase(JFrame frame, String titulo) {
		super(frame, titulo, true);
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init() throws Exception {
		jPanelTotal.setLayout(new BorderLayout());

		this.setLocationRelativeTo(null);
		JPanel panel2 = new JPanel();
		String[] status = StatusLink.getAllStatus();
		int size = status.length;
		ButtonGroup group = new ButtonGroup();
		jRadioButtonArray = new JRadioButton[size];
		for (int i = 0; i < size; i++) {
			JRadioButton jRadioButton = new JRadioButton(status[i]);
			jRadioButtonArray[i] = jRadioButton;
			panel2.add(jRadioButton);
			jRadioButton.setMnemonic(status[i].charAt(0));
			jRadioButton.setIcon(DownloadGUI.IMAGES[i]);
			jRadioButton.setSelectedIcon(DownloadGUI.IMAGES_PRESSED[i]);
			jRadioButton.setToolTipText("Link - " + status[i]);
			group.add(jRadioButton);
		}
		jPanelTotal.add(panel2, BorderLayout.SOUTH);
		this.getContentPane().add(jPanelTotal, BorderLayout.CENTER);
	}

	protected JRadioButton getJRadioButton(int pos) {
		return jRadioButtonArray[pos];
	}

	protected int getJRadioButtonSelected() {
		for (int i = 0; i < jRadioButtonArray.length; i++) {
			if (jRadioButtonArray[i].isSelected()) {
				return i;
			}
		}
		return 0;
	}
}