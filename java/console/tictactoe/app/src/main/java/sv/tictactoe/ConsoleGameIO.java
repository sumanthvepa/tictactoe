package sv.tictactoe;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

public class ConsoleGameIO implements GameIO{
  private final PrintStream os;
  private final Scanner ss;

  private @NotNull String displayBoard(@NotNull Board board) { return board.toString(); }

  private @NotNull String displayPrompt(@NotNull Board.Position[] allowedPositions) {
    var lineSeparator = System.getProperty("line.separator");
    return "Choose a position: "
        + Arrays.stream(allowedPositions).map(position -> position.label)
        .reduce("",(allowedPositionsString, positionLabel)
            -> (allowedPositionsString.length() > 0)?
            allowedPositionsString + ", " + positionLabel : positionLabel) + lineSeparator
        + "Type 'resign' to resign; or, 'exit' to end the game immediately." + lineSeparator
        + "Enter choice: ";
  }

  private @NotNull String getInput() throws AbnormalTerminationException {
    var input = ss.nextLine();
    if (input.equals("resign")) throw new PlayerResignedException();
    if (input.equals("exit")) throw new PlayerExitedException();
    return input;
  }

  public ConsoleGameIO(@NotNull InputStream is, @NotNull PrintStream os) {
    this.os = os;
    this.ss = new Scanner(is);
  }

  @Override public @NotNull Board.Position getPosition(@NotNull Board board) throws AbnormalTerminationException {
    os.println(displayBoard(board));
    while (true) {
      try {
        os.print(displayPrompt(board.allowedPositions()));
        return Board.Position.parse(getInput(), board.allowedPositions());
      } catch(Board.InvalidPositionException ex) { os.println(ex.getMessage()); }
    }
  }

  @Override public void declareWinner(@NotNull Board board, @NotNull Board.Symbol symbol, @NotNull Player player) {
    os.println(board);
    os.println("Player " + symbol.label + " has won!");
  }

  @Override public void declareResignation(@NotNull Board board, @NotNull Board.Symbol symbol, @NotNull Player currentPlayer, AbnormalTerminationException ex) {
    os.println(board);
    os.println("Player " + symbol.label + " " + ex.getMessage());
  }

  @Override public void declareDraw(@NotNull Board board) {
    os.println(board);
    os.println("It's a draw.");
  }

  @Override
  public void systemError(@NotNull String message) { os.println(message); }
}
