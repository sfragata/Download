/**
 * $Id: Utils.java,v 1.2 2006/03/03 23:13:44 sfragata Exp $
 */

package br.com.sfragata.download;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.swing.DefaultListModel;

/**
 * @author Silvio Fragata da Silva
 * @version $Revision: 1.2 $
 */
public class Utils {
	public static final char KBYTE = 'k';

	public static final char MBYTE = 'm';

	public static final String ARQUIVO = "messages";

	public static String ARQ = System.getProperty("user.home") + File.separator
			+ "proxy.properties";

	private static final DecimalFormat DECIMAL_FORMAT = (DecimalFormat) NumberFormat
			.getNumberInstance();

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(ARQUIVO);

	/**
	 * M�todo que coloca o frame no centro da tela
	 * 
	 * @param componente
	 *            Componente
	 * @param width
	 *            largura
	 * @param height
	 *            comprimento
	 */
	public static void center(Component componente, int width, int height) {
		componente.setSize(width, height);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = componente.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		componente.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		// componente.setVisible( true );
	}

	/**
	 * M�todo que converte o tamanho numa unidade de bytes, kbytes ou Mbytes.
	 * 
	 * @param size
	 *            tamanho
	 * @param unidade
	 *            unidade
	 * @return o valor convertido
	 */
	public static String convertSize(float size, char unidade) {
		float convert = 0.0f;
		switch (unidade) {
		case KBYTE:
			convert = size / 1024;
			break;
		case MBYTE:
			convert = size / (1024 * 1024);
			break;
		default:
			convert = size;
			break;
		}
		return formatSize(convert);
	}

	/**
	 * M�todo que formata a data
	 * 
	 * @param data
	 *            data
	 * @param formato
	 *            formato
	 * @return Data formatada
	 */
	public static String formatDate(long data, String formato) {
		SimpleDateFormat formatter = new SimpleDateFormat(formato);
		return formatter.format(new Date(data));
	}

	/**
	 * Formata em porcentagem
	 * 
	 * @param percent
	 *            valro
	 * @return valor convertido
	 */
	public static String formatPercent(float percent) {
		DECIMAL_FORMAT.setMaximumFractionDigits(2);
		return DECIMAL_FORMAT.format(percent * 100);
	}

	/**
	 * Formata��o decimal, com 2 casas decimais
	 * 
	 * @param size
	 *            tamanho
	 * @return valor formatado
	 */
	public static String formatSize(float size) {
		DECIMAL_FORMAT.setMaximumFractionDigits(2);
		return new StringBuffer(DECIMAL_FORMAT.format(size)).toString();
	}

	/**
	 * M�todo que arredonda um valor em percentagem
	 * 
	 * @param parcial
	 * @param total
	 * @return valor arredondado.
	 */
	public static BigDecimal getRoundValue(float parcial, float total) {
		BigDecimal par = new BigDecimal(parcial / total * 100);
		return par.setScale(0, BigDecimal.ROUND_DOWN);
	}

	/**
	 * M�todo para obter os recursos
	 * 
	 * @param classe
	 *            A classe para obter o recurso
	 * @param resource
	 *            O recurso
	 * @return A Url correpondente
	 */
	public static URL getURLResource(String resource) {
		return ClassLoader.getSystemClassLoader().getResource(resource);
	}

	/**
	 * Description of the Method
	 * 
	 * @param file
	 *            arquivo
	 * @return A String do arquivo
	 * @exception IOException
	 */
	public static String lerArqToString(URL file) throws IOException {
		return lerArqToString(file.openStream());
	}

	/**
	 * Description of the Method
	 * 
	 * @param in
	 *            InputStream
	 * @return A String do arquivo
	 * @exception IOException
	 */
	public static String lerArqToString(InputStream in) throws IOException {
		StringBuffer sb = new StringBuffer();
		int n = 0;
		while ((n = in.read()) != -1) {
			sb.append((char) n);
		}
		return sb.toString();
	}

	/**
	 * M�todo que faz a formata��o do tempo
	 * 
	 * @param mili
	 *            tempo em milisegundos
	 * @return o tempo em horas, minutos, segundos e milisegundos
	 */
	public static String msToCompleteHour(long mili) {
		long miliseconds = mili % 1000;
		long seconds = mili / 1000;
		long hours = seconds / 3600;
		float part = seconds / 3600f;
		part -= hours;

		BigDecimal minutes = new BigDecimal(part * 60);
		minutes = minutes.setScale(0, BigDecimal.ROUND_DOWN);
		seconds = seconds % 60;

		return new StringBuffer().append(hours).append(" h ")
				.append(minutes.toString()).append(" min ").append(seconds)
				.append(" s ").append(miliseconds).append(" ms.").toString();
	}

