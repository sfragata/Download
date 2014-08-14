package download;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import download.listener.ButtonOkListener;

import java.awt.*;
import java.awt.event.*;


/**
 * Classe base das caixas de diálogos
 * @author Silvio Fragata da Silva
 * @version 1.0
 */

public abstract class DialogBase
	extends JDialog
	implements ButtonOkListener, FocusListener
{
	private JPanel jPanelNorte = new JPanel();
	private JPanel jPanelSul = new JPanel();
	private JButton jButtonFechar = new JButton();
	protected JButton jButtonOk = new JButton();
	protected JFrame frame = null;

	/**
	 * @param frame
	 *           Frame proprietário
	 * @param title
	 *           Título da caixa de diálogo
	 * @param modal
	 *           Se a caixa de diálogo é modal ou não
	 * @throws HeadlessException
	 */
	public DialogBase(JFrame frame, String title, boolean modal)
		throws HeadlessException
	{
		super(frame, title, true);
		try
		{
			this.frame = frame;
			jbInit();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		jButtonFechar.setText(Utils.getMessages("close"));
		jButtonFechar.setMnemonic(KeyEvent.VK_F);
		jButtonFecharActionListener esc = new jButtonFecharActionListener();
		jPanelNorte.registerKeyboardAction( esc,
											KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
											JComponent.WHEN_IN_FOCUSED_WINDOW);

		jButtonFechar.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jButtonFechar_actionPerformed(e);
			}
		});
		jButtonOk.setText(Utils.getMessages("ok"));
		jButtonOk.setMnemonic(KeyEvent.VK_O);
		jButtonOk.addActionListener(this);
		this.addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				this_windowClosing(e);
			}
		});
		this.getContentPane().add(jPanelNorte, BorderLayout.CENTER);
		this.getContentPane().add(jPanelSul, BorderLayout.SOUTH);
		this.setResizable(false);
		jPanelSul.add(jButtonOk, null);
		jPanelSul.add(jButtonFechar, null);
		getRootPane().setDefaultButton(jButtonOk);

	}

	/**
	 * Ação do botão Fechar
	 * 
	 * @param e
	 *           O Evento
	 */
	void jButtonFechar_actionPerformed(ActionEvent e)
	{
		close();
	}

	private void close()
	{
		if (frame == null)
		{
			System.exit(0);
		} else
		{
			this.dispose();
		}
	}

	/**
	 * Evento de recebimento de foco
	 * @param e O Evento
	 */
	public void focusGained(FocusEvent e)
	{
		Component c = ((Component) e.getSource());
		if (c instanceof JTextComponent)
			 ((JTextComponent) c).selectAll();
	}

	/**
	 * Evento de perda de foco
	 * @param e O Evento
	 */
	public void focusLost(FocusEvent e)
	{}

	/**
	 * Classe para implementar a ação do botão fechar
	 * 
	 * @author Silvio Fragata da Silva
	 * @version 1.0
	 */
	protected class jButtonFecharActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			jButtonFechar.doClick();
			jButtonFechar_actionPerformed(e);
		}
	}

	/**
	 * Ação para quando a janela seja fechada
	 * @param e O Evento
	 */
	void this_windowClosing(WindowEvent e)
	{
		close();
	}

	
	public void actionPerformed(ActionEvent e)
	{
		jButtonOk.requestFocus();
	}
}