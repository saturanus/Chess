import java.util.LinkedList;

public abstract class ChessPieces implements Cloneable{
	protected ChessFrame cf;
	protected boolean pieceColor;
	protected int x, y;
	protected boolean killed=false;
	protected boolean firstMove = false;
	protected int direction;
	private int move=0;
	public void move() {
		if(move<2)
			move++;
		if(move==2)
			firstMove=true;
	}
	
	public void kill() {
		killed=true;
	}

	public void rebirth() {
		killed=false;
	}

	public abstract LinkedList<Integer> possibleMoves();
	
	public abstract int cost();
	
	public int getLocation() {
		return x * 8 + y;
	}

	public boolean getColor() {
		return pieceColor;
	}

	public void setLocation(int x, int y) {
		// TODO Auto-generated method stub
		this.x = x;
		this.y = y;
	}
	public abstract ChessPieces clone() throws CloneNotSupportedException;

	public boolean isValid(LinkedList<Integer> possibleMovesList, int x, int y) {

		if (x < 0 || x > 7)
			return false;
		if (y < 0 || y > 7)
			return false;
		long temp = 1;
		temp = temp << (x * 8 + y);
		if (getColor()) {
			if ((temp & ChessBoard.blackState) != 0) {
				possibleMovesList.add(x * 8 + y);
				return false;
			} else if ((temp & ChessBoard.whiteState) != 0) {
				return false;
			}
		} else {
			if ((temp & ChessBoard.whiteState) != 0) {
				possibleMovesList.add(x * 8 + y);
				return false;
			} else if ((temp & ChessBoard.blackState) != 0) {
				return false;
			}
		}
		return true;
	}
}
