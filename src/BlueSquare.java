import java.awt.Color;


public class BlueSquare extends SquareBox {

	private int red,green,blue;

	public BlueSquare() {
		super();
		repaint();
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Color setHovered(int degree){
		if(red+degree >= 120)
			red=red+degree;
		if(green+degree >= 120)
			green=green+degree;
		return new Color(red,green,blue);
	}
	
	public Color setNormal(){
		red=160;green=160;blue=220;
		return new Color(red,green,blue);	
	}
	

}
