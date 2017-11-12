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
        assertEquals(6, game.getBestMove("-OXOXO-X-"));
        assertEquals(4, game.getBestMove("XO------X"));
        assertNotEquals(4, game.getBestMove("OOX---X-X"));
        assertEquals(7, game.getBestMove("OX--X----"));
    }

}
