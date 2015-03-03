import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

public class Dialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String s;
	public static Dialog instance;
	public Dialog(String s) {
		this.setModal(true);
		this.setSize(500, 200);
		setLocation(200, 200);
		setLayout(new GridLayout());
		this.s = s;
		Text pane = new Text(s,this);
		this.add(pane);
		setResizable(false);
	}
}

class Text extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String s;

	public Text(String s,Dialog dialog) {
		this.s = s;
		repaint();
		setSize(500,200);
		setLayout(new BorderLayout());
		Button button = new Button(dialog);
		button.setLocation(150,125);
		button.setPreferredSize(new Dimension(150,40));
		button.setSize(150,40);
		this.add(button,BorderLayout.SOUTH);
	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.LIGHT_GRAY);
		g.setFont(new Font("DialogInput", Font.PLAIN, 15));
		FontMetrics fm = g.getFontMetrics();
		g.drawString(s, (this.getWidth() - fm.stringWidth(s)) / 2,
				Math.min((int) (this.getHeight() * 0.4), this.getHeight() - 10));
	}
}

class Button extends JPanel {

	private static final long forever = 100000000000L;
	private static final int delay = 10;

	private static final long serialVersionUID = 1L;

	private Color boxColor;
	private int red, green, blue;
	private int degreeColor = 0;
	private Hover h;
	private Thread t1;
	private Button instance;

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

	public Button(final Dialog dialog) {		
		setLocation(150,125);
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
				dialog.dispose();
				synchronized (instance) {
					sObservable.setChanged();
					sObservable.notifyObservers(instance);
				}
			}

		});
	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(boxColor);
		g.fillRect(150, 0, this.getWidth()-300, this.getHeight()-3);
		g.setColor(Color.DARK_GRAY);
		g.setFont(new Font("DialogInput", Font.PLAIN, 15));
		FontMetrics fm = g.getFontMetrics();
		g.drawString("OK", (this.getWidth() - fm.stringWidth("OK")) / 2,
				Math.min((int) (this.getHeight() * 0.6), this.getHeight() - 10));
		revalidate();
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
	public class Hover implements Runnable {

		private int shift;
		private Button sb;

		public Hover(Button x) {
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
			green+=2*degree;
			blue+=2*degree;
			return new Color(red,green,blue);
	}
	
	public Color setNormal(){
		red=255;green=120;blue=180;
		return new Color(red,green,blue);
	}
}
