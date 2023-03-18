package sv.tictactoe;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board {
  static class InvalidSymbolLabelException extends Exception {
    InvalidSymbolLabelException(@NotNull String label) {
      super(label + "is not a valid symbol label.");
    }
  }
  static class InvalidPositionException extends Exception {
    InvalidPositionException(@NotNull String message) { super(message); }
  }
  enum Position {
    TOP_LEFT("top-left", 0, 0),
    TOP_MIDDLE("top-middle", 0, 1),
    TOP_RIGHT("top-right", 0, 2),
    MIDDLE_LEFT("middle-left", 1, 0),
    MIDDLE("middle", 1, 1),
    MIDDLE_RIGHT("middle-right", 1, 2),
    BOTTOM_LEFT("bottom-left", 2, 0),
    BOTTOM_MIDDLE("bottom-middle", 2, 1),
    BOTTOM_RIGHT("bottom-right", 2, 2);

    public final @NotNull String label;
    public final int row, column;

    Position(@NotNull String label, int row, int column) {
      this.label = label;
      this.row = row;
      this.column = column;
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull Position @NotNull [] topRow() {
      return new Position[]{ Position.TOP_LEFT, Position.TOP_MIDDLE, Position.TOP_RIGHT };
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull Position @NotNull [] middleRow() {
      return new Position[]{ Position.MIDDLE_LEFT, Position.MIDDLE, Position.MIDDLE_RIGHT };
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull Position @NotNull [] bottomRow() {
      return new Position[]{ Position.BOTTOM_LEFT, Position.BOTTOM_MIDDLE, Position.BOTTOM_RIGHT };
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull Position @NotNull [] leftColumn() {
      return new Position[]{ Position.TOP_LEFT, Position.MIDDLE_LEFT, Position.BOTTOM_LEFT };
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull Position @NotNull [] middleColumn() {
      return new Position[]{ Position.TOP_MIDDLE, Position.MIDDLE, Position.BOTTOM_MIDDLE };
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull Position @NotNull [] rightColumn() {
      return new Position[]{ Position.TOP_RIGHT, Position.MIDDLE_RIGHT, Position.BOTTOM_RIGHT };
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull Position @NotNull [] primaryDiagonal() {
      return new Position[]{ Position.TOP_LEFT, Position.MIDDLE, Position.BOTTOM_RIGHT };
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull Position @NotNull [] secondaryDiagonal() {
      return new Position[]{ Position.TOP_RIGHT, Position.MIDDLE, Position.BOTTOM_LEFT };
    }


    public static @NotNull Position parse(@NotNull String input, @NotNull Position[] validPositions) throws InvalidPositionException {
      Position[] positions = (validPositions != null)? validPositions : Position.values();
      for (var position: positions) {
        if (input.equalsIgnoreCase(position.label)) return position;
      }
      throw new InvalidPositionException("'" + input + "' " + " is not a valid position.");
    }
  }
  enum Symbol {
    NOUGHT("O"),
    CROSS("X");

    public final String label;

    Symbol(String label) { this.label = label; }

    public @NotNull Symbol other() {
      if (this == Symbol.NOUGHT) return Symbol.CROSS;
      else return Symbol.NOUGHT;
    }

    public static @NotNull Symbol forString(@NotNull String label) throws InvalidSymbolLabelException {
      if (label.equals(NOUGHT.label)) return NOUGHT;
      if (label.equals(CROSS.label)) return CROSS;
      throw new  InvalidSymbolLabelException(label);
    }
    public static Symbol forStringNoEx(@NotNull String label) {
      try {
        return forString(label);
      } catch (InvalidSymbolLabelException ex) {
        return null;
      }
    }
  }

  private final @NotNull Map<Position, Symbol> cells;

  public Board() { this.cells = new HashMap<>(); }

  public Board(@NotNull Map<Position, Symbol> cells) { this.cells = cells; }

  @Override
  public @NotNull String toString() {
    var count = 0;
    final var buffer = new StringBuilder();
    final var allPositions = Position.values();
    final var lineSeparator = System.getProperty("line.separator");
    for (var position: allPositions) {
      var symbol = cells.get(position);
      if (symbol == null) buffer.append("_");
      else buffer.append(symbol.label);
      if (count < allPositions.length-1) {
        if (count % 3 == 2) buffer.append(lineSeparator);
        else buffer.append(" ");
      }
      ++count;
    }
    return buffer.toString();
  }

  public boolean isEmpty(@NotNull Position position) { return cells.get(position) == null; }

  public Symbol getMark(@NotNull Position position) { return cells.get(position); }

  public void placeMark(@NotNull Position position, @NotNull Symbol symbol) throws InvalidPositionException {
    if (!isEmpty(position))
      throw new InvalidPositionException(position + " is not empty.");
    cells.put(position, symbol);
  }

  public @NotNull Position @NotNull [] allowedPositions() {
    var allowed = new ArrayList<Position>();
    for (var position: Position.values()) {
      if (cells.get(position) == null) allowed.add(position);
    }
    return allowed.toArray(new Position[0]);
  }

  public boolean hasSameSymbol(@NotNull Position @NotNull [] positions, @NotNull Symbol symbol) {
    for (var position: positions) {
      if (cells.get(position) != symbol) return false;
    }
    return true;
  }

  public boolean isFull(@NotNull Position @NotNull [] positions) {
    for (var position: positions) {
      if (cells.get(position) == null) return false;
    }
    return true;
  }

  public boolean isFull() { return isFull(Position.values()); }
}
