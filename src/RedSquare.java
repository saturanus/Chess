import java.awt.Color;



public class RedSquare extends SquareBox {

	private int red,green,blue;

	public RedSquare() {
		super();
		repaint();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Color setHovered(int degree){
		if(blue+degree >=120)
			blue=blue+degree;
		if(green +degree >=120)
			green=green+degree;
		return new Color(red,green,blue);
	}
	
	public Color setNormal(){
		red=220;green=160;blue=160;
		return new Color(red,green,blue);
	}

	
	
}
