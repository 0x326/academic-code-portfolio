import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TicTacToeInteraction {

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        TicTacToe game = new TicTacToe();

        List<BoardState> boardState;
        int userInput;
        displayBoard(game.getCurrentBoardState());
        while (game.getWinner() == null) {
            do {
                System.out.print("Where would you like to place an X? ");
                userInput = keyboard.nextInt();
            } while (!(0 < userInput && userInput <= 9));
            game.placeX(userInput);
            game.makeOpponentMove();
            boardState = game.getCurrentBoardState();
            displayBoard(boardState);
        }
        System.out.println(String.format("%s is the winner", game.getWinner()));
    }

    private static void displayBoard(List<BoardState> board) {
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
