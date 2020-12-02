// Created by John Meyer on 3/30/2017.
// CSE 271 F
// Dr. Angel Bravo
// Lab09

package edu.miamioh.meyerjm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Represents a submit button for a custom DishItem
 */
public class SubmitButton extends JButton {
    // Define instance variables
    private JTextField itemNameField;
    private JTextField itemPriceField;
    private RestaurantBillFrame parentBill;

    /**
     * Creates a SubmitButton
     * @param itemNameField The field containing the custom DishItem name
     * @param itemPriceField The field containing the custom DishItem price
     * @param parentBill The bill to which to add the custom DishItem
     */
    public SubmitButton(JTextField itemNameField, JTextField itemPriceField, RestaurantBillFrame parentBill) {
        super("Add Custom Item");
        this.itemNameField = itemNameField;
        this.itemPriceField = itemPriceField;
        this.parentBill = parentBill;

        addActionListener(new SubmitItemListener());
    } // end constructor SubmitButton

    /**
     * Gets the field for the custom name
     * @return The field
     */
    public JTextField getItemNameField() {
        return itemNameField;
    } // end method getItemNameField

    /**
     * Gets the field for the custom price
     * @return The field
     */
    public JTextField getItemPriceField() {
        return itemPriceField;
    } // end method getItemPriceField

    /**
     * Listens for a button-click and adds a custom DishItem to the bill
     */
    class SubmitItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                Object source = e.getSource();
                if (source instanceof SubmitButton) {
                    SubmitButton button = (SubmitButton) source;
                    // Extract info
                    String itemName = button.getItemNameField().getText();
                    String itemPrice = button.getItemPriceField().getText().replaceAll("[\\$,.]", "");
                    int itemPriceInPennies = Integer.parseInt(itemPrice);
                    // Add item
                    DishItem customItem = new DishItem(itemName, itemPriceInPennies);
                    parentBill.addItem(customItem);
                }
            }
            catch (NumberFormatException error) {
                // Do nothing
            }
        } // end method actionPerformed
    } // end class SubmitItemListener
} // end class SubmitButton
