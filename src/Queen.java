import java.util.LinkedList;

public class Queen extends ChessPieces{

	public Queen (int x,int y,boolean color){
		super();
		this.x=x;
		this.y=y;
		super.pieceColor=color;
	}

	@Override
	public ChessPieces clone() throws CloneNotSupportedException{
		Queen clone=new Queen(x,y,pieceColor);
		return clone;
	}

	@Override
	public LinkedList<Integer> possibleMoves() {
		LinkedList<Integer> possibleMovesList = new LinkedList<Integer>();
		if(killed) return possibleMovesList;
		findMove(possibleMovesList,-1,0);
		findMove(possibleMovesList,1,0);
		findMove(possibleMovesList,-1,1);
		findMove(possibleMovesList,1,1);
		findMove(possibleMovesList,0,-1);
		findMove(possibleMovesList,0,1);
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
	
	
	@Override
	public String toString(){
		return "Queen";
	}



	@Override
	public int cost() {
		// TODO Auto-generated method stub
		return 2000;
	}
	
}
