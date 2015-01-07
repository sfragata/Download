package br.com.sfragata.download.listener;

import javax.swing.ListModel;

/**
 * @author Silvio Fragata da Silva
 */
public interface EventFrameListener extends LogListener {

	public void scrollUp();

	public void scrollDown(int offSet);

	public void iconConnect(int number);

	public void clearProgress(int number);

	public boolean getStopArray(int number);

	public void setStopArray(int number, boolean val);

	public void refresh();

	public ListModel getListModel();

	public String getTarget();

	public void setLabelProgress(int number, String msg);

	public void setToolTipLabelProgress(int number, String msg);

	public void setProgress(int parcial, float total, int number);

	public void iconDisconnect(int number);

	public void setFinishArray(int number, boolean b);

	public boolean validateFinish();

	public void setTextoInResult(String string, boolean b);

	public void setTimerText(String message);

	public void habDesab(boolean b);

}
