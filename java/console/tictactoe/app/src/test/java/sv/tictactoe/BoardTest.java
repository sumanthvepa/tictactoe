package sv.tictactoe;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import sv.tictactoe.Board.Position;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
  void testPositionLabels(
      @NotNull Map<String, Position> positionMap,
      @NotNull Position[] validPositions)
      throws Board.InvalidPositionException {
    for (var entry: positionMap.entrySet()) {
      var label = entry.getKey();
      var expected = entry.getValue();
      var actual = Position.parse(label, validPositions);
      assertEquals(expected, actual);
    }
  }

  @Test void parseValidPositionLabels()
      throws Board.InvalidPositionException {
    final var positionMap = new HashMap<String, Position>();
    positionMap.put("top-left", Position.TOP_LEFT);
    positionMap.put("top-middle", Position.TOP_MIDDLE);
    positionMap.put("top-right", Position.TOP_RIGHT);
    positionMap.put("middle-left", Position.MIDDLE_LEFT);
    positionMap.put("middle", Position.MIDDLE);
    positionMap.put("middle-right", Position.MIDDLE_RIGHT);
    positionMap.put("bottom-left", Position.BOTTOM_LEFT);
    positionMap.put("bottom-middle", Position.BOTTOM_MIDDLE);
    positionMap.put("bottom-right", Position.BOTTOM_RIGHT);
    testPositionLabels(positionMap, Position.values());
  }

  @Test void parseInvalidPositionLabel() {
    assertThrows(
        Board.InvalidPositionException.class,
        () -> Position.parse("garbage", Position.values()));
  }

  @Test void parseValidRestrictedPositionLabels()
      throws Board.InvalidPositionException {
    final var positionMap = new HashMap<String, Position>();
    positionMap.put("top-left", Position.TOP_LEFT);
    positionMap.put("top-middle", Position.TOP_MIDDLE);
    positionMap.put("top-right", Position.TOP_RIGHT);
    testPositionLabels(positionMap, positionMap.values().toArray(new Position[0]));
  }

  @Test void parseInvalidRestrictedPositionLabels() {
    final Position[] validPositions = {
        Position.TOP_LEFT,
        Position.TOP_MIDDLE,
        Position.TOP_RIGHT
    };
    assertThrows(
        Board.InvalidPositionException.class,
        () -> Position.parse("middle-left", validPositions));
    assertThrows(
        Board.InvalidPositionException.class,
        () -> Position.parse("garbage", validPositions));
  }

  @Test void createEmptyBoard() {
    var board = new Board();
    for (var position: Position.values())
      assertNull(board.getMark(position));
  }

  @Test void getAndPlaceMark() throws Board.InvalidPositionException {
    var board = new Board();
    assertNull(board.getMark(Position.MIDDLE_RIGHT));
    assertTrue(board.isEmpty(Position.MIDDLE_RIGHT));
    board.placeMark(Position.MIDDLE_RIGHT, Board.Symbol.CROSS);
    assertEquals(Board.Symbol.CROSS, board.getMark(Position.MIDDLE_RIGHT));
    assertFalse(board.isEmpty(Position.MIDDLE_RIGHT));

    assertThrows(
        Board.InvalidPositionException.class,
        () -> board.placeMark(Position.MIDDLE_RIGHT, Board.Symbol.NOUGHT));
  }

  @Test void getValidPositions() throws Board.InvalidPositionException {
    var board = new Board();
    board.placeMark(Position.BOTTOM_LEFT, Board.Symbol.NOUGHT);
    var positions = board.allowedPositions();
    assertEquals(8, positions.length);
    for (var position: positions) {
      assertNotEquals(Position.BOTTOM_LEFT, position);
    }
    board.placeMark(Position.MIDDLE, Board.Symbol.CROSS);
    positions = board.allowedPositions();
    assertEquals(7, positions.length);
    for (var position: positions) {
      assertNotEquals(Position.BOTTOM_LEFT, position);
      assertNotEquals(Position.MIDDLE, position);
    }
  }

  void testHasSameSymbols(@NotNull Position @NotNull[] positions) throws Board.InvalidPositionException {
    var board = new Board();
    for (var position: positions) {
      board.placeMark(position, Board.Symbol.NOUGHT);
    }
    assertTrue(board.hasSameSymbol(positions, Board.Symbol.NOUGHT));
    assertFalse(board.hasSameSymbol(positions, Board.Symbol.CROSS));
  }

  @Test void checkHasSameSymbol() throws Board.InvalidPositionException {
    Position[][] testCases = {
        Position.topRow(),
        Position.middleRow(),
        Position.bottomRow(),
        Position.leftColumn(),
        Position.middleColumn(),
        Position.rightColumn(),
        Position.primaryDiagonal(),
        Position.secondaryDiagonal(),
        new Position[]{ Position.TOP_LEFT, Position.MIDDLE, Position.TOP_RIGHT }
    };
    for (var testCase: testCases) testHasSameSymbols(testCase);
  }

  @Test void checkIsFull() throws Board.InvalidPositionException {
    var board = new Board();
    var symbol = Board.Symbol.NOUGHT;
    var allPositions = Position.values();
    // Fill the board with noughts.
    for (var position: allPositions) {
      board.placeMark(position, symbol);
    }
    assertTrue(board.isFull());

    // Fill the board with alternate noughts and crosses
    board = new Board();
    for (var position: allPositions) {
      board.placeMark(position, symbol);
      if (symbol == Board.Symbol.NOUGHT) symbol = Board.Symbol.CROSS;
      else symbol = Board.Symbol.NOUGHT;
    }
    assertTrue(board.isFull());

    // Leave an empty square to check if isFull finds it.
    board = new Board();
    for (var position: allPositions) {
      // Skip the middle square (i.e. leave it empty)
      if (position == Position.MIDDLE) continue;
      board.placeMark(position, symbol);
    }
    assertFalse(board.isFull());
  }

  @Test void checkToString() throws Board.InvalidPositionException {
    var lineSeparator = System.getProperty("line.separator");
    var board = new Board();
    var allPositions = Position.values();
    var symbol = Board.Symbol.NOUGHT;
    // Fill the board with alternating noughts and crosses, but
    // leave the middle square empty.
    for (var position: allPositions) {
      // Skip the middle square (i.e. leave it empty)
      if (position == Position.MIDDLE) continue;
      board.placeMark(position, symbol);
      if (symbol == Board.Symbol.NOUGHT) symbol = Board.Symbol.CROSS;
      else symbol = Board.Symbol.NOUGHT;
    }

    String expected = "O X O" + lineSeparator + "X _ O" + lineSeparator + "X O X";
    String actual = board.toString();
    assertEquals(expected, actual);

    board = new Board();
    expected = "_ _ _" + lineSeparator + "_ _ _" + lineSeparator + "_ _ _";
    actual = board.toString();
    assertEquals(expected, actual);
  }
}
