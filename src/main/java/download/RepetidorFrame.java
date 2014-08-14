package download;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import download.listener.LogListener;


/**
 * @author Silvio Fragata da Silva
 * @version 1.0
 */
public class RepetidorFrame extends DialogBase implements LogListener
{
	private static final long serialVersionUID = 6064307323765189267L;
	
	private JPanel jPanelRepetidor = new JPanel();
	private JPanel jPanelNorte = new JPanel();
	private JPanel jPanelCentro = new JPanel();
	private JLabel jLabelMensagem = new JLabel();
	private final int MAX_NUMBER = 4;
	private JPanel jPanelNorteSul = new JPanel();
	private JPanel jPanelNorteNorte = new JPanel();
	private JTextField jTUrl = new JTextField();
	private JLabel jLabelURL = new JLabel();
	private JCheckBox jCheckBoxZeros = new JCheckBox();
	private DefaultListModel listModel;
	private JFrame frame;
	private JPanel jPanelNorteCentro = new JPanel();
	private JLabel jLabelInicio = new JLabel();
	private JTextField jTInicio = new JTextField();
	private JLabel jLabelFim = new JLabel();
	private JTextField jTFim = new JTextField();

	/**
	 * Constructor for the RepetidorFrame object
	 * @param frame Frame pai
	 * @param listModel ListModel
	 */
	public RepetidorFrame(JFrame frame, DefaultListModel listModel)
	{
		super(frame, Utils.getMessages("repetidortitle"), true);
		try
		{
			this.listModel = listModel;
			jbInit();
		} 
		catch (Exception e)
		{
			fatal(e);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @exception Exception
	 *               Description of the Exception
	 */
	private void jbInit() throws Exception
	{
		jTUrl.setColumns(30);
		jTUrl.addFocusListener(this);
		jLabelURL.setText(Utils.getMessages("typeurl"));
		jLabelURL.setForeground(Color.black);
		jCheckBoxZeros.setSelected(true);
		jCheckBoxZeros.setText(Utils.getMessages("zerolenght"));
		jPanelNorte.setLayout(new BorderLayout());

		this.setResizable(false);
		jLabelInicio.setText(Utils.getMessages("begin"));
		jLabelInicio.setForeground(Color.black);
		jTInicio.setColumns(4);
		jTInicio.setText("0");
		jTInicio.addKeyListener(new java.awt.event.KeyAdapter()
		{
			public void keyTyped(KeyEvent e)
			{
				jTInicio_keyTyped(e);
			}
		});
		jTInicio.addFocusListener(this);

		jLabelFim.setText(Utils.getMessages("end"));
		jLabelFim.setForeground(Color.black);
		jTFim.setColumns(4);
		jTFim.setText("0");
		jTFim.addKeyListener(new java.awt.event.KeyAdapter()
		{
			public void keyTyped(KeyEvent e)
			{
				jTFim_keyTyped(e);
			}
		});
		jTFim.addFocusListener(this);

		this.getContentPane().add(jPanelRepetidor);

		jPanelRepetidor.setLayout(new BorderLayout());
		jPanelRepetidor.add(jPanelNorte, BorderLayout.NORTH);

		jPanelNorte.add(jPanelNorteNorte, BorderLayout.NORTH);

		jPanelNorteNorte.add(jLabelURL, null);
		jPanelNorteNorte.add(jTUrl, null);

		jPanelNorte.add(jPanelNorteSul, BorderLayout.SOUTH);

		jPanelNorteSul.add(jCheckBoxZeros, null);
		jPanelNorte.add(jPanelNorteCentro, BorderLayout.CENTER);
		jPanelNorteCentro.add(jLabelInicio, null);
		jPanelNorteCentro.add(jTInicio, null);
		jPanelNorteCentro.add(jLabelFim, null);
		jPanelNorteCentro.add(jTFim, null);

		jPanelRepetidor.add(jPanelCentro, BorderLayout.CENTER);
		jPanelCentro.add(jLabelMensagem, null);
		jLabelMensagem.setForeground(Color.red);

		Utils.center(this, 400, 200);
		setVisible(true);
	}

	/**
	 * Description of the Method
	 * @param e
	 *           Description of the Parameter
	 */
	public void actionPerformed(ActionEvent e)
	{
		super.actionPerformed(e);
		jLabelMensagem.setText(Utils.getMessages("getlinksmessage"));
		try
		{
			new Repetidor( listModel, jTUrl.getText().trim(),
						   Integer.parseInt(jTInicio.getText().trim()),
						   Integer.parseInt(jTFim.getText().trim()),
						   jCheckBoxZeros.isSelected()
						  );
			jLabelMensagem.setText(Utils.getMessages("linkscreated"));
		} 
		catch (Exception ex)
		{
			error( ex.getMessage() );
			jLabelMensagem.setText(ex.getMessage());
		}
	}

	/**
	 * @param e
	 *           Description of the Parameter
	 */
	private void jTInicio_keyTyped(KeyEvent e)
	{
		vetaLetras(e);
		if (jTInicio.getText().trim().length() == MAX_NUMBER - 1)
		{
			jTInicio.transferFocus();
		}
	}

	/**
	 * Description of the Method
	 * @param e
	 *           Description of the Parameter
	 */
	private void vetaLetras(KeyEvent e)
	{
		if (!Character.isDigit(e.getKeyChar()))
		{
			e.consume();
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param e
	 *           Description of the Parameter
	 */
	private void jTFim_keyTyped(KeyEvent e)
	{
		vetaLetras(e);
		if (jTFim.getText().trim().length() == MAX_NUMBER - 1)
		{
			jTFim.transferFocus();
		}
	}

	/**
	 *  Description of the Method */
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

	public void log(String log)
	{
		if( frame instanceof DownloadGUI )
		{
			((DownloadGUI)frame).log( log );
		}
	}

	public void fatal(Exception ex)
	{
		if( frame instanceof DownloadGUI )
		{
			((DownloadGUI)frame).fatal( ex );
		}
	}

	public void error(String error)
	{
		if( frame instanceof DownloadGUI )
		{
			((DownloadGUI)frame).error( error );
		}
	}
}