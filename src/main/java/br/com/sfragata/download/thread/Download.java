package br.com.sfragata.download.thread;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLConnection;

import javax.swing.JOptionPane;

import br.com.sfragata.download.DownloadGUI;
import br.com.sfragata.download.StatusLink;
import br.com.sfragata.download.Utils;
import br.com.sfragata.download.listener.EventFrameListener;


/**
 * @author Silvio Fragata da Silva
 */
public class Download implements Runnable {

	private int number;

	private EventFrameListener eventFrameListener;

	public Download(int number) {
		this.number = number;
	}

	public void addEventFrameListener(EventFrameListener eventFrameListener) {
		this.eventFrameListener = eventFrameListener;
	}

	public String getName() {
		return "Download-" + number;
	}

	private void copy(InputStream is, OutputStream os, float total, int number)
			throws IOException {
		byte[] b = new byte[1024];
		int parcial = 0;
		int n = 0;
		while ((n = is.read(b)) > -1) {
			if (eventFrameListener.getStopArray(number))
				throw new IOException(Utils.getMessages("downloadaborted"));
			parcial += n;
			eventFrameListener.setProgress(parcial, total, number);
			os.write(b, 0, n);
			os.flush();
		}
		os.flush();
		os.close();
		is.close();
	}

	public void run() {
		boolean ok = true;
		URI url = null;
		InputStream inputStream = null;
		FileOutputStream fileOutputStream = null;
		String urlfile = "";
		int listSize = eventFrameListener.getListModel().getSize();

		eventFrameListener.setStopArray(number, false);

		long tempoTotal = System.currentTimeMillis();

		String tagetDirectory = eventFrameListener.getTarget().trim();
		int scroll = 0;
		eventFrameListener.scrollUp();
		eventFrameListener.iconConnect(number);
		int option = DownloadGUI.NO;

		for (int i = 0; i < listSize; i++) {
			eventFrameListener.clearProgress(number);
			if (eventFrameListener.getStopArray(number)) {
				eventFrameListener.setStopArray(number, false);
				break;
			}
			StatusLink currentLink = (StatusLink) eventFrameListener
					.getListModel().getElementAt(i);
			if ((++scroll) == 15) {
				eventFrameListener.scrollDown(i);
				scroll = 0;
				eventFrameListener.refresh();
			}
			if (currentLink.getStatus() == StatusLink.NOT_DOWNLOADED) {
				urlfile = currentLink.getUrl();
				currentLink.setStatus(StatusLink.IN_USE);

				String nomeArq = currentLink.getTargetFile();

				nomeArq = Utils.removeSimbols(nomeArq);
				File targetFile = new File(new StringBuffer(tagetDirectory)
						.append(File.separator).append(nomeArq).toString());
				File fUrl = new File(urlfile);
				boolean exists = targetFile.exists();
				if (exists && (option != DownloadGUI.ALL)) {
					Object[] values = { Utils.getMessages("yes"),
							Utils.getMessages("no"),
							Utils.getMessages("yestoall") };
					option = JOptionPane.showOptionDialog(null,
							new StringBuffer(Utils.getMessages("likegetfile"))
									.append(targetFile.getName()).append(" ?")
									.toString(),
							Utils.getMessages("fileexist"),
							JOptionPane.DEFAULT_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, values,
							values[0]);
				}
				try {
					boolean baixar = true;
					if (exists && option == DownloadGUI.NO) {
						baixar = false;
						currentLink.setStatus(StatusLink.NOT_DOWNLOADED);
						eventFrameListener.log(new StringBuffer(getName())
								.append(" - [").append(nomeArq)
								.append(Utils.getMessages("filenotget"))
								.toString());
					}
					if (baixar) {
						fileOutputStream = new FileOutputStream(targetFile);

						if (fUrl.isFile()) {
							url = fUrl.toURI();
						} else {
							url = new URI(urlfile);
						}

						long begin = System.currentTimeMillis();
						URLConnection urlConnection = url.toURL()
								.openConnection();
						urlConnection.connect();
						int length = urlConnection.getContentLength();
						String tamanho = Utils.formatSize(length);
						inputStream = urlConnection.getInputStream();
						eventFrameListener.setLabelProgress(
								number,
								new StringBuffer(getName()).append(" [")
										.append(nomeArq).append("] ")
										.append(tamanho).append(" bytes")
										.toString());
						eventFrameListener.setToolTipLabelProgress(
								number,
								new StringBuffer(getName()).append(" ")
										.append(url.toString()).toString());
						copy(inputStream, fileOutputStream, length, number);
						currentLink.setStatus(StatusLink.DOWNLOADED);
						long end = System.currentTimeMillis();
						eventFrameListener.log(new StringBuffer(getName())
								.append(" - [").append(nomeArq)
								.append(Utils.getMessages("finisheddownload"))
								.append(Utils.msToCompleteHour(end - begin))
								.toString());

						eventFrameListener.log(new StringBuffer(getName())
								.append(" - [").append(nomeArq)
								.append(Utils.getMessages("lenght"))
								.append(listSize).append(" bytes").toString());
					}
				} catch (Exception e) {
					currentLink.setStatus(StatusLink.BROKEN);
					StringBuffer err = new StringBuffer(
							"-------------------------------------------------------------------\n");
					err.append(getName()).append(": ")
							.append(Utils.getMessages("downloaderror"))
							.append(nomeArq).append("\n");
					if (e instanceof FileNotFoundException)
						err.append(Utils.getMessages("filenotfound"));
					else
						err.append(e.getMessage());
					eventFrameListener.error(err.toString());
					ok = false;
					try {
						if (inputStream != null) {
							inputStream.close();
							inputStream = null;
						}
						if (fileOutputStream != null) {
							fileOutputStream.close();
							fileOutputStream = null;
							targetFile.delete();
						}
					} catch (Exception ex) {
						eventFrameListener.fatal(ex);
					}
				}
				eventFrameListener.refresh();
			} else {
				if (currentLink.getStatus() == StatusLink.FOLDER) {
					tagetDirectory = eventFrameListener.getTarget().trim()
							+ File.separator + currentLink.getUrl();
					try {
						File fArq = new File(tagetDirectory);
						if (!(fArq.mkdirs() || fArq.exists())) {
							throw new IOException(new StringBuffer(
									Utils.getMessages("dirnotcreated")).append(
									tagetDirectory).toString());
						}
					} catch (IOException ex) {
						tagetDirectory = eventFrameListener.getTarget().trim();
						eventFrameListener.fatal(ex);
					}
				}
			}
		}
		eventFrameListener.iconDisconnect(number);
		eventFrameListener.setFinishArray(number, true);
		boolean allFinished = eventFrameListener.validateFinish();
		if (ok) {
			eventFrameListener.setTextoInResult(new StringBuffer(getName())
					.append(" ").append(Utils.getMessages("enddownload"))
					.toString(), true);
		} else {
			eventFrameListener.setTextoInResult(new StringBuffer(getName())
					.append(" ").append(Utils.getMessages("geterrors"))
					.toString(), true);
		}
		if (!allFinished) {
			eventFrameListener.setTimerText(DownloadGUI.MESSAGE_DEFAULT);
		}
		eventFrameListener.log(new StringBuffer(getName())
				.append(" ")
				.append(Utils.getMessages("totaltime"))
				.append(Utils.msToCompleteHour(System.currentTimeMillis()
						- tempoTotal)).toString());

		eventFrameListener.clearProgress(number);
		eventFrameListener.setLabelProgress(number, getName());
		eventFrameListener.setToolTipLabelProgress(number, getName());
		option = DownloadGUI.NO;
		if (allFinished) {
			eventFrameListener.habDesab(true);
		}
	}
}