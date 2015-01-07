package br.com.sfragata.download;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.util.TimerTask;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * @author Silvio Fragata da Silva
 */
public class GCPanel extends JPanel {
	private static final long serialVersionUID = 8896247604235971256L;

	private JProgressBar jProgressBarGC = new JProgressBar();

	private JComponent owner = null;

	private String total = "";

	private String used = "";

	private final long INTERVALO = 2000;

	/**
	 * Construtor
	 */
	public GCPanel() {
		this((JComponent) null);
	}

	public GCPanel(JComponent owner) {
		try {
			this.owner = owner;
			init();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void init() throws Exception {
		this.setLayout(new BorderLayout());
		jProgressBarGC.setStringPainted(true);
		jProgressBarGC.setMinimum(0);
		jProgressBarGC.setMaximum(100);
		this.add(jProgressBarGC, BorderLayout.CENTER);
		jProgressBarGC.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					jProgressBarGC_mouseClicked(e);
				}
			}
		});
		jProgressBarGC.setFont(new java.awt.Font("Dialog", Font.BOLD, 11));
		jProgressBarGC.setStringPainted(true);
		java.util.Timer timer = new java.util.Timer();
		timer.schedule(new GCMonitor(), 0, INTERVALO);
	}

	void jProgressBarGC_mouseClicked(MouseEvent e) {
		StringBuffer sb = new StringBuffer();
		sb.append(Utils.getMessages("totalheap")).append(total)
				.append(" bytes.").append("\n");

		sb.append(Utils.getMessages("usedheap")).append(used).append(" bytes.");

		JOptionPane.showMessageDialog(owner, sb.toString(),
				Utils.getMessages("usedheap"), JOptionPane.INFORMATION_MESSAGE);
	}

	class GCMonitor extends TimerTask {
		public void run() {
			Runtime runtime = Runtime.getRuntime();
			long totalMemory = runtime.totalMemory();
			long usedMemory = totalMemory - runtime.freeMemory();
			total = String.valueOf(Utils.formatSize(totalMemory));
			used = String.valueOf(Utils.formatSize(usedMemory));
			float usedPercent = (float) usedMemory / (float) totalMemory;
			jProgressBarGC.setValue(Utils
					.getRoundValue(usedMemory, totalMemory).intValue());
			jProgressBarGC.setString(Utils.convertSize(usedMemory, Utils.MBYTE)
					.concat(" / ")
					.concat(Utils.convertSize(totalMemory, Utils.MBYTE))
					.concat("   Mb."));
			jProgressBarGC.setToolTipText(Utils.getMessages("percusedheap")
					.concat(Utils.formatPercent(usedPercent)).concat(" %"));
		}
	}
}