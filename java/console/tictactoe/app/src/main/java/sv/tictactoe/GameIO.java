package sv.tictactoe;

import org.jetbrains.annotations.NotNull;

public interface GameIO {
  @NotNull Board.Position getPosition(@NotNull Board board);
  void declareWinner(@NotNull Board board, @NotNull Board.Symbol symbol, @NotNull Player noughts);
  void declareDraw(@NotNull Board board);
  void systemError(@NotNull String message);
}
