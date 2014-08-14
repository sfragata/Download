package download;

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

import download.listener.MensagemListener;
import download.thread.GetLinks;

/**
 * Description of the Class
 * 
 * @author Silvio Fragata da Silva
 */
public class GetLinksFrame extends DialogBase implements MensagemListener {
	private static final long serialVersionUID = -3578987691546708651L;

	private JPanel jPanelGetLinks = new JPanel();

	private JPanel jPanelDialogNorte = new JPanel();

	private JPanel jPanelCentro = new JPanel();

	private JLabel jLabelMensagem = new JLabel();

	private JPanel jPanelDialogNorteCentro = new JPanel();

	private JTextField jTUrl = new JTextField();

	private JLabel jLabelURL = new JLabel();

	private DefaultListModel listModel;

	private String[] filtros = { ".jpeg", ".jpg", ".gif", ".mpg", ".mpeg", ".avi", ".mov", ".wmv" };

	private JCheckBox[] checkBoxFiltros;

	private JPanel jPanelFiltros = new JPanel();

	/**
	 * Constructor for the GetLinksFrame object
	 * 
	 * @param frame
	 *            Description of the Parameter
	 * @param listModel
	 *            Description of the Parameter
	 */
	public GetLinksFrame(JFrame frame, DefaultListModel listModel) {
		super(frame, "Get Links", true);
		try {
			this.listModel = listModel;
			jbInit();
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
	private void jbInit() throws Exception {
		checkBoxFiltros = new JCheckBox[filtros.length];
		
		jTUrl.setColumns(35);
		jTUrl.addFocusListener(this);

		jLabelURL.setText(Utils.getMessages("typeurl"));
		jLabelURL.setForeground(Color.black);

		jLabelMensagem.setForeground(Color.red);
		jLabelMensagem.setText("");

		jPanelDialogNorteCentro.add(jLabelURL, null);
		jPanelDialogNorteCentro.add(jTUrl, null);

		jPanelCentro.setLayout(new FlowLayout());
		jPanelCentro.add(jLabelMensagem, null);

		jPanelDialogNorte.setLayout(new BorderLayout());
		jPanelDialogNorte.add(jPanelDialogNorteCentro, BorderLayout.CENTER);
		jPanelDialogNorte.add(jPanelCentro, BorderLayout.SOUTH);

		jPanelGetLinks.setLayout(new BorderLayout());
		jPanelGetLinks.add(jPanelDialogNorte, BorderLayout.NORTH);

		this.setResizable(false);

		this.getContentPane().add(jPanelGetLinks);

		jPanelFiltros.setLayout(new FlowLayout());

		jPanelFiltros.setBorder(BorderFactory.createTitledBorder(Utils
				.getMessages("filters")));
		for (int i = 0; i < filtros.length; i++) {
			checkBoxFiltros[i] = new JCheckBox("*" + filtros[i]);
			jPanelFiltros.add(checkBoxFiltros[i]);
		}
		jPanelGetLinks.add(jPanelFiltros, BorderLayout.CENTER);

		jLabelMensagem.setForeground(Color.red);
		Utils.center(this, 470, 200);
		setVisible(true);
	}

	/**
	 * Description of the Method
	 * 
	 * @param e
	 *            Description of the Parameter
	 */
	public void actionPerformed(ActionEvent e) {
		try {
			super.actionPerformed(e);
			List filters = getFilters();
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
			jLabelMensagem.setText(ex.getMessage());
		}
	}

	private List getFilters() {
		List filters = new ArrayList();
		for (int i = 0; i < checkBoxFiltros.length; i++) {
			if( checkBoxFiltros[i].isSelected() )
				filters.add(filtros[i]);
		}
		return filters;
	}

	public int getSizeList() {
		return listModel.getSize();
	}

	public void addLista(StatusLink statusLink) {
		final StatusLink d = statusLink;
		if (!Utils.search(statusLink, listModel)) {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						listModel.addElement(d);
					}
				});
			} catch (Exception e) {
				fatal(e);
			}
		}
	}

	public void setTexto(final String texto) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					jLabelMensagem.setText(texto);
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