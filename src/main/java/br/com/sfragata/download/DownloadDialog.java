package br.com.sfragata.download;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author: Silvio Fragata da Silva
 */
public class DownloadDialog extends DownloadDialogBase {
	private static final long serialVersionUID = -8836006504603733212L;

	private JPanel jPanelUrl = new JPanel();

	private JLabel jLabelUrl = new JLabel();

	private JTextField jTextFieldUrl = new JTextField();

	protected StatusLink statusLink = null;

	public StatusLink getStatusLink() {
		return statusLink;
	}

	public void setStatusLink(StatusLink s) {
		statusLink = s;
	}

	public DownloadDialog(JFrame frame, StatusLink statusLink, String titulo) {
		super(frame, titulo);
		if (statusLink == null) {
			statusLink = new StatusLink("", 1);
		}
		setStatusLink(statusLink);
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void jbInit() throws Exception {
		jLabelUrl.setText(Utils.getMessages("typeurl"));
		jTextFieldUrl.setColumns(40);
		jTextFieldUrl.setText(statusLink.getUrl());
		jTextFieldUrl.addFocusListener(this);

		JPanel panelUrl = new JPanel();
		jPanelUrl.setLayout(new BorderLayout());
		panelUrl.add(jLabelUrl, null);
		panelUrl.add(jTextFieldUrl, null);

		jPanelUrl.add(panelUrl, BorderLayout.NORTH);
		jPanelTotal.add(jPanelUrl, BorderLayout.NORTH);
		getJRadioButton(statusLink.getStatus()).setSelected(true);
		Utils.center(this, 600, 130);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		statusLink = new StatusLink(jTextFieldUrl.getText().trim(),
				getJRadioButtonSelected());
		windowClosing(null);
	}
}