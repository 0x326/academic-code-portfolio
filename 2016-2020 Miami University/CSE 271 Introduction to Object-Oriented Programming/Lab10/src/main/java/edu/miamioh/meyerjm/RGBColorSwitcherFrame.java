package edu.miamioh.meyerjm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// Created by John Meyer on 4/9/2017.
// CSE 271 F
// Dr. Angel Bravo
// Lab10

/**
 * A frame that switches the background color between red, green, and blue
 * @author John Meyer
 */
public class RGBColorSwitcherFrame extends JFrame {
    // Define instance variable
    private JPanel panel;
    private enum ColorOption {
        Red,
        Green,
        Blue
    }

    /**
     * Creates a RGBColorSwitcherFrame
     */
    public RGBColorSwitcherFrame() {
        super("RGB Color Switcher");

        // Build menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu colorMenu = createMenu("Color");
        menuBar.add(colorMenu);
        setJMenuBar(menuBar);

        // Build background panel
        this.panel = createPanel();
        add(panel);
        setSize(150, 200);
    } // end constructor RGBColorSwitcherFrame

    /**
     * Creates a JMenu with MenuItems for every ColorOption
     * @param title The title of the menu
     * @return The menu
     */
    private JMenu createMenu(String title) {
        JMenu menu = new JMenu(title);
        for (ColorOption colorOption : ColorOption.values()) {
            menu.add(createMenuItem(colorOption));
        }
        return menu;
    } // end method createMenu

    /**
     * Creates a JMenuItem for the given ColorOption
     * @param itemName The color option
     * @return The menu item
     */
    private JMenuItem createMenuItem(final ColorOption itemName) {
        JMenuItem menuItem = new JMenuItem(itemName.toString());
        class SelectionListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                panel.setBackground(getColor(itemName));
            }
        }
        menuItem.addActionListener(new SelectionListener());
        return menuItem;
    } // end method createMenuItem

    /**
     * Sets the background with the given ColorOption
     * @param color The ColorOption
     */
    private Color getColor(ColorOption color) {
        switch (color) {
            case Red:
                return Color.RED;
            case Green:
                return Color.GREEN;
            case Blue:
                return Color.BLUE;
            default:
                return null;
        }
    } // end method getColor

    /**
     * Creates a JPanel object and attaches a MouseListener
     * @return The JPanel
     */
    private JPanel createPanel() {
        final JPanel panel = new JPanel();

        // Listens for a click and switches the background color
        class ClickListener implements MouseListener {
            public void mouseClicked(MouseEvent e) {
                ColorOption[] colorOptions = ColorOption.values();
                // Determine the present color
                int i;
                for (i = 0; i < colorOptions.length - 1; i++) {
                    if (getColor(colorOptions[i]).equals(panel.getBackground())) {
                        break;
                    }
                }
                // Increment color by one
                i = (i + 1) % colorOptions.length;
                panel.setBackground(getColor(colorOptions[i]));
            } // end method mouseClicked

            public void mousePressed(MouseEvent e) {
                // Do nothing
            } // end method mousePressed

            public void mouseReleased(MouseEvent e) {
                // Do nothing
            } // end method mouseReleased

            public void mouseEntered(MouseEvent e) {
                // Do nothing
            } // end method mouseEntered

            public void mouseExited(MouseEvent e) {
                // Do nothing
            } // end method mouseExited
        } // end class ClickListener
        panel.addMouseListener(new ClickListener());
        if (ColorOption.values().length > 0) {
            panel.setBackground(getColor(ColorOption.values()[0]));
        }
        return panel;
    } // end method createPanel

    /**
     * Creates a String representation of this RGBColorSwitcherFrame.
     * Created in accordance with TA's recommendation https://miamioh.instructure.com/courses/40463/discussion_topics/283398
     * @return The string representation
     */
    @Override
    public String toString() {
        return "RGBColorSwitcherFrame{} " + super.toString();
    } // end method toString
} // end class RGBColorSwitcherFrame
