/**
 * $Id: Repetidor.java,v 1.2 2006/03/03 23:14:31 sfragata Exp $
 */

package download;

import javax.swing.DefaultListModel;

/**
 * Classe que tem como função repetir os links que são sequenciais, isto é,
 * possuem números sequenciais
 * 
 * @author Silvio Fragata da Silva
 * @version $Revision: 1.2 $
 */
public class Repetidor {
	/**
	 * Constructor for the Repetidor object
	 * 
	 * @param listModel
	 *            ListModel
	 * @param texto
	 *            Texto
	 * @param inicio
	 *            faixa inicial
	 * @param fim
	 *            faixa final
	 * @param fillZero
	 *            preenchimento com zero
	 * @exception Exception
	 */
	public Repetidor(DefaultListModel listModel, String texto, int inicio,
			int fim, boolean fillZero) throws Exception {
		validate(texto, inicio, fim);
		for (int i = inicio; i <= fim; i++) {
			StringBuffer sb = new StringBuffer(texto);
			StringBuffer number = new StringBuffer(String.valueOf(i));
			if (i < 10 && fillZero) {
				number.insert(0, "0");
			}
			sb.replace(sb.toString().indexOf("*"),
					sb.toString().indexOf("*") + 1, number.toString());
			StatusLink sLink = new StatusLink(sb.toString(),
					StatusLink.NAO_BAIXADO);
			if (!Utils.search(sLink, listModel))
				listModel.addElement(sLink);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param texto
	 *            Description of the Parameter
	 * @param inicio
	 *            Description of the Parameter
	 * @param fim
	 *            Description of the Parameter
	 * @exception IllegalArgumentException
	 *                Description of the Exception
	 */
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