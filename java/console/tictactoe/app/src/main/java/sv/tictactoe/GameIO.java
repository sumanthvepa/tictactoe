package sv.tictactoe;

import org.jetbrains.annotations.NotNull;

public interface GameIO {
  public static class AbnormalTerminationException extends Exception {
    AbnormalTerminationException(@NotNull String message) { super(message); }
  }

  public static class PlayerResignedException extends AbnormalTerminationException {
    PlayerResignedException() { super("resigned"); }
  }

  public static class PlayerExitedException extends AbnormalTerminationException {
    PlayerExitedException() { super("quit"); }
  }

  @NotNull Board.Position getPosition(@NotNull Board board) throws AbnormalTerminationException;

  void declareWinner(@NotNull Board board, @NotNull Board.Symbol symbol, @NotNull Player currentPlayer);
  void declareResignation(@NotNull Board board, @NotNull Board.Symbol symbol, @NotNull Player currentPlayer, AbnormalTerminationException ex);
  void declareDraw(@NotNull Board board);
  void systemError(@NotNull String message);
}
