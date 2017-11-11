import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Course:
 * Instructor:
 * <p>
 * Project 06
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

}
