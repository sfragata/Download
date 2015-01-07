package br.com.sfragata.download;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author Silvio Fragata da Silva
 */

public class ProxyConfig extends DialogBase {
	private static final long serialVersionUID = 7608520583447168958L;

	private JPanel jPanelConfig = new JPanel();

	private JPanel jPanelNorth = new JPanel();

	private JPanel jPanelCenter = new JPanel();
	private JTextField jTHost = new JTextField();
	private JTextField jTPort = new JTextField();

	private JLabel jLabelHost = new JLabel();
	private JLabel jLabelPort = new JLabel();

	private JCheckBox jCheckBoxUseProxy = new JCheckBox();

	public ProxyConfig(JFrame frame) {
		super(frame, Utils.getMessages("configtitle"), true);
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init() throws Exception {
		jCheckBoxUseProxy.setText(Utils.getMessages("useproxy"));
		jCheckBoxUseProxy
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jCheckBoxUseProxyActionPerformed(e);
					}
				});

		jLabelHost.setText(Utils.getMessages("host"));
		jLabelPort.setText(Utils.getMessages("port"));

		this.setResizable(false);

		jTPort.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				jTPortKeyTyped(e);
			}
		});
		jTHost.addFocusListener(this);

		jTPort.addFocusListener(this);

		this.getContentPane().add(jPanelConfig);

		jPanelConfig.setLayout(new BorderLayout());

		jPanelCenter.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Proxy"));

		jPanelConfig.add(jPanelNorth, BorderLayout.NORTH);
		jPanelConfig.add(jPanelCenter, BorderLayout.CENTER);

		jPanelNorth.setLayout(new BorderLayout());
		jPanelNorth.add(jCheckBoxUseProxy, BorderLayout.CENTER);

		jPanelCenter.setLayout(new GridLayout(2, 2));
		jPanelCenter.add(jLabelHost);
		jPanelCenter.add(jTHost);
		jPanelCenter.add(jLabelPort);
		jPanelCenter.add(jTPort);

		Utils.center(this, 500, 160);

		setEnableFields(false);

		getConfigValues();

		setVisible(true);
	}

	private void getConfigValues() {
		String[] val = Utils.getValores();
		if (val != null) {
			String useProxy = val[0];
			String host = val[1];
			String port = val[2];
			if (useProxy.equalsIgnoreCase("true")) {
				jTHost.setText(host);
				jTPort.setText(port);
				jCheckBoxUseProxy.setSelected(Boolean.valueOf(useProxy)
						.booleanValue());
				Utils.setSystemProp(val);
				setEnableFields(jCheckBoxUseProxy.isSelected());
			}
		}
	}

	private void setConfigValues() {
		String useProxy = String.valueOf(jCheckBoxUseProxy.isSelected());
		String host = jTHost.getText();
		String port = jTPort.getText();
		if (useProxy.equalsIgnoreCase("true")) {
			String[] val = new String[] { useProxy, host, port };
			Utils.setSystemProp(val);
			Utils.storeSystemProp(val);
		} else {
			Utils.removeStoreSystemProp();
			Utils.removeSystemProp();
		}
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		setConfigValues();
		close();
	}

	void jTPortKeyTyped(KeyEvent e) {
		avoidCharacter(e);
	}

	private void avoidCharacter(KeyEvent e) {
		if (!Character.isDigit(e.getKeyChar())) {
			e.consume();
		}
	}

	private void jCheckBoxUseProxyActionPerformed(ActionEvent e) {
		setEnableFields(jCheckBoxUseProxy.isSelected());
	}

	private void setEnableFields(boolean cond) {
		jTHost.setEnabled(cond);
		jTPort.setEnabled(cond);
	}

	private void close() {
		if (frame == null) {
			System.exit(0);
		} else {
			this.dispose();
		}
	}

}