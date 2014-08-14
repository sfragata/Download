package download.listener;

/**
 * Interface que serve como listener para os logs
 * @author Silvio Fragata da Silva
 */
public interface LogListener
{
	/**
	 * M�todo que loga mensagens de OUT  
	 * @param log texto de log
	 */
	public void log(String log);

	/**
	 * M�todo que loga as exceptions 
	 * @param ex a exception
	 */
	public void fatal(Exception ex);

	/**
	 * M�todo que loga os erros 
	 * @param error texto
	 */
	public void error(String error);

}
