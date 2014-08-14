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
	 * Método que retorna o tamanho da lista 
	 * @return o tamanho da lista
	 */
	public int getSizeList();
	
	/**
	 * Método que adiciona um link à lista 
	 * @param statusLink o link
	 */
	public void addLista(StatusLink statusLink);
}
