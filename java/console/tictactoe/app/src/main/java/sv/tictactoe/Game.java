package sv.tictactoe;

import org.jetbrains.annotations.NotNull;

public class Game {
  private final @NotNull  Board board;
  private final @NotNull GameAnalytics analytics;
  private final @NotNull Player noughts, crosses;
  private @NotNull Player currentPlayer;
  private final @NotNull GameIO io;

  private @NotNull Player nextPlayer() {
    currentPlayer = (currentPlayer == noughts)? crosses : noughts;
    return currentPlayer;
  }

  private @NotNull Board.Symbol playerSymbol() {
    return (currentPlayer == noughts)? Board.Symbol.NOUGHT : Board.Symbol.CROSS;
  }

  private void declareResult() {
    if (analytics.hasWon(board, Board.Symbol.NOUGHT)) io.declareWinner(board, Board.Symbol.NOUGHT, noughts);
    else if (analytics.hasWon(board, Board.Symbol.CROSS)) io.declareWinner(board, Board.Symbol.CROSS, crosses);
    else io.declareDraw(board);
  }

  public Game(@NotNull GameAnalytics analytics, @NotNull Player noughts, @NotNull Player crosses, @NotNull Board board, @NotNull GameIO io) {
    this.board = board;
    this.analytics = analytics;
    this.noughts = noughts;
    this.crosses = crosses;
    this.currentPlayer = crosses;
    this.io = io;
  }

  public void play() {
    while (!analytics.isGameOver(board))
      nextPlayer().makeMove(board, playerSymbol());
    declareResult();
  }

  public static @NotNull Game make() {
    var io = new ConsoleGameIO(System.in, System.out);
    var board = new Board();
    var referee = new GameAnalytics();
    var player1 = new HumanPlayer(io);
    var player2 = new HumanPlayer(io);
    return new Game(referee, player1, player2, board, io);
  }
}
