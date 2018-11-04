// John Meyer
// July 4, 2015
// Restaurant Cash Register Display
// This program displays the menu, asks for the order, calculates the bill, and calculates the change

#include <iostream>
#include <iomanip>
#include <string>

using namespace std;

void displayMenu ();
void displayBill (double bill, double totBill, double tax, double tip);
void calculateChange (double totBill, double amtTendered);

// Define global constants
const double HAMBURGER_COST = 6.00;
const double HOTDOG_COST = 4.50;
const double PEANUT_COST = 3.75;
const double POPCORN_COST = 5.50;
const double SODA_COST = 2.80;
const double CHIP_COST = 1.00;
const double WATER_COST = 2.00;

// Function main
int main ()
{
    // Define Constants
    const double TIP_PERCENTAGE = 0.20;
    const double TAX_PERCENTAGE = 0.065;

    // Define Variables
    double subTotal = 0.00, tip, tax, total;
    double amountTendered;
    int menuNumberOrder;

    // Define Flags
    bool orderIsComplete = false;
    bool tenderedEnoughMoney = false;

    // Set Two Decimal Precision
    cout << setprecision(2) << fixed;

    // Set Text Alignment
    cout << left;

    // Display Menu
    displayMenu();

    // Ask for order
    while (!orderIsComplete)
    {
        cout << "Enter a menu item" << endl;
        cin >> menuNumberOrder;
        switch (menuNumberOrder)
        {
            default:
                cout << "The number you have entered is invalid." << endl;
                cout << "Please ";
                break;
            case 1:
                subTotal += HAMBURGER_COST;
                break;
            case 2:
                subTotal += HOTDOG_COST;
                break;
            case 3:
                subTotal += PEANUT_COST;
                break;
            case 4:
                subTotal += POPCORN_COST;
                break;
            case 5:
                subTotal += SODA_COST;
                break;
            case 6:
                subTotal += CHIP_COST;
                break;
            case 7:
                subTotal += WATER_COST;
                break;
            case 8:
                orderIsComplete = true;
                break;
        }
    }

    // Calculate Bill
    tip = subTotal * TIP_PERCENTAGE;
    tax = subTotal * TAX_PERCENTAGE;
    total = subTotal + tip + tax;
    displayBill(subTotal, total, tax, tip);

    // Ask for amount of cash tendered
    while (!tenderedEnoughMoney)
    {
        cout << "Enter total amount of cash tendered" << endl;
        cin >> amountTendered;
        if (amountTendered >= total)
            tenderedEnoughMoney = true;
        else if (amountTendered < 0)
            cout << "Enter a positive cash value" << endl;
        else
            cout << "This amount is not enough. Try again and ";
    }

    // Calculate/Display the change
    calculateChange(total, amountTendered);

    // Insert Space
    cout << endl;

    system("pause");
    return 0;
}


// This function displays the menu
void displayMenu ()
{
    unsigned short fieldWidth = 15;
    cout << "Baseball Game Snack Menu" << endl << 
        setw(fieldWidth) << "1 - Hamburger" << "$" << HAMBURGER_COST << endl <<
        setw(fieldWidth) << "2 - Hotdog" << "$" << HOTDOG_COST << endl <<
        setw(fieldWidth) << "3 - Peanuts" << "$" << PEANUT_COST << endl <<
        setw(fieldWidth) << "4 - Popcorn" << "$" << POPCORN_COST << endl <<
        setw(fieldWidth) << "5 - Soda" << "$" << SODA_COST << endl <<
        setw(fieldWidth) << "6 - Chips" << "$" << CHIP_COST << endl <<
        setw(fieldWidth) << "7 - Water" << "$" << WATER_COST << endl <<
        setw(fieldWidth) << "8 - End order" << endl <<
        endl;
}

// This function displays the bill
void displayBill (double bill, double totBill, double tax, double tip)
{
    unsigned short fieldWidth = 15;
    cout << endl <<
        setw(fieldWidth) << "Subtotal: " << bill << endl <<
        setw(fieldWidth) << " Tip: " << tip << endl <<
        setw(fieldWidth) << " Tax: " << tax << endl <<
        setw(fieldWidth) << "Total: " << totBill << endl <<
        endl;
}

// This function calculates and displays the change to give back to the customer
void calculateChange (double totBill, double amtTendered)
{
    unsigned short fieldWidth = 15;
    double amountOfChange = amtTendered - totBill;
    cout << setw(fieldWidth) << "Change: " << "$" << amountOfChange << endl <<
        endl;
}
