/**
 * $Id: DownloadGUI.java,v 1.3 2006/03/03 23:15:36 sfragata Exp $
 */

package download;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

import download.listener.EventFrameListener;
import download.thread.Download;
import download.thread.VerifyLinks;

/**
 * Programa para efetuar download de uma lista de url´s
 * 
 * @author Silvio Fragata da Silva
 * @version $Revision: 1.3 $
 */
public class DownloadGUI extends JFrame implements Runnable, EventFrameListener {

	private static final long serialVersionUID = -6935547906204203650L;

	private final String VERSION = Utils.getMessages("version");

	private final String TITLE = Utils.getMessages("conexaotitle");

	private static CellRenderer cellRenderer = new CellRenderer();

	static final ImageIcon[] IMAGES = { cellRenderer.DOWNLOAD_ICON,
			cellRenderer.N_DOWNLOAD_ICON, cellRenderer.BROKEN_ICON,
			cellRenderer.DIR_ICON };

	static final ImageIcon[] IMAGES_PRESSED = {
			cellRenderer.DOWNLOAD_ICON_PRESSED,
			cellRenderer.N_DOWNLOAD_ICON_PRESSED,
			cellRenderer.BROKEN_ICON_PRESSED, cellRenderer.DIR_ICON_PRESSED };

	private JPanel jPanelPrincipal = new JPanel();

	private JPanel jPanelDownload = new JPanel();

	private JPanel jPanelURL = new JPanel();

	private JPanel jPanelDestino = new JPanel();

	private JPanel jPanelBotoes = new JPanel();

	private JPanel jPanelStatus = new JPanel();

	private JPanel jPaneldescUrl = new JPanel();

	private JLabel jLabelDestino = new JLabel();

	private JLabel jLabelStatus = new JLabel();

	private JTextField jTextFieldDestino = new JTextField();

	private JButton jButtonDownload = new JButton();

	private JButton jButtonVerify = new JButton();

	private JButton jButtonProcurar = new JButton();

	private DefaultListModel listModel;

	private JList jListUrls;

	private JScrollPane jScrollPaneListUrl = new JScrollPane();

	private JMenuBar jMenuBarDownload = new JMenuBar();

	private JMenu jMenuArquivo = new JMenu();

	private JMenuItem jMenuItemAbrir = new JMenuItem();

	private JMenuItem jMenuItemFechar = new JMenuItem();

	private JMenu jMenuInserir = new JMenu();

	private JMenuItem jMenuItemNovo = new JMenuItem();

	private JMenuItem jMenuItemRepetirLinks = new JMenuItem();

	private JMenuItem jMenuItemLimpar = new JMenuItem();

	private JMenu jMenuAcoes = new JMenu();

	private JMenuItem jMenuItemDownload = new JMenuItem();

	private JMenuItem jMenuItemVerify = new JMenuItem();

	private JMenu jMenuAjuda = new JMenu();

	private JMenuItem jMenuItemSobre = new JMenuItem();

	private JMenuItem jMenuItemConfig = new JMenuItem();

	private JMenuItem jMenuItemGC = new JMenuItem();

	private JPopupMenu jPopupMenuList = new JPopupMenu();

	private JMenuItem jMenuItemPopUpRemover = new JMenuItem();

	private JMenuItem jMenuItemPopUpInserir = new JMenuItem();

	private JMenuItem jMenuItemPopUpAlterar = new JMenuItem();

	private JMenuItem jMenuItemPopUpAlterarStatus = new JMenuItem();

	public static final int MAX_THREADS = System.getProperty("num.threads") == null ? 4
			: Integer.parseInt(System.getProperty("num.threads"));

	public static final int NAO = 1;

	public static final int TODOS = 2;

	private int quantidadeDiretorios = 0;

	private URL descricao;

	public static final char OUT = 'O';

	public static final char ERR = 'E';

	public static final String[] SIMBOLS = { "!", "@", "#", "$", "%", "¨", "&",
			"*", "(", ")", "+", "?", "=", "~", "´", "§" };

	private JMenuItem jMenuItemSalvar = new JMenuItem();

	private JRadioButtonMenuItem jRadioButtonMenuItemWindows = new JRadioButtonMenuItem(
			"Windows");

	private JRadioButtonMenuItem jRadioButtonMenuItemMetal = new JRadioButtonMenuItem(
			"Metal");

	private JRadioButtonMenuItem jRadioButtonMenuItemKunststoff = new JRadioButtonMenuItem(
			"Kunststoff");

	private JMenu jMenuLookAndFeel = new JMenu();

	private JMenuItem jMenuItemPopUpRemoveAll = new JMenuItem();

	private JMenuItem jMenuItemGetLinks = new JMenuItem();

	private JMenuItem jMenuItemMemoryMonitor = new JMenuItem();

	private JMenu jMenuAlloy = new JMenu();

	private JRadioButtonMenuItem jRadioButtonMenuItemAlloyGlass = new JRadioButtonMenuItem(
			"Glass");

	private JRadioButtonMenuItem jRadioButtonMenuItemAlloyBedouin = new JRadioButtonMenuItem(
			"Bedouin");

	private JRadioButtonMenuItem jRadioButtonMenuItemAlloyAcid = new JRadioButtonMenuItem(
			"Acid");

	private JRadioButtonMenuItem jRadioButtonMenuItemAlloyDefault = new JRadioButtonMenuItem(
			"Default");

	private Class kunststoff;

	private Class alloy;

	private boolean classKunststoff;

	private boolean classAlloy;

	private JTabbedPane jTabbedPaneDownload = new JTabbedPane();

	private JPanel jPanelThreads = new JPanel();

	private JPanel jPanelLogs = new JPanel();

	private JProgressBar[] jProgressBarArray = new JProgressBar[MAX_THREADS];

	private JLabel[] jLabelArray = new JLabel[MAX_THREADS];

	private JLabel[] jLabelImageIconArray = new JLabel[MAX_THREADS];

	private JScrollPane jScrollPaneLog = new JScrollPane();

	private JTextArea jTextAreaLog = new JTextArea();

	private JButton jButtonClearLog = new JButton(Utils.getMessages("clearlog"));

	private String currentDirOpen = ".";

	private String currentDirSave = currentDirOpen;

	private String currentDirSearch = ".";

	private String currentFileOpen = ".";

	private String currentFileSave = currentFileOpen;

	private boolean change = false;

	boolean[] stopArray = new boolean[MAX_THREADS];

	boolean[] finishArray = new boolean[MAX_THREADS];

	TimerText timerText = null;

	public static final String MESSAGE_DEFAULT = Utils.getMessages("download");

	private JMenuItem jMenuItemSelectAll = new JMenuItem();

	private Object syncObject = new Object();

	/**
	 * Construtor da Classe
	 * 
	 * @param kunststoff
	 *            Classe correpondente ao LookAndFeel Kunststoff
	 * @param alloy
	 *            Classe correpondente ao LookAndFeel Alloy
	 */
	public DownloadGUI(Class kunststoff, Class alloy) {
		this.kunststoff = kunststoff;
		this.alloy = alloy;
		this.classKunststoff = kunststoff != null;
		this.classAlloy = alloy != null;

		try {
			jbInit();
			jRadioButtonMenuItemKunststoff.setEnabled(classKunststoff);
			jRadioButtonMenuItemKunststoff.setSelected(classKunststoff);
			jRadioButtonMenuItemMetal
					.setSelected(!(classKunststoff || classAlloy));
			jMenuAlloy.setEnabled(classAlloy);
			jRadioButtonMenuItemAlloyDefault.setSelected(classAlloy);
		} catch (Exception e) {
			logExcecao(e);
		}
	}

