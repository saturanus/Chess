import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

public class ChessBoard extends JPanel {

	/**
	 * 
	 */
	private static final boolean BLACK = false;
	private static final boolean WHITE = true;
	public volatile static LinkedList<ChessPieces> blackChessPieces = new LinkedList<ChessPieces>();
	public volatile static LinkedList<ChessPieces> whiteChessPieces = new LinkedList<ChessPieces>();
	private static final long serialVersionUID = 1L;
	private SquareBox boxes[][] = new SquareBox[8][8];
	private volatile static boolean turn = WHITE;

	class Selection implements Observer {

		@Override
		public void update(Observable o, Object arg) {
			setSelectedBox((SquareBox) arg);
		}
	}

	private Selection selection = new Selection();
	private static SquareBox selectedBox;
	public static long whiteState = 0, blackState = 0;
	private ChessFrame chessFrame;
	private Controls controls;
	private ChessPieces whiteKing, blackKing;
	public ChessBoard(final ChessFrame cf,Controls c) {
		chessFrame = cf;
		controls=c;
		turn = WHITE;
		setLayout(new GridLayout(8, 8));
		int bool = 1;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 4; j++) {
				if (bool == 0) {
					add(boxes[i][j * 2] = new RedSquare());
					boxes[i][j * 2].setLocationOnBoard(i, j * 2);
					boxes[i][j * 2].addSelectionObserver(selection);
					add(boxes[i][j * 2 + 1] = new BlueSquare());
					boxes[i][j * 2 + 1].setLocationOnBoard(i, j * 2 + 1);
					boxes[i][j * 2 + 1].addSelectionObserver(selection);
				} else {
					add(boxes[i][j * 2] = new BlueSquare());
					boxes[i][j * 2].setLocationOnBoard(i, j * 2);
					boxes[i][j * 2].addSelectionObserver(selection);
					add(boxes[i][j * 2 + 1] = new RedSquare());
					boxes[i][j * 2 + 1].setLocationOnBoard(i, j * 2 + 1);
					boxes[i][j * 2 + 1].addSelectionObserver(selection);
				}
			}
			bool = (bool + 1) % 2;
		}
		init();
	}

	public void setSelectedBox(SquareBox box) {
		if (box == selectedBox)
			selectedBox = null;
		else if (selectedBox != null) {
			if (selectedBox.getPiece() != null
					&& selectedBox.getPiece().possibleMoves()
							.contains(box.getLocationOnBoard())
					&& whoseTurn() == selectedBox.getPiece().getColor()) {
				box.setPiece(selectedBox.getPiece());
				box.getPiece().move();
				selectedBox.setPiece(null);
				switchTurn();
			}
			selectedBox = box;
		} else
			selectedBox = box;
		if (selectedBox != null)
			selectedBox.toggleSelect();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (selectedBox != boxes[i][j] && boxes[i][j].isSelected) {
					boxes[i][j].toggleSelect();
				}
				if (selectedBox != boxes[i][j] && boxes[i][j].isLit
						&& !boxes[i][j].isHover) {
					boxes[i][j].removeHovering();
				}
			}
		}

		if (selectedBox != null && selectedBox.getPiece() != null) {
			Thread t = new Thread(new movesHover());
			t.start();
		}

	}

	public static boolean whoseTurn() {
		return turn;
	}

	public void switchTurn() {
		controls.switchTurn();
		turn = !turn;
	}

	public void reset(){
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				boxes[i][j].setPiece(null);
			}
		}
		init();
		setSelectedBox(boxes[2][3]);
		turn=WHITE;
	}
	public void init() {
		ChessPieces c;
		try {
			for (int i = 0; i < 8; i++) {
				c = new Pawn(1, i, BLACK);
				blackChessPieces.add(c.clone());
				boxes[1][i].setPiece(c);
			}
			for (int i = 0; i < 8; i++) {
				c = new Pawn(6, i, WHITE);
				whiteChessPieces.add(c.clone());
				boxes[6][i].setPiece(c);
			}
			c = new Rook(0, 0, BLACK);
			blackChessPieces.add(c.clone());
			boxes[0][0].setPiece(c);
			c = new Rook(0, 7, BLACK);
			blackChessPieces.add(c.clone());
			boxes[0][7].setPiece(c);
			c = new Rook(7, 0, WHITE);
			whiteChessPieces.add(c.clone());
			boxes[7][0].setPiece(c);
			c = new Rook(7, 7, WHITE);
			whiteChessPieces.add(c.clone());
			boxes[7][7].setPiece(c);

			c = new Bishop(0, 1, BLACK);
			blackChessPieces.add(c.clone());
			boxes[0][1].setPiece(c);
			c = new Bishop(0, 6, BLACK);
			blackChessPieces.add(c.clone());
			boxes[0][6].setPiece(c);
			c = new Bishop(7, 1, WHITE);
			whiteChessPieces.add(c.clone());
			boxes[7][1].setPiece(c);
			c = new Bishop(7, 6, WHITE);
			whiteChessPieces.add(c.clone());
			boxes[7][6].setPiece(c);

			c = new Knight(0, 2, BLACK);
			blackChessPieces.add(c.clone());
			boxes[0][2].setPiece(c);
			c = new Knight(0, 5, BLACK);
			blackChessPieces.add(c.clone());
			boxes[0][5].setPiece(c);
			c = new Knight(7, 2, WHITE);
			whiteChessPieces.add(c.clone());
			boxes[7][2].setPiece(c);
			c = new Knight(7, 5, WHITE);
			whiteChessPieces.add(c.clone());
			boxes[7][5].setPiece(c);

			blackKing=c = new King(0, 4, BLACK,chessFrame);
			blackChessPieces.add(c.clone());
			boxes[0][4].setPiece(c);
			whiteKing=c = new King(7, 4, WHITE,chessFrame);
			whiteChessPieces.add(c.clone());
			boxes[7][4].setPiece(c);

			c = new Queen(0, 3, BLACK);
			blackChessPieces.add(c.clone());
			boxes[0][3].setPiece(c);
			c = new Queen(7, 3, WHITE);
			whiteChessPieces.add(c.clone());
			boxes[7][3].setPiece(c);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

	public static SquareBox getSelection() {
		return selectedBox;
	}

	public class movesHover implements Runnable {
		final LinkedList<Integer> list = selectedBox.getPiece().possibleMoves();

		public void run() {
			Iterator<Integer> it = list.iterator();
			while (it.hasNext()) {
				int loc = it.next();
				try {
					synchronized (selectedBox) {
						SquareBox sq = boxes[loc / 8][loc % 8];
						if (!sq.isLit)
							sq.startHovering();
						sq.toggleSelect();
					}
				} catch (NullPointerException e) {}
				try {
					Thread.sleep(25);
				} catch (InterruptedException e) {
				}
			}
		}
	}
	public void isBlackChecked(){
		
	}
	public void isWhiteChecked(){
		
	}
	public void isBlackCheckMated(){
		
	}
	public void isWhiteCheckMated(){
		
	}
}
