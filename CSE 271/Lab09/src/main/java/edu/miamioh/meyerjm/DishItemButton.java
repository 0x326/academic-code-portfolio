// Created by John Meyer on 3/30/2017.
// CSE 271 F
// Dr. Angel Bravo
// Lab09

package edu.miamioh.meyerjm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Represents a button for a popular dish item
 */
public class DishItemButton extends JButton {
    // Define instance variables
    private DishItem dishItem;
    private RestaurantBillFrame parentBill;

    /**
     * Creates a DishItemButton
     * @param dishItem The DishItem out of which to make a button
     * @param parentBill The Bill to which to add the item when this button is pressed
     */
    public DishItemButton(DishItem dishItem, RestaurantBillFrame parentBill) {
        super(dishItem.getItemName() + " ($" + (dishItem.getItemPrice() / 100)
            + "." + (dishItem.getItemPrice() / 10 % 10) + (dishItem.getItemPrice() % 10) + ")");
        this.dishItem = dishItem;
        this.parentBill = parentBill;

        addActionListener(new AddItemListener());
    } // end constructor DishItemButton

    /**
     * Gets the represented DishItem
     * @return the DishItem
     */
    public DishItem getDishItem() {
        return dishItem;
    } // end method getDishItem

    /**
     * Listens for a button-click and adds the DishItem to the bill
     */
    class AddItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source instanceof DishItemButton) {
                DishItemButton button = (DishItemButton) source;
                parentBill.addItem(button.getDishItem());
            }
        } // end method actionPerformed
    } // end class AddItemListener
} // end class DishItemButton
