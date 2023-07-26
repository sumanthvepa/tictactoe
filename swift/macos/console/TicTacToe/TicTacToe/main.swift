import Foundation

class Board: CustomStringConvertible {
  enum Position: String, CaseIterable {
    case TOP_LEFT = "top-left"
    case TOP_MIDDLE = "top-middle"
    case TOP_RIGHT = "top-right"
    case MIDDLE_LEFT = "middle-left"
    case MIDDLE = "middle"
    case MIDDLE_RIGHT = "middle-right"
    case BOTTOM_LEFT = "bottom-left"
    case BOTTOM_MIDDLE = "bottom-middle"
    case BOTTOM_RIGHT = "bottom-right"
    
    init?(positionString: String) {
      switch positionString {
      case Position.TOP_LEFT.rawValue:
        self = .TOP_LEFT
      case Position.TOP_MIDDLE.rawValue:
        self = .TOP_MIDDLE
      case Position.TOP_RIGHT.rawValue:
        self = .TOP_RIGHT
      case Position.MIDDLE_LEFT.rawValue:
        self = .MIDDLE_LEFT
      case Position.MIDDLE.rawValue:
        self = .MIDDLE
      case Position.MIDDLE_RIGHT.rawValue:
        self = .MIDDLE_RIGHT
      case Position.BOTTOM_LEFT.rawValue:
        self = .BOTTOM_LEFT
      case Position.BOTTOM_MIDDLE.rawValue:
        self = .BOTTOM_MIDDLE
      case Position.BOTTOM_RIGHT.rawValue:
        self = .BOTTOM_RIGHT
      default:
        return nil
      }
    }
    
    static func topRow() -> [Position] {
      return [.TOP_LEFT, .TOP_MIDDLE, .TOP_RIGHT]
    }
    
    static func middleRow() -> [Position] {
      return [.MIDDLE_LEFT, .MIDDLE, .MIDDLE_RIGHT]
    }
    
    static func bottomRow() -> [Position] {
      return [.BOTTOM_LEFT, .BOTTOM_MIDDLE, .BOTTOM_RIGHT]
    }
    
    static func leftColumn() -> [Position] {
      return [.TOP_LEFT, .MIDDLE_LEFT, .BOTTOM_LEFT]
    }
    
    static func middleColumn() -> [Position] {
      return [.TOP_MIDDLE, .MIDDLE, .BOTTOM_MIDDLE]
    }
    
    static func rightColumn() -> [Position] {
      return [.TOP_RIGHT, .MIDDLE_RIGHT, .BOTTOM_RIGHT]
    }
    
    static func primaryDiagonal() -> [Position] {
      return [.TOP_LEFT, .MIDDLE, .BOTTOM_RIGHT]
    }
    
    static func secondaryDiagonal() -> [Position] {
      return [.TOP_RIGHT, .MIDDLE, .BOTTOM_LEFT]
    }
  }
  
  enum Symbol: String {
    case NOUGHT = "O"
    case CROSS = "X"
  }
  
  private var cells: [Position: Symbol] = [:]
  
  
  var description: String {
    return Position.allCases.map {
      cells[$0] != nil ? cells[$0]!.rawValue : "_"}
      .enumerated().map{$0 % 3 == 2 ? $1 + "\n" : $1 + " "}
      .reduce("", +)
  }
  
  func getMark(position: Position) -> Symbol? { return cells[position] }
  
  func placeMark(position: Position, symbol: Symbol) { cells[position] = symbol }
  
  func hasSameSymbol(positions: [Position], symbol: Symbol) -> Bool {
    return positions.reduce(true) {
      cells[$1] != nil ? $0 && (cells[$1]! == symbol) : false
    }
  }
  
  func isFull() -> Bool {
    return Position.allCases.reduce(true) {
      cells[$1] != nil ? $0 && true : false
    }
  }
  
  func allowedPositions() -> [Position] {
    return Position.allCases.filter{cells[$0] == nil}
  }
}

enum AbnormalTermination: Error {
  case PlayerQuit(symbol: Board.Symbol)
  
  var symbol: Board.Symbol {
    switch self {
    case .PlayerQuit(let symbol): return symbol
    }
  }
}

protocol GameIO {
  func getPosition(board: Board, symbol: Board.Symbol) throws -> Board.Position
  func declareWinner(board: Board, symbol: Board.Symbol)
  func declareDraw(board: Board)
  func declareAbnormalTermination(board: Board, cause: AbnormalTermination)
  func systemError(error: Error)
}

class ConsoleGameIO: GameIO {
  private func displayPrompt(board: Board) {
    print("Choose a position: ", terminator: "")
    print(board.allowedPositions().map({$0.rawValue}).reduce("", {$0.isEmpty ? $1 : $0 + ", " + $1}))
    print("Type 'resign to resign or, 'exit' or 'quit' to end the game immediately.")
    print("Enter choice: ", terminator: "")
  }
  