	/**
	 * Método que seleciona arquivo de links
	 * 
	 * @return o caminho do arquivo de links
	 */
	private String open() {
		JFileChooser chooser = new JFileChooser();
		TextFileFilter filter = new TextFileFilter();
		filter.addExtension("txt");
		filter.setDescription(Utils.getMessages("textfile"));
		chooser.setFileFilter(filter);
		chooser.setDialogTitle(Utils.getMessages("chooselinkfile"));
		chooser.setCurrentDirectory(new File(currentDirOpen));
		chooser.setFileHidingEnabled(false);
		chooser.setMultiSelectionEnabled(false);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			if (listModel.size() > 0) {
				Object[] values = { Utils.getMessages("yes"),
						Utils.getMessages("no") };
				if (JOptionPane.showOptionDialog(null, Utils
						.getMessages("clearlist"), Utils
						.getMessages("clearlist"), JOptionPane.DEFAULT_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, values, values[0]) == JOptionPane.YES_OPTION) {
					listModel.removeAllElements();
					refresh();
				}
			}
			currentDirOpen = chooser.getSelectedFile().getParent();
			currentFileOpen = chooser.getSelectedFile().getName();
			return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}

	/**
	 * Método que lê o arquivo de links e insere na lista
	 * 
	 * @param pathFile
	 *            caminho do arquivo
	 * @throws IOException
	 *             caso ocorra algum erro
	 */
	private void lerArqToListModel(String pathFile) throws IOException {
		BufferedReader b = new BufferedReader(new FileReader(pathFile));
		String linha;
		while (b.ready()) {
			linha = b.readLine();
			if (!((linha == null) || linha.equals(""))) {
				int status = getLinkStatus(linha);
				linha = removeSimbolo(linha, status);
				if (status == StatusLink.DIRETORIO) {
					quantidadeDiretorios++;
				}
				if (!linha.trim().equals("")) {
					StatusLink d = new StatusLink(linha, status);
					if (!Utils.search(d, listModel)) {
						listModel.addElement(d);
					}
				}
			}
		}
		b.close();
		refresh();
	}

	/**
	 * Método que remove o simbolo que representa o diretório
	 * 
	 * @param link
	 *            link
	 * @param status
	 *            Status do Link
	 * @return o link sem o simbolo
	 */
	private String removeSimbolo(String link, int status) {
		if (status == StatusLink.DIRETORIO) {
			return link.substring(1, link.length() - 1);
		} else {
			if (status == StatusLink.BAIXADO || status == StatusLink.QUEBRADO) {
				return link.substring(1);
			}
		}

		return link;
	}

	/**
	 * Método que retorna o status do link
	 * 
	 * @param link
	 *            link
	 * @return status do link
	 */
	private int getLinkStatus(String link) {
		if (link.startsWith("[") && link.endsWith("]")) {
			return StatusLink.DIRETORIO;
		} else {
			if (link.startsWith("*")) {
				return StatusLink.BAIXADO;
			} else {
				if (link.startsWith("#")) {
					return StatusLink.QUEBRADO;
				}
			}
		}
		return StatusLink.NAO_BAIXADO;
	}

	/**
	 * Método que limpa a lista
	 */
	private void clearList() {
		listModel.removeAllElements();
		refresh();
		jTextFieldDestino.setText("");
		quantidadeDiretorios = 0;
		setTitle(TITLE);
		currentDirOpen = ".";
		currentDirSave = ".";
		currentFileOpen = ".";
		currentFileSave = ".";
	}

	/**
	 * Método para setar um texto na linha de resultado
	 * 
	 * @param m
	 *            texto a ser incluído
	 * @param apagar
	 *            informa se o texto será apagado ou não
	 */
	public void setTextoInResult(String m, boolean apagar) {
		setMessage(m, jLabelStatus);
		if (apagar) {
			timerText.setTextLabel("");
			// Timer t = new Timer( 5000, new java.awt.event.ActionListener()
			// {
			// public void actionPerformed( ActionEvent e )
			// {
			// jLabelStatus.setText( "" );
			// }
			// } );
		}
	}

	private void setTimetTextInResult(String m) {
		timerText.setTextLabel(m);
	}

	/**
	 * Método que atualiza as mensagens
	 * 
	 * @param m
	 *            Mensagem
	 * @param label
	 *            Label onde deve ser inserida a mensagem
	 */
	protected static void setMessage(final String m, final JLabel label) {
		label.setText(m);
	}

