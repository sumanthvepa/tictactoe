package sv.tictactoe;

import org.jetbrains.annotations.NotNull;

public class GameAnalytics {
  public boolean hasWon(@NotNull Board board, @NotNull Board.Symbol symbol) {
    return board.hasSameSymbol(Board.Position.topRow(), symbol)
        || board.hasSameSymbol(Board.Position.middleRow(), symbol)
        || board.hasSameSymbol(Board.Position.bottomRow(), symbol)
        || board.hasSameSymbol(Board.Position.leftColumn(), symbol)
        || board.hasSameSymbol(Board.Position.middleColumn(), symbol)
        || board.hasSameSymbol(Board.Position.rightColumn(), symbol)
        || board.hasSameSymbol(Board.Position.primaryDiagonal(), symbol)
        || board.hasSameSymbol(Board.Position.secondaryDiagonal(), symbol);
  }

  public boolean isDraw(@NotNull Board board) {
    return !hasWon(board, Board.Symbol.NOUGHT) && !hasWon(board, Board.Symbol.CROSS) && board.isFull();
  }

  public boolean isGameOver(@NotNull Board board) {
    return hasWon(board, Board.Symbol.NOUGHT) || hasWon(board, Board.Symbol.CROSS) || isDraw(board);
  }

  public Board.Symbol winner(@NotNull Board board) {
    if (hasWon(board, Board.Symbol.NOUGHT)) return Board.Symbol.NOUGHT;
    if (hasWon(board, Board.Symbol.CROSS)) return Board.Symbol.CROSS;
    return null;
  }
}
