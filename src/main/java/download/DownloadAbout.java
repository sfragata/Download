/**
 * $Id: DownloadAbout.java,v 1.2 2006/03/03 23:15:55 sfragata Exp $
 */

package download;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

/**
 * Classe para exibir a janela About Copyright: Copyright (c) 2001
 * 
 * @author Silvio Fragata da Silva
 * @created 5 de Dezembro de 2002
 * @version $Revision: 1.2 $
 */

public class DownloadAbout extends JDialog {
	private static final long serialVersionUID = 6128656184280320303L;

	JEditorPane editorPane = new JEditorPane();

	JPanel jPanelAbout = new JPanel();

	JPanel jPanelClose = new JPanel();

	JPanel jPanelOSUserName = new JPanel();

	JPanel jPanelSystemProperties = new JPanel();

	JScrollPane scroll = new JScrollPane();

	JButton jButtonClose = new JButton();

	JLabel jLabelVersion = new JLabel();

	JScrollPane jScrollPaneDescricao = new JScrollPane();

	JScrollPane jScrollPaneInfo = new JScrollPane();

	JTabbedPane tabbedPane = new JTabbedPane();

	JTextPane textPane = new JTextPane();

	private JFrame frame = null;

	DefaultTableModel tModel = null;

	JTable table = null;

