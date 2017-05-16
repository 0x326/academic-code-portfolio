// Created by John Meyer on 3/30/2017.
// CSE 271 F
// Dr. Angel Bravo
// Lab09

package edu.miamioh.meyerjm;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Represents a JFrame for a restaurant bill
 */
public class RestaurantBillFrame extends JFrame {
    // Define instance variables
    private JPanel jPanel;
    private DishItem[] popularItems;
    private DishItemButton[] buttonsForPopularItems;
    private JTextField itemNameInputFiled;
    private JTextField itemPriceInputField;
    private SubmitButton itemSumbitButton;
    private ArrayList<DishItem> billItems;
    private JTextArea billPreview;
    private CalculateBillButton calculateButton;

    /**
     * Creates a RestaurantBillFrame object
     * @param popularItems An array of popular DishItems
     */
    public RestaurantBillFrame (DishItem[] popularItems) {
        super("Restaurant Bill");
        this.jPanel = new JPanel();
        this.popularItems = popularItems;
        this.buttonsForPopularItems = new DishItemButton[this.popularItems.length];
        // Create and add buttons
        for (int i = 0; i < this.popularItems.length; i++) {
            this.buttonsForPopularItems[i] = createButton(this.popularItems[i]);
            this.jPanel.add(this.buttonsForPopularItems[i]);
        } // end constructor RestaurantBillFrame
        this.billItems = new ArrayList<DishItem>();

        // Create fields for custom items
        this.itemNameInputFiled = new JTextField(15);
        this.itemNameInputFiled.setText("Custom Item Name");
        this.itemPriceInputField = new JTextField(15);
        this.itemPriceInputField.setText("$4.99");
        this.itemSumbitButton = new SubmitButton(this.itemNameInputFiled, this.itemPriceInputField, this);

        // Create bill preview text area
        this.billPreview = new JTextArea();
        this.billPreview.setEditable(false);
        this.calculateButton = new CalculateBillButton(this);

        // Attach components
        attachItems();
        add(jPanel);
        setSize(400, 400);
    } // end constructor RestaurantBillFrame

    /**
     * Attaches non-popular buttons to panel
     */
    private void attachItems() {
        jPanel.add(this.itemNameInputFiled);
        jPanel.add(this.itemPriceInputField);
        jPanel.add(this.itemSumbitButton);
        jPanel.add(this.calculateButton);
        jPanel.add(this.billPreview);
    } // end method attachItems

    /**
     * Creates a DishItemButton
     * @param item The item out of which to make a button
     * @return The DishItemButton
     */
    private DishItemButton createButton(DishItem item) {
        DishItemButton button = null;
        if (item != null) {
            button = new DishItemButton(item, this);
        }
        return button;
    } // end method createButton

    /**
     * Adds a DishItem to this bill
     * @param item The DishItem
     */
    public void addItem(DishItem item) {
        if (item != null) {
            this.billItems.add(item);
        }
    } // end method addItem

    /**
     * Coverts the given tally of pennies to dollar notation
     * @param pennies The count of pennies
     * @return The dollar representation
     */
    private String convertToDollarFormat(int pennies) {
        return "$" + (pennies / 100) + "." + (pennies / 10 % 10) + (pennies % 10);
    } // end method convertToDollarFormat

    /**
     * Updates the responsible JTextField with a printout of the bill
     */
    public void calculateBill() {
        if (this.billItems.size() == 0) {
            return;
        }
        int totalPrice = 0;
        String billText = "";
        // Traverse all items
        for (DishItem item : this.billItems) {
            totalPrice += item.getItemPrice();
            billText += convertToDollarFormat(item.getItemPrice()) + ": " + item.getItemName() + "\n";
        }
        // Calculate ending info
        int tip = (int) (totalPrice * 0.15);
        billText += "=====\n";
        billText += "Subtotal: " + convertToDollarFormat(totalPrice) + "\n";
        billText += "Tip: " + convertToDollarFormat(tip) + "\n";
        totalPrice += tip;
        billText += "Total: " + convertToDollarFormat(totalPrice) + "\n";
        // Display bill
        this.billPreview.setText(billText);
    } // end method calculateBill
} // end class RestaurantBillFrame