  func getPosition(board: Board, symbol: Board.Symbol) throws -> Board.Position {
    var position: Board.Position? = nil
    print(board)
    repeat {
      displayPrompt(board: board)
      if let input: String = readLine() {
        if input == "exit" || input == "quit" || input == "resign" {
          throw AbnormalTermination.PlayerQuit(symbol: symbol)
        }
        position = Board.Position(positionString: input)
        if position == nil {
          print("Invalid position")
        }
      }
    } while position == nil
    return position!
  }
  
  func declareWinner(board: Board, symbol: Board.Symbol) {
    print(board)
    print("Player \(symbol) has won!")
  }
  
  func declareDraw(board: Board) {
    print(board)
    print("It's a draw.")
  }
  
  func declareAbnormalTermination(board: Board, cause: AbnormalTermination) {
    print(board)
    print("Player \(cause.symbol) quit.")
  }
  
  func systemError(error: Error) {
    print(error)
  }
}

protocol Player {
  var uuid: UUID { get }
  func makeMove(
    board: Board,
    symbol: Board.Symbol) throws -> Board.Position
}

class HumanPlayer: Player {
  private(set) var uuid: UUID = UUID()
  private let io: GameIO
  
  init(io: GameIO) { self.io = io }
  
  func makeMove(board: Board, symbol: Board.Symbol) throws -> Board.Position {
    return try io.getPosition(board: board, symbol: symbol)
  }
}


class Game {
  private let board: Board
  private let noughts, crosses: Player
  private var currentPlayer: Player
  private let io: GameIO
  
  private struct GameAnalytics {
    static func hasWon(board: Board, symbol: Board.Symbol) -> Bool {
      return board.hasSameSymbol(positions: Board.Position.topRow(), symbol: symbol) ||
        board.hasSameSymbol(positions: Board.Position.middleRow(), symbol: symbol) ||
        board.hasSameSymbol(positions: Board.Position.bottomRow(), symbol: symbol) ||
        board.hasSameSymbol(positions: Board.Position.leftColumn(), symbol: symbol) ||
        board.hasSameSymbol(positions: Board.Position.middleColumn(), symbol: symbol) ||
        board.hasSameSymbol(positions: Board.Position.rightColumn(), symbol: symbol) ||
        board.hasSameSymbol(positions: Board.Position.primaryDiagonal(), symbol: symbol) ||
        board.hasSameSymbol(positions: Board.Position.secondaryDiagonal(), symbol: symbol)
    }
    
    static func isDraw(board: Board) -> Bool {
      return board.isFull() &&
        !hasWon(board: board, symbol: .NOUGHT) &&
        !hasWon(board: board, symbol: .CROSS)
    }
    
    static func isGameOver(board: Board) -> Bool {
      return isDraw(board: board) ||
        hasWon(board: board, symbol: .NOUGHT) ||
        hasWon(board: board, symbol: .CROSS)
    }
    
    static func winner(board: Board) -> Board.Symbol? {
      return hasWon(board: board, symbol: .NOUGHT) ?
        .NOUGHT : hasWon(board: board, symbol: .CROSS) ? .CROSS : nil
    }
  }
  
  private func symbolFor(player: Player) -> Board.Symbol {
    return (player.uuid == noughts.uuid) ? .NOUGHT : .CROSS
  }
  
  private func declareResult(termination error: Error?) {
    if let error: Error = error {
      if let error = error as? AbnormalTermination  {
        io.declareAbnormalTermination(board: board, cause: error)
      } else {
        io.systemError(error: error)
      }
    } else if GameAnalytics.hasWon(board: board, symbol: .NOUGHT) {
        io.declareWinner(board: board, symbol: .NOUGHT)
    } else if GameAnalytics.hasWon(board: board, symbol: .CROSS) {
      io.declareWinner(board: board, symbol: .CROSS)
    } else {
      io.declareDraw(board: board)
    }
  }
  
  init(board: Board, noughts: Player, crosses: Player, io: GameIO) {
    self.board = board
    self.noughts = noughts
    self.crosses = crosses
    self.currentPlayer = noughts
    self.io = io
  }
  
  func play() {
    var termination: Error? = nil
    do {
      while !GameAnalytics.isGameOver(board: board) {
        let player = (currentPlayer.uuid == noughts.uuid) ? noughts : crosses
        let symbol = symbolFor(player: player)
        let position = try player.makeMove(board: board, symbol: symbol)
        board.placeMark(position: position, symbol: symbol)
        currentPlayer = (currentPlayer.uuid == noughts.uuid) ? crosses : noughts
      }
    } catch { termination = error }
    declareResult(termination: termination)
  }
  
  static func makeConsoleGameWithTwoHumanPlayers() -> Game {
    let io = ConsoleGameIO()
    let board = Board()
    let noughts = HumanPlayer(io: io)
    let crosses = HumanPlayer(io: io)
    return Game(board: board, noughts: noughts, crosses: crosses, io: io)
  }
}


enum TicTacToeApp {
  static func main() {
    Game.makeConsoleGameWithTwoHumanPlayers().play()
  }
}

TicTacToeApp.main()
