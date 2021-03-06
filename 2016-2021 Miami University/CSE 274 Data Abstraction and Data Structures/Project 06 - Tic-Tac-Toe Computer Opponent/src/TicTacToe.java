import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.AbstractMap;

/**
 * Project 06
 * <p>
 * Course: CSE 274 F
 * Instructor: Dr. Gani
 *
 * @author John Meyer
 */
public class TicTacToe {
    private ArrayList<BoardState> currentBoardState = new ArrayList<>(9);
    private AbstractMap<ArrayList<BoardState>, Integer> bestMoveDictionary = new HashMap<>();

    public TicTacToe() {
        // Initialize state
        for (int i = 0; i < 9; i++) {
            currentBoardState.add(null);
        }

        generatePossibleBoards(currentBoardState, bestMoveDictionary);
    }

    /**
     * Generates all possible boards and adds its best move to the dictionary.
     * <p>
     * Generates all possible states of a board, under the constraints of the given board
     * (ex: if the given board has an X at location 0, all possible boards must have an X at location 0).
     * For each possible board, its best move is computed and added to the dictionary.
     *
     * @param board              The board (serves as the constraint)
     * @param bestMoveDictionary The dictionary to which to add the best moves of each possible board
     */
    private static void generatePossibleBoards(ArrayList<BoardState> board,
                                               AbstractMap<ArrayList<BoardState>, Integer> bestMoveDictionary) {
        BoardState playerToMove = getTurn(board);
        for (int i = 0; i < board.size(); i++) {
            if (board.get(i) == null) {
                // Suppose the player were to move here
                board.set(i, playerToMove);

                // Boards which are already won are invalid - another move cannot be made
                if (!isBoardFull(board) && findWinner(board) == null) {
                    OptimalMove bestMove = computeBestMove(board);
                    //noinspection unchecked
                    bestMoveDictionary.put((ArrayList<BoardState>) board.clone(), bestMove.location);

                    // Recurse
                    generatePossibleBoards(board, bestMoveDictionary);
                }

                // Remove supposition
                board.set(i, null);
            }
        }
    }

    /**
     * Computes the best move given the board.
     *
     * @param board The board state
     * @return The best move
     */
    private static OptimalMove computeBestMove(List<BoardState> board) {
        BoardState playerToOptimize = getTurn(board);
        FutureGameEnd bestForeseeableGameEnd = null;
        int moveThatYieldsBestGameEnd = 0;

        for (int i = 0; i < board.size(); i++) {
            if (board.get(i) == null) {
                // Suppose the player were to move here
                board.set(i, playerToOptimize);
                // Compute time to winning move
                FutureGameEnd futureWin = predictGameEnding(board);

                // Decide whether to update best estimate
                if (isFutureBetter(bestForeseeableGameEnd, futureWin, playerToOptimize)) {

                    // Update best estimate of the future
                    bestForeseeableGameEnd = futureWin;
                    moveThatYieldsBestGameEnd = i;
                }

                // Remove supposition
                board.set(i, null);
            }
        }

        return new OptimalMove(moveThatYieldsBestGameEnd, bestForeseeableGameEnd);
    }

    /**
     * Determines whether a new future is better than an already discovered future.
     *
     * @param original      The already discovered future
     * @param newFuture     The new future
     * @param playerToFavor The player in whose favor this method should rule (usually the current player)
     * @return Whether the new future should replace the old
     */
    private static boolean isFutureBetter(FutureGameEnd original, FutureGameEnd newFuture, BoardState playerToFavor) {
        return (
            // newFuture can't be better if it doesn't exist
            (newFuture != null) && (
                // But anything is better than nothing
                (original == null) || ((
                    // Since they both exist, they need to be compared

                    // Do we already foresee a win?
                    (original.winner == playerToFavor) && (
                        // Does newFuture foresees a win?
                        (newFuture.winner == playerToFavor) &&
                            // Is the new win sooner than our win?
                            (newFuture.movesFromNow < original.movesFromNow))) || (

                    // Do we already foresee a scratch?
                    (original.winner == null) && (
                        // Does newFuture foresee a win?
                        (newFuture.winner == playerToFavor) || (

                        // Does newFuture foresee a scratch?
                        (newFuture.winner == null) &&
                            // Is the new scratch more postponed than our scratch?
                            (newFuture.movesFromNow > original.movesFromNow)))) || (

                    // Do we already foresee a loss?
                    (original.winner != playerToFavor && original.winner != null) && (
                        // Is newFuture a win or a scratch?
                        (newFuture.winner == playerToFavor || newFuture.winner == null) || (
                            // Or, since newFuture foresees a loss (true by elimination),
                            // is the new loss later than our loss?
                            (newFuture.movesFromNow > original.movesFromNow))))))
        );
    }

