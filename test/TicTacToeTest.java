import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Project 06
 * <p>
 * Course: CSE 274 F
 * Instructor: Dr. Gani
 *
 * @author John Meyer
 */
class TicTacToeTest {
    private TicTacToe game = new TicTacToe();

    @Test
    void getBestMove() {
        assertNotEquals(1, game.getBestMove("-OXOXO-X-") + 1);
        assertNotEquals(4, game.getBestMove("OOX---X-X") + 1);
    }

    @Test
    void findWinningMove() {

    }

}
