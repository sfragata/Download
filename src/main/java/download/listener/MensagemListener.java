package download.listener;

import download.StatusLink;

/**
 * Interface que serve como listener.
 * @author Silvio Fragata da Silva
 * @version 1.0
 */

public interface MensagemListener extends LogListener
{
	/**
	 * Altera o texto 
	 * @param texto
	 */
	public void setTexto(String texto);
	
	/**
	 * M�todo que retorna o tamanho da lista 
	 * @return o tamanho da lista
	 */
	public int getSizeList();
	
	/**
	 * M�todo que adiciona um link � lista 
	 * @param statusLink o link
	 */
	public void addLista(StatusLink statusLink);
}
