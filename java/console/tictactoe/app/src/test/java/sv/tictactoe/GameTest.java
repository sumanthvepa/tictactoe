package sv.tictactoe;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
  @Test void playSimpleGame() {
    var lineSeparator = System.getProperty("line.separator");
    var input = "top-left" + lineSeparator
        + "middle-left" + lineSeparator
        + "top-middle" + lineSeparator
        + "middle" + lineSeparator
        + "top-right" + lineSeparator;
    var board = new Board();
    var analytics = new GameAnalytics();
    var is = new ByteArrayInputStream(input.getBytes());
    var baos = new ByteArrayOutputStream();
    var os = new PrintStream(baos);
    var io = new ConsoleGameIO(is, os);
    var noughts = new HumanPlayer(io);
    var crosses = new HumanPlayer(io);
    var game = new Game(analytics, noughts, crosses,board, io);
    game.play();
    assertTrue(analytics.isGameOver(board));
    assertTrue(analytics.hasWon(board, Board.Symbol.NOUGHT));
    assertEquals(Board.Symbol.NOUGHT, analytics.winner(board));
    assertFalse(analytics.isDraw(board));
  }
}
