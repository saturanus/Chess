import java.util.LinkedList;


public class Bishop extends ChessPieces {

	public Bishop (int x,int y,boolean color){
		super();
		this.x=x;
		this.y=y;
		pieceColor=color;
	}
	
	@Override
	public ChessPieces clone() throws CloneNotSupportedException{
		Bishop clone=new Bishop(x,y,pieceColor);
		return clone;
	}
	
	@Override
	public LinkedList<Integer> possibleMoves() {
		// TODO Auto-generated method stub
		LinkedList<Integer> possibleMovesList = new LinkedList<Integer>();
		if(killed) return possibleMovesList;
		if(isValid(possibleMovesList,x+2,y+1)){
			if(!possibleMovesList.contains((x+2)*8 + y+1))
				possibleMovesList.add(((x+2)*8 + y+1));
		}
		if(isValid(possibleMovesList,x-2,y+1)){
			if(!possibleMovesList.contains((x-2)*8 + y+1))
				possibleMovesList.add(((x-2)*8 + y+1));
		}
		if(isValid(possibleMovesList,x+2,y-1)){
			if(!possibleMovesList.contains((x+2)*8 + y-1))
				possibleMovesList.add(((x+2)*8 + y-1));
		}
		if(isValid(possibleMovesList,x-2,y-1)){
			if(!possibleMovesList.contains((x-2)*8 + y-1))
				possibleMovesList.add(((x-2)*8 + y-1));
		}
		if(isValid(possibleMovesList,x+1,y+2)){
			if(!possibleMovesList.contains((x+1)*8 + y+2))
				possibleMovesList.add(((x+1)*8 + y+2));
		}
		if(isValid(possibleMovesList,x-1,y+2)){
			if(!possibleMovesList.contains((x-1)*8 + y+2))
				possibleMovesList.add(((x-1)*8 + y+2));
		}
		if(isValid(possibleMovesList,x+1,y-2)){
			if(!possibleMovesList.contains((x+1)*8 + y-2))
				possibleMovesList.add(((x+1)*8 + y-2));
		}
		if(isValid(possibleMovesList,x-1,y-2)){
			if(!possibleMovesList.contains((x-1)*8 + y-2))
				possibleMovesList.add(((x-1)*8 + y-2));
		}
		return possibleMovesList;
	}

	public String toString(){
		return "Bishop";
	}

	@Override
	public int cost() {
		// TODO Auto-generated method stub
		return 250;
	}
}
