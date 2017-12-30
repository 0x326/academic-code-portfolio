// Created by John Meyer on 3/30/2017.
// CSE 271 F
// Dr. Angel Bravo
// Lab09

package edu.miamioh.meyerjm;

import javax.swing.*;

public class Part1GuiDemo {
    public static void main(String[] args) {
        // Define variables
        String[] itemNames = {
            "Pizza",
            "Cantaloupe",
            "Pineapple",
            "Shrimp",
            "Fish",
            "Chicken",
            "Wine",
            "Water",
            "Root Beer",
            "Pie"
        };
        int[] itemPrices = {
            1000,
            500,
            600,
            800,
            799,
            1605,
            1700,
            0,
            300,
            400
        };
        // Create DishItems
        DishItem[] items = new DishItem[itemNames.length];
        for (int i = 0; i < itemNames.length; i++) {
            items[i] = new DishItem(itemNames[i], itemPrices[i]);
        }
        // Instantiate RestaurantBillFrame
        RestaurantBillFrame mainWindow = new RestaurantBillFrame(items);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setVisible(true);
    } // end method main
} // end class Part1GuiDemo
