package br.com.sfragata.download;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.com.sfragata.download.listener.LogListener;


/**
 * @author Silvio Fragata da Silva
 */
public class BatchLinkCreatorFrame extends DialogBase implements LogListener {
	private static final long serialVersionUID = 6064307323765189267L;

	private JPanel jPanelBatchLinkCreator = new JPanel();
	private JPanel jPanelNorth = new JPanel();
	private JPanel jPanelCenter = new JPanel();
	private JLabel jLabelMessage = new JLabel();
	private final int MAX_NUMBER = 4;
	private JPanel jPanelNorthSouth = new JPanel();
	private JPanel jPanelNorthNorth = new JPanel();
	private JTextField jTUrl = new JTextField();
	private JLabel jLabelURL = new JLabel();
	private JCheckBox jCheckBoxZeros = new JCheckBox();
	private DefaultListModel listModel;
	private JFrame frame;
	private JPanel jPanelNorthCenter = new JPanel();
	private JLabel jLabelStartRange = new JLabel();
	private JTextField jTStartRange = new JTextField();
	private JLabel jLabelEndRange = new JLabel();
	private JTextField jTEndRange = new JTextField();

	public BatchLinkCreatorFrame(JFrame frame, DefaultListModel listModel) {
		super(frame, Utils.getMessages("batchLinkCreatortitle"), true);
		try {
			this.listModel = listModel;
			init();
		} catch (Exception e) {
			fatal(e);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @exception Exception
	 *                Description of the Exception
	 */
	private void init() throws Exception {
		jTUrl.setColumns(30);
		jTUrl.addFocusListener(this);
		jLabelURL.setText(Utils.getMessages("typeurl"));
		jLabelURL.setForeground(Color.black);
		jCheckBoxZeros.setSelected(true);
		jCheckBoxZeros.setText(Utils.getMessages("zerolenght"));
		jPanelNorth.setLayout(new BorderLayout());

		this.setResizable(false);
		jLabelStartRange.setText(Utils.getMessages("begin"));
		jLabelStartRange.setForeground(Color.black);
		jTStartRange.setColumns(4);
		jTStartRange.setText("0");
		jTStartRange.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				jTStartRangeKeyTyped(e);
			}
		});
		jTStartRange.addFocusListener(this);

		jLabelEndRange.setText(Utils.getMessages("end"));
		jLabelEndRange.setForeground(Color.black);
		jTEndRange.setColumns(4);
		jTEndRange.setText("0");
		jTEndRange.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				jTEndRangeKeyTyped(e);
			}
		});
		jTEndRange.addFocusListener(this);

		this.getContentPane().add(jPanelBatchLinkCreator);

		jPanelBatchLinkCreator.setLayout(new BorderLayout());
		jPanelBatchLinkCreator.add(jPanelNorth, BorderLayout.NORTH);

		jPanelNorth.add(jPanelNorthNorth, BorderLayout.NORTH);

		jPanelNorthNorth.add(jLabelURL, null);
		jPanelNorthNorth.add(jTUrl, null);

		jPanelNorth.add(jPanelNorthSouth, BorderLayout.SOUTH);

		jPanelNorthSouth.add(jCheckBoxZeros, null);
		jPanelNorth.add(jPanelNorthCenter, BorderLayout.CENTER);
		jPanelNorthCenter.add(jLabelStartRange, null);
		jPanelNorthCenter.add(jTStartRange, null);
		jPanelNorthCenter.add(jLabelEndRange, null);
		jPanelNorthCenter.add(jTEndRange, null);

		jPanelBatchLinkCreator.add(jPanelCenter, BorderLayout.CENTER);
		jPanelCenter.add(jLabelMessage, null);
		jLabelMessage.setForeground(Color.red);

		Utils.center(this, 450, 200);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		jLabelMessage.setText(Utils.getMessages("getlinksmessage"));
		try {
			new BatchLinkCreator(listModel, jTUrl.getText().trim(),
					Integer.parseInt(jTStartRange.getText().trim()),
					Integer.parseInt(jTEndRange.getText().trim()),
					jCheckBoxZeros.isSelected());
			jLabelMessage.setText(Utils.getMessages("linkscreated"));
		} catch (Exception ex) {
			error(ex.getMessage());
			jLabelMessage.setText(ex.getMessage());
		}
	}

	/**
	 * @param e
	 *            Description of the Parameter
	 */
	private void jTStartRangeKeyTyped(KeyEvent e) {
		onlyDigits(e);
		if (jTStartRange.getText().trim().length() == MAX_NUMBER - 1) {
			jTStartRange.transferFocus();
		}
	}

	private void onlyDigits(KeyEvent e) {
		if (!Character.isDigit(e.getKeyChar())) {
			e.consume();
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param e
	 *            Description of the Parameter
	 */
	private void jTEndRangeKeyTyped(KeyEvent e) {
		onlyDigits(e);
		if (jTEndRange.getText().trim().length() == MAX_NUMBER - 1) {
			jTEndRange.transferFocus();
		}
	}

	public void log(String log) {
		if (frame instanceof DownloadGUI) {
			((DownloadGUI) frame).log(log);
		}
	}

	public void fatal(Exception ex) {
		if (frame instanceof DownloadGUI) {
			((DownloadGUI) frame).fatal(ex);
		}
	}

	public void error(String error) {
		if (frame instanceof DownloadGUI) {
			((DownloadGUI) frame).error(error);
		}
	}
}