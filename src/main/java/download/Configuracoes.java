/**
 * $Id: Configuracoes.java,v 1.2 2006/03/03 23:06:47 sfragata Exp $
 */
package download;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Classe para a configuração do proxy 
 * @author Silvio Fragata da Silva
 * @version $Revision: 1.2 $
 */

public class Configuracoes extends DialogBase
{
	private  static final long serialVersionUID = 7608520583447168958L;
	
	private JPanel jPanelConfiguracoes = new JPanel();

	private JPanel jPanelNorte = new JPanel();

	private JPanel jPanelCentro = new JPanel();
	private JTextField jTHost = new JTextField();
	private JTextField jTPorta = new JTextField();

	private JLabel jLabelHost = new JLabel();
	private JLabel jLabelPorta = new JLabel();

	private JCheckBox jCheckBoxUsarProxy = new JCheckBox();

	/**
	 * @param frame Frame pai
	 */
	public Configuracoes(JFrame frame)
	{
		super(frame, Utils.getMessages("configtitle"), true);
		try
		{
			jbInit();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Método que monta os componentes
	 * @exception Exception 
	 */
	private void jbInit() throws Exception
	{
		jCheckBoxUsarProxy.setText(Utils.getMessages("useproxy"));
		jCheckBoxUsarProxy.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jCheckBoxUsarProxy_actionPerformed(e);
			}
		});

		jLabelHost.setText(Utils.getMessages("host"));
		jLabelPorta.setText(Utils.getMessages("port"));

		this.setResizable(false);

		jTPorta.addKeyListener(new java.awt.event.KeyAdapter()
		{
			public void keyTyped(KeyEvent e)
			{
				jTPorta_keyTyped(e);
			}
		});
		jTHost.addFocusListener(this);

		jTPorta.addFocusListener(this);

		this.getContentPane().add(jPanelConfiguracoes);

		jPanelConfiguracoes.setLayout(new BorderLayout());
		
		jPanelCentro.setBorder( BorderFactory.createTitledBorder(
									BorderFactory.createEtchedBorder(),
									"Proxy"));
		
		jPanelConfiguracoes.add(jPanelNorte, BorderLayout.NORTH);
		jPanelConfiguracoes.add(jPanelCentro, BorderLayout.CENTER);

		jPanelNorte.setLayout(new BorderLayout());
		jPanelNorte.add(jCheckBoxUsarProxy, BorderLayout.CENTER);
		
		jPanelCentro.setLayout( new GridLayout(2,2) );
		jPanelCentro.add(jLabelHost);
		jPanelCentro.add(jTHost);
		jPanelCentro.add(jLabelPorta);
		jPanelCentro.add(jTPorta);

		Utils.center(this, 500, 160);

		setHabilitaCampos(false);

		getValores();

		setVisible(true);
	}

	private void getValores()
	{
		String[] val = Utils.getValores();
		if (val != null)
		{
			String useProxy = val[0];
			String host = val[1];
			String port = val[2];
			if (useProxy.equalsIgnoreCase("true"))
			{
				jTHost.setText(host);
				jTPorta.setText(port);
				jCheckBoxUsarProxy.setSelected(
					Boolean.valueOf(useProxy).booleanValue());
				Utils.setSystemProp(val);
				setHabilitaCampos(jCheckBoxUsarProxy.isSelected());
			}
		}
	}

	private void setValores()
	{
		String useProxy = String.valueOf(jCheckBoxUsarProxy.isSelected());
		String host = jTHost.getText();
		String port = jTPorta.getText();
		if( useProxy.equalsIgnoreCase("true") )
		{
		    String[] val = new String[] { useProxy, host, port };
		    Utils.setSystemProp(val);
		    Utils.storeSystemProp(val);
		}
		else
		{
		    Utils.removeStoreSystemProp();
		    Utils.removeSystemProp();
		}
	}

	/**
	 * Ação do Botão OK 
	 * @param e O evento
	 */
	public void actionPerformed(ActionEvent e)
	{
		super.actionPerformed(e);
		setValores();
		close();
	}

	/**
	 * Ação no TextField 
	 * @param e O Evento
	 */
	void jTPorta_keyTyped(KeyEvent e)
	{
		vetaLetras(e);
	}

	private void vetaLetras(KeyEvent e)
	{
		if (!Character.isDigit(e.getKeyChar()))
		{
			e.consume();
		}
	}

	private void jCheckBoxUsarProxy_actionPerformed(ActionEvent e)
	{
		setHabilitaCampos(jCheckBoxUsarProxy.isSelected());
	}

	private void setHabilitaCampos(boolean cond)
	{
		jTHost.setEnabled(cond);
		jTPorta.setEnabled(cond);
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

}
