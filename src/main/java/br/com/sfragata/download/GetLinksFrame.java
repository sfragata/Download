package br.com.sfragata.download;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import br.com.sfragata.download.listener.MensagemListener;
import br.com.sfragata.download.thread.GetLinks;


/**
 * @author Silvio Fragata da Silva
 */
public class GetLinksFrame extends DialogBase implements MensagemListener {
	private static final long serialVersionUID = -3578987691546708651L;

	private JPanel jPanelGetLinks = new JPanel();

	private JPanel jPanelDialogNorth = new JPanel();

	private JPanel jPanelCenter = new JPanel();

	private JLabel jLabelMenssage = new JLabel();

	private JPanel jPanelDialogNorthCenter = new JPanel();

	private JTextField jTUrl = new JTextField();

	private JLabel jLabelURL = new JLabel();

	private DefaultListModel listModel;

	private String[] filters = { ".jpeg", ".jpg", ".gif", ".mpg", ".mpeg",
			".avi", ".mov", ".wmv" };

	private JCheckBox[] checkBoxFilters;

	private JPanel jPanelFilters = new JPanel();

	public GetLinksFrame(JFrame frame, DefaultListModel listModel) {
		super(frame, "Get Links", true);
		try {
			this.listModel = listModel;
			init();
		} catch (Exception e) {
			fatal(e);
		}
	}

	private void init() throws Exception {
		checkBoxFilters = new JCheckBox[filters.length];

		jTUrl.setColumns(35);
		jTUrl.addFocusListener(this);

		jLabelURL.setText(Utils.getMessages("typeurl"));
		jLabelURL.setForeground(Color.black);

		jLabelMenssage.setForeground(Color.red);
		jLabelMenssage.setText("");

		jPanelDialogNorthCenter.add(jLabelURL, null);
		jPanelDialogNorthCenter.add(jTUrl, null);

		jPanelCenter.setLayout(new FlowLayout());
		jPanelCenter.add(jLabelMenssage, null);

		jPanelDialogNorth.setLayout(new BorderLayout());
		jPanelDialogNorth.add(jPanelDialogNorthCenter, BorderLayout.CENTER);
		jPanelDialogNorth.add(jPanelCenter, BorderLayout.SOUTH);

		jPanelGetLinks.setLayout(new BorderLayout());
		jPanelGetLinks.add(jPanelDialogNorth, BorderLayout.NORTH);

		this.setResizable(false);

		this.getContentPane().add(jPanelGetLinks);

		jPanelFilters.setLayout(new FlowLayout());

		jPanelFilters.setBorder(BorderFactory.createTitledBorder(Utils
				.getMessages("filters")));
		for (int i = 0; i < filters.length; i++) {
			checkBoxFilters[i] = new JCheckBox("*" + filters[i]);
			jPanelFilters.add(checkBoxFilters[i]);
		}
		jPanelGetLinks.add(jPanelFilters, BorderLayout.CENTER);

		jLabelMenssage.setForeground(Color.red);
		Utils.center(this, 470, 200);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		try {
			super.actionPerformed(e);
			List<String> filters = getFilters();
			if (jTUrl.getText().trim().equals("")) {
				throw new MalformedURLException(Utils.getMessages("nourl"));
			}
			GetLinks getLinks = new GetLinks(jTUrl.getText().trim(), filters);
			getLinks.addListener(this);
			Thread thread = new Thread(getLinks);
			thread.setName("Get Links");
			thread.start();
		} catch (MalformedURLException ex) {
			error(ex.getMessage());
			jLabelMenssage.setText(ex.getMessage());
		}
	}

	private List<String> getFilters() {
		List<String> filterList = new ArrayList<String>();
		for (int i = 0; i < checkBoxFilters.length; i++) {
			if (checkBoxFilters[i].isSelected())
				filterList.add(filters[i]);
		}
		return filterList;
	}

	public int getSizeList() {
		return listModel.getSize();
	}

	public void addList(StatusLink statusLink) {
		final StatusLink link = statusLink;
		if (!Utils.search(statusLink, listModel)) {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						listModel.addElement(link);
					}
				});
			} catch (Exception e) {
				fatal(e);
			}
		}
	}

	public void setText(final String texto) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					jLabelMenssage.setText(texto);
				}
			});
		} catch (Exception e) {
			fatal(e);
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