    /**
     * Computes the end of the game in advance.
     * <p>
     * This method acts as an oracle.
     * It uses recursion to foresee who will win the game, given its current state,
     * assuming the next player makes the best possible move.
     *
     * @param board The board
     * @return The foreseen game ending
     */
    private static FutureGameEnd predictGameEnding(List<BoardState> board) {
        BoardState winner = findWinner(board);
        WinningMove winningMove = findWinningMove(board);
        if (isBoardFull(board)) {
            // The game is a scratch
            return new FutureGameEnd(null, 0);
        } else if (winner != null) {
            // The game is already won
            return new FutureGameEnd(winner, 0);
        } else if (winningMove != null && winningMove.player == getTurn(board)) {
            // The current player is just about to win the game
            return new FutureGameEnd(winningMove.player, 1);
        } else {
            // Suppose the next player takes the best move
            // Since the player takes the best move,
            // the future of this game is equal to that on which the best move is calculated
            OptimalMove optimalMove = computeBestMove(board);
            FutureGameEnd gameEnd = optimalMove.future;
            gameEnd.movesFromNow++;
            return gameEnd;
        }
    }

    /**
     * Computes whether the board is full.
     *
     * @param board The board
     * @return Whether it is full
     */
    private static boolean isBoardFull(List<BoardState> board) {
        for (BoardState state : board) {
            if (state == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Computes whose turn it is by examining the state of the board.
     *
     * @param board The board
     * @return The player who is to go next
     */
    private static BoardState getTurn(List<BoardState> board) {
        int numberOfXs = 0;
        int numberOfOs = 0;
        for (BoardState state : board) {
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

    /**
     * Searches for a move that immediately results in a finished game.
     *
     * @param board The board
     * @return The winning move.  Null if not immediately available.
     */
    private static WinningMove findWinningMove(List<BoardState> board) {
        Integer moveLocation = null;
        BoardState winningParty = null;
        // Check rows
        for (int row = 0; moveLocation == null && row < 3; row++) {
            if (board.get(3 * row + 1) != null && board.get(3 * row + 1) == board.get(3 * row + 2) && board.get(3 * row) == null) {
                moveLocation = 3 * row;
                winningParty = board.get(3 * row + 1);
            } else if (board.get(3 * row) != null && board.get(3 * row) == board.get(3 * row + 2) && board.get(3 * row + 1) == null) {
                moveLocation = 3 * row + 1;
                winningParty = board.get(3 * row);
            } else if (board.get(3 * row) != null && board.get(3 * row) == board.get(3 * row + 1) && board.get(3 * row + 2) == null) {
                moveLocation = 3 * row + 2;
                winningParty = board.get(3 * row);
            }
        }
        // Check columns
        for (int column = 0; moveLocation == null && column < 3; column++) {
            if (board.get(column + 3) != null && board.get(column + 3) == board.get(column + 6) && board.get(column) == null) {
                moveLocation = column;
                winningParty = board.get(column + 3);
            } else if (board.get(column) != null && board.get(column) == board.get(column + 6) && board.get(column + 3) == null) {
                moveLocation = column + 3;
                winningParty = board.get(column);
            } else if (board.get(column) != null && board.get(column) == board.get(column + 3) && board.get(column + 6) == null) {
                moveLocation = column + 6;
                winningParty = board.get(column);
            }
        }
        // Check forward diagonals
        if (board.get(4) != null && board.get(4) == board.get(8) && board.get(0) == null) {
            moveLocation = 0;
            winningParty = board.get(4);
        } else if (board.get(0) != null && board.get(0) == board.get(8) && board.get(4) == null) {
            moveLocation = 4;
            winningParty = board.get(0);
        } else if (board.get(0) != null && board.get(0) == board.get(4) && board.get(8) == null) {
            moveLocation = 8;
            winningParty = board.get(0);
        }
        // Check backward diagonals
        else if (board.get(4) != null && board.get(4) == board.get(6) && board.get(2) == null) {
            moveLocation = 2;
            winningParty = board.get(4);
        } else if (board.get(2) != null && board.get(2) == board.get(6) && board.get(4) == null) {
            moveLocation = 4;
            winningParty = board.get(2);
        } else if (board.get(2) != null && board.get(2) == board.get(4) && board.get(6) == null) {
            moveLocation = 6;
            winningParty = board.get(2);
        }

        if (moveLocation == null) {
            // There is no immediate win
            return null;
        } else {
            return new WinningMove(moveLocation, winningParty);
        }
    }

    /**
     * Computes the winner of the game.
     *
     * @param board The board
     * @return The winner, or null if there is no winner.
     */
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

    /**
     * Converts a board from the String representation to its internal List representation
     *
     * @param stringBoard The String representation to parse
     * @return The List representation
     */
    private static ArrayList<BoardState> toBoard(String stringBoard) {
        ArrayList<BoardState> board = new ArrayList<>(9);
        for (char character : stringBoard.toCharArray()) {
            switch (character) {
                case 'X':
                    board.add(BoardState.X);
                    break;
                case 'O':
                    board.add(BoardState.O);
                    break;
                case '-':
                    board.add(null);
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Unrecognized character in string %s", character));
            }
        }
        return board;
    }

    public List<BoardState> getCurrentBoardState() {
        return new ArrayList<>(currentBoardState);
    }

    public WinState getWinner() {
        BoardState winner = findWinner(currentBoardState);
        if (winner == null) {
            return isBoardFull(currentBoardState) ? WinState.SCRATCH : null;
        }
        switch (winner) {
            case X:
                return WinState.X;
            case O:
                return WinState.O;
            default:
                return null;
        }
    }

    /**
     * Places the given mark at the given location.
     *
     * @param position The position at which to add the mark
     * @param mark     The mark to add
     */
    private void placeMark(int position, BoardState mark) {
        if (!(0 <= position && position < 9) || currentBoardState.get(position) != null) {
            throw new IllegalArgumentException();
        } else if (getTurn(currentBoardState) != mark) {
            throw new IllegalStateException(String.format("It's not %s's turn to go", mark));
        }

        currentBoardState.set(position, mark);
    }

    /**
     * Places an X at the given location.  Ought to be called on behalf of the user.
     *
     * @param position The position at which to add [1, 9]
     */
    public void placeX(int position) {
        placeMark(position, BoardState.X);
    }

    /**
     * Prompts the "computer" to take his turn.
     * Uses a dictionary to lookup the best move.
     */
    public void makeOpponentMove() {
        int bestMovePosition = bestMoveDictionary.get(currentBoardState);
        if (currentBoardState.get(bestMovePosition) != null) {
            throw new RuntimeException("Dictionary is not properly constructed");
        }
        placeMark(bestMovePosition, BoardState.O);
    }

    /**
     * A public method for testing purposes.  Accepts a String of length 9 consisting of 'X', 'O', or '-' characters.
     *
     * @param board A String representation of a board
     * @return The position of the best move [0, 9)
     */
    public int getBestMove(String board) {
        if (board.length() != 9) {
            throw new IllegalArgumentException("Board must have 9 spaces");
        }

        return bestMoveDictionary.get(toBoard(board));
    }

    /**
     * Represents a foreseeable moment when a player will win the game.
     * Denotes winner as well as time.
     */
    private static class FutureGameEnd {
        /**
         * The player foreseen to win. Null if a scratch is foreseen
         */
        BoardState winner;

        /**
         * The time at which he is to win
         */
        int movesFromNow;

        FutureGameEnd(BoardState winner, int movesFromNow) {
            this.winner = winner;
            this.movesFromNow = movesFromNow;
        }

        @Override
        public String toString() {
            return String.format("%s wins in %d moves", winner, movesFromNow);
        }
    }

    /**
     * Represents a move that immediately causes a player to win the game.
     */
    private static class WinningMove {
        /**
         * Location of a the winning move
         */
        int location;

        /**
         * The player that wins the game as a result of this move
         */
        BoardState player;

        WinningMove(int location, BoardState player) {
            this.location = location;
            this.player = player;
        }

        @Override
        public String toString() {
            return String.format("%s wins if he marks %s", player, location);
        }
    }

    /**
     * Represents an optimal move.
     * Optimality can be compared by examining the future it is calculated upon.
     */
    private static class OptimalMove {
        /**
         * The location of where the player should move
         */
        int location;

        /**
         * The best possible future foreseen if this move is made
         */
        FutureGameEnd future;

        OptimalMove(int location, FutureGameEnd future) {
            this.location = location;
            this.future = future;
        }

        @Override
        public String toString() {
            return String.format("Mark %d for %s win in %d moves", location, future.winner, future.movesFromNow);
        }
    }
}
