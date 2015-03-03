import java.util.LinkedList;

public class Pawn extends ChessPieces {


	public ChessPieces clone() throws CloneNotSupportedException{
		Pawn clone=new Pawn(x,y,pieceColor);
		clone.setDirection(direction);
		clone.setFirstMove(firstMove);
		return clone;
	} 
	public void setDirection(int d){
		direction=d;
	}
	public void setFirstMove(boolean fm){
		firstMove=fm;
	}
	public Pawn(int x, int y, boolean color) {
		super();
		this.x = x;
		this.y = y;
		pieceColor = color;
		if (pieceColor)
			direction = -1;
		else
			direction = 1;
	}

	@Override
	public LinkedList<Integer> possibleMoves() {
		LinkedList<Integer> possibleMovesList = new LinkedList<Integer>();
		if(killed) return possibleMovesList;
		if (!firstMove) {
			if (x + direction < 8 && x + direction >= 0 && !pawnValidity(x + direction, y)) {
				possibleMovesList.add((x + direction) * 8 + y);
				if (x + direction < 8 && x + direction >= 0 && !pawnValidity(x + 2 * direction, y)) {
					possibleMovesList.add((x + 2 * direction) * 8 + y);
				}
			}
		} else if(x + direction < 8 && x + direction >= 0 && !pawnValidity(x + direction, y)){
			possibleMovesList.add((x + direction) * 8 + y);
		}
		if (pawnValidity(x + direction, y + direction)) {
			possibleMovesList.add(((x + direction) * 8) + y + direction);
		}
		if (pawnValidity(x + direction, y - direction)) {
			possibleMovesList.add(((x + direction) * 8) + y - direction);
		}

		return possibleMovesList;
	}

	private boolean pawnValidity(int x, int y) {
		if (x >= 0 && y >= 0 && x < 8 && y < 8) {
			long temp = 1;
			temp = temp << (x * 8 + y);
			if (getColor()) {
				if ((temp & ChessBoard.blackState) != 0) {
					return true;
				}
			} else {
				if ((temp & ChessBoard.whiteState) != 0) {
					return true;
				}
			}
		}
		return false;
	}

	public String toString() {
		return "Pawn";
	}

	@Override
	public int cost() {
		return 50;
	}

}
