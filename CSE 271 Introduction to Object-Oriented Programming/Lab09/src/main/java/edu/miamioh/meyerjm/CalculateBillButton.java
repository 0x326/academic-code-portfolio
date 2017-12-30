// Created by John Meyer on 3/30/2017.
// CSE 271 F
// Dr. Angel Bravo
// Lab09

package edu.miamioh.meyerjm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Represents a button to calculate a bill
 */
public class CalculateBillButton extends JButton {
    // Define instance variables
    private RestaurantBillFrame parentBill;

    /**
     * Creates a CalculateBillButton
     * @param parentBill The bill it should calculate when pressed
     */
    public CalculateBillButton(RestaurantBillFrame parentBill) {
        super("Calculate Bill");
        this.parentBill = parentBill;
        addActionListener(new CalculateBillListener());
    } // end constructor CalculateBillButton

    /**
     * Gets the bill to which this button is assigned
     * @return the bill
     */
    public RestaurantBillFrame getParentBill() {
        return parentBill;
    } // end method getParentBill

    /**
     * Listens for a button click and initiates the bill calculation
     */
    class CalculateBillListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source instanceof CalculateBillButton) {
                CalculateBillButton button = (CalculateBillButton) source;
                button.getParentBill().calculateBill();
            }
        } // end method actionPerformed
    } // end class CalculateBillListener
} // end class CalculateBillButton
