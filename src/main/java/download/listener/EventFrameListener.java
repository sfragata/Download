package download.listener;

import javax.swing.ListModel;

/**
 * Interface para servir como Listener da Classe DownlodGUI par itera��o com
 * as threads
 * @author Silvio Fragata da Silva
 * @version 1.0
 */
public interface EventFrameListener extends LogListener{

	/**
	 * M�todo que faz com que o scroll v� para o in�cio
	 */
	public void scrollUp();
	
	/**
	 * M�todo que faz com que o scroll des�a um delocamento 
	 * @param offSet o deslocamento
	 */
	public void scrollDown(int offSet);
	
	/**
	 * M�todo que muda o �cone para o conectado 
	 * @param number n�mero da thread correspondente
	 */
	public void iconConnect(int number);
	
	/**
	 * M�todo que limpar o ProgressBar da thread 
	 * @param number n�mero da thread correspondente
	 */
	public void clearProgress(int number);
	
	/**
	 * M�todo que retorna se uma thread est� parada ou n�o 
	 * @param number n�mero da thread correspondente
	 * @return se a thread est� parada ou n�o
	 */
	public boolean getStopArray( int number );
	
	/**
	 * M�todo que altera o status da thread correspondente 
	 * @param number n�mero da thread correspondente
	 * @param val true ou false
	 */
	public void setStopArray( int number, boolean val );
	
	/**
	 * M�todo que d� um refresh na GUI
	 */
	public void refresh();
	
	/**
	 * M�todo que retorna o ListModel 
	 * @return ListModel.
	 */
	public ListModel getListModel();
	
	/**
	 * M�todo que retorna o caminho destino dos arquivos 
	 * @return target
	 */
	public String getTarget();
	
	/**
	 * M�todo que Muda o nome do label de uma thread 
	 * @param number n�mero da thread correspondente
	 * @param msg texto
	 */
	public void setLabelProgress( int number, String msg );
	
	/**
	 * M�todo que altera o tool tip da uma thread 
	 * @param number n�mero da thread correspondente
	 * @param msg texto
	 */
	public void setToolTipLabelProgress(int number, String msg);
	
	/**
	 * 
	 * M�todo que altera a ProgressBar de uma thread
	 * @param parcial valor parcial
	 * @param total	valor total
	 * @param number n�mero da thread correspondente
	 */
	public void setProgress(int parcial, float total, int number);
	

	/**
	 * M�todo que muda o �cone para o desconectado 
	 * @param number n�mero da thread correspondente
	 */
	public void iconDisconnect(int number);
	
	/**
	 * M�todo que altera a finaliza��o de uma thread 
	 * @param number n�mero da thread correspondente
	 * @param b true ou false
	 */
	public void setFinishArray(int number, boolean b);
	
	/**
	 * M�todo que valida se todas as threads finalizaram 
	 * @return true ou false
	 */
	public boolean validateFinish();
	
	/**
	 * M�todo que coloca um texto em um JLabel 
	 * @param string texto
	 * @param b se � para apagar o texto depois ou n�o
	 */
	public void setTextoInResult(String string, boolean b);
	
	/**
	 * M�todo que coloca um texto em um JLabel mas esse texto some depois de
	 * um tempo
	 * @param message texto
	 */
	public void setTimerText(String message);
	
	/**
	 * M�todo que habilida ou desabilita um conjunto de componentes 
	 * @param b true ou false.
	 */
	public void habDesab(boolean b);

}
