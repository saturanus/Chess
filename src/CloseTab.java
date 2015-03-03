import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowStateListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;


public class CloseTab extends JComponent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final long forever = 100000000000L;
	private static final int delay = 10;
	
	private Color boxColor;
	private int red,green,blue;

	private int degreeColor = 0;
	private Hover h;
	private Thread t1;
	private CloseTab instance;

	protected boolean isSelected = false, isHover = false, isLit = false;
	
	
	final class selectionObservable extends Observable {
		@Override
		protected synchronized void setChanged() {
			super.setChanged();
		}
	}

	private selectionObservable sObservable = new selectionObservable();

	public void addSelectionObserver(Observer obs) {
		sObservable.addObserver(obs);
	}
	public CloseTab(final ChessFrame cf){
		setToolTipText("Exit");
		boxColor = setNormal();
		h = new Hover(this);
		t1 = new Thread(h);
		t1.start();
		repaint();
		instance = this;
		this.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent E) {
				isHover = true;
				startHovering();
			}

			public void mouseExited(MouseEvent E) {
				isHover = false;
				removeHovering();
			}

			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}

		});
	}
	public void startHovering() {
		synchronized (this) {
			if (isSelected && isLit)
				return;
			isLit = true;
			h.setShift();
			t1.interrupt();
		}
	}

	public void removeHovering() {
		synchronized (this) {
			if (isSelected && isLit)
				return;
			isLit = false;
			h.setShift();
			t1.interrupt();
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(boxColor);
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		g2d.setColor(Color.DARK_GRAY);
		g2d.setFont(new Font ("DialogInput",Font.PLAIN,30));
		g2d.setColor(Color.DARK_GRAY);
		FontMetrics fm= g2d.getFontMetrics();
		String s1="X";
		g2d.drawString(s1,(this.getWidth()-fm.stringWidth(s1))/2,Math.min((int)(this.getHeight()*0.75),this.getHeight()-10));

		revalidate();
	}
	public class Hover implements Runnable {

		private int shift;
		private CloseTab sb;

		public Hover(CloseTab x) {
			sb = x;
			shift = 1;
		}

		@Override
		public void run() {
			long sleepTime = forever;
			while (true) {
				if (degreeColor == 0 || degreeColor == 20)
					sleepTime = forever;
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException E) {
					sleepTime = delay;
				}
				degreeColor -= shift;
				synchronized (sb) {
					sb.boxColor = sb.setHovered(shift);
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							sb.repaint();
						}
					});
				}
			}
		}

		public void setShift() {
			shift = -shift;
		}

	}
	public Color setHovered(int degree){
			blue+=2*degree;
			green+=2*degree;
		return new Color(red,green,blue);
	}
	
	public Color setNormal(){
		red=230;green=160;blue=160;
		return new Color(red,green,blue);
	}
}
