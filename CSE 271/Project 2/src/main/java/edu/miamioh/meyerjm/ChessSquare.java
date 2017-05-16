package edu.miamioh.meyerjm;

import javax.swing.*;
import java.awt.*;

// Created by John Meyer on 4/22/2017.
// CSE 271 F
// Dr. Angel Bravo
// Project2

/**
 * Represents a chess square
 */
public class ChessSquare extends JComponent {
    // Define instance variables
    private Color backgroundColor;
    private Color highlightColor;
    private Color selectedColor;
    private Image queenImage;
    private boolean hasQueen;

    /**
     * Sets whether a queen is on this ChessSquare
     * @param val True to put a queen on this square
     */
    public void setQueen(boolean val) {
        hasQueen = val;
    } // end method setQueen

    /**
     * Returns whether there is a queen on this ChessSquare
     * @return True if there is a queen on this square
     */
    public boolean hasQueen() {
        return hasQueen;
    } // end method hasQueen

    /**
     * Sets the highlight color for this ChessSquare
     * @param color The color with which to highlight this square. null for no highlight
     */
    public void setHighlightColor(Color color) {
        highlightColor = color;
        repaint();
    } // end method setHighlightColor

    /**
     * Creates a ChessSquare object
     * @param backgroundColor The background color for this square
     * @param queenImage The image to use when a queen is placed on this square
     */
    public ChessSquare(Color backgroundColor, Image queenImage) {
        this.backgroundColor = backgroundColor;
        this.selectedColor = null;
        this.queenImage = queenImage;
        this.hasQueen = false;
    } // end constructor ChessSquare

    /**
     * Creates a ChessSquare object
     * @param backgroundColor The background color for this square
     * @param selectedColor The color with which to draw a 'Q' when a queen is placed on this square
     */
    public ChessSquare(Color backgroundColor, Color selectedColor) {
        this.backgroundColor = backgroundColor;
        this.selectedColor = selectedColor;
        this.queenImage = null;
        this.hasQueen = false;
    } // end constructor ChessSquare

    /**
     * Draws this ChessSquare. Uses the queen image if it is available.
     * @param g The Graphics on which to draw
     */
    public void paintComponent(Graphics g) {
        g.setColor(highlightColor != null ? highlightColor : backgroundColor);
        g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
        if (hasQueen) {
            if (queenImage != null) {
                g.setColor(Color.BLACK);
                g.drawImage(queenImage, 0, 0, g.getClipBounds().width, g.getClipBounds().height, null);
            } else {
                g.setColor(selectedColor);
                g.drawString("Q", g.getClipBounds().width / 4, g.getClipBounds().height * 3 / 4);
            }
        }
    } // end method paintComponent
} // end class ChessSquare
