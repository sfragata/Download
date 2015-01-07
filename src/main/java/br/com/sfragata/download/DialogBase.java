package br.com.sfragata.download;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;

import br.com.sfragata.download.listener.ButtonOkListener;


/**
 * @author Silvio Fragata da Silva
 */

public abstract class DialogBase extends JDialog implements ButtonOkListener,
		FocusListener {
	private static final long serialVersionUID = 8633461334604744156L;

	private JPanel jPanelNorth = new JPanel();
	private JPanel jPanelSouth = new JPanel();
	private JButton jButtonClose = new JButton();
	protected JButton jButtonOk = new JButton();
	protected JFrame frame = null;

	public DialogBase(JFrame frame, String title, boolean modal)
			throws HeadlessException {
		super(frame, title, true);
		try {
			this.frame = frame;
			initiate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initiate() throws Exception {
		jButtonClose.setText(Utils.getMessages("close"));
		jButtonClose.setMnemonic(KeyEvent.VK_F);
		jButtonFecharActionListener esc = new jButtonFecharActionListener();
		jPanelNorth.registerKeyboardAction(esc,
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		jButtonClose.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonCloseActionPerformed(e);
			}
		});
		jButtonOk.setText(Utils.getMessages("ok"));
		jButtonOk.setMnemonic(KeyEvent.VK_O);
		jButtonOk.addActionListener(this);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				windowClosing(e);
			}
		});
		this.getContentPane().add(jPanelNorth, BorderLayout.CENTER);
		this.getContentPane().add(jPanelSouth, BorderLayout.SOUTH);
		this.setResizable(false);
		jPanelSouth.add(jButtonOk, null);
		jPanelSouth.add(jButtonClose, null);
		getRootPane().setDefaultButton(jButtonOk);

	}

	void jButtonCloseActionPerformed(ActionEvent e) {
		close();
	}

	private void close() {
		if (frame == null) {
			System.exit(0);
		} else {
			this.dispose();
		}
	}

	public void focusGained(FocusEvent e) {
		Component c = ((Component) e.getSource());
		if (c instanceof JTextComponent)
			((JTextComponent) c).selectAll();
	}

	public void focusLost(FocusEvent e) {
	}

	protected class jButtonFecharActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			jButtonClose.doClick();
			jButtonCloseActionPerformed(e);
		}
	}

	void windowClosing(WindowEvent e) {
		close();
	}

	public void actionPerformed(ActionEvent e) {
		jButtonOk.requestFocus();
	}
}