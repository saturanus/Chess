import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.*;

public abstract class SquareBox extends JComponent {

	private static final long serialVersionUID = 1L;

	private static final int delay = 10;

	private static final long forever = 100000000000L;

	abstract public Color setHovered(int degree);

	abstract public Color setNormal();

	private Color boxColor;
	private int degreeColor = 0, degreeSize = 0;
	private Hover h;
	private Thread t1, t2;
	private Select s;
	protected boolean isSelected = false, isHover = false, isLit = false;
	private SquareBox instance;
	private ChessPieces chessPiece;
	Thread t;

	final class selectionObservable extends Observable {
		@Override
		protected synchronized void setChanged() {
			super.setChanged();
		}
	}

	private selectionObservable sObservable = new selectionObservable();

	public void addSelectionObserver(Observer obs) {
		sObservable.addObserver(obs);
	}

	public SquareBox() {
		chessPiece = null;
		boxColor = setNormal();
		h = new Hover(this);
		t1 = new Thread(h);
		s = new Select(this);
		t2 = new Thread(s);
		t1.start();
		t2.start();
		repaint();
		instance = this;
		this.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent E) {
				isHover = true;
				startHovering();
			}

			public void mouseExited(MouseEvent E) {
				isHover = false;
				removeHovering();
			}

