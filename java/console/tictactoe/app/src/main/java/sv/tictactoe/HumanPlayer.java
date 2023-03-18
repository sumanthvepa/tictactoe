package sv.tictactoe;

import org.jetbrains.annotations.NotNull;

public class HumanPlayer implements Player {
  private final @NotNull GameIO io;

  public HumanPlayer(@NotNull GameIO io) { this.io = io; }

  @Override
  public void makeMove(Board board, Board.Symbol symbol) {
    try {
      board.placeMark(io.getPosition(board), symbol);
    } catch(Board.InvalidPositionException ex) {
      io.systemError(ex.getMessage());
    }
  }
}
