package edu.miamioh.meyerjm;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;

// Created by John Meyer on 4/22/2017.
// CSE 271 F
// Dr. Angel Bravo
// Project2

/**
 * A JFrame for an N-Queens puzzle
 */
public class NQueensProblemFrame extends JFrame {
    // Define instance variables
    private final Color WARNING_COLOR = Color.RED;
    private final Color TIP_COLOR = Color.YELLOW;
    private int lengthOfChessBoard;
    private int numOfQueensOnBoard = 0;
    private ChessSquare[][] chessSquares;
    private BufferedImage queenImage;
    private JButton tipButton;
    private JButton smartTipButton;
    private ArrayList<Point> smartTipSequence = null;
    private boolean[][] smartTipBoard;
    private JLabel outputLabel;
    private enum BacktrackingReturnType {
        Accepted,
        Possible,
        Abandoned
    }

    /**
     * Creates a ChessSquare object and listeners
     * @param row The row in which the square will be placed
     * @param col The column in which the sqaure will be placed
     * @return The ChessSqare object
     */
    private ChessSquare createSquare(int row, int col) {
        boolean isColoredSquare = ((row & 1) ^ (col & 1)) == 1;
        Color backgroundColor = isColoredSquare ? Color.GRAY : Color.WHITE;
        final ChessSquare square;
        if (queenImage != null) {
            square = new ChessSquare(backgroundColor, queenImage);
        }
        else {
            square = new ChessSquare(backgroundColor, Color.BLUE);
        }
        class ClickListener implements MouseListener {
            public void mouseClicked(MouseEvent e) {
                numOfQueensOnBoard += !square.hasQueen() ? 1 : -1;
                if (numOfQueensOnBoard > lengthOfChessBoard) {
                    // Undo operation
                    numOfQueensOnBoard--;
                }
                else {
                    square.setQueen(!square.hasQueen());
                    square.setHighlightColor(null);
                    square.repaint();
                }
                tipButton.setEnabled(numOfQueensOnBoard < lengthOfChessBoard);
                smartTipButton.setEnabled(numOfQueensOnBoard < lengthOfChessBoard);
            }

            public void mousePressed(MouseEvent e) {
                // Do nothing
            }

            public void mouseReleased(MouseEvent e) {
                // Do nothing
            }

            public void mouseEntered(MouseEvent e) {
                // Do nothing
            }

            public void mouseExited(MouseEvent e) {
                // Do nothing
            }
        }
        square.addMouseListener(new ClickListener());
        return square;
    } // end method createSquare

