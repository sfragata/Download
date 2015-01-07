/**
 * $Id: DownloadGUI.java,v 1.3 2006/03/03 23:15:36 sfragata Exp $
 */

package br.com.sfragata.download;

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
import java.math.BigDecimal;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import br.com.sfragata.download.listener.EventFrameListener;
import br.com.sfragata.download.thread.Download;
import br.com.sfragata.download.thread.VerifyLinks;


/**
 * Main class
 * 
 * @author Silvio Fragata da Silva
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

	private JPanel jPanelTarget = new JPanel();

	private JPanel jPanelButtons = new JPanel();

	private JPanel jPanelStatus = new JPanel();

	private JLabel jLabelTarget = new JLabel();

	private JLabel jLabelStatus = new JLabel();

	private JTextField jTextFieldTarget = new JTextField();

	private JButton jButtonDownload = new JButton();

	private JButton jButtonVerify = new JButton();

	private JButton jButtonFind = new JButton();

	private DefaultListModel listModel;

	private JList jListUrls;

	private JScrollPane jScrollPaneListUrl = new JScrollPane();

	private JMenuBar jMenuBarDownload = new JMenuBar();

	private JMenu jMenuFile = new JMenu();

	private JMenuItem jMenuItemOpen = new JMenuItem();

	private JMenuItem jMenuItemClose = new JMenuItem();

	private JMenu jMenuInsert = new JMenu();

	private JMenuItem jMenuItemNew = new JMenuItem();

	private JMenuItem jMenuItemRepeatLinks = new JMenuItem();

	private JMenuItem jMenuItemCleaner = new JMenuItem();

	private JMenu jMenuActions = new JMenu();

	private JMenuItem jMenuItemDownload = new JMenuItem();

	private JMenuItem jMenuItemVerify = new JMenuItem();

	private JMenu jMenuHelp = new JMenu();

	private JMenuItem jMenuItemAbout = new JMenuItem();

	private JMenuItem jMenuItemConfig = new JMenuItem();

	private JMenuItem jMenuItemGC = new JMenuItem();

	private JPopupMenu jPopupMenuList = new JPopupMenu();

	private JMenuItem jMenuItemPopUpRemove = new JMenuItem();

	private JMenuItem jMenuItemPopUpInsert = new JMenuItem();

	private JMenuItem jMenuItemPopUpChange = new JMenuItem();

	private JMenuItem jMenuItemPopUpChangeStatus = new JMenuItem();

	public static final int MAX_THREADS = System.getProperty("num.threads") == null ? 4
			: Integer.parseInt(System.getProperty("num.threads"));

	public static final int NO = 1;

	public static final int ALL = 2;

	private int folderCount = 0;

	private URL description;

	public static final char OUT = 'O';

	public static final char ERR = 'E';

	public static final String[] SIMBOLS = { "!", "@", "#", "$", "%", "¨", "&",
			"*", "(", ")", "+", "?", "=", "~", "´", "§" };

	private JMenuItem jMenuItemSalve = new JMenuItem();

	private JMenuItem jMenuItemPopUpRemoveAll = new JMenuItem();

	private JMenuItem jMenuItemGetLinks = new JMenuItem();

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

	public DownloadGUI() {

		try {
			init();
		} catch (Exception e) {
			logException(e);
		}
	}

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
				if (JOptionPane.showOptionDialog(null,
						Utils.getMessages("clearlist"),
						Utils.getMessages("clearlist"),
						JOptionPane.DEFAULT_OPTION,
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

	private void fileToListModel(String pathFile) throws IOException {
		BufferedReader b = new BufferedReader(new FileReader(pathFile));
		String line;
		while (b.ready()) {
			line = b.readLine();
			if (!((line == null) || line.equals(""))) {
				int status = getStatusLink(line);
				line = removeSimbol(line, status);
				if (status == StatusLink.FOLDER) {
					folderCount++;
				}
				if (!line.trim().equals("")) {
					StatusLink d = new StatusLink(line, status);
					if (!Utils.search(d, listModel)) {
						listModel.addElement(d);
					}
				}
			}
		}
		b.close();
		refresh();
	}

	private String removeSimbol(String link, int status) {
		if (status == StatusLink.FOLDER) {
			return link.substring(1, link.length() - 1);
		} else {
			if (status == StatusLink.DOWNLOADED || status == StatusLink.BROKEN) {
				return link.substring(1);
			}
		}

		return link;
	}

	private int getStatusLink(String link) {
		if (link.startsWith("[") && link.endsWith("]")) {
			return StatusLink.FOLDER;
		} else {
			if (link.startsWith("*")) {
				return StatusLink.DOWNLOADED;
			} else {
				if (link.startsWith("#")) {
					return StatusLink.BROKEN;
				}
			}
		}
		return StatusLink.NOT_DOWNLOADED;
	}

	private void clearList() {
		listModel.removeAllElements();
		refresh();
		jTextFieldTarget.setText("");
		folderCount = 0;
		setTitle(TITLE);
		currentDirOpen = ".";
		currentFileOpen = ".";
		currentFileSave = ".";
	}

	public void setTextoInResult(String m, boolean apagar) {
		setMessage(m, jLabelStatus);
		if (apagar) {
			timerText.setTextLabel("");
		}
	}

	private void setTimetTextInResult(String m) {
		timerText.setTextLabel(m);
	}

	protected static void setMessage(final String m, final JLabel label) {
		label.setText(m);
	}

	public void setProgress(final int parcial, final float total,
			final int number) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					BigDecimal par = new BigDecimal(parcial / total * 100);
					par = par.setScale(0, BigDecimal.ROUND_DOWN);
					jProgressBarArray[number].setString(new StringBuilder(par
							.toString()).append("%").toString());
					jProgressBarArray[number].setValue(par.intValue());
					jProgressBarArray[number].setToolTipText(new StringBuilder(
							Utils.getMessages("untildownload"))
							.append(Utils.formatSize(parcial))
							.append(" bytes.").toString());
				}
			});
		} catch (Exception e) {
			logException(e);
		}
	}

	public void setLabelProgress(int number, String texto) {
		jLabelArray[number].setText(texto);
	}

	public void setToolTipLabelProgress(int number, String texto) {
		jLabelArray[number].setToolTipText(texto);
	}

	public void clearProgress(int number) {
		jProgressBarArray[number].setValue(0);
		jProgressBarArray[number].setString("0%");
		jProgressBarArray[number].setToolTipText("");
	}

	private void scroll(int valor) {
		JScrollBar bar = jScrollPaneListUrl.getVerticalScrollBar();
		if (bar != null) {
			bar.setValue(valor);

		}
		jScrollPaneListUrl.validate();
		jListUrls.validate();
		this.validate();
	}

	public void scrollDown(int number) {
		scroll(number * 17);
	}

	private void scrollBottom() {
		JScrollBar bar = jScrollPaneListUrl.getVerticalScrollBar();
		if (bar != null) {
			scroll(bar.getMaximum());
		}
	}

	public void scrollUp() {
		scroll(0);
	}

	public void logException(Exception e) {
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
		jTabbedPaneDownload.add(jPanelDownload,
				Utils.getMessages("urltabbedtitle"));

		jPanelPrincipal.add(jPanelStatus, BorderLayout.SOUTH);

		jPanelTarget.setLayout(new FlowLayout());
		jPanelTarget.add(jLabelTarget);
		jPanelTarget.add(jTextFieldTarget);
		jPanelTarget.add(jButtonFind);

		jPanelURL.setBorder(BorderFactory.createTitledBorder(Utils
				.getMessages("urltitle")));
		jPanelURL.setLayout(new BorderLayout());
		jPanelURL.add(jScrollPaneListUrl, BorderLayout.CENTER);

		jPanelButtons.setLayout(new FlowLayout());
		jPanelButtons.add(jButtonDownload, null);
		jPanelButtons.add(jButtonVerify, null);
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
		jPanelDownload.add(jPanelTarget);
		jPanelDownload.add(Box.createRigidArea(new Dimension(0, 30)));
		jPanelDownload.add(jPanelButtons);
	}

	private void createJLabels() {
		jLabelTarget.setText(Utils.getMessages("target"));
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

		URL u = Utils.getResource("resources/download.gif");
		if (u != null) {
			jButtonDownload.setIcon(new ImageIcon(u));
		}
		jButtonVerify.setText(Utils.getMessages("testlinks"));
		jButtonVerify.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonVerify_actionPerformed(e);
			}
		});
		u = Utils.getResource("resources/execute.gif");
		if (u != null) {
			jButtonVerify.setIcon(new ImageIcon(u));

		}
		jButtonFind.setBounds(new Rectangle(386, 252, 112, 27));
		jButtonFind.setText(Utils.getMessages("find"));
		jButtonFind.setMnemonic(KeyEvent.VK_P);
		jButtonFind.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonFindActionPerformed(e);
			}
		});
		u = Utils.getResource("resources/find.gif");
		if (u != null) {
			jButtonFind.setIcon(new ImageIcon(u));
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
		jTextFieldTarget.setText("");
		jTextFieldTarget.setFont(new java.awt.Font("Tahoma", Font.BOLD, 12));
		jTextFieldTarget.setColumns(30);
		jTextFieldTarget.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(FocusEvent e) {
				jTextFieldTargetFocusGained(e);
			}
		});
	}

	private void createJMenus() {
		jMenuFile.setText(Utils.getMessages("file"));
		jMenuFile.setMnemonic(KeyEvent.VK_A);

		jMenuInsert.setText(Utils.getMessages("insert"));
		jMenuInsert.setMnemonic(KeyEvent.VK_I);

		jMenuActions.setText(Utils.getMessages("actions"));
		jMenuActions.setMnemonic(KeyEvent.VK_E);

		jMenuHelp.setText(Utils.getMessages("help"));
		jMenuHelp.setMnemonic(KeyEvent.VK_J);

		jMenuFile.add(jMenuItemOpen);
		jMenuFile.add(jMenuItemSalve);
		jMenuFile.add(jMenuItemClose);

		jMenuInsert.add(jMenuItemNew);
		jMenuInsert.add(jMenuItemRepeatLinks);
		jMenuInsert.add(jMenuItemGetLinks);
		jMenuInsert.add(jMenuItemCleaner);

		jMenuActions.add(jMenuItemDownload);
		jMenuActions.add(jMenuItemVerify);

		jMenuHelp.add(jMenuItemAbout);
		jMenuHelp.add(jMenuItemConfig);
		jMenuHelp.add(jMenuItemGC);
	}

	private void createJMenuItems() {
		jMenuItemOpen.setText(Utils.getMessages("open"));
		URL u = Utils.getResource("resources/import.gif");
		if (u != null) {
			jMenuItemOpen.setIcon(new ImageIcon(u));
		}
		jMenuItemOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				KeyEvent.VK_O, KeyEvent.CTRL_MASK, false));
		jMenuItemOpen.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemAbrir_actionPerformed(e);
			}
		});

		jMenuItemClose.setText(Utils.getMessages("close"));
		u = Utils.getResource("resources/exit.gif");
		if (u != null) {
			jMenuItemClose.setIcon(new ImageIcon(u));
		}
		jMenuItemClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				KeyEvent.VK_X, KeyEvent.ALT_MASK, false));
		jMenuItemClose.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemFechar_actionPerformed(e);
			}
		});

		jMenuItemNew.setText(Utils.getMessages("new"));
		u = Utils.getResource("resources/new.gif");
		if (u != null) {
			jMenuItemNew.setIcon(new ImageIcon(u));
		}
		jMenuItemNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				KeyEvent.VK_INSERT, 0, false));
		jMenuItemNew.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemNovo_actionPerformed(e);
			}
		});

		jMenuItemRepeatLinks.setText(Utils.getMessages("repeatlinks"));
		u = Utils.getResource("resources/repeat.gif");
		if (u != null) {
			jMenuItemRepeatLinks.setIcon(new ImageIcon(u));
		}
		jMenuItemRepeatLinks.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				KeyEvent.VK_R, KeyEvent.ALT_MASK, false));
		jMenuItemRepeatLinks
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jMenuItemRepetirLinks_actionPerformed(e);
					}
				});

		jMenuItemCleaner.setText(Utils.getMessages("clean"));
		u = Utils.getResource("resources/clean.gif");
		if (u != null) {
			jMenuItemCleaner.setIcon(new ImageIcon(u));
		}
		jMenuItemCleaner.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				KeyEvent.VK_DELETE, KeyEvent.CTRL_MASK, false));
		jMenuItemCleaner.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemLimpar_actionPerformed(e);
			}
		});
		jMenuItemDownload.setText(Utils.getMessages("download"));
		u = Utils.getResource("resources/download.gif");
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
		u = Utils.getResource("resources/execute.gif");
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

		jMenuItemAbout.setText(Utils.getMessages("about"));
		jMenuItemAbout.setMnemonic(KeyEvent.VK_S);
		jMenuItemAbout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				KeyEvent.VK_F1, 0, false));
		u = Utils.getResource("resources/about.gif");
		if (u != null) {
			jMenuItemAbout.setIcon(new ImageIcon(u));
		}
		jMenuItemAbout.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemSobre_actionPerformed(e);
			}
		});

		jMenuItemConfig.setText(Utils.getMessages("config"));
		jMenuItemConfig.setMnemonic(KeyEvent.VK_C);
		jMenuItemConfig.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				KeyEvent.VK_P, KeyEvent.CTRL_MASK, false));
		u = Utils.getResource("resources/setup.gif");
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
		u = Utils.getResource("resources/gc.gif");
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

		jMenuItemSalve.setText(Utils.getMessages("save"));
		u = Utils.getResource("resources/export.gif");
		if (u != null) {
			jMenuItemSalve.setIcon(new ImageIcon(u));
		}
		jMenuItemSalve.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				KeyEvent.VK_S, KeyEvent.CTRL_MASK, false));
		jMenuItemSalve.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemSalvar_actionPerformed(e);
			}
		});

		jMenuItemGetLinks.setText(Utils.getMessages("getlinks"));
		jMenuItemGetLinks.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				KeyEvent.VK_G, KeyEvent.ALT_MASK, false));
		u = Utils.getResource("resources/link.gif");
		if (u != null) {
			jMenuItemGetLinks.setIcon(new ImageIcon(u));
		}
		jMenuItemGetLinks
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jMenuItemGetLinksActionPerformed(e);
					}
				});
	}

	private void createJMenuItemPopUp() {
		jMenuItemPopUpRemove.setText(Utils.getMessages("remove"));
		URL u = Utils.getResource("resources/del.gif");
		if (u != null) {
			jMenuItemPopUpRemove.setIcon(new ImageIcon(u));
		}
		jMenuItemPopUpRemove
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jMenuItemPopUpRemover_actionPerformed(e);
					}
				});
		jMenuItemPopUpRemove.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				KeyEvent.VK_DELETE, 0, false));

		jMenuItemPopUpInsert.setText(Utils.getMessages("insert"));
		u = Utils.getResource("resources/new.gif");
		if (u != null) {
			jMenuItemPopUpInsert.setIcon(new ImageIcon(u));
		}
		jMenuItemPopUpInsert
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jMenuItemPopUpInserir_actionPerformed(e);
					}
				});
		jMenuItemPopUpInsert.setAccelerator(jMenuItemNew.getAccelerator());

		jMenuItemPopUpChangeStatus.setText(Utils.getMessages("changestatus"));
		u = Utils.getResource("resources/edit.gif");
		if (u != null) {
			jMenuItemPopUpChangeStatus.setIcon(new ImageIcon(u));
		}
		jMenuItemPopUpChangeStatus
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jMenuItemPopUpAlterarStatus_actionPerformed(e);
					}
				});
		jMenuItemPopUpChangeStatus.setAccelerator(javax.swing.KeyStroke
				.getKeyStroke(KeyEvent.VK_F3, 0, false));

		jMenuItemPopUpChange.setText(Utils.getMessages("change"));
		u = Utils.getResource("resources/edit.gif");
		if (u != null) {
			jMenuItemPopUpChange.setIcon(new ImageIcon(u));
		}
		jMenuItemPopUpChange
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jMenuItemPopUpAlterar_actionPerformed(e);
					}
				});
		jMenuItemPopUpChange.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				KeyEvent.VK_F2, 0, false));

		jMenuItemPopUpRemoveAll.setText(Utils.getMessages("clean"));
		u = Utils.getResource("resources/removeall.gif");
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
		u = Utils.getResource("resources/selall.gif");
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

	private void init() throws Exception {
		URL u = Utils.getResource("resources/globe.gif");
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
				jButtonClearLogActionPerformed(e);
			}
		});
		jButtonClearLog.setSelected(true);
		this.getContentPane().add(jPanelPrincipal, BorderLayout.CENTER);

		jMenuBarDownload.add(jMenuFile);
		jMenuBarDownload.add(jMenuInsert);
		jMenuBarDownload.add(jMenuActions);
		jMenuBarDownload.add(jMenuHelp);

		jPopupMenuList.add(jMenuItemPopUpInsert);
		jPopupMenuList.add(jMenuItemPopUpChange);
		jPopupMenuList.add(jMenuItemPopUpChangeStatus);
		jPopupMenuList.add(jMenuItemPopUpRemove);
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
			jLabelArray[i] = new JLabel("Thread " + i);
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

	void jButtonDownload_actionPerformed(ActionEvent e) {
		setTextoInResult(Utils.getMessages("creatingdir"), false);
		String dir = jTextFieldTarget.getText().trim();
		if ((dir != null) && !dir.equals("") && (listModel.size() > 0)) {
			try {
				File file = new File(dir);

				if (!file.exists()) {
					file.mkdirs();
				}
				String time = new StringBuilder(
						Utils.getMessages("initialingdownload")).append(
						Utils.formatDate(System.currentTimeMillis(),
								"EEEE, d MMMM 'de' yyyy - HH:mm:ss"))
						.toString();
				clearLog();
				addLog(time, OUT);
				this.habDesab(false);
				String t = new StringBuilder(
						Utils.getMessages("efetuandodownload")).append(" ")
						.append((listModel.getSize() - folderCount))
						.append(" ").append(Utils.getMessages("fileswith"))
						.append(" ").append(MAX_THREADS).append(" ")
						.append(Utils.getMessages("threads")).toString();
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

	private void setIconConexao(boolean status, int number) {
		String gif = "resources/disconnect.gif";
		if (status) {
			gif = "resources/connect.gif";
		}
		URL u = Utils.getResource(gif);
		if (u != null) {
			jLabelImageIconArray[number].setIcon(new ImageIcon(u));
			jLabelImageIconArray[number].setToolTipText(status ? Utils
					.getMessages("connected") : Utils
					.getMessages("disconnected"));
			jLabelImageIconArray[number].repaint();
		}
	}

	public void iconConnect(int number) {
		setIconConexao(true, number);
	}

	public void iconDisconnect(int number) {
		setIconConexao(false, number);
	}

	public void habDesab(boolean status) {
		jButtonDownload.setEnabled(status);
		jListUrls.setEnabled(status);
		jTextFieldTarget.setEnabled(status);
		jMenuInsert.setEnabled(status);
		jMenuItemOpen.setEnabled(status);
		jButtonVerify.setEnabled(status);
		jMenuActions.setEnabled(status);
		jMenuItemDownload.setEnabled(status);
		jMenuItemVerify.setEnabled(status);
		jButtonFind.setEnabled(status);
	}

	void this_windowClosing(WindowEvent e) {
		if (listModel.size() > 0 && jListUrls.isEnabled()) {
			if (currentFileOpen.equals(".") || change) {
				if (JOptionPane.showConfirmDialog(null,
						Utils.getMessages("save.list"),
						Utils.getMessages("save.list"),
						JOptionPane.YES_NO_OPTION,
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

	private void alterarURL() {
		if (jListUrls.getSelectedValue() != null) {
			boolean wasDir = false;
			int pos = jListUrls.getSelectedIndex();
			StatusLink d = (StatusLink) jListUrls.getSelectedValue();
			if (d.getStatus() == StatusLink.FOLDER) {
				wasDir = true;
			}
			StatusLink dados = new DownloadDialog(this, d,
					Utils.getMessages("changeurl")).getStatusLink();
			String url = dados.getUrl();
			if ((url != null) && !url.equals("")) {
				if (dados.getStatus() == StatusLink.FOLDER) {
					folderCount++;
				} else {
					if (wasDir) {
						folderCount--;
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

	void jMenuItemFechar_actionPerformed(ActionEvent e) {
		this.this_windowClosing(null);
	}

	void jMenuItemAbrir_actionPerformed(ActionEvent e) {
		try {
			String path = open();
			if (path != null && !path.equals("")) {
				fileToListModel(path);
				setTitle(TITLE + " - " + path);
				scrollBottom();
				refresh();
				jListUrls.setEnabled(true);
				jButtonDownload.setEnabled(true);
				jTextFieldTarget.setEnabled(true);
			}
		} catch (Exception ec) {
			logException(ec);
		}
	}

	void jMenuItemRepetirLinks_actionPerformed(ActionEvent e) {
		new BatchLinkCreatorFrame(DownloadGUI.this, listModel);
		changeIt();
	}

	void jMenuItemNovo_actionPerformed(ActionEvent e) {
		if (jListUrls.isEnabled()) {
			StatusLink dados = new DownloadDialog(this, null,
					Utils.getMessages("inserturl")).getStatusLink();
			if (dados != null) {
				String url = dados.getUrl();
				if ((url != null) && !url.equals("")) {
					if (!Utils.search(dados, listModel)) {
						if (dados.getStatus() == StatusLink.FOLDER) {
							folderCount++;
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

	void jMenuItemLimpar_actionPerformed(ActionEvent e) {
		if (jListUrls.isEnabled()) {
			clearList();
			validate();
			changeIt();
		}
	}

	void jMenuItemPopUpRemover_actionPerformed(ActionEvent e) {
		int[] indices = jListUrls.getSelectedIndices();
		int i = indices.length - 1;
		int lastIndex = 0;
		while (i >= 0) {
			StatusLink d = (StatusLink) listModel.elementAt(i);

			if (d.getStatus() == StatusLink.FOLDER) {
				folderCount--;
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

	void jMenuItemPopUpInserir_actionPerformed(ActionEvent e) {
		this.jMenuItemNovo_actionPerformed(e);
	}

	void jMenuItemPopUpAlterarStatus_actionPerformed(ActionEvent e) {
		int[] indices = jListUrls.getSelectedIndices();
		int qtdDir = 0;
		int size = indices.length;
		StatusLink[] d = new StatusLink[size];
		for (int i = 0; i < size; i++) {
			d[i] = (StatusLink) listModel.elementAt(indices[i]);
			if (d[i].getStatus() == StatusLink.FOLDER) {
				qtdDir++;
			}
		}
		int status = new DownloadChangeStatus(DownloadGUI.this, d,
				Utils.getMessages("changelinkstatus")).getStatus();
		if (status != StatusLink.FOLDER) {
			folderCount -= qtdDir;

		} else {
			folderCount += size - qtdDir;

		}
		changeIt();
		refresh();
	}

	void jMenuItemPopUpAlterar_actionPerformed(ActionEvent e) {
		alterarURL();
	}

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

	void jButtonVerify_actionPerformed(ActionEvent e) {
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

	void jMenuItemSobre_actionPerformed(ActionEvent e) {
		try {
			if (description == null) {
				URL url = Utils.getResource(Utils.getMessages("html"));
				description = url;
			}
			new DownloadAbout(this, Utils.getMessages("about"),
					Utils.getMessages("downloadversion") + VERSION, description);
		} catch (Exception ex) {
			logException(ex);
		}
	}

	void jMenuItemConfig_actionPerformed(ActionEvent e) {
		new ProxyConfig(this);
	}

	void jMenuItemGC_actionPerformed(ActionEvent e) {
		forceGC();
	}

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
				currentFileSave = chooser.getSelectedFile().getAbsolutePath();
				return currentFileSave;
			}
			return null;
		} else
			return currentFileSave;
	}

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
				StringBuilder sb = new StringBuilder(dado.getUrl());

				if (dado.getStatus() == StatusLink.FOLDER) {
					sb.insert(0, "[");
					sb.append("]");
				} else {
					if (dado.getStatus() == StatusLink.BROKEN) {
						sb.insert(0, "#");

					} else {
						if (dado.getStatus() == StatusLink.DOWNLOADED) {
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

	private synchronized void forceGC() {
		System.gc();
	}

	class PopupListener extends MouseAdapter {

		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			int[] indexes = jListUrls.getSelectedIndices();
			if (e.isPopupTrigger() && jListUrls.isEnabled()) {
				if ((listModel.size() == 0)
						|| jListUrls.getSelectedIndex() == -1) {
					jMenuItemPopUpChangeStatus.setEnabled(false);
					jMenuItemPopUpChange.setEnabled(false);
					jMenuItemPopUpRemove.setEnabled(false);
					jMenuItemPopUpRemoveAll.setEnabled(false);
					jMenuItemSelectAll.setEnabled(listModel.size() != 0);
				} else {
					jMenuItemSelectAll.setEnabled(true);
					if (indexes.length > 1) {
						jMenuItemPopUpChangeStatus.setEnabled(true);
						jMenuItemPopUpChange.setEnabled(false);
						jMenuItemPopUpRemove.setEnabled(true);
						jMenuItemPopUpRemoveAll.setEnabled(true);
					} else {
						jMenuItemPopUpChangeStatus.setEnabled(true);
						jMenuItemPopUpChange.setEnabled(true);
						jMenuItemPopUpRemove.setEnabled(true);
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

		public void setTextLabel(final String text) {
			Timer t = new Timer(5000, new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jLabelStatus.setText(text);
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

	void jButtonFindActionPerformed(ActionEvent e) {
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
			jTextFieldTarget.setText(currentDirSearch);
		}
	}

	void jTextFieldTargetFocusGained(FocusEvent e) {
		jTextFieldTarget.selectAll();
	}

	void jMenuItemGetLinksActionPerformed(ActionEvent e) {
		new GetLinksFrame(DownloadGUI.this, listModel);
		changeIt();
		refresh();
	}

	public void refresh() {
		jPanelButtons.validate();
		jPanelURL.validate();
		jButtonDownload.validate();
		jButtonFind.validate();
		jButtonVerify.validate();
		jListUrls.validate();
		jTextFieldTarget.validate();
		jPanelButtons.validate();
		jPanelStatus.validate();
		this.validate();
	}

	void jButtonClearLogActionPerformed(ActionEvent e) {
		clearLog();
	}

	void jMenuItemSelectAll_actionPerformed(ActionEvent e) {
		jListUrls.setSelectionInterval(0, listModel.getSize());
		jListUrls.repaint();
	}

	public void run() {
		this.setVisible(true);
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
		return jTextFieldTarget.getText();
	}

	public void log(String log) {
		addLog(log, OUT);
	}

	public void error(String error) {
		addLog(error, ERR);
	}

	public void fatal(Exception ex) {
		logException(ex);
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
		DownloadGUI downloadGUI = new DownloadGUI();
		Thread t = new Thread(downloadGUI, "DownloadGUI");
		t.start();
	}
}