	/**
	 * M�todo que retorna o valor da mensagem que est� no arquivo de properties
	 * 
	 * @param key
	 *            chave do arquivo
	 * @return o valor
	 */
	public static String getMessages(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException ex) {
			return null;
		}
	}

	/**
	 * M�todo que faz a substitui��o de uma string em outra
	 * 
	 * @param text
	 *            texto
	 * @param string
	 *            string a ser substituida
	 * @param newString
	 *            nova string
	 * @return o texto com a substitui��o
	 */
	public static String replace(String text, String string, String newString) {
		StringBuffer sb = new StringBuffer(text);
		int indexString = text.toLowerCase().indexOf(string.toLowerCase());
		if (indexString != -1)
			sb = sb.replace(indexString, indexString + string.length(),
					newString);
		return sb.toString();
	}

	/**
	 * M�todo que faz a todas as substitui��es de uma string em outra
	 * 
	 * @param text
	 *            texto
	 * @param string
	 *            string a ser substituida
	 * @param newString
	 *            nova string
	 * @return o texto com a substitui��o
	 */
	public static String replaceAll(String text, String string, String newString) {
		StringTokenizer st = new StringTokenizer(text.toLowerCase(),
				string.toLowerCase());
		int qtd = st.countTokens();
		for (int i = 0; i < qtd; i++) {
			text = replace(text, string, newString);
		}
		return text;
	}

	/**
	 * M�todo que seta os valores do proxy
	 * 
	 * @param values
	 *            os valores
	 */
	public static void setSystemProp(String[] values) {
		System.getProperties().put("proxySet", values[0]);
		System.getProperties().put("proxyHost", values[1]);
		System.getProperties().put("proxyPort", values[2]);
	}

	/**
	 * M�todo que remove o arquivo de proxy
	 */
	public static void removeStoreSystemProp() {
		File f = new File(Utils.ARQ);
		f.delete();
	}

	/**
	 * M�todo que armazena os dados do proxy no arquivo
	 * 
	 * @param values
	 */
	public static void storeSystemProp(String[] values) {
		PrintStream p = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(Utils.ARQ));
			p = new PrintStream(fos);
			p.print(values[0]);
			p.print("|");
			p.print(values[1]);
			p.print("|");
			p.print(values[2]);
			p.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			if (p != null)
				p.close();
		}

	}

	/**
	 * M�todo que remove as propriedades do proxy
	 */
	public static void removeSystemProp() {
		System.getProperties().remove("proxySet");
		System.getProperties().remove("proxyHost");
		System.getProperties().remove("proxyPort");
	}

	/**
	 * M�todo que retorna os valores do proxy do arquivo
	 * 
	 * @return os valores
	 */
	public static String[] getValores() {
		// -DproxySet=true -DproxyHost=proxy -DproxyPort=128
		FileInputStream fos = null;
		try {
			fos = new FileInputStream(new File(ARQ));
			String cont = Utils.lerArqToString(fos);
			StringTokenizer st = new StringTokenizer(cont, "|");
			String[] values = new String[3];
			if (st.countTokens() == 3) {
				for (int i = 0; i < values.length; i++) {
					values[i] = st.nextToken();
				}
			} else
				throw new IllegalArgumentException("Deve possuir 3 par�metros");
			return values;
		} catch (Exception e) {
			return null;
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		}
	}

	/**
	 * M�todo que remove do link simbolos especiais
	 * 
	 * @param texto
	 * @return o texto com os links removidos
	 */
	public static String removeSimbols(String texto) {
		for (int i = 0; i < DownloadGUI.SIMBOLS.length; i++) {
			texto = Utils.replaceAll(texto, DownloadGUI.SIMBOLS[i], "");
		}
		return texto;
	}

	/**
	 * M�todo que verifica se o link j� existe na lista
	 * 
	 * @param dados
	 *            o link
	 * @param lModel
	 *            ListModel
	 * @return true se existir e false se n�o
	 */
	public static boolean search(StatusLink dados, DefaultListModel lModel) {
		return lModel.contains((StatusLink) dados);
	}

	public static URL getResource(String resourcePath) {
		return Utils.class.getClassLoader().getResource(resourcePath);
	}
}