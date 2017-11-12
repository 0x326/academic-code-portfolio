import java.util.*;

/**
 * Project 06
 * <p>
 * Course: CSE 274 F
 * Instructor: Dr. Gani
 *
 * @author John Meyer
 */
public class TicTacToeInteraction {

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        TicTacToe game = new TicTacToe();

        Iterator<ArrayList<BoardState>> bestMoveDictionaryKeyIterator = game.getBestMoveDictionary().getKeyIterator();
        while (bestMoveDictionaryKeyIterator.hasNext()) {
            ArrayList<BoardState> key = bestMoveDictionaryKeyIterator.next();
            displayBoard(key);
            System.out.println(game.getBestMoveDictionary().getValue(key) + 1);
            System.out.println();
        }

        System.out.println("The board is numbered as follows:");
        displayBoard(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        System.out.println("-----\n");

        List<BoardState> boardState;
        int userInput;
        displayBoard(game.getCurrentBoardState());
        while (true) {
            do {
                System.out.print("Where would you like to place an X? ");
                userInput = keyboard.nextInt();
            } while (!(0 < userInput && userInput <= 9));

            try {
                game.placeX(userInput);
                if (game.getWinner() != null) {
                    break;
                }

                game.makeOpponentMove();
                if (game.getWinner() != null) {
                    break;
                }
            } catch (IllegalArgumentException error) {
                System.out.println(String.format("There is already a mark at position %d", userInput));
            }

            boardState = game.getCurrentBoardState();
            displayBoard(boardState);
        }
        boardState = game.getCurrentBoardState();
        displayBoard(boardState);
        WinState winner = game.getWinner();
        if (winner != WinState.SCRATCH) {
            System.out.println(String.format("%s is the winner", game.getWinner()));
        } else {
            System.out.println("Scratch game");
        }
    }

    private static void displayBoard(List board) {
        for (int i = 0; i < board.size(); i++) {
            if (board.get(i) == null) {
                System.out.print("_ ");
            } else {
                System.out.print(board.get(i) + " ");
            }
            if (i % 3 == 2) {
                System.out.println();
            }
        }
    }
}
