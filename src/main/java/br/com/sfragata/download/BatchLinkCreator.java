package br.com.sfragata.download;

import javax.swing.DefaultListModel;

/**
 * @author Silvio Fragata da Silva
 */
public class BatchLinkCreator {
	public BatchLinkCreator(DefaultListModel listModel, String texto, int inicio,
			int fim, boolean fillZero) throws Exception {
		validate(texto, inicio, fim);
		for (int i = inicio; i <= fim; i++) {
			StringBuilder sb = new StringBuilder(texto);
			StringBuilder number = new StringBuilder(String.valueOf(i));
			if (i < 10 && fillZero) {
				number.insert(0, "0");
			}
			sb.replace(sb.toString().indexOf("*"),
					sb.toString().indexOf("*") + 1, number.toString());
			StatusLink sLink = new StatusLink(sb.toString(),
					StatusLink.NOT_DOWNLOADED);
			if (!Utils.search(sLink, listModel))
				listModel.addElement(sLink);
		}
	}

	private void validate(String texto, int inicio, int fim)
			throws IllegalArgumentException {
		if (texto == null || texto.equals("")) {
			throw new IllegalArgumentException(Utils.getMessages("invalidurl"));
		}
		if (inicio >= fim) {
			throw new IllegalArgumentException(Utils.getMessages("rangeerror"));
		}
		if (texto.indexOf("*") == -1) {
			throw new IllegalArgumentException(Utils.getMessages("wildcard"));
		}
	}
}