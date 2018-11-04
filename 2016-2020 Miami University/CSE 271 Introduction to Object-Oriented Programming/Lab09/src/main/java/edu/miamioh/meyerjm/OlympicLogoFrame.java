// Created by John Meyer on 3/30/2017.
// CSE 271 F
// Dr. Angel Bravo
// Lab09

package edu.miamioh.meyerjm;

import javax.swing.*;
import java.awt.*;

public class OlympicLogoFrame extends JFrame {
    // Define instance variables
    private JPanel jPanel;
    private JComponent logo;
    private int ringRadius;

    /**
     * Represents an Olympic logo
     */
    class OlympicLogoComponent extends JComponent {
        /**
         * Makes the Olympic Logo
         * @param g The graphic on which to draw
         */
        public void paintComponent(Graphics g) {
            Point[] points = {
                new Point(ringRadius, ringRadius),
                new Point(3 * ringRadius, ringRadius),
                new Point(5 * ringRadius, ringRadius),
                new Point(2 * ringRadius, 2 * ringRadius),
                new Point(4 * ringRadius, 2 * ringRadius)
            };
            Color[] colors = {
                Color.BLUE,
                Color.BLACK,
                Color.RED,
                Color.YELLOW,
                Color.GREEN
            };
            g.setColor(Color.WHITE);
            for (int i = 0; i < points.length; i++) {
                drawRing(g, points[i], ringRadius, colors[i]);
            }
        } // end method paintComponent

        /**
         * Draws a given ring
         * @param g The graphic with which to draw
         */
        private void drawRing(Graphics g, Point center, int radius, Color color) {
            // Define variables
            int imageStartXCoordinate;
            int imageStartYCoordinate;
            // Draw the ring
            g.setColor(color);
            imageStartXCoordinate = (int) center.getX() - radius;
            imageStartYCoordinate = (int) center.getY() - radius;
            g.drawOval(imageStartXCoordinate, imageStartYCoordinate, 2 * radius, 2 * radius);
        } // end method drawRing
    } // end class OlympicLogoComponent

    /**
     * Creates an OlympicLogoFrame
     */
    public OlympicLogoFrame(int ringRadius) {
       super("Olympic Logo");
       this.jPanel = new JPanel();
       this.logo = new OlympicLogoComponent();
       this.ringRadius = ringRadius;
       // Add components
       jPanel.add(this.logo);
       add(this.logo);
       setSize(7 * ringRadius, 4 * ringRadius);
    } // end constructor OlympicLogoFrame
} // end class OlympicLogoFrame
