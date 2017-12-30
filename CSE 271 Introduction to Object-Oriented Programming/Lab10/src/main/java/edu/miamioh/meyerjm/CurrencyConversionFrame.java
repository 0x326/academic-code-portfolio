package edu.miamioh.meyerjm;

// Created by John Meyer on 4/9/2017.
// CSE 271 F
// Dr. Angel Bravo
// Lab10

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A frame that converts currency from one form to another
 * @author John Meyer
 */
public class CurrencyConversionFrame extends JFrame {
    private enum ConversionOption {
        Dollars,
        Euros,
        Pounds
    }
    private JTextField currencyInput;
    private JLabel currencyOutput;
    private JComboBox[] operands;
    private JLabel warningLabel;

    /**
     * Creates a CurrencyConversionFrame object
     */
    public CurrencyConversionFrame() {
        super("Currency Converter");
        // Create core panel layout
        JPanel panel = new JPanel(new BorderLayout());

        // Add currency fields/combo boxes
        JPanel fieldPanel = new JPanel(new GridLayout(2, 2));
        this.operands = new JComboBox[2];
        for (int i = 0; i < operands.length; i++) {
            operands[i] = createComboBox(ConversionOption.values());
            fieldPanel.add(operands[i]);
        }
        this.currencyInput = new JTextField("0.00", 25);
        this.currencyOutput = new JLabel("0.00");
        fieldPanel.add(currencyInput);
        fieldPanel.add(currencyOutput);

        // Add action components
        JPanel actionPanel = new JPanel(new GridLayout(2, 1));
        actionPanel.add(createConversionButton());
        this.warningLabel = new JLabel();
        actionPanel.add(warningLabel);

        // Build Frame
        panel.add(fieldPanel, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);
        add(panel);
        pack();
    } // end constructor CurrencyConversionFrame

    /**
     * Creates a combo box with the given options
     * @param options The options for this combo box
     * @return The JComboBox
     */
    private JComboBox createComboBox(ConversionOption[] options) {
        JComboBox comboBox = new JComboBox(options);

        // Listens for a click and clears the warning label
        class ClickListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                if (operands[0].getSelectedItem() != operands[1].getSelectedItem()) {
                    // If selections are different, clear warning label
                    warningLabel.setText("");
                }
            } // end method actionPerformed
        } // end class ClickListener
        comboBox.addActionListener(new ClickListener());
        comboBox.setEditable(true);
        return comboBox;
    } // end method createComboBox

    /**
     * Creates a conversion button
     * @return The button
     */
    private JButton createConversionButton() {
        JButton button = new JButton("Convert");

        // Listens for a click and performs the currency conversion
        class ClickListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Check the currency operands
                    if (operands[0].getSelectedItem() == operands[1].getSelectedItem()) {
                        warningLabel.setText("Choose two currencies to convert");
                    }
                    else {
                        // Parse input
                        double enteredValue = Double.parseDouble(currencyInput.getText());
                        ConversionOption enteredCurrencyType = (ConversionOption) operands[0].getSelectedItem();
                        ConversionOption desiredCurrencyType = (ConversionOption) operands[1].getSelectedItem();
                        // Display output
                        currencyOutput.setText(convertCurrency(enteredValue, enteredCurrencyType, desiredCurrencyType));
                    }
                }
                catch (Exception error) {
                    // Do nothing
                }
            } // end method actionPerformed
        } // end class ClickListener
        button.addActionListener(new ClickListener());
        return button;
    } // end method createConversionButton

    /**
     * Converts the given monetary amount into an equal value in a different currency.
     * @param value The numeric value to convert
     * @param fromType The currency in which the value represents
     * @param toType The currency to which to convert
     * @return The numeric value in the new currency
     */
    private String convertCurrency(double value, ConversionOption fromType, ConversionOption toType) {
        // Determine how to apply conversion factors
        final double RATIO_OF_DOLLARS_PER_EURO = 1.42;
        final double RATIO_OF_DOLLARS_PER_POUND = 1.64;
        final double RATIO_OF_EUROS_PER_POUND = 1.13;
        switch (fromType) {
            case Euros:
                if (toType == ConversionOption.Dollars) {
                    value *= RATIO_OF_DOLLARS_PER_EURO;
                }
                else if (toType == ConversionOption.Pounds){
                    value /= RATIO_OF_EUROS_PER_POUND;
                }
                break;
            case Pounds:
                if (toType == ConversionOption.Dollars) {
                    value *= RATIO_OF_DOLLARS_PER_POUND;
                }
                else if (toType == ConversionOption.Euros){
                    value *= RATIO_OF_EUROS_PER_POUND;
                }
                break;
            case Dollars:
                if (toType == ConversionOption.Euros) {
                    value /= RATIO_OF_DOLLARS_PER_EURO;
                }
                else if (toType == ConversionOption.Pounds){
                    value /= RATIO_OF_DOLLARS_PER_POUND;
                }
                break;
            default:
        }
        // Ensure value is not negative
        value = value > 0 ? value : 0.00;
        return "" + (int) value + "." + (int) (value * 10 % 10) + (int) (value * 100 % 10);
    } // end method convertCurrency

    /**
     * Creates a String representation of this CurrencyConversionFrame.
     * Created in accordance with TA's recommendation https://miamioh.instructure.com/courses/40463/discussion_topics/283398
     * @return The string representation
     */
    @Override
    public String toString() {
        return "CurrencyConversionFrame{} " + super.toString();
    } // end method toString
} // end class CurrencyConversionFrame
