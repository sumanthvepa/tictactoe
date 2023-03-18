package sv.tictactoe;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ConsoleGameIOTest {
  @Test void checkGetPosition() throws Board.InvalidPositionException {
    var lineSeparator = System.getProperty("line.separator");
    var input = "top-left" + lineSeparator
        + "top-middle" + lineSeparator
        + "top-left" + lineSeparator
        + "middle" + lineSeparator;
    var is = new ByteArrayInputStream(input.getBytes());
    var baos = new ByteArrayOutputStream();
    var os = new PrintStream(baos);
    var io = new ConsoleGameIO(is, os);
    var board = new Board();

    var position = io.getPosition(board);
    assertEquals(Board.Position.TOP_LEFT, position);
    var expectedOutput = board
        + lineSeparator + "Choose a position: "
        + String.join(
            ", ", Arrays.stream(Board.Position.values()).map((element) -> element.label).toArray(String[]::new))
        + lineSeparator + "Enter choice: ";
    var actualOutput = baos.toString();
    assertEquals(expectedOutput, actualOutput);
    board.placeMark(position, Board.Symbol.NOUGHT);

    position = io.getPosition(board);
    assertEquals(Board.Position.TOP_MIDDLE, position);
    board.placeMark(position, Board.Symbol.CROSS);

    // The io system should reject invalid input.
    // For this call to get position, it should consume
    // and reject the second attemp to place a mark
    // on top-left, and take the correct input on the
    // next try, namely middle.
    position = io.getPosition(board);
    assertEquals(Board.Position.MIDDLE, position);
    board.placeMark(position, Board.Symbol.NOUGHT);
  }
}
