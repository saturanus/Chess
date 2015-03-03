import java.util.*;

public class findMove {
	volatile LinkedList<ChessPieces> blackList = new LinkedList<ChessPieces>(),
			whiteList = new LinkedList<ChessPieces>();

	public findMove() {
		
		try {
			for (ChessPieces c : new LinkedList<ChessPieces>(
					ChessBoard.whiteChessPieces)) {
				whiteList.add(c.clone());
			}
			for (ChessPieces c : new LinkedList<ChessPieces>(
					ChessBoard.blackChessPieces)) {
				blackList.add(c.clone());
			}
			System.out.println(dfsBlack(0, blackList, whiteList, 3) + " "
					+ movedPiece + " " + movedToPosition);

		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	long whiteState, blackState;
	public ChessPieces movedPiece;
	public int movedToPosition, movedFromPosition;

	public int dfsBlack(Integer cost, LinkedList<ChessPieces> blackList,
			LinkedList<ChessPieces> whiteList, int depth)
			throws CloneNotSupportedException {
		if (depth == 0)
			return 0;
		int max = -10000;
		for (ChessPieces c : new LinkedList<ChessPieces>(blackList))
			synchronized (c) {
				LinkedList<Integer> list = c.possibleMoves();
				Iterator<Integer> in = list.iterator();
				while (in.hasNext()) {
					int i = in.next();
					int d = depth;
					ChessPieces cc = c.clone();
					int cst = makeMoveBlack(cost, c, i, blackList, whiteList,
							depth);
					if (d == 1 && cst >= max) {
						movedFromPosition = cc.getLocation();
						movedPiece = cc;
						movedToPosition = i;
					}
					c=cc;
					max = Math.max(cst, max);
				}
			}
		//System.out.println("dfsBlack "+max);
		return max;
	}

	public int dfsWhite(Integer cost, LinkedList<ChessPieces> blackList,
			LinkedList<ChessPieces> whiteList, int depth)
			throws CloneNotSupportedException {
		if (depth==0)
			return 0;
		int min = -10000;
		for (ChessPieces c : new LinkedList<ChessPieces>(whiteList))
			synchronized (c) {
				LinkedList<Integer> list = c.possibleMoves();
				Iterator<Integer> in = list.iterator();
				while (in.hasNext()) {
					ChessPieces cp=c.clone();
					min = Math.max(
							makeMoveWhite(cost, c, in.next(), blackList,
									whiteList, depth), min);
					
					c=cp;
				}
			}
		//System.out.println("dfsWhite "+min);
		return min;
	}

	private int makeMoveBlack(Integer cost, ChessPieces c, int location,
			LinkedList<ChessPieces> blackList,
			LinkedList<ChessPieces> whiteList, int depth)
			throws CloneNotSupportedException {
		if (depth == 0)
			return cost;
		int max = -10000;
		synchronized (c) {
			int loc = c.getLocation();
			for (ChessPieces k : new LinkedList<ChessPieces>(whiteList)) {
				synchronized (k) {
					if (k.getLocation() == c.getLocation()) {
							c.setLocation(location / 8, location % 8);
							k.kill();
							int cst = dfsWhite(cost, blackList, whiteList,
									depth-1);
							k.rebirth();
							c.setLocation(loc / 8, loc % 8);
							if (k.cost()*depth + cst >= max) {
								max = Math.max(k.cost()*depth + cst, max);
							}
						}
					else{
						c.setLocation(location / 8, location % 8);
						int cst = dfsWhite(cost, blackList, whiteList,
								depth-1);
						c.setLocation(loc / 8, loc % 8);
						if (cst >= max) {
							max = Math.max(cst, max);
						}
					}
				}
			}
		}
		//System.out.println("makeMoveBlack "+max);
		return max;
	}

	private int makeMoveWhite(Integer cost, ChessPieces c, int location,
			LinkedList<ChessPieces> blackList,
			LinkedList<ChessPieces> whiteList, int depth)
			throws CloneNotSupportedException {
		if (depth == 0)
			return cost;
		int min = -10000;
		synchronized (c) {
			int loc = c.getLocation();
			for (ChessPieces k : new LinkedList<ChessPieces>(blackList)) {
				synchronized (k) {
					if (k.getLocation() == location) {
						c.setLocation(location / 8, location % 8);
						k.kill();
						int cst=dfsBlack(cost, blackList, whiteList, depth-1);
						k.rebirth();
						c.setLocation(loc / 8, loc % 8);
						if (cst-(k.cost()*depth) >= min) {
							min = Math.max(cst-(k.cost()*depth), min);
						}
					}else {
						c.setLocation(location / 8, location % 8);
						int cst=dfsBlack(cost, blackList, whiteList, depth-1);
						c.setLocation(loc / 8, loc % 8);
						if (cst >= min) {
							min = Math.max(cst, min);
						}
					}
				}
			}
		}
		//System.out.println("makeMoveWhite "+min);
		return min;
	}

}
