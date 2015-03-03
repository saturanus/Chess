import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;




public class ConnectTab extends JComponent {

	/**
	 * 
	 */
	
	private static final long forever = 100000000000L;
	private static final int delay = 10;

	private static final long serialVersionUID = 1L;
	
	private Color boxColor=new Color(130,80,210);
	private int red=130,green=80,blue=210;

	private int degreeColor = 0;
	private Hover h;
	private Thread t1;
	private ConnectTab instance;

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
	public ConnectTab(){
		setToolTipText("find an opponent online");
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
				Dialog d=new Dialog("Ask at yahska111@gmail.com for the Multiplayer version.");
				d.setVisible(true);
				synchronized (instance) {
					sObservable.setChanged();
					sObservable.notifyObservers(instance);
				}
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
		BasicStroke s=new BasicStroke(2.0f,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND,1.0f);
		g2d.setStroke(s);
		g2d.drawArc(160, 5, 80, 80, 40, 100);
		g2d.drawArc(170, 15, 60, 80, 50, 80);
		g2d.drawArc(180, 25, 40, 80, 60, 60);
		g2d.fillOval(196, 32, 10, 10);
		revalidate();
	}
	public class Hover implements Runnable {

		private int shift;
		private ConnectTab sb;

		public Hover(ConnectTab x) {
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
			red=red+degree;
			green=green+degree;
			blue=blue+degree;
		return new Color(red,green,blue);
	}
	
	public Color setNormal(){
		red=150;green=100;blue=230;
		return new Color(red,green,blue);
	}


}
