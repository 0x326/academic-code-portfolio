import java.util.ArrayList;
import java.util.List;

/**
 * Course:
 * Instructor:
 * <p>
 * Project 06
 *
 * @author John Meyer
 */
public class TicTacToe {
    private ArrayList<BoardState> currentBoardState = new ArrayList<>(9);
    private DictionaryInterface<ArrayList<BoardState>, Integer> bestMoveDictionary = new HashedDictionary2<ArrayList<BoardState>,Integer>();

    public DictionaryInterface<ArrayList<BoardState>, Integer> getBestMoveDictionary() {
        return bestMoveDictionary;
    }

    public TicTacToe() {
        // Initialize state
        for (int i = 0; i < 9; i++) {
            currentBoardState.add(null);
        }

        generateBoards(currentBoardState);
    }

    private void generateBoards(ArrayList<BoardState> board) {
        BoardState playerToMove = getTurn(board);
        for (int i = 0; i < board.size(); i++) {
            if (board.get(i) == null) {
                // Suppose the player were to move here
                board.set(i, playerToMove);

                if (findWinner(board) == null) {
                    OptimalMove bestMove = computeBestMove(board);
                    if (bestMove != null) {
                        //noinspection unchecked
                        bestMoveDictionary.add((ArrayList<BoardState>) board.clone(), bestMove.location);
                    }

                    generateBoards(board);
                }

                // Remove supposition
                board.set(i, null);
            }
        }
    }

    private OptimalMove computeBestMove(List<BoardState> board) {
        BoardState playerToOptimize = getTurn(board);
        FutureGameEnd bestForeseeableGameEnd = null;
        int moveThatYieldsMinimumTime = 0;

        for (int i = 0; i < board.size(); i++) {
            if (board.get(i) == null) {
                // Suppose the player were to move here
                board.set(i, playerToOptimize);
                // Compute time to winning move
                FutureGameEnd futureWin = predictGameEnding(board);

                // Decide whether to update best estimate
                if (futureWin != null &&
                    (bestForeseeableGameEnd == null ||
                        // We found a win
                        (futureWin.winner == playerToOptimize &&
                            // This is a better win than what we knew before
                            (futureWin.movesFromNow < bestForeseeableGameEnd.movesFromNow ||
                                // Or, it's a win when we thought we were doomed to lose
                                bestForeseeableGameEnd.winner != futureWin.winner)) ||
                        // We found a loss
                        (futureWin.winner != playerToOptimize &&
                            // And, we don't know of any way to win
                            bestForeseeableGameEnd.winner != playerToOptimize &&
                            // Is this loss more postponed than the one we knew about?
                            futureWin.movesFromNow > bestForeseeableGameEnd.movesFromNow))) {

                    // Update best estimate
                    bestForeseeableGameEnd = futureWin;
                    moveThatYieldsMinimumTime = i;
                }

                // Remove supposition
                board.set(i, null);
            }
        }

        if (bestForeseeableGameEnd != null) {
            return new OptimalMove(moveThatYieldsMinimumTime, bestForeseeableGameEnd);
        } else {
            return null;
        }
    }

    private FutureGameEnd predictGameEnding(List<BoardState> board) {
        BoardState winner = findWinner(board);
        Integer winningMove = findWinningMove(board);
        if (winner != null) {
            return new FutureGameEnd(winner, 0);
        } else if (winningMove != null) {
            return new FutureGameEnd(getTurn(board), 1);
        } else {
            OptimalMove optimalMove = computeBestMove(board);
            if (optimalMove != null) {
                FutureGameEnd gameEnd = optimalMove.future;
                gameEnd.movesFromNow++;
                return gameEnd;
            } else {
                return null;
            }
        }
    }

    private class FutureGameEnd {
        BoardState winner;
        int movesFromNow;

        FutureGameEnd(BoardState winner, int movesFromNow) {
            this.winner = winner;
            this.movesFromNow = movesFromNow;
        }
    }

    private class OptimalMove {
        int location;
        FutureGameEnd future;

        public OptimalMove(int location, FutureGameEnd future) {
            this.location = location;
            this.future = future;
        }
    }

    private static boolean isBoardFull(List<BoardState> board) {
        for (BoardState state:
             board) {
            if (state == null) {
                return false;
            }
        }
        return true;
    }

