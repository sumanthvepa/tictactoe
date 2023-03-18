package sv.tictactoe;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class GameAnalyticsTest {

  private static @NotNull Board makeBoard(@NotNull String boardString)  {
    var labels = boardString.toCharArray();
    var positions = Board.Position.values();
    Map<Board.Position, Board.Symbol> cells = new HashMap<>();
    for (int index = 0; index < positions.length; ++index) {
      var position = positions[index];
      var symbol = Board.Symbol.forStringNoEx(Character.toString((index < labels.length)? labels[index] : '_'));
      cells.put(position, symbol);
    }
    return new Board(cells);
  }

  void testHasWon(@NotNull String @NotNull [] boardStrings, @NotNull Board.Symbol symbol) {
    final var boards = Stream.of(boardStrings).map(GameAnalyticsTest::makeBoard).toList();
    final var analytics = new GameAnalytics();
    for (final var board: boards) {
      assertTrue(analytics.hasWon(board, symbol));
      assertEquals(symbol, analytics.winner(board));
      assertFalse(analytics.hasWon(board, symbol.other()));
      assertFalse(analytics.isDraw(board));
      assertTrue(analytics.isGameOver(board));
    }
  }

  @Test void winningCombinations() {
    final String[] crossesWins = {
        "XXXOO____",
        "_O_XXX__O",
        "_O__O_XXX",
        "XO_XO_X__",
        "OXO_XO_X_",
        "OOX__X__X",
        "XOO_X_O_X",
        "__X_X_XOO"
    };
    testHasWon(crossesWins, Board.Symbol.CROSS);
    final String [] naughtsWins = {
        "OOOXX___X",
        "XX_OOOX__",
        "XX_X__OOO",
        "OXXOX_O__",
        "XO_XO__OX",
        "X_O_XOX_O",
        "OX_XO__XO",
        "X_OXOXO__"
    };
    testHasWon(naughtsWins, Board.Symbol.NOUGHT);
  }

  void testDraws(String[] boardStrings) {
    final var boards = Stream.of(boardStrings).map(GameAnalyticsTest::makeBoard).toList();
    final var analytics = new GameAnalytics();
    for (final var board: boards) {
      assertFalse(analytics.hasWon(board, Board.Symbol.NOUGHT));
      assertFalse(analytics.hasWon(board, Board.Symbol.CROSS));
      assertTrue(analytics.isDraw(board));
      assertNull(analytics.winner(board));
      assertTrue(analytics.isGameOver(board));
    }
  }
  @Test void drawnCombinations() {
    final String [] draws = {
        "XOXXXOOXO",
        "OXXXXOOOX",
        "OXOXOXXOX",
    };
    testDraws(draws);
  }
}
