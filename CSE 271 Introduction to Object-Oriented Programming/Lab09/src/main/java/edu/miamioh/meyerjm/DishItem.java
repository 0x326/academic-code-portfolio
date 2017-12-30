// Created by John Meyer on 3/30/2017.
// CSE 271 F
// Dr. Angel Bravo
// Lab09

package edu.miamioh.meyerjm;

/**
 * Represents a dish item
 */
public class DishItem {
    // Define instance variables
    private String itemName;
    private int itemPrice;

    /**
     * Creates a DishItem
     * @param itemName The item's name
     * @param itemPrice The item's price (in pennies)
     */
    public DishItem(String itemName, int itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    } // end constructor DishItem

    /**
     * Gets this DishItem's name
     * @return The name
     */
    public String getItemName() {
        return itemName;
    } // end method getItemName

    /**
     * Gets this DishItem's price
     * @return The price
     */
    public int getItemPrice() {
        return itemPrice;
    } // end method getItemPrice
} // end class DishItem
