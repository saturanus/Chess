import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;


public class ScoreTab extends JComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ScoreTab() {
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new Color(255,200,200));
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
	}
}