    private static BoardState getTurn(List<BoardState> board) {
        int numberOfXs = 0;
        int numberOfOs = 0;
        for (BoardState state:
             board) {
            if (state == BoardState.X) {
                numberOfXs++;
            } else if (state == BoardState.O) {
                numberOfOs++;
            }
        }
        if (numberOfXs - numberOfOs != 0 && numberOfXs - numberOfOs != 1) {
            throw new IllegalStateException();
        }
        return numberOfXs == numberOfOs ? BoardState.X : BoardState.O;
    }

    private Integer findWinningMove(List<BoardState> board) {
        // Check rows
        for (int row = 0; row < 3; row++) {
            if (board.get(3 * row + 1) != null && board.get(3 * row + 1) == board.get(3 * row + 2) && board.get(3 * row) == null) {
                return 3 * row;
            } else if (board.get(3 * row) != null && board.get(3 * row) == board.get(3 * row + 2) && board.get(3 * row + 1) == null) {
                return 3 * row + 1;
            } else if (board.get(3 * row) != null && board.get(3 * row) == board.get(3 * row + 1) && board.get(3 * row + 2) == null) {
                return 3 * row + 2;
            }
        }
        // Check columns
        for (int column = 0; column < 3; column++) {
            if (board.get(column + 3) != null && board.get(column + 3) == board.get(column + 6) && board.get(column) == null) {
                return column;
            } else if (board.get(column) != null && board.get(column) == board.get(column + 6) && board.get(column + 3) == null) {
                return column + 3;
            } else if (board.get(column) != null && board.get(column) == board.get(column + 3) && board.get(column + 6) == null) {
                return column + 6;
            }
        }
        // Check forward diagonals
        if (board.get(4) != null && board.get(4) == board.get(8) && board.get(0) == null) {
            return 0;
        } else if (board.get(0) != null && board.get(0) == board.get(8) && board.get(4) == null) {
            return 4;
        } else if (board.get(0) != null && board.get(0) == board.get(4) && board.get(8) == null) {
            return 8;
        }
        // Check backward diagonals
        if (board.get(4) != null && board.get(4) == board.get(6) && board.get(2) == null) {
            return 2;
        } else if (board.get(2) != null && board.get(2) == board.get(6) && board.get(4) == null) {
            return 4;
        } else if (board.get(2) != null && board.get(2) == board.get(4) && board.get(6) == null) {
            return 6;
        }
        // There is no immediate win
        return null;
    }

    private static BoardState findWinner(List<BoardState> board) {
        // Check rows
        for (int row = 0; row < 3; row++) {
            if (board.get(3 * row) != null &&
                board.get(3 * row) == board.get(3 * row + 1) &&
                board.get(3 * row + 1) == board.get(3 * row + 2)) {
                return board.get(3 * row);
            }
        }
        // Check columns
        for (int column = 0; column < 3; column++) {
            if (board.get(column) != null &&
                board.get(column) == board.get(column + 3) &&
                board.get(column + 3) == board.get(column + 6)) {
                return board.get(column);
            }
        }
        // Check diagonals
        if (board.get(0) != null &&
            board.get(0) == board.get(4) &&
            board.get(4) == board.get(8)) {
            return board.get(4);
        } else if (board.get(2) != null &&
            board.get(2) == board.get(4) &&
            board.get(4) == board.get(6)) {
            return board.get(4);
        }
        return null;
    }

    public List<BoardState> getCurrentBoardState() {
        return new ArrayList<>(currentBoardState);
    }

    public BoardState getWinner() {
        return findWinner(currentBoardState);
    }

    public void placeX(int position) {
        if (!(0 < position && position <= 9) || currentBoardState.get(position - 1) != null) {
            throw new IllegalArgumentException();
        }
        currentBoardState.set(position - 1, BoardState.X);
    }

    public void makeOpponentMove() {
        int bestMovePosition = bestMoveDictionary.getValue(currentBoardState);
        if (currentBoardState.get(bestMovePosition) != null) {
            throw new RuntimeException("Dictionary is not properly constructed");
        }
        currentBoardState.set(bestMovePosition, BoardState.O);
    }

    public int getBestMove(String board) {
        if (board.length() != 9) {
            throw new IllegalArgumentException("Board must have 9 spaces");
        }

        ArrayList<BoardState> listBoard = new ArrayList<>(9);
        for (char character : board.toCharArray()) {
            switch (character) {
                case 'X':
                    listBoard.add(BoardState.X);
                    break;
                case 'O':
                    listBoard.add(BoardState.O);
                    break;
                case '-':
                    listBoard.add(null);
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Unrecognized character in string %s", character));
            }
        }

        return bestMoveDictionary.getValue(listBoard);
    }
}
