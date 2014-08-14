package download.listener;

import javax.swing.ListModel;

/**
 * Interface para servir como Listener da Classe DownlodGUI par iteração com
 * as threads
 * @author Silvio Fragata da Silva
 * @version 1.0
 */
public interface EventFrameListener extends LogListener{

	/**
	 * Método que faz com que o scroll vá para o início
	 */
	public void scrollUp();
	
	/**
	 * Método que faz com que o scroll desça um delocamento 
	 * @param offSet o deslocamento
	 */
	public void scrollDown(int offSet);
	
	/**
	 * Método que muda o ícone para o conectado 
	 * @param number número da thread correspondente
	 */
	public void iconConnect(int number);
	
	/**
	 * Método que limpar o ProgressBar da thread 
	 * @param number número da thread correspondente
	 */
	public void clearProgress(int number);
	
	/**
	 * Método que retorna se uma thread está parada ou não 
	 * @param number número da thread correspondente
	 * @return se a thread está parada ou não
	 */
	public boolean getStopArray( int number );
	
	/**
	 * Método que altera o status da thread correspondente 
	 * @param number número da thread correspondente
	 * @param val true ou false
	 */
	public void setStopArray( int number, boolean val );
	
	/**
	 * Método que dá um refresh na GUI
	 */
	public void refresh();
	
	/**
	 * Método que retorna o ListModel 
	 * @return ListModel.
	 */
	public ListModel getListModel();
	
	/**
	 * Método que retorna o caminho destino dos arquivos 
	 * @return target
	 */
	public String getTarget();
	
	/**
	 * Método que Muda o nome do label de uma thread 
	 * @param number número da thread correspondente
	 * @param msg texto
	 */
	public void setLabelProgress( int number, String msg );
	
	/**
	 * Método que altera o tool tip da uma thread 
	 * @param number número da thread correspondente
	 * @param msg texto
	 */
	public void setToolTipLabelProgress(int number, String msg);
	
	/**
	 * 
	 * Método que altera a ProgressBar de uma thread
	 * @param parcial valor parcial
	 * @param total	valor total
	 * @param number número da thread correspondente
	 */
	public void setProgress(int parcial, float total, int number);
	

	/**
	 * Método que muda o ícone para o desconectado 
	 * @param number número da thread correspondente
	 */
	public void iconDisconnect(int number);
	
	/**
	 * Método que altera a finalização de uma thread 
	 * @param number número da thread correspondente
	 * @param b true ou false
	 */
	public void setFinishArray(int number, boolean b);
	
	/**
	 * Método que valida se todas as threads finalizaram 
	 * @return true ou false
	 */
	public boolean validateFinish();
	
	/**
	 * Método que coloca um texto em um JLabel 
	 * @param string texto
	 * @param b se é para apagar o texto depois ou não
	 */
	public void setTextoInResult(String string, boolean b);
	
	/**
	 * Método que coloca um texto em um JLabel mas esse texto some depois de
	 * um tempo
	 * @param message texto
	 */
	public void setTimerText(String message);
	
	/**
	 * Método que habilida ou desabilita um conjunto de componentes 
	 * @param b true ou false.
	 */
	public void habDesab(boolean b);

}
