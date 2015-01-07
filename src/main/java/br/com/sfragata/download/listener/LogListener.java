package br.com.sfragata.download.listener;

/**
 * @author Silvio Fragata da Silva
 */
public interface LogListener {
	public void log(String log);

	public void fatal(Exception ex);

	public void error(String error);

}
