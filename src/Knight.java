import java.util.LinkedList;


public class Knight extends ChessPieces {

	
	public Knight (int x,int y,boolean color){
		super();
		this.x=x;
		this.y=y;
		pieceColor=color;
	}
	
	@Override
	public ChessPieces clone() throws CloneNotSupportedException{
		Knight clone=new Knight(x,y,pieceColor);
		return clone;
	}

	@Override
	public LinkedList<Integer> possibleMoves() {
		LinkedList<Integer> possibleMovesList = new LinkedList<Integer>();
		if(killed) return possibleMovesList;
		findMove(possibleMovesList,-1,1);
		findMove(possibleMovesList,1,1);
		findMove(possibleMovesList,1,-1);
		findMove(possibleMovesList,-1,-1);
		return possibleMovesList;
	}

	public void findMove(LinkedList<Integer> possibleMovesList, int a,int b){
		for (int i = 1; i < 8; i++) {
			if (isValid(possibleMovesList,x + (a*i), y + (b*i)))
				possibleMovesList.add(((x + (a*i))*8)+ y + (b*i));
			else break;
		}
	}
	
	public String toString(){
		return "Knight";
	}

	@Override
	public int cost() {
		// TODO Auto-generated method stub
		return 500;
	}

}