	/**
	 * Construtor
	 * 
	 * @param frame
	 *            Frame proprietário
	 * @param title
	 *            Título
	 * @param version
	 *            Versão
	 * @param descricao
	 *            Descrição
	 */
	public DownloadAbout(JFrame frame, String title, String version,
			URL descricao) {
		super(frame, title, true);
		try {
			this.frame = frame;
			jbInit(descricao, version);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit(URL descricao, String version) throws Exception {
		Vector colunas = new Vector();
		colunas.addElement(Utils.getMessages("key"));
		colunas.addElement(Utils.getMessages("value"));
		Vector linhas = new Vector();

		Vector key = getKeysProperties();
		Vector values = getValuesProperties();

		int size = key.size();

		for (int i = 0; i < size; i++) {
			Vector linha = new Vector();
			linha.addElement(key.get(i));
			linha.addElement(values.get(i));
			linhas.addElement(linha);
		}
		table = new JTable(linhas, colunas);
		table.setEnabled(false);

		scroll.getViewport().add(table);

		this.setResizable(false);
		editorPane.setEditable(false);
		editorPane.addHyperlinkListener(new Hyperactive());
		if (descricao != null) {
			editorPane.setPage(descricao);

		}
		jButtonClose.setText(Utils.getMessages("close"));

		jButtonClose.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonClose_actionPerformed(e);
			}
		});

		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				this_windowClosing(e);
			}
		});

		jLabelVersion.setFont(new Font("Tahoma", Font.BOLD, 18));
		jLabelVersion.setText(version);

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		jPanelAbout.setLayout(new BorderLayout());

		tabbedPane.add(jScrollPaneDescricao, Utils.getMessages("about"));
		tabbedPane.add(jPanelAbout, Utils.getMessages("info"));
		tabbedPane.add(jPanelSystemProperties, Utils
				.getMessages("systemproperty"));

		jScrollPaneDescricao.getViewport().add(editorPane, null);

		this.getContentPane().add(jPanelClose, BorderLayout.SOUTH);

		jPanelClose.add(jButtonClose, null);
		jPanelAbout.add(jPanelOSUserName, BorderLayout.CENTER);

		jPanelSystemProperties.setLayout(new BorderLayout());
		jPanelSystemProperties.add(scroll, BorderLayout.CENTER);

		textPane = createTextPane();
		textPane.setEnabled(false);
		textPane.setBackground(jScrollPaneInfo.getBackground());
		jScrollPaneInfo.getViewport().add(textPane, null);
		jPanelOSUserName.setLayout(new BorderLayout());
		jPanelOSUserName.add(jScrollPaneInfo, BorderLayout.CENTER);
		JPanel jPanelVersion = new JPanel();
		this.getContentPane().add(jPanelVersion, BorderLayout.NORTH);
		jPanelVersion.add(jLabelVersion, null);
		jButtonClose.setMnemonic(KeyEvent.VK_F);

		ESCActionListener esc = new ESCActionListener();

		tabbedPane.registerKeyboardAction(esc, KeyStroke.getKeyStroke(
				KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		this.getRootPane().setDefaultButton(jButtonClose);
		Utils.center(this, 480, 380);
		setVisible(true);
	}

	public Vector getKeysProperties() {
		Properties p = System.getProperties();
		Enumeration en = p.keys();
		Vector vetKeys = new Vector();
		while (en.hasMoreElements())
			vetKeys.add(en.nextElement());
		return vetKeys;
	}

	public Vector getValuesProperties() {
		Properties p = System.getProperties();
		Vector vetValues = new Vector();
		Iterator it = p.values().iterator();
		while (it.hasNext())
			vetValues.add(it.next());

		return vetValues;
	}

	/**
	 * Ação do botão Fechar
	 * 
	 * @param e
	 *            O Evento
	 */
	void jButtonClose_actionPerformed(ActionEvent e) {
		if (frame != null) {
			this.dispose();
		} else {
			System.exit(0);
		}
	}

	/**
	 * Ação quando a janela é fechada
	 * 
	 * @param e
	 *            O Evento
	 */
	void this_windowClosing(WindowEvent e) {
		jButtonClose_actionPerformed(null);
	}

	private JTextPane createTextPane() {
		JTextPane textPane = new JTextPane();
		String[] initString = {
				/* " ", */
				"Java version ",
				System.getProperty("java.vm.version") + "\n"
						+ System.getProperty("java.runtime.name") + "\n",
				System.getProperty("java.vm.name") + " "
						+ System.getProperty("java.vm.info") + "\n",
				"Java vendor: ",
				System.getProperty("java.vm.vendor") + "\n\n\n",
				Utils.getMessages("user"),
				System.getProperty("user.name") + "\n",
				Utils.getMessages("os"), System.getProperty("os.name") };

		String[] initStyles = {
		/* "icon", */
		"regular", "bold", "bold", "regular", "bold", "regular", "boldRed",
				"regular", "boldRed" };

		initStylesForTextPane(textPane);

		Document doc = textPane.getDocument();

		try {
			for (int i = 0; i < initString.length; i++) {
				doc.insertString(doc.getLength(), initString[i], textPane
						.getStyle(initStyles[i]));
			}
		} catch (BadLocationException ble) {
			ble.printStackTrace();
			textPane.setText(Utils.getMessages("cant.get.text"));
		}
		return textPane;
	}

	private void initStylesForTextPane(JTextPane textPane) {
		// Initialize some styles.
		Style def = StyleContext.getDefaultStyleContext().getStyle(
				StyleContext.DEFAULT_STYLE);

		Style regular = textPane.addStyle("regular", def);
		StyleConstants.setFontFamily(def, "Courier");

		Style s = textPane.addStyle("italic", regular);
		StyleConstants.setItalic(s, true);

		s = textPane.addStyle("bold", regular);
		StyleConstants.setBold(s, true);

		s = textPane.addStyle("boldItalic", regular);
		StyleConstants.setBold(s, true);
		StyleConstants.setItalic(s, true);

		s = textPane.addStyle("boldRed", regular);
		StyleConstants.setBold(s, true);
		StyleConstants.setForeground(s, Color.red);

		s = textPane.addStyle("large", regular);
		StyleConstants.setFontSize(s, 16);

		s = textPane.addStyle("icon", regular);

		URL u = Utils.getURLResource("java.gif");
		if (u != null) {
			StyleConstants.setIcon(s, new ImageIcon(u));
		}
	}

	/**
	 * Classe para implementar a ação do botão ESC
	 * 
	 * @author Silvio Fragata da Silva
	 * @version 1.0
	 */
	protected class ESCActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			jButtonClose_actionPerformed(e);
		}
	}

	/**
	 * Classe para fazer com que os links exibidos no TextArea sejam funcionais
	 * 
	 * @author Silvio Fragata da Silva
	 * @version $Revision: 1.2 $
	 */
	class Hyperactive implements HyperlinkListener {
		public void hyperlinkUpdate(HyperlinkEvent e) {
			if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				JEditorPane pane = (JEditorPane) e.getSource();
				if (e instanceof HTMLFrameHyperlinkEvent) {
					HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) e;
					HTMLDocument doc = (HTMLDocument) pane.getDocument();
					doc.processHTMLFrameHyperlinkEvent(evt);
				} else {
					try {
						pane.setPage(e.getURL());
					} catch (Throwable t) {
						t.printStackTrace();
					}
				}
			}
		}
	}
}