package br.com.sfragata.download.thread;

import java.io.File;
import java.net.URI;
import java.net.URLConnection;

import br.com.sfragata.download.StatusLink;
import br.com.sfragata.download.Utils;
import br.com.sfragata.download.listener.EventFrameListener;


/**
 * @author Silvio Fragata da Silva
 */
public class VerifyLinks implements Runnable {

	private EventFrameListener eventFrameListener;

	public void addEventFrameListener(EventFrameListener eventFrameListener) {
		this.eventFrameListener = eventFrameListener;
	}

	public void run() {
		int tamanho = eventFrameListener.getListModel().getSize();
		eventFrameListener.setTextoInResult(Utils.getMessages("testinglinks"),
				false);

		for (int i = 0; i < tamanho; i++) {
			URI page = null;
			URLConnection u = null;
			StatusLink dado = (StatusLink) eventFrameListener.getListModel()
					.getElementAt(i);

			if (dado.getStatus() == StatusLink.NOT_DOWNLOADED) {
				dado.setStatus(StatusLink.IN_USE);

				String urlfile = dado.getUrl();
				File fUrl = new File(urlfile);
				try {
					if (fUrl.isFile()) {
						page = fUrl.toURI();

					} else {
						page = new URI(urlfile);
					}
					u = page.toURL().openConnection();
					u.connect();
					if (u.getContentLength() == -1) {
						throw new Exception(Utils.getMessages("nolink"));
					}
					dado.setStatus(StatusLink.NOT_DOWNLOADED);

				} catch (Exception ex) {
					eventFrameListener.log(ex.getMessage() + " "
							+ dado.getUrl());
					dado.setStatus(StatusLink.BROKEN);
				}
			}
		}
		eventFrameListener.habDesab(true);
		eventFrameListener.setTextoInResult(Utils.getMessages("testedlinks"),
				true);
	}
}