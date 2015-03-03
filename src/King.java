
import java.util.LinkedList;


public class King extends ChessPieces {

	public King (int x,int y,boolean color,ChessFrame cf){
		super();
		this.x=x;
		this.y=y;
		pieceColor=color;
		this.cf=cf;
	}
	@Override
	public void kill() {
		killed=true;
		Dialog kingKilled=new Dialog(pieceColor==true?"BLACK":"WHITE"+" has won the game");
		kingKilled.setLocation(300,100);
		kingKilled.setVisible(true);
		cf.reset();
	}
	
	
	public ChessPieces clone() throws CloneNotSupportedException{
		King clone=new King(x,y,pieceColor,cf);
		return clone;
	}
	
	@Override
	public LinkedList<Integer> possibleMoves() {
		// TODO Auto-generated method stub
		LinkedList<Integer> possibleMovesList = new LinkedList<Integer>();
		if(killed) return possibleMovesList;
		for(int i=-1;i<=1;i++){
			for(int j=-1;j<=1;j++){
				if(i==0 && j==0);
				else if(isValid(possibleMovesList,x+i,y+j))
					possibleMovesList.add((x+i)*8 + y+j);
			}
		}
		return possibleMovesList;
	}

	@Override
	public String toString(){
		return "King";
	}

	@Override
	public int cost() {
		return 5000;
	}

}
