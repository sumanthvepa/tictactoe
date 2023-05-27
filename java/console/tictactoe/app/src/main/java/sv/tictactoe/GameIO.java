package sv.tictactoe;

import org.jetbrains.annotations.NotNull;

public interface GameIO {
  class AbnormalTerminationException extends Exception {
    AbnormalTerminationException(@NotNull String message) { super(message); }
  }

  class PlayerResignedException extends AbnormalTerminationException {
    PlayerResignedException() { super("resigned"); }
  }

  class PlayerQuitException extends AbnormalTerminationException {
    PlayerQuitException() { super("quit"); }
  }

  @NotNull Board.Position getPosition(@NotNull Board board) throws AbnormalTerminationException;

  void declareWinner(@NotNull Board board, @NotNull Board.Symbol symbol, @NotNull Player currentPlayer);

  void declareDraw(@NotNull Board board);

  void declareAbnormalTermination(@NotNull Board board, @NotNull Board.Symbol symbol, @NotNull Player currentPlayer, @NotNull AbnormalTerminationException ex);

  void systemError(@NotNull String message);
}
