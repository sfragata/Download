/**
 * $Id: MemoryMonitor.java,v 1.2 2006/03/03 23:12:58 sfragata Exp $
 */

package download;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * Tracks Memory allocated & used, displayed in graph form.
 * (#)MemoryMonitor.java 1.30 01/12/03 Copyright 2002 Sun Microsystems, Inc. All
 * rights reserved. SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license
 * terms.
 * 
 * @version $Revision: 1.2 $
 */

public class MemoryMonitor extends JPanel {
	private static final long serialVersionUID = -1256667213096410297L;

	JPanel controls;

	JTextField tf;

	/**
	 * Description of the Field
	 * 
	 * @uml.property name="surf"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	public Surface surf;

	boolean doControls;

	static JCheckBox dateStampCB = new JCheckBox(Utils.getMessages("logmemory"));

	/**
	 * Description of the Field
	 */
	public static boolean isActive = false;

	/**
	 * Constructor for the MemoryMonitor object
	 */
	public MemoryMonitor() {
		setLayout(new BorderLayout());
		setBorder(new TitledBorder(new EtchedBorder(), Utils
				.getMessages("memorymonitor")));
		add(surf = new Surface());
		controls = new JPanel();
		controls.setPreferredSize(new Dimension(135, 80));
		Font font = new Font("serif", Font.PLAIN, 10);
		JLabel label = new JLabel(Utils.getMessages("interval"));
		label.setFont(font);
		label.setForeground(Color.black);
		controls.add(label);
		tf = new JTextField("600");
		tf.setPreferredSize(new Dimension(45, 20));
		controls.add(tf);
		controls.add(label = new JLabel("ms"));
		label.setFont(font);
		label.setForeground(Color.black);
		controls.add(dateStampCB);
		dateStampCB.setFont(font);
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				removeAll();
				doControls = !doControls;
				if (doControls) {
					surf.stop();
					add(controls);
				} else {
					try {
						surf.sleepAmount = Long.parseLong(tf.getText().trim());
					} catch (Exception ex) {
					}
					surf.start();
					add(surf);
				}
				validate();
				repaint();
			}
		});
	}

	/*
	 * public static void main( String s[] ) { final MemoryMonitor memoryMonitor =
	 * new MemoryMonitor(); WindowListener l = new WindowAdapter() { public void
	 * windowClosing( WindowEvent e ) { System.exit( 0 ); } public void
	 * windowDeiconified( WindowEvent e ) { memoryMonitor.surf.start(); } public
	 * void windowIconified( WindowEvent e ) { memoryMonitor.surf.stop(); } };
	 * JFrame f = new JFrame( Utils.getMessages("memorymonitor") );
	 * f.addWindowListener( l ); f.setDefaultCloseOperation( f.DISPOSE_ON_CLOSE );
	 * f.setResizable( false ); f.getContentPane().add( "Center", memoryMonitor );
	 * f.pack(); f.setSize( new Dimension( 200, 200 ) ); f.setVisible( true );
	 * memoryMonitor.surf.start(); }
	 */
	/**
	 * Description of the Method
	 * 
	 * @param frame
	 *            Description of the Parameter
	 */
	public static void showMonitor(JFrame frame) {
		final MemoryMonitor memoryMonitor = new MemoryMonitor();
		WindowListener l = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				isActive = false;
				memoryMonitor.surf.stop();
			}

			public void windowDeiconified(WindowEvent e) {
				memoryMonitor.surf.start();
			}

			public void windowIconified(WindowEvent e) {
				memoryMonitor.surf.stop();
			}
		};
		JDialog d = new JDialog(frame, Utils.getMessages("memorymonitor"),
				false);
		d.addWindowListener(l);
		d.setResizable(false);
		d.getContentPane().add("Center", memoryMonitor);
		d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		Utils.center(d, 200, 200);
		d.setVisible(true);
		memoryMonitor.surf.start();
		isActive = true;
	}

	class Surface extends JPanel implements Runnable {

		private static final long serialVersionUID = -7819149824435902052L;

		public long sleepAmount = 1000;

		public Thread thread;

		private int ascent;

		private int descent;

		private Graphics2D big;

		private BufferedImage bimg;

		private int columnInc;

		private Font font = new Font("Times New Roman", Font.PLAIN, 11);

		private float freeMemory;

		private float totalMemory;

		private Color graphColor = new Color(46, 139, 87);

		private Line2D graphLine = new Line2D.Float();

		private Rectangle graphOutlineRect = new Rectangle();

		private Color mfColor = new Color(0, 100, 0);

		private Rectangle2D mfRect = new Rectangle2D.Float();

		private Rectangle2D muRect = new Rectangle2D.Float();

		private int ptNum;

		private int pts[];

		private Runtime r = Runtime.getRuntime();

		private String usedStr;

		private int w;

		private int h;

		/**
		 * Constructor for the Surface object
		 */
		public Surface() {
			setBackground(Color.black);
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (thread == null) {
						start();
					} else {
						stop();
					}
				}
			});
		}

		public Dimension getMaximumSize() {
			return getPreferredSize();
		}

		public Dimension getMinimumSize() {
			return getPreferredSize();
		}

		public Dimension getPreferredSize() {
			return new Dimension(135, 80);
		}

		public void paint(Graphics g) {

			if (big == null) {
				return;
			}

			big.setBackground(getBackground());
			big.clearRect(0, 0, w, h);

			freeMemory = (float) r.freeMemory();
			totalMemory = (float) r.totalMemory();

			// .. Draw allocated and used strings ..
			big.setColor(Color.green);
			big.drawString(String.valueOf((int) totalMemory / 1024)
					+ Utils.getMessages("ktotal"), 4.0f, (float) ascent + 0.5f);
			usedStr = String.valueOf(((int) (totalMemory - freeMemory)) / 1024)
					+ Utils.getMessages("kused");
			big.drawString(usedStr, 4, h - descent);

			// Calculate remaining size
			float ssH = ascent + descent;
			float remainingHeight = (float) (h - (ssH * 2) - 0.5f);
			float blockHeight = remainingHeight / 10;
			float blockWidth = 20.0f;
			float remainingWidth = (float) (w - blockWidth - 10);

			// .. Memory Free ..
			big.setColor(mfColor);
			int MemUsage = (int) ((freeMemory / totalMemory) * 10);
			int i = 0;
			for (; i < MemUsage; i++) {
				mfRect.setRect(5, (float) ssH + i * blockHeight, blockWidth,
						(float) blockHeight - 1);
				big.fill(mfRect);
			}

			// .. Memory Used ..
			big.setColor(Color.green);
			for (; i < 10; i++) {
				muRect.setRect(5, (float) ssH + i * blockHeight, blockWidth,
						(float) blockHeight - 1);
				big.fill(muRect);
			}

			// .. Draw History Graph ..
			big.setColor(graphColor);
			int graphX = 30;
			int graphY = (int) ssH;
			int graphW = w - graphX - 5;
			int graphH = (int) remainingHeight;
			graphOutlineRect.setRect(graphX, graphY, graphW, graphH);
			big.draw(graphOutlineRect);

			int graphRow = graphH / 10;

			// .. Draw row ..
			for (int j = graphY; j <= graphH + graphY; j += graphRow) {
				graphLine.setLine(graphX, j, graphX + graphW, j);
				big.draw(graphLine);
			}

			// .. Draw animated column movement ..
			int graphColumn = graphW / 15;

			if (columnInc == 0) {
				columnInc = graphColumn;
			}

			for (int j = graphX + columnInc; j < graphW + graphX; j += graphColumn) {
				graphLine.setLine(j, graphY, j, graphY + graphH);
				big.draw(graphLine);
			}

			--columnInc;

			if (pts == null) {
				pts = new int[graphW];
				ptNum = 0;
			} else if (pts.length != graphW) {
				int tmp[] = null;
				if (ptNum < graphW) {
					tmp = new int[ptNum];
					System.arraycopy(pts, 0, tmp, 0, tmp.length);
				} else {
					tmp = new int[graphW];
					System.arraycopy(pts, pts.length - tmp.length, tmp, 0,
							tmp.length);
					ptNum = tmp.length - 2;
				}
				pts = new int[graphW];
				System.arraycopy(tmp, 0, pts, 0, tmp.length);
			} else {
				big.setColor(Color.yellow);
				pts[ptNum] = (int) (graphY + graphH
						* (freeMemory / totalMemory));
				for (int j = graphX + graphW - ptNum, k = 0; k < ptNum; k++, j++) {
					if (k != 0) {
						if (pts[k] != pts[k - 1]) {
							big.drawLine(j - 1, pts[k - 1], j, pts[k]);
						} else {
							big.fillRect(j, pts[k], 1, 1);
						}

					}
				}
				if (ptNum + 2 == pts.length) {
					// throw out oldest point
					for (int j = 1; j < ptNum; j++) {
						pts[j - 1] = pts[j];
					}

					--ptNum;
				} else {
					ptNum++;
				}

			}
			g.drawImage(bimg, 0, 0, this);
		}

		public void run() {

			Thread me = Thread.currentThread();

			while (thread == me && !isShowing() || getSize().width == 0) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					return;
				}
			}

			while (thread == me && isShowing()) {
				Dimension d = getSize();
				if (d.width != w || d.height != h) {
					w = d.width;
					h = d.height;
					bimg = (BufferedImage) createImage(w, h);
					big = bimg.createGraphics();
					big.setFont(font);
					FontMetrics fm = big.getFontMetrics(font);
					ascent = (int) fm.getAscent();
					descent = (int) fm.getDescent();
				}
				repaint();
				try {
					Thread.sleep(sleepAmount);
				} catch (InterruptedException e) {
					break;
				}
				if (MemoryMonitor.dateStampCB.isSelected()) {
					System.out.println(new Date().toString() + " "
							+ String.valueOf((int) totalMemory / 1024)
							+ Utils.getMessages("ktotal") + usedStr + " "
							+ String.valueOf((int) freeMemory / 1024)
							+ Utils.getMessages("kfree"));
				}

			}
			thread = null;
		}

		public void start() {
			thread = new Thread(this);
			thread.setPriority(Thread.MIN_PRIORITY);
			thread.setName("MemoryMonitor");
			thread.start();
		}

		public synchronized void stop() {
			thread = null;
			notify();
		}
	}
}
