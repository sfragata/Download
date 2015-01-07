package br.com.sfragata.download.listener;

import br.com.sfragata.download.StatusLink;

/**
 * @author Silvio Fragata da Silva
 */

public interface MensagemListener extends LogListener {
	public void setText(String texto);

	public int getSizeList();

	public void addList(StatusLink statusLink);
}
