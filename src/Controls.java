import java.awt.*;
import javax.swing.*;


public class Controls extends JComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConnectTab connect;
	private RestartTab restart;
	private CloseTab close;
	private TimerTab myTimer;
	private TimerTab hisTimer;
	private boolean myTurn=true,hisTurn=false,turn=true;
	private ChessFrame chessFrame;
	public Controls(ChessFrame cf){
		repaint();
		chessFrame=cf;
		hisTimer=new TimerTab();
		hisTimer.setLocation(50,100);
		hisTimer.setSize(400,48);
		this.add(hisTimer);
		connect=new ConnectTab();
		connect.setLocation(50,150);
		connect.setSize(400,48);
		this.add(connect);
		restart=new RestartTab(chessFrame);
		restart.setLocation(50,200);
		restart.setSize(199,48);
		this.add(restart);
		close=new CloseTab(chessFrame);
		close.setLocation(251,200);
		close.setSize(199,48);
		this.add(close);
		myTimer=new TimerTab();
		myTimer.setLocation(50,250);
		myTimer.setSize(400,48);
		this.add(myTimer);
		myTimer.startTimer();
	}
	public void switchTurn(){
		if(turn==myTurn){
			myTimer.stopTimer();
			hisTimer.startTimer();
			turn=!turn;
		}else if(turn==hisTurn){
			hisTimer.stopTimer();
			myTimer.startTimer();
			turn=!turn;
		}
	}
	
	public void reset(){
		if(myTimer!=null)
			myTimer.reset();
		if(hisTimer!=null)
			hisTimer.reset();
		myTimer.startTimer();
	}
	@Override
	public void paintComponent(Graphics g){
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	}
	
}