	/**
	 * Método que atualiza o progressbar de evolução da cópia do arquivo
	 * 
	 * @param parcial
	 *            Tamanho parcial do arquivo
	 * @param total
	 *            Tamanho total do arquivo
	 * @param number
	 *            A thread
	 */
	public void setProgress(final int parcial, final float total,
			final int number) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					BigDecimal par = new BigDecimal(parcial / total * 100);
					par = par.setScale(0, BigDecimal.ROUND_DOWN);
					jProgressBarArray[number].setString(new StringBuffer(par
							.toString()).append("%").toString());
					jProgressBarArray[number].setValue(par.intValue());
					jProgressBarArray[number].setToolTipText(new StringBuffer(
							Utils.getMessages("untildownload")).append(
							Utils.formatSize(parcial)).append(" bytes.")
							.toString());
				}
			});
		} catch (Exception e) {
			logExcecao(e);
		}
	}

	public void setLabelProgress(int number, String texto) {
		jLabelArray[number].setText(texto);
	}

	public void setToolTipLabelProgress(int number, String texto) {
		jLabelArray[number].setToolTipText(texto);
	}

	/**
	 * Método de limpa(zera) o progressbar
	 * 
	 * @param number
	 *            A thread
	 */
	public void clearProgress(int number) {
		jProgressBarArray[number].setValue(0);
		jProgressBarArray[number].setString("0%");
		jProgressBarArray[number].setToolTipText("");
	}

	/**
	 * Seleciona o indice representado pelo parametro
	 * 
	 * @param i
	 *            Índice da lista
	 */
	private void setFillList(int i) {
		jListUrls.setSelectedIndex(i);
	}

	/**
	 * Altera o texto na barra de status
	 * 
	 * @param texto
	 *            texto
	 */
	private void setStatusText(String texto) {
		// int index = jListUrls.getSelectedIndex();
		// String texto = "";
		// if( index != -1 )
		// {
		// StatusLink sl = (StatusLink) listModel.get( index );
		// texto = sl.getUrl();
		// }
		jLabelStatus.setText(texto);
	}

	/**
	 * Método que faz com que o scrollbar role
	 * 
	 * @param valor
	 *            para o scrollbar rolar
	 */
	private void scroll(int valor) {
		JScrollBar bar = jScrollPaneListUrl.getVerticalScrollBar();
		if (bar != null) {
			bar.setValue(valor);

		}
		jScrollPaneListUrl.validate();
		jListUrls.validate();
		this.validate();
	}

	/**
	 * Método que faz com que o scrollbar desça 17 unidades
	 * 
	 * @param number
	 *            o offset
	 */
	public void scrollDown(int number) {
		scroll(number * 17);
	}

	private void scrollBottom() {
		JScrollBar bar = jScrollPaneListUrl.getVerticalScrollBar();
		if (bar != null) {
			scroll(bar.getMaximum());
		}
	}

	/**
	 * Método que faz com que o scrollbar vá até o topo
	 */
	public void scrollUp() {
		scroll(0);
	}

	/**
	 * Método que insere em arquivo o StackTrace do erro gerado
	 * 
	 * @param e
	 *            Exception gerada
	 */
	public void logExcecao(Exception e) {
		try {
			StringWriter s = new StringWriter();
			e.printStackTrace(new PrintWriter(s));
			s.flush();
			s.close();
			addLog(s.getBuffer().toString(), OUT);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void createJPanels() {
		jPanelPrincipal.setLayout(new BorderLayout());
		jPanelPrincipal.add(jTabbedPaneDownload, BorderLayout.CENTER);
		jTabbedPaneDownload.add(jPanelDownload, Utils
				.getMessages("urltabbedtitle"));

		jPanelPrincipal.add(jPanelStatus, BorderLayout.SOUTH);

		jPanelDestino.setLayout(new FlowLayout());
		jPanelDestino.add(jLabelDestino);
		jPanelDestino.add(jTextFieldDestino);
		jPanelDestino.add(jButtonProcurar);

		jPanelURL.setBorder(BorderFactory.createTitledBorder(Utils
				.getMessages("urltitle")));
		jPanelURL.setLayout(new BorderLayout());
		jPanelURL.add(jScrollPaneListUrl, BorderLayout.CENTER);

		jPanelBotoes.setLayout(new FlowLayout());
		jPanelBotoes.add(jButtonDownload, null);
		jPanelBotoes.add(jButtonVerify, null);
		jTabbedPaneDownload.add(jPanelThreads, Utils.getMessages("threads"));
		jTabbedPaneDownload.add(jPanelLogs, Utils.getMessages("logs"));

		JPanel gcPanel = new GCPanel(jListUrls);
		gcPanel.setBorder(BorderFactory.createEtchedBorder());

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c1 = new GridBagConstraints();
		GridBagConstraints c2 = new GridBagConstraints();

		jPanelStatus.setLayout(gridbag);
		jPanelStatus.setBorder(BorderFactory.createLoweredBevelBorder());

		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.weightx = 0.5;
		c1.gridwidth = 2;
		c1.gridx = 0;
		gridbag.setConstraints(jLabelStatus, c1);
		jPanelStatus.add(jLabelStatus);

		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.gridwidth = 1;
		c2.gridx = 2;
		gridbag.setConstraints(gcPanel, c2);
		jPanelStatus.add(gcPanel);

		jPanelDownload.setBorder(BorderFactory.createEtchedBorder());
		jPanelDownload
				.setLayout(new BoxLayout(jPanelDownload, BoxLayout.Y_AXIS));
		jPanelDownload.add(Box.createRigidArea(new Dimension(0, 10)));
		jPanelDownload.add(jPanelURL);
		jPanelDownload.add(Box.createRigidArea(new Dimension(0, 30)));
		jPanelDownload.add(jPanelDestino);
		jPanelDownload.add(Box.createRigidArea(new Dimension(0, 30)));
		jPanelDownload.add(jPanelBotoes);
	}

	private void createJLabels() {
		jLabelDestino.setText(Utils.getMessages("target"));
		jLabelStatus.setText("");
	}

	private void createJButtons() {
		jButtonDownload.setMaximumSize(new Dimension(57, 27));
		jButtonDownload.setMinimumSize(new Dimension(57, 27));
		jButtonDownload.setPreferredSize(new Dimension(130, 27));
		jButtonDownload.setText(Utils.getMessages("download"));
		jButtonDownload.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonDownload_actionPerformed(e);
			}
		});

		URL u = getClass().getClassLoader().getResource(
				"resources/download.gif");
		if (u != null) {
			jButtonDownload.setIcon(new ImageIcon(u));
		}
		jButtonVerify.setText(Utils.getMessages("testlinks"));
		jButtonVerify.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonVerify_actionPerformed(e);
			}
		});
		u = getClass().getClassLoader().getResource("resources/execute.gif");
		if (u != null) {
			jButtonVerify.setIcon(new ImageIcon(u));

		}
		jButtonProcurar.setBounds(new Rectangle(386, 252, 112, 27));
		jButtonProcurar.setText(Utils.getMessages("find"));
		jButtonProcurar.setMnemonic(KeyEvent.VK_P);
		jButtonProcurar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonProcurar_actionPerformed(e);
			}
		});
		u = getClass().getClassLoader().getResource("resources/find.gif");
		if (u != null) {
			jButtonProcurar.setIcon(new ImageIcon(u));
		}
	}

	private void createJList() {
		listModel = new DefaultListModel();
		jListUrls = new JList(listModel);
		MouseListener popupListener = new PopupListener();
		jListUrls.addMouseListener(popupListener);
		jListUrls.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				jListUrls_mouseClicked(e);
			}
		});
		jListUrls.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				jListUrls_keyPressed(e);
			}
		});
		jScrollPaneListUrl.getViewport().add(jListUrls, null);
		jListUrls.setCellRenderer(cellRenderer);
		jListUrls
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}

	private void createJTextField() {
		jTextFieldDestino.setText("");
		jTextFieldDestino.setFont(new java.awt.Font("Tahoma", Font.BOLD, 12));
		jTextFieldDestino.setColumns(30);
		jTextFieldDestino.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(FocusEvent e) {
				jTextFieldDestino_focusGained(e);
			}
		});
	}

	private void createJMenus() {
		jMenuArquivo.setText(Utils.getMessages("file"));
		jMenuArquivo.setMnemonic(KeyEvent.VK_A);

		jMenuInserir.setText(Utils.getMessages("insert"));
		jMenuInserir.setMnemonic(KeyEvent.VK_I);

		jMenuAcoes.setText(Utils.getMessages("actions"));
		jMenuAcoes.setMnemonic(KeyEvent.VK_E);

		jMenuAjuda.setText(Utils.getMessages("help"));
		jMenuAjuda.setMnemonic(KeyEvent.VK_J);

		jMenuAlloy.setText("Alloy");
		URL u = getClass().getClassLoader().getResource("resources/eye.gif");
		if (u != null) {
			jMenuAlloy.setIcon(new ImageIcon(u));
		}
		jMenuAlloy.setMnemonic(KeyEvent.VK_A);
		jMenuAlloy.add(jRadioButtonMenuItemAlloyAcid);
		jMenuAlloy.add(jRadioButtonMenuItemAlloyBedouin);
		jMenuAlloy.add(jRadioButtonMenuItemAlloyDefault);
		jMenuAlloy.add(jRadioButtonMenuItemAlloyGlass);

		jMenuArquivo.add(jMenuItemAbrir);
		jMenuArquivo.add(jMenuItemSalvar);
		jMenuArquivo.add(jMenuItemFechar);

		jMenuInserir.add(jMenuItemNovo);
		jMenuInserir.add(jMenuItemRepetirLinks);
		jMenuInserir.add(jMenuItemGetLinks);
		jMenuInserir.add(jMenuItemLimpar);

		jMenuAcoes.add(jMenuItemDownload);
		jMenuAcoes.add(jMenuItemVerify);

		jMenuLookAndFeel.setText(Utils.getMessages("lef"));
		jMenuLookAndFeel.setMnemonic(KeyEvent.VK_L);
		jMenuLookAndFeel.add(jRadioButtonMenuItemWindows);
		jMenuLookAndFeel.add(jRadioButtonMenuItemMetal);
		jMenuLookAndFeel.add(jRadioButtonMenuItemKunststoff);
		jMenuLookAndFeel.add(jMenuAlloy);

		jMenuAjuda.add(jMenuItemSobre);
		jMenuAjuda.add(jMenuItemConfig);
		jMenuAjuda.add(jMenuItemGC);
		jMenuAjuda.add(jMenuItemMemoryMonitor);
	}

	private void createJMenuItems() {
		jMenuItemAbrir.setText(Utils.getMessages("open"));
		URL u = getClass().getClassLoader().getResource("resources/import.gif");
		if (u != null) {
			jMenuItemAbrir.setIcon(new ImageIcon(u));
		}
		jMenuItemAbrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				KeyEvent.VK_O, KeyEvent.CTRL_MASK, false));
		jMenuItemAbrir.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemAbrir_actionPerformed(e);
			}
		});

		jMenuItemFechar.setText(Utils.getMessages("close"));
		u = getClass().getClassLoader().getResource("resources/exit.gif");
		if (u != null) {
			jMenuItemFechar.setIcon(new ImageIcon(u));
		}
		jMenuItemFechar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				KeyEvent.VK_X, KeyEvent.ALT_MASK, false));
		jMenuItemFechar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemFechar_actionPerformed(e);
			}
		});

		jMenuItemNovo.setText(Utils.getMessages("new"));
		u = getClass().getClassLoader().getResource("resources/new.gif");
		if (u != null) {
			jMenuItemNovo.setIcon(new ImageIcon(u));
		}
		jMenuItemNovo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				KeyEvent.VK_INSERT, 0, false));
		jMenuItemNovo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemNovo_actionPerformed(e);
			}
		});

		jMenuItemRepetirLinks.setText(Utils.getMessages("repeatlinks"));
		u = getClass().getClassLoader().getResource("resources/repeat.gif");
		if (u != null) {
			jMenuItemRepetirLinks.setIcon(new ImageIcon(u));
		}
		jMenuItemRepetirLinks.setAccelerator(javax.swing.KeyStroke
				.getKeyStroke(KeyEvent.VK_R, KeyEvent.ALT_MASK, false));
		jMenuItemRepetirLinks
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jMenuItemRepetirLinks_actionPerformed(e);
					}
				});

		jMenuItemLimpar.setText(Utils.getMessages("clean"));
		u = getClass().getClassLoader().getResource("resources/clean.gif");
		if (u != null) {
			jMenuItemLimpar.setIcon(new ImageIcon(u));
		}
		jMenuItemLimpar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				KeyEvent.VK_DELETE, KeyEvent.CTRL_MASK, false));
		jMenuItemLimpar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemLimpar_actionPerformed(e);
			}
		});
		jMenuItemDownload.setText(Utils.getMessages("download"));
		u = getClass().getClassLoader().getResource("resources/download.gif");
		if (u != null) {
			jMenuItemDownload.setIcon(new ImageIcon(u));
		}
		jMenuItemDownload.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				KeyEvent.VK_ENTER, 0, false));
		jMenuItemDownload
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jButtonDownload.doClick();
					}
				});

		jMenuItemVerify.setText(Utils.getMessages("testlinks"));
		u = getClass().getClassLoader().getResource("resources/execute.gif");
		if (u != null) {
			jMenuItemVerify.setIcon(new ImageIcon(u));
		}
		jMenuItemVerify.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				KeyEvent.VK_V, KeyEvent.ALT_MASK, false));
		jMenuItemVerify.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonVerify.doClick();
			}
		});

		jMenuItemSobre.setText(Utils.getMessages("about"));
		jMenuItemSobre.setMnemonic(KeyEvent.VK_S);
		jMenuItemSobre.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				KeyEvent.VK_F1, 0, false));
		u = getClass().getClassLoader().getResource("resources/about.gif");
		if (u != null) {
			jMenuItemSobre.setIcon(new ImageIcon(u));
		}
		jMenuItemSobre.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemSobre_actionPerformed(e);
			}
		});

		jMenuItemConfig.setText(Utils.getMessages("config"));
		jMenuItemConfig.setMnemonic(KeyEvent.VK_C);
		jMenuItemConfig.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				KeyEvent.VK_P, KeyEvent.CTRL_MASK, false));
		u = getClass().getClassLoader().getResource("resources/setup.gif");
		if (u != null) {
			jMenuItemConfig.setIcon(new ImageIcon(u));
		}
		jMenuItemConfig.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemConfig_actionPerformed(e);
			}
		});

		jMenuItemGC.setText(Utils.getMessages("gc"));
		jMenuItemGC.setMnemonic(KeyEvent.VK_G);
		u = getClass().getClassLoader().getResource("resources/gc.gif");
		if (u != null) {
			jMenuItemGC.setIcon(new ImageIcon(u));
		}
		jMenuItemGC.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				KeyEvent.VK_G, KeyEvent.CTRL_MASK, false));
		jMenuItemGC.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemGC_actionPerformed(e);
			}
		});

		jMenuItemSalvar.setText(Utils.getMessages("save"));
		u = getClass().getClassLoader().getResource("resources/export.gif");
		if (u != null) {
			jMenuItemSalvar.setIcon(new ImageIcon(u));
		}
		jMenuItemSalvar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				KeyEvent.VK_S, KeyEvent.CTRL_MASK, false));
		jMenuItemSalvar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemSalvar_actionPerformed(e);
			}
		});

		jMenuItemGetLinks.setText(Utils.getMessages("getlinks"));
		jMenuItemGetLinks.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				KeyEvent.VK_G, KeyEvent.ALT_MASK, false));
		u = getClass().getClassLoader().getResource("resources/link.gif");
		if (u != null) {
			jMenuItemGetLinks.setIcon(new ImageIcon(u));
		}
		jMenuItemGetLinks
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jMenuItemGetLinks_actionPerformed(e);
					}
				});

		jMenuItemMemoryMonitor.setText(Utils.getMessages("memorymonitor"));
		jMenuItemMemoryMonitor.setMnemonic(KeyEvent.VK_M);
		jMenuItemMemoryMonitor.setAccelerator(javax.swing.KeyStroke
				.getKeyStroke(KeyEvent.VK_M, KeyEvent.ALT_MASK, false));
		u = getClass().getClassLoader().getResource("resources/monitor.gif");
		if (u != null) {
			jMenuItemMemoryMonitor.setIcon(new ImageIcon(u));
		}
		jMenuItemMemoryMonitor
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jMenuItemMemoryMonitor_actionPerformed(e);
					}
				});

		jRadioButtonMenuItemAlloyAcid.setMnemonic(KeyEvent.VK_C);
		jRadioButtonMenuItemAlloyAcid
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jRadioButtonMenuItemAlloyAcid_actionPerformed(e);
					}
				});

		jRadioButtonMenuItemAlloyBedouin.setMnemonic(KeyEvent.VK_B);
		jRadioButtonMenuItemAlloyBedouin
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jRadioButtonMenuItemAlloyBedouin_actionPerformed(e);
					}
				});

		jRadioButtonMenuItemAlloyDefault.setMnemonic(KeyEvent.VK_D);
		jRadioButtonMenuItemAlloyDefault
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jRadioButtonMenuItemAlloyDefault_actionPerformed(e);
					}
				});

		jRadioButtonMenuItemAlloyGlass.setMnemonic(KeyEvent.VK_G);
		jRadioButtonMenuItemAlloyGlass
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jRadioButtonMenuItemAlloyGlass_actionPerformed(e);
					}
				});

		u = getClass().getClassLoader().getResource("resources/eye.gif");
		if (u != null) {
			jRadioButtonMenuItemKunststoff.setIcon(new ImageIcon(u));
		}
		jRadioButtonMenuItemKunststoff.setMnemonic(KeyEvent.VK_K);
		jRadioButtonMenuItemKunststoff
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jRadioButtonMenuItemKunststoff_actionPerformed(e);
					}
				});

		u = getClass().getClassLoader().getResource("resources/windows.gif");
		if (u != null) {
			jRadioButtonMenuItemWindows.setIcon(new ImageIcon(u));
		}
		jRadioButtonMenuItemWindows.setMnemonic(KeyEvent.VK_W);
		jRadioButtonMenuItemWindows
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jRadioButtonMenuItemWindows_actionPerformed(e);
					}
				});

		u = getClass().getClassLoader().getResource("resources/metal.gif");
		if (u != null) {
			jRadioButtonMenuItemMetal.setIcon(new ImageIcon(u));
		}
		jRadioButtonMenuItemMetal.setMnemonic(KeyEvent.VK_M);
		jRadioButtonMenuItemMetal
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jRadioButtonMenuItemMetal_actionPerformed(e);
					}
				});
	}

	private void createJMenuItemPopUp() {
		jMenuItemPopUpRemover.setText(Utils.getMessages("remove"));
		URL u = getClass().getClassLoader().getResource("resources/del.gif");
		if (u != null) {
			jMenuItemPopUpRemover.setIcon(new ImageIcon(u));
		}
		jMenuItemPopUpRemover
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jMenuItemPopUpRemover_actionPerformed(e);
					}
				});
		jMenuItemPopUpRemover.setAccelerator(javax.swing.KeyStroke
				.getKeyStroke(KeyEvent.VK_DELETE, 0, false));

		jMenuItemPopUpInserir.setText(Utils.getMessages("insert"));
		u = getClass().getClassLoader().getResource("resources/new.gif");
		if (u != null) {
			jMenuItemPopUpInserir.setIcon(new ImageIcon(u));
		}
		jMenuItemPopUpInserir
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jMenuItemPopUpInserir_actionPerformed(e);
					}
				});
		jMenuItemPopUpInserir.setAccelerator(jMenuItemNovo.getAccelerator());

		jMenuItemPopUpAlterarStatus.setText(Utils.getMessages("changestatus"));
		u = getClass().getClassLoader().getResource("resources/edit.gif");
		if (u != null) {
			jMenuItemPopUpAlterarStatus.setIcon(new ImageIcon(u));
		}
		jMenuItemPopUpAlterarStatus
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jMenuItemPopUpAlterarStatus_actionPerformed(e);
					}
				});
		jMenuItemPopUpAlterarStatus.setAccelerator(javax.swing.KeyStroke
				.getKeyStroke(KeyEvent.VK_F3, 0, false));

		jMenuItemPopUpAlterar.setText(Utils.getMessages("change"));
		u = getClass().getClassLoader().getResource("resources/edit.gif");
		if (u != null) {
			jMenuItemPopUpAlterar.setIcon(new ImageIcon(u));
		}
		jMenuItemPopUpAlterar
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jMenuItemPopUpAlterar_actionPerformed(e);
					}
				});
		jMenuItemPopUpAlterar.setAccelerator(javax.swing.KeyStroke
				.getKeyStroke(KeyEvent.VK_F2, 0, false));

		jMenuItemPopUpRemoveAll.setText(Utils.getMessages("clean"));
		u = getClass().getClassLoader().getResource("resources/removeall.gif");
		if (u != null) {
			jMenuItemPopUpRemoveAll.setIcon(new ImageIcon(u));
		}
		jMenuItemPopUpRemoveAll
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jMenuItemPopUpRemoveAll_actionPerformed(e);
					}
				});
		jMenuItemPopUpRemoveAll.setAccelerator(javax.swing.KeyStroke
				.getKeyStroke(KeyEvent.VK_DELETE, KeyEvent.CTRL_MASK, false));

		jMenuItemSelectAll.setText(Utils.getMessages("selectall"));
		u = getClass().getClassLoader().getResource("resources/selall.gif");
		if (u != null) {
			jMenuItemSelectAll.setIcon(new ImageIcon(u));
		}
		jMenuItemSelectAll
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jMenuItemSelectAll_actionPerformed(e);
					}
				});
	}

	/**
	 * Método que inicializa todos os componentes visuais
	 * 
	 * @exception Exception
	 *                Caso ocorra algum erro
	 */
	private void jbInit() throws Exception {
		URL u = getClass().getClassLoader().getResource("resources/globe.gif");
		if (u != null) {
			this.setIconImage(new ImageIcon(u).getImage());
		}
		this.setJMenuBar(jMenuBarDownload);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				this_windowClosing(e);
			}
		});
		this.setTitle(TITLE);
		this.getContentPane().setLayout(new BorderLayout());
		createJMenuItemPopUp();
		createJList();
		createJMenuItems();
		createJButtons();
		createJLabels();
		createJTextField();
		createJMenus();
		createJPanels();

		jPanelLogs.setLayout(new BorderLayout());
		jTextAreaLog.setFont(new java.awt.Font("DialogInput", 0, 12));
		jTextAreaLog.setText("");
		jTextAreaLog.setEditable(false);
		jTextAreaLog.setWrapStyleWord(true);
		jButtonClearLog.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonClearLog_actionPerformed(e);
			}
		});
		jButtonClearLog.setSelected(true);
		this.getContentPane().add(jPanelPrincipal, BorderLayout.CENTER);

		ButtonGroup group = new ButtonGroup();
		group.add(jRadioButtonMenuItemWindows);
		group.add(jRadioButtonMenuItemMetal);
		group.add(jRadioButtonMenuItemKunststoff);
		group.add(jRadioButtonMenuItemAlloyAcid);
		group.add(jRadioButtonMenuItemAlloyBedouin);
		group.add(jRadioButtonMenuItemAlloyDefault);
		group.add(jRadioButtonMenuItemAlloyGlass);

		jMenuBarDownload.add(jMenuArquivo);
		jMenuBarDownload.add(jMenuInserir);
		jMenuBarDownload.add(jMenuAcoes);
		jMenuBarDownload.add(jMenuLookAndFeel);
		jMenuBarDownload.add(jMenuAjuda);

		jPopupMenuList.add(jMenuItemPopUpInserir);
		jPopupMenuList.add(jMenuItemPopUpAlterar);
		jPopupMenuList.add(jMenuItemPopUpAlterarStatus);
		jPopupMenuList.add(jMenuItemPopUpRemover);
		jPopupMenuList.add(jMenuItemPopUpRemoveAll);
		jPopupMenuList.add(jMenuItemSelectAll);

		jPanelLogs.add(jScrollPaneLog, BorderLayout.CENTER);
		JPanel p = new JPanel();
		p.add(jButtonClearLog);
		jPanelLogs.add(p, BorderLayout.SOUTH);
		jScrollPaneLog.getViewport().add(jTextAreaLog, null);

		jPanelThreads.setLayout(new GridLayout(20, 2));
		jPanelThreads.setBorder(BorderFactory.createLoweredBevelBorder());

		for (int i = 0; i < jProgressBarArray.length; i++) {
			jProgressBarArray[i] = new JProgressBar();

			jProgressBarArray[i].setFont(new java.awt.Font("Dialog", 1, 10));
			jProgressBarArray[i].setStringPainted(true);
			jLabelArray[i] = new JLabel("Download " + i);
			jLabelArray[i].setToolTipText(jLabelArray[i].getText());

			jLabelImageIconArray[i] = new JLabel();
			jLabelImageIconArray[i].addMouseListener(new MouseAdapterLabel(i));
			iconDisconnect(i);

			JPanel pthread = new JPanel();
			pthread.setLayout(new BorderLayout());
			pthread.add(jProgressBarArray[i], BorderLayout.CENTER);
			pthread.add(jLabelImageIconArray[i], BorderLayout.EAST);
			jPanelThreads.add(jLabelArray[i]);
			jPanelThreads.add(pthread);
			stopArray[i] = false;
			clearProgress(i);
		}
		timerText = new TimerText();
		String[] val = Utils.getValores();
		if (val != null)
			Utils.setSystemProp(val);

		Utils.center(this, 600, 550);
	}

	/**
	 * Método executado quando o botão de download e clicado ou pressionado
	 * 
	 * @param e
	 *            ActionEvent
	 */
	void jButtonDownload_actionPerformed(ActionEvent e) {
		// TODO: ACTION PARA O DOWNLOAD
		setTextoInResult(Utils.getMessages("creatingdir"), false);
		String dir = jTextFieldDestino.getText().trim();
		if ((dir != null) && !dir.equals("") && (listModel.size() > 0)) {
			try {
				File file = new File(dir);

				if (!file.exists()) {
					file.mkdirs();
				}
				String time = new StringBuffer(Utils
						.getMessages("initialingdownload")).append(
						Utils.formatDate(System.currentTimeMillis(),
								"EEEE, d MMMM 'de' yyyy - HH:mm:ss"))
						.toString();
				clearLog();
				addLog(time, OUT);
				this.habDesab(false);
				String t = new StringBuffer(Utils
						.getMessages("efetuandodownload")).append(" ").append(
						(listModel.getSize() - quantidadeDiretorios)).append(
						" ").append(Utils.getMessages("fileswith")).append(" ")
						.append(MAX_THREADS).append(" ").append(
								Utils.getMessages("threads")).toString();
				setTextoInResult(t, false);

				jListUrls.clearSelection();
				for (int i = 0; i < MAX_THREADS; i++) {
					Download download = new Download(i);
					download.addEventFrameListener(this);
					Thread thread = new Thread(download);
					thread.setName("Download-" + i);
					thread.start();
					finishArray[i] = false;
				}
			} catch (Exception ez) {
				fatal(ez);
			}
		} else {
			setTextoInResult(Utils.getMessages("valerrors"), true);
		}
	}

	public void addLog(String texto, char tipo) {
		addLog(texto, tipo, true);
	}

	private void addLog(String texto, char tipo, boolean pular) {
		if (tipo == ERR) {
			texto = Utils.getMessages("errtitle") + texto;
		}
		if (pular)
			texto += "\n";
		synchronized (syncObject) {
			jTextAreaLog.append(texto);
		}
		jTextAreaLog.setAutoscrolls(true);
	}

	private void clearLog() {
		jTextAreaLog.setText("");
	}

	/**
	 * Altera o ícone e o tootip do globo
	 * 
	 * @param status
	 *            se esta conectado ou não
	 * @param number
	 *            A thread
	 */
	private void setIconConexao(boolean status, int number) {
		String gif = "resources/disconnect.gif";
		if (status) {
			gif = "resources/connect.gif";
		}
		URL u = getClass().getClassLoader().getResource(gif);
		if (u != null) {
			jLabelImageIconArray[number].setIcon(new ImageIcon(u));
			jLabelImageIconArray[number].setToolTipText(status ? Utils
					.getMessages("connected") : Utils
					.getMessages("disconnected"));
			jLabelImageIconArray[number].repaint();
		}
	}

	/**
	 * Método que altera a sitiação do ícone para conectado
	 * 
	 * @param number
	 *            A thread
	 */
	public void iconConnect(int number) {
		setIconConexao(true, number);
	}

	/**
	 * Método que altera a sitiação do ícone para desconectado
	 * 
	 * @param number
	 *            A thread
	 */
	public void iconDisconnect(int number) {
		setIconConexao(false, number);
	}

	/**
	 * Método que a depender do parâmetro, habilida ou desabilita um conjunto de
	 * componentes
	 * 
	 * @param status
	 *            se é para habilitar ou desabilitar
	 */
	public void habDesab(boolean status) {
		jButtonDownload.setEnabled(status);
		jListUrls.setEnabled(status);
		jTextFieldDestino.setEnabled(status);
		jMenuInserir.setEnabled(status);
		jMenuItemAbrir.setEnabled(status);
		// jMenuItemSalvar.setEnabled(status);
		jButtonVerify.setEnabled(status);
		jMenuAcoes.setEnabled(status);
		jMenuItemDownload.setEnabled(status);
		jMenuItemVerify.setEnabled(status);
		jButtonProcurar.setEnabled(status);
	}

	/**
	 * Evento quando a janela for fechada
	 * 
	 * @param e
	 *            Evento
	 */
	void this_windowClosing(WindowEvent e) {
		if (listModel.size() > 0 && jListUrls.isEnabled()) {
			if (currentFileOpen.equals(".") || change) {
				if (JOptionPane.showConfirmDialog(null, Utils
						.getMessages("save.list"), Utils
						.getMessages("save.list"), JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null) == JOptionPane.YES_OPTION) {
					jMenuItemSalvar_actionPerformed(null);
				}
			} else {
				try {
					lerListModelToArq(currentDirOpen + File.separator
							+ currentFileOpen);
				} catch (IOException e1) {
					fatal(e1);
				}
			}
		}
		System.exit(0);
	}

	void jListUrls_mouseClicked(MouseEvent e) {
		jListUrls.repaint();
		if (jListUrls.isEnabled()) {
			if ((e.getClickCount() == 2)) {
				int index = jListUrls.getSelectedIndex();
				if (listModel.getSize() == 0) {
					index = -1;
				}
				if (index != -1) {
					alterarURL();
				} else {
					jMenuItemNovo_actionPerformed(null);
				}
			}
		}
	}

	/**
	 * Método que abre um JDialog para alterar a url ou o status de um link
	 */
	private void alterarURL() {
		if (jListUrls.getSelectedValue() != null) {
			boolean wasDir = false;
			int pos = jListUrls.getSelectedIndex();
			StatusLink d = (StatusLink) jListUrls.getSelectedValue();
			String urlAux = "";
			if (d.getStatus() == StatusLink.DIRETORIO) {
				wasDir = true;
			}
			StatusLink dados = new DownloadDialog(this, d, Utils
					.getMessages("changeurl")).getStatusLink();
			String url = dados.getUrl();
			if ((url != null) && !url.equals("")) {
				if (dados.getStatus() == StatusLink.DIRETORIO) {
					quantidadeDiretorios++;
				} else {
					if (wasDir) {
						quantidadeDiretorios--;
					}
				}
				listModel.set(pos, dados);
				changeIt();
			}
		}
		refresh();
	}

	private void changeIt() {
		if (!currentFileOpen.equals(".")) {
			change = true;
			String title = getTitle();
			title = title.replace('*', ' ').trim();
			setTitle(title + "*");
		}
	}

	/**
	 * Método para fechar a aplicação
	 * 
	 * @param e
	 *            Evento
	 */
	void jMenuItemFechar_actionPerformed(ActionEvent e) {
		this.this_windowClosing(null);
	}

	/**
	 * Método para importar o arquivo de links
	 * 
	 * @param e
	 *            Evento
	 */
	void jMenuItemAbrir_actionPerformed(ActionEvent e) {
		try {
			String path = open();
			if (path != null && !path.equals("")) {
				lerArqToListModel(path);
				setTitle(TITLE + " - " + path);
				scrollBottom();
				refresh();
				jListUrls.setEnabled(true);
				jButtonDownload.setEnabled(true);
				jTextFieldDestino.setEnabled(true);
			}
		} catch (Exception ec) {
			logExcecao(ec);
		}
	}

	/**
	 * Método que abre o Repetidor de Links
	 * 
	 * @param e
	 *            Evento
	 */
	void jMenuItemRepetirLinks_actionPerformed(ActionEvent e) {
		new RepetidorFrame(DownloadGUI.this, listModel);
		changeIt();
	}

	/**
	 * Método que insere um novo ítem na lista
	 * 
	 * @param e
	 *            Evento
	 */
	void jMenuItemNovo_actionPerformed(ActionEvent e) {
		if (jListUrls.isEnabled()) {
			StatusLink dados = new DownloadDialog(this, null, Utils
					.getMessages("inserturl")).getStatusLink();
			if (dados != null) {
				String url = dados.getUrl();
				if ((url != null) && !url.equals("")) {
					if (!Utils.search(dados, listModel)) {
						if (dados.getStatus() == StatusLink.DIRETORIO) {
							quantidadeDiretorios++;
						}
						if (jListUrls.getSelectedIndex() > -1) {
							listModel.add(jListUrls.getSelectedIndex(), dados);
							jListUrls.setSelectedIndex(jListUrls
									.getSelectedIndex() + 1);
						} else
							listModel.addElement(dados);
					}
				}
				changeIt();
				scrollBottom();
				refresh();
			}
		}
	}

	/**
	 * Método para limpar a lista
	 * 
	 * @param e
	 *            Evento
	 */
	void jMenuItemLimpar_actionPerformed(ActionEvent e) {
		if (jListUrls.isEnabled()) {
			clearList();
			validate();
			changeIt();
		}
	}

	/**
	 * Método para remover um ou mais ítens da lista
	 * 
	 * @param e
	 *            Evento
	 */
	void jMenuItemPopUpRemover_actionPerformed(ActionEvent e) {
		int[] indices = jListUrls.getSelectedIndices();
		int i = indices.length - 1;
		int lastIndex = 0;
		while (i >= 0) {
			StatusLink d = (StatusLink) listModel.elementAt(i);

			if (d.getStatus() == StatusLink.DIRETORIO) {
				quantidadeDiretorios--;
			}
			listModel.removeElementAt(indices[i]);
			lastIndex = indices[i--];
		}
		while (lastIndex >= listModel.getSize()) {
			lastIndex--;
		}

		jListUrls.setSelectedIndex(lastIndex);
		changeIt();
		refresh();
	}

	/**
	 * Método que insere um novo ítem na lista
	 * 
	 * @param e
	 *            Evento
	 */
	void jMenuItemPopUpInserir_actionPerformed(ActionEvent e) {
		this.jMenuItemNovo_actionPerformed(e);
	}

	/**
	 * Método que altera o status de um ou mais links
	 * 
	 * @param e
	 *            Evento
	 */
	void jMenuItemPopUpAlterarStatus_actionPerformed(ActionEvent e) {
		int[] indices = jListUrls.getSelectedIndices();
		int qtdDir = 0;
		int size = indices.length;
		StatusLink[] d = new StatusLink[size];
		for (int i = 0; i < size; i++) {
			d[i] = (StatusLink) listModel.elementAt(indices[i]);
			if (d[i].getStatus() == StatusLink.DIRETORIO) {
				qtdDir++;
			}
		}
		int status = new DownloadChangeStatus(DownloadGUI.this, d, Utils
				.getMessages("changelinkstatus")).getStatus();
		if (status != StatusLink.DIRETORIO) {
			quantidadeDiretorios -= qtdDir;

		} else {
			quantidadeDiretorios += size - qtdDir;

		}
		changeIt();
		refresh();
	}

	/**
	 * Método para alterar a url ou o status de um link
	 * 
	 * @param e
	 *            Evento
	 */
	void jMenuItemPopUpAlterar_actionPerformed(ActionEvent e) {
		alterarURL();
	}

	/**
	 * Método de evento quando é pressionada uma tecla na lista
	 * 
	 * @param e
	 *            Evento
	 */
	void jListUrls_keyPressed(KeyEvent e) {
		if (jListUrls.isEnabled() && (listModel.size() > 0)
				&& (jListUrls.getSelectedIndex() != -1)) {
			if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				jMenuItemPopUpRemover_actionPerformed(null);
			} else {
				if (e.getKeyCode() == KeyEvent.VK_F2) {
					jMenuItemPopUpAlterar_actionPerformed(null);
				} else {
					if (e.getKeyCode() == KeyEvent.VK_F3) {
						jMenuItemPopUpAlterarStatus_actionPerformed(null);
					}
				}
			}
		}
		refresh();
	}

	/**
	 * Método que invoca a thread de Verify para verificação de links
	 * 
	 * @param e
	 *            Evento
	 */
	void jButtonVerify_actionPerformed(ActionEvent e) {
		// TODO: ACTION PARA TESTAR OS LINKS
		if (listModel.getSize() > 0) {
			habDesab(false);
			for (int i = 0; i < MAX_THREADS; i++) {
				VerifyLinks verifyLinks = new VerifyLinks();
				verifyLinks.addEventFrameListener(this);
				Thread thread = new Thread(verifyLinks);
				thread.setName("VerifyLinks-" + i);
				thread.start();
			}
		} else {
			setTextoInResult(Utils.getMessages("nolinkstested"), true);
		}
	}

	/**
	 * Método que exibe o sobre
	 * 
	 * @param e
	 *            Evento
	 */
	void jMenuItemSobre_actionPerformed(ActionEvent e) {
		try {
			if (descricao == null) {
				URL url = getClass().getClassLoader().getResource(
						Utils.getMessages("html"));
				descricao = url;
				// != null ? url : new URL(
				// "http://geocities.yahoo.com.br/silviofragata/downloads/downloads/download.html"
				// );
			}
			new DownloadAbout(this, Utils.getMessages("about"), Utils
					.getMessages("downloadversion")
					+ VERSION, descricao);
		} catch (Exception ex) {
			logExcecao(ex);
		}
	}

	void jMenuItemConfig_actionPerformed(ActionEvent e) {
		new Configuracoes(this);
	}

	/**
	 * Método que executa o gc
	 * 
	 * @param e
	 *            Evento
	 */
	void jMenuItemGC_actionPerformed(ActionEvent e) {
		forceGC();
	}

	/**
	 * Método que retorna o caminho do arquivo para salvar a lista
	 * 
	 * @return o path do arquivo
	 */
	private String getFileSave() {
		if (currentFileSave.equals(".")) {
			JFileChooser chooser = new JFileChooser();
			TextFileFilter filter = new TextFileFilter();
			filter.addExtension("txt");
			filter.setDescription(Utils.getMessages("textfile"));
			chooser.setDialogType(JFileChooser.SAVE_DIALOG);
			chooser.setFileFilter(filter);
			chooser.setDialogTitle(Utils.getMessages("linksfilename"));
			chooser.setCurrentDirectory(new File(currentDirOpen));
			chooser.setSelectedFile(new File(currentDirOpen + File.separator
					+ currentFileOpen));
			chooser.setFileHidingEnabled(false);
			chooser.setMultiSelectionEnabled(false);

			int returnVal = chooser.showSaveDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				currentDirSave = chooser.getSelectedFile().getParent();
				currentFileSave = chooser.getSelectedFile().getAbsolutePath();
				return currentFileSave;
			}
			return null;
		} else
			return currentFileSave;
	}

	/**
	 * Método que salva a lista em arquivo
	 * 
	 * @param e
	 *            Evento
	 */
	void jMenuItemSalvar_actionPerformed(ActionEvent e) {
		if (listModel.size() == 0) {
			setTextoInResult(Utils.getMessages("emptylist"), true);
		} else {
			String file = getFileSave();
			change = false;

			if ((file != null) && !file.equals("")) {
				try {
					if (file.indexOf(".") == -1) {
						file += ".txt";
					}
					lerListModelToArq(file);
					refresh();
				} catch (Exception ex) {
					fatal(ex);
				}
			}
		}
	}

	/**
	 * Método que obtem os ítens da lista de indere no arquivo
	 * 
	 * @param pathFile
	 *            arquivo
	 * @exception IOException
	 *                Caso ocorra erro na gravação
	 */
	private void lerListModelToArq(String pathFile) throws IOException {
		PrintStream p = null;
		try {
			int tam = listModel.size();

			if (tam == 0) {
				setTextoInResult(Utils.getMessages("emptylist"), true);
				throw new IOException(Utils.getMessages("emptylist"));
			}

			p = new PrintStream(new FileOutputStream(pathFile, false));

			for (int i = 0; i < tam; i++) {
				StatusLink dado = (StatusLink) listModel.get(i);
				StringBuffer sb = new StringBuffer(dado.getUrl());

				if (dado.getStatus() == StatusLink.DIRETORIO) {
					sb.insert(0, "[");
					sb.append("]");
				} else {
					if (dado.getStatus() == StatusLink.QUEBRADO) {
						sb.insert(0, "#");

					} else {
						if (dado.getStatus() == StatusLink.BAIXADO) {
							sb.insert(0, "*");

						}
					}
				}
				p.println(sb.toString());
			}
			setTextoInResult(Utils.getMessages("filesaved"), true);
			setTitle(TITLE + " - " + pathFile);
		} finally {
			if (p != null) {
				p.close();
			}
		}
	}

	/**
	 * Método que irá executar o GC
	 */
	private synchronized void forceGC() {
		System.gc();
	}

	void jRadioButtonMenuItemAlloyAcid_actionPerformed(ActionEvent e) {
		setLookAndFeelAlloy("com.incors.plaf.alloy.themes.acid.AcidTheme",
				jRadioButtonMenuItemAlloyAcid);
	}

	void jRadioButtonMenuItemAlloyBedouin_actionPerformed(ActionEvent e) {
		setLookAndFeelAlloy(
				"com.incors.plaf.alloy.themes.bedouin.BedouinTheme",
				jRadioButtonMenuItemAlloyBedouin);
	}

	void jRadioButtonMenuItemAlloyDefault_actionPerformed(ActionEvent e) {
		setLookAndFeelAlloy("com.incors.plaf.alloy.DefaultAlloyTheme",
				jRadioButtonMenuItemAlloyDefault);
	}

	void jRadioButtonMenuItemAlloyGlass_actionPerformed(ActionEvent e) {
		setLookAndFeelAlloy("com.incors.plaf.alloy.themes.glass.GlassTheme",
				jRadioButtonMenuItemAlloyGlass);
	}

	private void setLookAndFeelAlloy(String theme, JRadioButtonMenuItem radio) {
		try {
			Class acidClass = Class.forName(theme);
			Class[] par = { Class.forName("com.incors.plaf.alloy.AlloyTheme") };
			Object[] objs = { acidClass.newInstance() };
			Constructor construtor = alloy.getDeclaredConstructor(par);
			LookAndFeel alloyLnF = (LookAndFeel) construtor.newInstance(objs);
			setLookFeel(alloyLnF);
		} catch (Exception ex) {
			fatal(ex);
			radio.setSelected(false);
			radio.setEnabled(false);
		}
	}

	private void setLookAndFeelOther(String lf, JRadioButtonMenuItem radio) {
		try {
			setLookFeel(lf);
		} catch (Exception ex) {
			logExcecao(ex);
			radio.setSelected(false);
			radio.setEnabled(false);
		}
	}

	private void setLookFeel(Object lf) throws Exception {
		if (lf instanceof LookAndFeel) {
			UIManager.setLookAndFeel((LookAndFeel) lf);
		} else {
			if (lf instanceof String) {
				UIManager.setLookAndFeel((String) lf);
			} else {
				throw new IllegalArgumentException(Utils
						.getMessages("invalidlef"));
			}
		}
		SwingUtilities.updateComponentTreeUI(DownloadGUI.this);
	}

	void jRadioButtonMenuItemWindows_actionPerformed(ActionEvent e) {
		setLookAndFeelOther(
				"com.sun.java.swing.plaf.windows.WindowsLookAndFeel",
				jRadioButtonMenuItemWindows);
	}

	void jRadioButtonMenuItemMetal_actionPerformed(ActionEvent e) {
		setLookAndFeelOther("javax.swing.plaf.metal.MetalLookAndFeel",
				jRadioButtonMenuItemMetal);
	}

	void jRadioButtonMenuItemKunststoff_actionPerformed(ActionEvent e) {
		setLookAndFeelOther("com.incors.plaf.kunststoff.KunststoffLookAndFeel",
				jRadioButtonMenuItemKunststoff);
	}

	/**
	 * Classe que adiciona o menu popup
	 */
	class PopupListener extends MouseAdapter {

		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			int[] indexes = jListUrls.getSelectedIndices();
			if (e.isPopupTrigger() && jListUrls.isEnabled()) {
				if ((listModel.size() == 0)
						|| jListUrls.getSelectedIndex() == -1) {
					jMenuItemPopUpAlterarStatus.setEnabled(false);
					jMenuItemPopUpAlterar.setEnabled(false);
					jMenuItemPopUpRemover.setEnabled(false);
					jMenuItemPopUpRemoveAll.setEnabled(false);
					jMenuItemSelectAll.setEnabled(listModel.size() != 0);
				} else {
					jMenuItemSelectAll.setEnabled(true);
					if (indexes.length > 1) {
						jMenuItemPopUpAlterarStatus.setEnabled(true);
						jMenuItemPopUpAlterar.setEnabled(false);
						jMenuItemPopUpRemover.setEnabled(true);
						jMenuItemPopUpRemoveAll.setEnabled(true);
					} else {
						jMenuItemPopUpAlterarStatus.setEnabled(true);
						jMenuItemPopUpAlterar.setEnabled(true);
						jMenuItemPopUpRemover.setEnabled(true);
						jMenuItemPopUpRemoveAll.setEnabled(true);
					}
				}
				jPopupMenuList.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	class TimerText {
		public TimerText() {
		}

		public void setTextLabel(final String texto) {
			Timer t = new Timer(5000, new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jLabelStatus.setText(texto);
				}
			});
			t.setRepeats(false);
			t.start();
		}
	}

	class MouseAdapterLabel extends java.awt.event.MouseAdapter {
		private int number;

		public MouseAdapterLabel(int number) {
			super();
			this.number = number;
		}

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2 && stopArray[number]) {
				stopArray[number] = !stopArray[number];
				setIconConexao(stopArray[number], number);
			}
		}
	}

	public boolean validateFinish() {
		boolean ok = true;
		for (int i = 0; i < finishArray.length; i++) {
			ok &= finishArray[i];
		}
		return ok;
	}

	void jMenuItemPopUpRemoveAll_actionPerformed(ActionEvent e) {
		listModel.removeAllElements();
		changeIt();
	}

	void jButtonProcurar_actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		TextFileFilter filter = new TextFileFilter();
		filter.setDescription(Utils.getMessages("dirs"));
		filter.setExtensionListInDescription(false);
		chooser.setFileFilter(filter);
		chooser.setDialogTitle(Utils.getMessages("choosedir"));
		chooser.setCurrentDirectory(new File(currentDirSearch));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int returnVal = chooser.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			currentDirSearch = chooser.getSelectedFile().getAbsolutePath();
			jTextFieldDestino.setText(currentDirSearch);
		}
	}

	void jTextFieldDestino_focusGained(FocusEvent e) {
		jTextFieldDestino.selectAll();
	}

	void jMenuItemGetLinks_actionPerformed(ActionEvent e) {
		new GetLinksFrame(DownloadGUI.this, listModel);
		changeIt();
		refresh();
	}

	public void refresh() {
		jPanelBotoes.validate();
		jPanelURL.validate();
		jButtonDownload.validate();
		jButtonProcurar.validate();
		jButtonVerify.validate();
		jListUrls.validate();
		jTextFieldDestino.validate();
		jPanelBotoes.validate();
		jPanelStatus.validate();
		this.validate();
	}

	void jMenuItemMemoryMonitor_actionPerformed(ActionEvent e) {
		if (!MemoryMonitor.isActive) {
			MemoryMonitor.showMonitor(DownloadGUI.this);
		}
	}

	void jButtonClearLog_actionPerformed(ActionEvent e) {
		clearLog();
	}

	void jMenuItemSelectAll_actionPerformed(ActionEvent e) {
		jListUrls.setSelectionInterval(0, listModel.getSize());
		jListUrls.repaint();
	}

	public void run() {
		this.show();
	}

	public boolean getStopArray(int number) {
		return stopArray[number];
	}

	public void setStopArray(int number, boolean val) {
		stopArray[number] = val;
	}

	public ListModel getListModel() {
		return listModel;
	}

	public String getTarget() {
		return jTextFieldDestino.getText();
	}

	public void log(String log) {
		addLog(log, OUT);
	}

	public void error(String error) {
		addLog(error, ERR);
	}

	public void fatal(Exception ex) {
		logExcecao(ex);
	}

	public void setFinishArray(int number, boolean b) {
		finishArray[number] = b;
	}

	public void setTimerText(String messageDefault) {
		setTimetTextInResult(messageDefault);
	}

	/**
	 * The main program for the DownloadGUI class
	 */
	public static void main(String[] args) {
		Class kunststoff;
		Class alloy;
		try {
			kunststoff = Class
					.forName("com.incors.plaf.kunststoff.KunststoffLookAndFeel");
			UIManager.setLookAndFeel((LookAndFeel) kunststoff.newInstance());
		} catch (Exception e) {
			kunststoff = null;
		}
		try {
			alloy = Class.forName("com.incors.plaf.alloy.AlloyLookAndFeel");
			UIManager.setLookAndFeel((LookAndFeel) alloy.newInstance());
		} catch (Exception e) {
			alloy = null;
		}

		/*
		 * System.getProperties().put( "proxySet", "true" );
		 * System.getProperties().put( "proxyHost", "cascelano" );
		 * System.getProperties().put( "proxyPort", "1080" );
		 */
		DownloadGUI downloadGUI = new DownloadGUI(kunststoff, alloy);
		Thread t = new Thread(downloadGUI, "DownloadGUI");
		t.start();
	}
}