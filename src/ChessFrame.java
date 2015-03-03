import java.awt.GridLayout;

import javax.swing.*;


public class ChessFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ChessBoard cb;
	private Controls crtl;
	private ChessFrame() throws CloneNotSupportedException{
		setLayout(new GridLayout());
		crtl=new Controls(this);
		cb=new ChessBoard(this,crtl);
		add(cb);
		add(crtl);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}
	public void reset(){
		crtl.reset();
		cb.reset();
	}
	
	public static void main(String args[]){
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				ChessFrame chess;
				try {
					chess = new ChessFrame();
					chess.setLocation(50,50);
					chess.setSize(1000,500);
					chess.setResizable(false);
					chess.setVisible(true);
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				
			}			
		});
	}
}
