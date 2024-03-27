package main;

import java.awt.*;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

import javax.swing.*;
import java.util.ArrayList;
import main.input;

public class Board extends JPanel {
	public int tileSize = 85;
	int cols = 8;
	int rows = 8;

	ArrayList<Piece> pieceList = new ArrayList<>();

	public Piece selectedPiece;

	input input = new input(this);

	public int enPassantfile = -1;
	public CheckScanner checkScanner = new CheckScanner(this);

	public Board() {
		// size = 8 *8 square where each is 85 pixels
		this.setPreferredSize(new Dimension(cols * tileSize, rows * tileSize));

		this.addMouseListener(input);
		this.addMouseMotionListener(input);

		addPieces();
	}

	public Piece getPiece(int col, int row) {
		for (Piece piece : pieceList) {
			if (piece.col == col && piece.row == row) {
				return piece;
			}
		}
		return null;
	}

	public void makeMove(Move move) {

		if (move.piece.name.equals("Pawn")) {
			movePawn(move);

		} else if (move.piece.name.equals("King")) {
			moveKing(move);
		} 
			move.piece.col = move.newCol;
			move.piece.row = move.newRow;
			move.piece.xPos = move.newCol * tileSize;
			move.piece.yPos = move.newRow * tileSize;
			move.piece.isFirstMove = false;
			capture(move.capture);
	}

	private void promotePawn(Move move) {
		pieceList.add(new Queen(this, move.newCol, move.newRow, move.piece.isWhite));
		capture(move.piece);
	}

	private void moveKing(Move move) {

		if (Math.abs(move.piece.col - move.newCol) == 2) {
			Piece rook;
			if (move.piece.col < move.newCol) {
				rook = getPiece(7, move.piece.row);
				rook.col = 5;
			} else {
				rook = getPiece(0, move.piece.row);
				rook.col = 3;
			}
			rook.xPos = rook.col * tileSize;
		}
	}

	private void movePawn(Move move) {
		int colorIndex = move.piece.isWhite ? 1 : -1;

		if (getTileNum(move.newCol, move.newRow) == enPassantfile) {
			move.capture = getPiece(move.newCol, move.newRow + colorIndex);
		}
		if (Math.abs(move.piece.row - move.newRow) == 2) {
			enPassantfile = getTileNum(move.newCol, move.newRow + colorIndex);
		} else {
			enPassantfile = -1;
		}

		colorIndex = move.piece.isWhite ? 0 : 7;
		if (move.newRow == colorIndex) {
			promotePawn(move);
		}

		move.piece.col = move.newCol;
		move.piece.row = move.newRow;
		move.piece.xPos = move.newCol * tileSize;
		move.piece.yPos = move.newRow * tileSize;

		move.piece.isFirstMove = false;
		capture(move.capture);
	}

	public void capture(Piece piece) {
		pieceList.remove(piece);
	}

	public boolean isValidMove(Move move) {
		if (sameTeam(move.piece, move.capture)) {
			return false;
		}

		if (!move.piece.isValidMovement(move.newCol, move.newRow)) {
			return false;
		}

		if (move.piece.moveCollidesWithPiece(move.newCol, move.newRow)) {
			return false;
		}
		if (checkScanner.isKingChecked(move)) {
			return false;
		}
		return true;
	}

	public boolean sameTeam(Piece p1, Piece p2) {
		if (p1 == null || p2 == null) {
			return false;
		}
		return p1.isWhite == p2.isWhite;
	}

	Piece findKing(boolean isWhite) {
		for (Piece piece : pieceList) {
			if (isWhite == piece.isWhite && piece.name.equals("King")) {
				return piece;
			}
		}
		return null;
	}

	public int getTileNum(int col, int row) {
		return row * rows + col;
	}

	public void addPieces() {
		pieceList.add(new Rook(this, 0, 0, false));
		pieceList.add(new Knight(this, 1, 0, false));
		pieceList.add(new Bishop(this, 2, 0, false));
		pieceList.add(new Queen(this, 3, 0, false));
		pieceList.add(new King(this, 4, 0, false));
		pieceList.add(new Bishop(this, 5, 0, false));
		pieceList.add(new Knight(this, 6, 0, false));
		pieceList.add(new Rook(this, 7, 0, false));

		pieceList.add(new Pawn(this, 0, 1, false));
		pieceList.add(new Pawn(this, 1, 1, false));
		pieceList.add(new Pawn(this, 2, 1, false));
		pieceList.add(new Pawn(this, 3, 1, false));
		pieceList.add(new Pawn(this, 4, 1, false));
		pieceList.add(new Pawn(this, 5, 1, false));
		pieceList.add(new Pawn(this, 6, 1, false));
		pieceList.add(new Pawn(this, 7, 1, false));

		pieceList.add(new Rook(this, 0, 7, true));
		pieceList.add(new Knight(this, 1, 7, true));
		pieceList.add(new Bishop(this, 2, 7, true));
		pieceList.add(new Queen(this, 3, 7, true));
		pieceList.add(new King(this, 4, 7, true));
		pieceList.add(new Bishop(this, 5, 7, true));
		pieceList.add(new Knight(this, 6, 7, true));
		pieceList.add(new Rook(this, 7, 7, true));

		pieceList.add(new Pawn(this, 0, 6, true));
		pieceList.add(new Pawn(this, 1, 6, true));
		pieceList.add(new Pawn(this, 2, 6, true));
		pieceList.add(new Pawn(this, 3, 6, true));
		pieceList.add(new Pawn(this, 4, 6, true));
		pieceList.add(new Pawn(this, 5, 6, true));
		pieceList.add(new Pawn(this, 6, 6, true));
		pieceList.add(new Pawn(this, 7, 6, true));

	}

	// already, a method in the J class, must change
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				// if the value is divisible by 2, make the tile a certain color , else other
				// color
				g2d.setColor((r + c) % 2 == 0 ? new Color(230, 200, 180) : new Color(157, 105, 53));

				g2d.fillRect(c * tileSize, r * tileSize, tileSize, tileSize);

			}
		}

		if (selectedPiece != null)
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					if (isValidMove(new Move(this, selectedPiece, c, r))) {
						g2d.setColor(new Color(68, 180, 57, 190));
						g2d.fillRect(c * tileSize, r * tileSize, tileSize, tileSize);
					}
				}
			}
		for (Piece piece : pieceList) {
			piece.paint(g2d);
		}
	}

}
