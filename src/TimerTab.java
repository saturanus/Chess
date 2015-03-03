import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class TimerTab extends JComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String s_time="Timer";
	int time1=1200,time2=18000;
	private Thread t;
	public TimerTab(){
		setToolTipText("Timer");
		repaint();
	}
	public void startTimer(){
		t=new Thread(new Runnable(){
			public void run(){
				while(true){
					try {
						Thread.sleep(100);
						time1--;
						time2--;
					} catch (InterruptedException e) {
						time1=1200;
						repaint();
						break;
					}
					repaint();
				}
			}
		});
		t.start();
	}
	public void reset(){
		stopTimer();
		time1=1200;
		time2=18000;
		repaint();
	}
	public void stopTimer(){
		if(t!=null)
			t.interrupt();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.DARK_GRAY.brighter());
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		g2d.setColor(Color.DARK_GRAY);
		g2d.setFont(new Font ("Calibri",Font.PLAIN,25));
		FontMetrics fm= g2d.getFontMetrics();
		s_time=time2/600+":"+(time2%600)/100+((time2%600)/10)%10+" | "+time1/600+":"+(time1%600)/100+((time1%600)/10)%10+"";
		g2d.drawString(s_time,(this.getWidth()-fm.stringWidth(s_time))/2,Math.min((int)(this.getHeight()*0.7),this.getHeight()-10));
	}
}
