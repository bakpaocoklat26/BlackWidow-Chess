package com.chess.engine.classic.pieces;

import java.util.List;

import com.chess.engine.classic.Alliance;
import com.chess.engine.classic.board.Board;
import com.chess.engine.classic.board.Move;
import com.chess.engine.classic.board.Move.AttackMove;
import com.chess.engine.classic.board.Tile;
import com.google.common.collect.ImmutableList.Builder;

public final class Rook extends Piece {

    private final static int[] candidateMoveCoordinates = { -8, -1, 1, 8 };

    public Rook(final Alliance alliance,
                final int piecePosition,
                final boolean isFirstMove) {
        super(Type.ROOK, alliance, piecePosition, isFirstMove);
    }

    public Rook(final Alliance alliance, final int piecePosition) {
        super(Type.ROOK, alliance, piecePosition, true);
    }

    private Rook(final Rook rook) {
        super(rook);
    }

    @Override
    public List<Move> calculateLegalMoves(final Board board) {
        final Builder<Move> legalMoves = new Builder<>();
        int candidateDestinationCoordinate;
        for (final int currentCandidate : candidateMoveCoordinates) {
            candidateDestinationCoordinate = this.piecePosition;
            while (true) {
                if (isColumnExclusion(currentCandidate, candidateDestinationCoordinate)) {
                    break;
                }
                candidateDestinationCoordinate += currentCandidate;
                if (!Board.isValidTileCoordinate(candidateDestinationCoordinate)) {
                    break;
                } else {
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    if (!candidateDestinationTile.isTileOccupied()) {
                        legalMoves.add(new Move(this.piecePosition, candidateDestinationCoordinate, this));
                    } else {
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAtDestinationAllegiance = pieceAtDestination.getPieceAllegiance();
                        if (this.pieceAlliance != pieceAtDestinationAllegiance) {
                            legalMoves.add(new AttackMove(this.piecePosition, candidateDestinationCoordinate, this,
                                    pieceAtDestination));
                        }
                        break;
                    }
                }
            }
        }
        return legalMoves.build();
    }

    @Override
    public int getPieceValue() {
        return Type.ROOK.getPieceValue();
    }

    @Override
    public int locationBonus() {
        return this.pieceAlliance.rookBonus(this.piecePosition);
    }

    @Override
    public Rook createCopy() {
        return new Rook(this);
    }

    @Override
    public Rook createTransitionedPiece(final Move move) {
        return new Rook(move.getMovedPiece().getPieceAllegiance(), move.getDestinationCoordinate(), false);
    }

    @Override
    public String toString() {
        return Type.ROOK.toString();
    }

    private static boolean isColumnExclusion(final int currentCandidate, final int candidateDestinationCoordinate) {
        return (Board.FIRST_COLUMN[candidateDestinationCoordinate] && (currentCandidate == -1)) ||
               (Board.EIGHTH_COLUMN[candidateDestinationCoordinate] && (currentCandidate == 1));
    }

}