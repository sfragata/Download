/**
 * $Id: VerifyLinks.java,v 1.2 2006/03/03 23:13:12 sfragata Exp $
 */

package download.thread;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;

import download.StatusLink;
import download.Utils;
import download.listener.EventFrameListener;

/**
 * Classe que verifica os links, alterando o status para quebrado, se o link não
 * estiver disponível
 * 
 * @author Silvio Fragata da Silva
 * @version $Revision: 1.2 $
 */
public class VerifyLinks implements Runnable {

	private EventFrameListener eventFrameListener;

	/**
	 * Método que adiciona o listener de evento
	 * 
	 * @param eventFrameListener
	 */
	public void addEventFrameListener(EventFrameListener eventFrameListener) {
		this.eventFrameListener = eventFrameListener;
	}

	public void run() {
		int tamanho = eventFrameListener.getListModel().getSize();
		eventFrameListener.setTextoInResult(Utils.getMessages("testinglinks"),
				false);

		for (int i = 0; i < tamanho; i++) {
			URL page = null;
			URLConnection u = null;
			StatusLink dado = (StatusLink) eventFrameListener.getListModel()
					.getElementAt(i);

			if (dado.getStatus() == StatusLink.NAO_BAIXADO) {
				dado.setStatus(StatusLink.EM_USO);

				String urlfile = dado.getUrl();
				File fUrl = new File(urlfile);
				try {
					if (fUrl.isFile()) {
						page = fUrl.toURL();

					} else {
						page = new URL(urlfile);
					}
					u = page.openConnection();
					u.connect();
					if (u.getContentLength() == -1) {
						throw new Exception(Utils.getMessages("nolink"));
					}
					dado.setStatus(StatusLink.NAO_BAIXADO);

				} catch (Exception ex) {
					eventFrameListener.log(ex.getMessage() + " "
							+ dado.getUrl());
					dado.setStatus(StatusLink.QUEBRADO);
				}
			}
		}
		eventFrameListener.habDesab(true);
		eventFrameListener.setTextoInResult(Utils.getMessages("testedlinks"),
				true);
	}
}