			public void mouseClicked(MouseEvent e) {
				synchronized (instance) {
					sObservable.setChanged();
					sObservable.notifyObservers(instance);
				}
			}

		});
	}

	public void startHovering() {
		synchronized (this) {
			if (isSelected && isLit)
				return;
			isLit = true;
			h.setShift();
			t1.interrupt();
		}
	}

	public void removeHovering() {
		synchronized (this) {
			if (isSelected && isLit)
				return;
			isLit = false;
			h.setShift();
			t1.interrupt();
		}
	}

	public void toggleSelect() {
		synchronized (this) {
			if (this == ChessBoard.getSelection() && isSelected)
				return;
			isSelected = !isSelected;
			if (!isSelected) {
				if (!isHover && isLit) {
					removeHovering();
				} else if (isHover && !isLit) {
					startHovering();
				}
			} else {
				if (!isLit) {
					startHovering();
				}
			}
			s.setShift();
			t2.interrupt();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(boxColor);
		g2d.fillRect(2, 2, this.getWidth() - 2, this.getHeight() - 2);
		if (chessPiece != null)
			printPieces(g, chessPiece);
	}

	public class Hover implements Runnable {

		private int shift;
		private SquareBox sb;

		public Hover(SquareBox x) {
			sb = x;
			shift = 1;
		}

		@Override
		public void run() {
			long sleepTime = forever;
			while (true) {
				if (degreeColor == 0 || degreeColor == 20)
					sleepTime = forever;
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException E) {
					sleepTime = delay;
				}
				degreeColor -= shift;
				synchronized (sb) {
					sb.boxColor = sb.setHovered(shift * 2);
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							sb.repaint();
						}
					});
				}
			}
		}

		public void setShift() {
			shift = -shift;
		}

	}

	public class Select implements Runnable {

		private int shift;
		private SquareBox sb;

		public Select(SquareBox x) {
			sb = x;
			shift = -1;
		}

		@Override
		public void run() {
			long sleepTime = forever;
			/*while (true) {
				if (degreeSize <= 0 || degreeSize >= 0) {
					sleepTime = forever;
				}
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException E) {
					sleepTime = delay;
				}
				degreeSize += shift;
				synchronized (sb) {
					sb.setLocation(getLocation().x - shift, getLocation().y
							- shift);
					sb.setSize(getSize().width + 2 * shift, getSize().height
							+ 2 * shift);
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							sb.repaint();
						}
					});
				}
			}*/
		}

		public void setShift() {
			shift = -shift;
		}

	}
	public void setPiece(ChessPieces c) {
		if (chessPiece != null && c != null) {
			ChessPieces cp = chessPiece;
			
			for (ChessPieces cc : new LinkedList<ChessPieces>(
					ChessBoard.blackChessPieces)) {
				if (cc.getLocation() == chessPiece.getLocation()) {
					ChessBoard.blackChessPieces.remove(cc);
					chessPiece.kill();
				}
				if (cc.getLocation() == c.getLocation()) {
					cc.setLocation(getLocationOnBoardX(), getLocationOnBoardY());
					cc.move();
					cp = c;
				}
			}
			for (ChessPieces cc : new LinkedList<ChessPieces>(
					ChessBoard.whiteChessPieces)) {
				if (cc.getLocation() == chessPiece.getLocation()) {
					ChessBoard.whiteChessPieces.remove(cc);
					System.out.println(chessPiece.getLocation());
					chessPiece.kill();
				}
				if (cc.getLocation() == c.getLocation()) {
					cc.setLocation(getLocationOnBoardX(), getLocationOnBoardY());
					cc.move();
					cp = c;
				}
			}
			chessPiece = cp;
			chessPiece
					.setLocation(getLocationOnBoardX(), getLocationOnBoardY());
			chessPiece.move();
			long temp=1;
			if (chessPiece.getColor())
				ChessBoard.whiteState |= temp << getLocationOnBoard();
			else
				ChessBoard.blackState |= temp << getLocationOnBoard();

		} else if (c != null) {
			ChessPieces cp = chessPiece;
			for (ChessPieces cc : new LinkedList<ChessPieces>(
					ChessBoard.blackChessPieces)) {
				if (cc.getLocation() == c.getLocation()) {
					cc.setLocation(getLocationOnBoardX(), getLocationOnBoardY());
					cc.move();
					cp = c;
				}
			}
			for (ChessPieces cc : new LinkedList<ChessPieces>(
					ChessBoard.whiteChessPieces)) {
				if (cc.getLocation() == c.getLocation()) {
					cc.setLocation(getLocationOnBoardX(), getLocationOnBoardY());
					cc.move();
					cp = c;
				}
			}
			chessPiece = cp;
			if(cp==null) return;
			chessPiece.move();
			chessPiece
					.setLocation(getLocationOnBoardX(), getLocationOnBoardY());
			long temp = 1;
			if (chessPiece.getColor())
				ChessBoard.whiteState |= temp << getLocationOnBoard();
			else
				ChessBoard.blackState |= temp << getLocationOnBoard();
		} else {
			chessPiece = null;
			long temp = 1;
			temp = temp << getLocationOnBoard();
			temp = ~temp;
			ChessBoard.whiteState &= temp;
			ChessBoard.blackState &= temp;
		}
		repaint();
	}

	public ChessPieces getPiece() {
		return chessPiece;
	}

	public void printPieces(Graphics g, ChessPieces c) {
		BufferedImage img = null;
		if (c.getColor() == false) {
			switch (c.toString()) {
			case "Queen": {
				try {
					img = ImageIO
							.read(new File("./ChessPieces/Queen_Black.png"));
				} catch (IOException e) {
				}
				break;
			}
			case "King": {
				try {
					img = ImageIO
							.read(new File("./ChessPieces/King_Black.png"));
				} catch (IOException e) {
				}
				break;
			}
			case "Pawn": {
				try {
					img = ImageIO
							.read(new File("./ChessPieces/Pawn_Black.png"));
				} catch (IOException e) {
				}
				break;
			}
			case "Knight": {
				try {
					img = ImageIO.read(new File(
							"./ChessPieces/Knight_Black.png"));
				} catch (IOException e) {
				}
				break;
			}
			case "Bishop": {
				try {
					img = ImageIO.read(new File(
							"./ChessPieces/Bishop_Black.png"));
				} catch (IOException e) {
				}
				break;
			}
			case "Rook": {
				try {
					img = ImageIO
							.read(new File("./ChessPieces/Rook_Black.png"));
				} catch (IOException e) {
				}
				break;
			}
			default: {
				img = null;
			}
			}
		} else {
			switch (c.toString()) {
			case "Queen": {
				try {
					img = ImageIO
							.read(new File("./ChessPieces/Queen_White.png"));
				} catch (IOException e) {
				}
				break;
			}
			case "King": {
				try {
					img = ImageIO
							.read(new File("./ChessPieces/King_White.png"));
				} catch (IOException e) {
				}
				break;
			}
			case "Pawn": {
				try {
					img = ImageIO
							.read(new File("./ChessPieces/Pawn_White.png"));
				} catch (IOException e) {
				}
				break;
			}
			case "Knight": {
				try {
					img = ImageIO.read(new File(
							"./ChessPieces/Knight_White.png"));
				} catch (IOException e) {
				}
				break;
			}
			case "Bishop": {
				try {
					img = ImageIO.read(new File(
							"./ChessPieces/Bishop_White.png"));
				} catch (IOException e) {
				}
				break;
			}
			case "Rook": {
				try {
					img = ImageIO
							.read(new File("./ChessPieces/Rook_White.png"));
				} catch (IOException e) {
				}
				break;
			}
			default: {
				img = null;
			}
			}
		}
		if (img != null)
			g.drawImage(img, 13, 7, this.getWidth() - 25,
					this.getHeight() - 12, null);

	}

	public void setLocationOnBoard(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getLocationOnBoard() {
		return x * 8 + y;
	}

	public int getLocationOnBoardX() {
		return x;
	}

	public int getLocationOnBoardY() {
		return y;
	}

	private int x, y;
}