    /**
     * Creates a solution checking button
     * @return The JButton
     */
    private JButton createCheckButton() {
        JButton button = new JButton("Check Solution");
        class ClickListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                // Check solution
                Object[] result = checkSolution(chessSquares);
                if (result.length >= 1 && result[0] instanceof BacktrackingReturnType) {
                    BacktrackingReturnType solutionStatus = (BacktrackingReturnType) result[0];
                    // Display results
                    clearHighlights();
                    switch (solutionStatus) {
                        case Accepted:
                            outputLabel.setText("Great job! This is a complete solution to the problem.");
                            break;
                        case Possible:
                            outputLabel.setText("Good work! This solution works so far but is not yet complete.");
                            break;
                        case Abandoned:
                            if (result.length >= 5 && result[1] instanceof Integer && result[2] instanceof Integer) {
                                int problemRow = (Integer) result[1];
                                int problemCol = (Integer) result[2];
                                int problemRow2 = (Integer) result[3];
                                int problemCol2 = (Integer) result[4];
                                outputLabel.setText(MessageFormat.format(
                                    "Not quite. It looks like {1}{0} can attack {3}{2}",
                                    lengthOfChessBoard - problemRow, (char) ('a' + problemCol),
                                    lengthOfChessBoard - problemRow2, (char) ('a' + problemCol2)));
                                chessSquares[problemRow][problemCol].setHighlightColor(WARNING_COLOR);
                                chessSquares[problemRow2][problemCol2].setHighlightColor(WARNING_COLOR);
                            }
                            break;
                    }
                }
            }
        }
        button.addActionListener(new ClickListener());
        return button;
    } // end method createCheckButton

    /**
     * Clears all the highlight on all ChessSquares
     */
    private void clearHighlights() {
        for (ChessSquare[] row : chessSquares) {
            for (ChessSquare square : row) {
                square.setHighlightColor(null);
            }
        }
    } // end method clearHighlights

    /**
     * Creates a tip button
     * @return The JButton
     */
    private JButton createTipButton() {
        JButton button = new JButton("Tip");
        class ClickListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                // Get tip
                Point suggestion = blindTip(chessSquares);
                // Display tip
                clearHighlights();
                if (suggestion != null) {
                    chessSquares[suggestion.x][suggestion.y].setHighlightColor(TIP_COLOR);
                }
                else {
                    outputLabel.setText("I don't have any tips. Have you tried checking your solution?");
                }
            }
        }
        button.addActionListener(new ClickListener());
        return button;
    } // end method createTipButton

    /**
     * Creates a button for a smart tip
     * @return The JButton
     */
    private JButton createSmartTipButton() {
        JButton button = new JButton("Smart Tip");
        class ClickListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                // Check to see whether the last tip sequence is still applicable
                boolean isApplicable = smartTipSequence != null && smartTipSequence.size() > 0;
                // See if there are any queens in spaces not recommended by the tip sequence
                for (int row = 0; isApplicable && row < chessSquares.length; row++) {
                    for (int col = 0; isApplicable && col < chessSquares[row].length; col++) {
                        if (chessSquares[row][col].hasQueen()) {
                            // Search sequence
                            boolean isChessSquareFoundInSequence = false;
                            for (Point tip : smartTipSequence) {
                                if (tip.x == row && tip.y == col) {
                                    isChessSquareFoundInSequence = true;
                                    break;
                                }
                            }
                            isApplicable = isChessSquareFoundInSequence;
                        }
                    }
                }
                // Check to see if any queens were removed
                for (int row = 0; isApplicable && row < smartTipBoard.length; row++) {
                    for (int col = 0; isApplicable && col < smartTipBoard[row].length; col++) {
                        if (!chessSquares[row][col].hasQueen() && smartTipBoard[row][col]) {
                            // A queen has been removed. Better tips may now exist
                            isApplicable = false;
                        }
                    }
                }
                // If it is not applicable, generate a new tip sequence
                if (!isApplicable) {
                    smartTipSequence = smartTip(chessSquares);
                    // Remember chessSquares for next time
                    for (int row = 0; row < chessSquares.length; row++) {
                        for (int col = 0; col < chessSquares[row].length; col++) {
                            smartTipBoard[row][col] = chessSquares[row][col].hasQueen();
                        }
                    }
                }
                // Display tip
                clearHighlights();
                boolean didDisplayTip = false;
                for (Point tip : smartTipSequence) {
                    if (!chessSquares[tip.x][tip.y].hasQueen()) {
                        chessSquares[tip.x][tip.y].setHighlightColor(TIP_COLOR);
                        didDisplayTip = true;
                        break;
                    }
                }
                if (!didDisplayTip) {
                    outputLabel.setText("I don't have any smart tips. Have you tried checking your solution?");
                }
            }
        }
        button.addActionListener(new ClickListener());
        return button;
    } // end method createSmartTipButton

    /**
     * Creates an NQueensProblemFrame object
     * @param lengthOfChessBoard The length of the chess board's sides. (Typically 8)
     * @param queenFile An image when a queen is placed on a ChessSquare. Can be null
     */
    public NQueensProblemFrame(int lengthOfChessBoard, File queenFile) {
        this.lengthOfChessBoard = lengthOfChessBoard;
        // Read queen image file
        if (queenFile != null) {
            try {
                this.queenImage = ImageIO.read(queenFile);
            } catch (IOException e) {
                this.queenImage = null;
            }
        }
        else {
            this.queenImage = null;
        }

        // Create chess board layout
        JPanel chessGridPanel = new JPanel(new GridLayout(lengthOfChessBoard + 1, lengthOfChessBoard + 1));
        // Create column labels
        this.chessSquares = new ChessSquare[lengthOfChessBoard][lengthOfChessBoard];
        this.smartTipBoard = new boolean[lengthOfChessBoard][lengthOfChessBoard];
        chessGridPanel.add(new JLabel(" "));
        for (int col = 0; col < lengthOfChessBoard; col++) {
            chessGridPanel.add(new JLabel("" + (char) ('a' + col)));
        }
        // Create rows and labels
        for (int row = 0; row < this.chessSquares.length; row++) {
            chessGridPanel.add(new JLabel("" + (lengthOfChessBoard - row)));
            for (int col = 0; col < this.chessSquares[row].length; col++) {
                this.chessSquares[row][col] = createSquare(row, col);
                chessGridPanel.add(this.chessSquares[row][col]);
            }
        }

        // Create button layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createCheckButton());
        this.tipButton = createTipButton();
        buttonPanel.add(this.tipButton);
        this.smartTipButton = createSmartTipButton();
        buttonPanel.add(this.smartTipButton);
        // Create output label
        this.outputLabel = new JLabel("");
        // Finalize layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel buttonLabelPanel = new JPanel(new GridLayout(2, 1));
        buttonLabelPanel.add(buttonPanel);
        buttonLabelPanel.add(this.outputLabel);
        mainPanel.add(chessGridPanel, BorderLayout.CENTER);
        mainPanel.add(buttonLabelPanel, BorderLayout.SOUTH);
        add(mainPanel);
        pack();
    } // end constructor NQueensProblemFrame

    /**
     * Checks whether a given solution is complete, partially complete, or wrong.
     * @param chessSquares The solution to check
     * @return The status of the solution
     */
    private Object[] checkSolution(ChessSquare[][] chessSquares) {
        // Sanity check to ensure chess board has at least one square
        if (chessSquares.length == 0 || chessSquares[0].length == 0) {
            return new Object[] { BacktrackingReturnType.Accepted };
        }
        // Check rows for endangered queens
        for (int row = 0; row < chessSquares.length; row++) {
            boolean rowHasQueen = false;
            int colOfFirstQueen = 0;
            for (int col = 0; col < chessSquares[row].length; col++) {
                if (chessSquares[row][col].hasQueen()) {
                    if (rowHasQueen) {
                        return new Object[] { BacktrackingReturnType.Abandoned, row, col , row, colOfFirstQueen };
                    } else {
                        rowHasQueen = true;
                        colOfFirstQueen = col;
                    }
                }
            }
        }
        // Check columns for endangered queens
        boolean[] colHasQueen = new boolean[lengthOfChessBoard];
        int[] rowOfFirstQueens = new int[lengthOfChessBoard];
        for (int row = 0; row < chessSquares.length; row++) {
            for (int col = 0; col < chessSquares[row].length; col++) {
                if (chessSquares[row][col].hasQueen()) {
                    if (colHasQueen[col]) {
                        return new Object[] { BacktrackingReturnType.Abandoned, row, col, rowOfFirstQueens[col], col };
                    } else {
                        colHasQueen[col] = true;
                        rowOfFirstQueens[col] = row;
                    }
                }
            }
        }
        // Check back-slash diagonals for endangered queens
        for (int startingRow = 0, startingCol = 0; startingRow < chessSquares.length; startingRow++) {
            boolean diagonalHasQueen = false;
            int rowOfFirstQueen = 0;
            int colOfFirstQueen = 0;
            for (int searchingRow = startingRow, searchingCol = startingCol;
                 searchingRow < chessSquares.length && searchingCol < chessSquares[searchingRow].length;
                 searchingRow++, searchingCol++) {
                if (chessSquares[searchingRow][searchingCol].hasQueen()) {
                    if (diagonalHasQueen) {
                        return new Object[] { BacktrackingReturnType.Abandoned, searchingRow, searchingCol, rowOfFirstQueen, colOfFirstQueen };
                    }
                    else {
                        diagonalHasQueen = true;
                        rowOfFirstQueen = searchingRow;
                        colOfFirstQueen = searchingCol;
                    }
                }
            }
        }
        for (int startingRow = 0, startingCol = 0; startingCol < chessSquares[startingRow].length; startingCol++) {
            boolean diagonalHasQueen = false;
            int rowOfFirstQueen = 0;
            int colOfFirstQueen = 0;
            for (int searchingRow = startingRow, searchingCol = startingCol;
                 searchingRow < chessSquares.length && searchingCol < chessSquares[searchingRow].length;
                 searchingRow++, searchingCol++) {
                if (chessSquares[searchingRow][searchingCol].hasQueen()) {
                    if (diagonalHasQueen) {
                        return new Object[] { BacktrackingReturnType.Abandoned, searchingRow, searchingCol, rowOfFirstQueen, colOfFirstQueen };
                    }
                    else {
                        diagonalHasQueen = true;
                        rowOfFirstQueen = searchingRow;
                        colOfFirstQueen = searchingCol;
                    }
                }
            }
        }
        // Check forward-slash diagonals for endangered queens
        for (int startingRow = chessSquares.length - 1, startingCol = 0; startingRow >= 0; startingRow--) {
            boolean diagonalHasQueen = false;
            int rowOfFirstQueen = 0;
            int colOfFirstQueen = 0;
            for (int searchingRow = startingRow, searchingCol = startingCol;
                 searchingRow >= 0 && searchingCol < chessSquares[searchingRow].length;
                 searchingRow--, searchingCol++) {
                if (chessSquares[searchingRow][searchingCol].hasQueen()) {
                    if (diagonalHasQueen) {
                        return new Object[] { BacktrackingReturnType.Abandoned, searchingRow, searchingCol, rowOfFirstQueen, colOfFirstQueen };
                    }
                    else {
                        diagonalHasQueen = true;
                        rowOfFirstQueen = searchingRow;
                        colOfFirstQueen = searchingCol;
                    }
                }
            }
        }
        for (int startingRow = chessSquares.length - 1, startingCol = 0; startingCol < chessSquares[startingRow].length;
             startingCol++) {
            boolean diagonalHasQueen = false;
            int rowOfFirstQueen = 0;
            int colOfFirstQueen = 0;
            for (int searchingRow = startingRow, searchingCol = startingCol;
                 searchingRow >= 0 && searchingCol < chessSquares[searchingRow].length;
                 searchingRow--, searchingCol++) {
                if (chessSquares[searchingRow][searchingCol].hasQueen()) {
                    if (diagonalHasQueen) {
                        return new Object[] { BacktrackingReturnType.Abandoned, searchingRow, searchingCol, rowOfFirstQueen, colOfFirstQueen };
                    }
                    else {
                        diagonalHasQueen = true;
                        rowOfFirstQueen = searchingRow;
                        colOfFirstQueen = searchingCol;
                    }
                }
            }
        }
        // This solution is not invalid
        // Determine whether it is a partial or complete solution by counting queens
        int tallyOfQueens = 0;
        for (ChessSquare[] row : chessSquares) {
            for (ChessSquare square : row) {
                if (square.hasQueen()) {
                    tallyOfQueens++;
                }
            }
        }
        if (tallyOfQueens < lengthOfChessBoard) {
            return new Object[] { BacktrackingReturnType.Possible };
        }
        else {
            return new Object[] { BacktrackingReturnType.Accepted };
        }
    }

    /**
     * Gives a tip based on the given partial solution. This tip is 'blind' as it does not look ahead to see if
     * the tip leads toward a possible full solution.
     * @param chessSquares The partial solution about which to give a tip
     * @return A Point where the tip suggests where the user should place a queen
     */
    private Point blindTip(ChessSquare[][] chessSquares) {
        // Search for a square where the user could validly place a queen
        for (int row = 0; row < chessSquares.length; row++) {
            for (int col = 0; col < chessSquares[row].length; col++) {
                ChessSquare square = chessSquares[row][col];
                if (!square.hasQueen()) {
                    square.setQueen(true);
                    Object[] solutionCheckArr = checkSolution(chessSquares);
                    square.setQueen(false);
                    if (solutionCheckArr.length > 0 && solutionCheckArr[0] instanceof BacktrackingReturnType) {
                        BacktrackingReturnType returnVal = (BacktrackingReturnType) solutionCheckArr[0];
                        if (returnVal != BacktrackingReturnType.Abandoned) {
                            return new Point(row, col);
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Gives a tip based on the given partial solution. This tip is 'smart' as it is chosen from the sequence which
     * gets the user as close to a full solution as possible.
     * @param chessSquares The partial solution for which to generate a smart tip
     * @return A sequence of tips towards the full solution.
     * It can be reused as long as the user does not deviate from it
     */
    private ArrayList<Point> smartTip(ChessSquare[][] chessSquares) {
        ArrayList<Point> suggestedSequence = new ArrayList<Point>();
        // Evaluate present partial solution
        Object[] solutionCheckArr = checkSolution(chessSquares);
        if (solutionCheckArr.length >= 1 && solutionCheckArr[0] instanceof BacktrackingReturnType) {
            BacktrackingReturnType solutionStatus = (BacktrackingReturnType) solutionCheckArr[0];
            if (solutionStatus != BacktrackingReturnType.Abandoned) {
                // If solution is not invalid, store it as a potential tip sequence
                for (int row = 0; row < chessSquares.length; row++) {
                    for (int col = 0; col < chessSquares[row].length; col++) {
                        if (chessSquares[row][col].hasQueen()) {
                            suggestedSequence.add(new Point(row, col));
                        }
                    }
                }
                // Try to find a better solution
                for (ChessSquare[] row : chessSquares) {
                    for (ChessSquare square : row) {
                        if (!square.hasQueen()) {
                            square.setQueen(true);
                            ArrayList<Point> newSuggestedSequence = smartTip(chessSquares);
                            // Replace the suggested sequence with the new one if it has more queens
                            if (newSuggestedSequence.size() > suggestedSequence.size()) {
                                suggestedSequence = newSuggestedSequence;
                            }
                            square.setQueen(false);
                        }
                        if (suggestedSequence.size() == lengthOfChessBoard) {
                            break;
                        }
                    }
                    if (suggestedSequence.size() == lengthOfChessBoard) {
                        break;
                    }
                }
            }
        }
        return suggestedSequence;
    } // end method smartTip

    /**
     * Creates a simple NQueensProblemFrame
     * @param args Commandline arguments
     */
    public static void main(String[] args) {
        final int LENGTH_OF_CHESS_BOARD = 8;
        File queenImage = new File("assets/Queen-Logo.png");
        JFrame mainWindow = new NQueensProblemFrame(LENGTH_OF_CHESS_BOARD, queenImage);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setVisible(true);
    } // end method main
} // end class NQueensProblemFrame
