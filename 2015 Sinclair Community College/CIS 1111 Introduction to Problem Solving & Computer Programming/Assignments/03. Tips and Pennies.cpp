// John Meyer
// June 2, 2015
// Coin Calculator
// This program calculates the monetary value of the user's coins
//  and calculates the least amount of coinage for the user's coins

#include <iostream>
#include <string>
#include <iomanip>

using namespace std;

int main()
{
    // Defining Variables
    string nameOfUser;
    int numOfDollars, numOfQuarters, numOfDimes, numOfNickels, numOfPennies, subTotal;
    double monetaryValueOfCoins;
    const short FIELD_WIDTH = 21; // for setw stream manipulator

    // Defining Variables for Part 2
    int numOfPenniesYouHave, value;

    // Asking for User's Name
    cout << "What is your name?" << endl;
    getline(cin, nameOfUser);
    
    // Asking for Coin Amounts
    cout << "How many dollars do you have?" << endl;
    cin >> numOfDollars;
    cout << "How many quarters do you have?" << endl;
    cin >> numOfQuarters;
    cout << "How many dimes do you have?" << endl;
    cin >> numOfDimes;
    cout << "How many nickels do you have?" << endl;
    cin >> numOfNickels;
    cout << "How many pennies do you have?" << endl;
    cin >> numOfPennies;
    
    // Converting coin amount to monetary value using conversion factors.
    /*** This expression will result with a value in dollar units.
    ( A pennies
    + B Nickels * (5 pennies / 1 nickel)
    + C Dimes * (10 pennies / 1 dime)
    + D Quarters * (25 pennies / 1 quarter)
    + E Dollars * (100 pennies / 1 dollar)
    ) * (1 dollar / 100 pennies)
    ***/
    subTotal = numOfPennies;
    subTotal += numOfNickels * 5;
    subTotal += numOfDimes * 10;
    subTotal += numOfQuarters * 25;
    subTotal += numOfDollars * 100;
    monetaryValueOfCoins = subTotal / 100.0;
    
    // Displaying user's name, the amount of coins entered, and the value of those coins
    cout << setw(FIELD_WIDTH) << left << "Current User: " << nameOfUser << endl <<
        setw(FIELD_WIDTH) << "Amount of Dollars: " << numOfDollars << endl <<
        setw(FIELD_WIDTH) << "Amount of Quarters: " << numOfQuarters << endl <<
        setw(FIELD_WIDTH) << "Amount of Dimes: " << numOfDimes << endl <<
        setw(FIELD_WIDTH) << "Amount of Nickels: " << numOfNickels << endl <<
        setw(FIELD_WIDTH) << "Amount of Pennies: " << numOfPennies << endl <<
        setw(FIELD_WIDTH) << endl <<
        setw(FIELD_WIDTH) << "Value of Coins: " << "$" << setprecision(2) << fixed << monetaryValueOfCoins << endl <<
        "\n";

    ////////// PART 2 //////////

    // Clearing variables for use in Part 2
    numOfDollars = numOfQuarters = numOfDimes = numOfNickels = numOfPennies = 0;
    numOfPenniesYouHave = 0;
    value = 0;

    // Asking for Penny Amount
    cout << "How many pennies do you have?" << endl;
    cin >> numOfPenniesYouHave;

    // Begin Calculations
    value = numOfPenniesYouHave;
    // Calculating the maximum number of dollars
    numOfDollars = value / 100;
    value = value % 100;
    // Calculating the maximum number of quarters
    numOfQuarters = value / 25;
    value = value % 25;
    // Calculating the maximum number of dimes
    numOfDimes = value / 10;
    value = value % 10;
    // Calculating the maximum number of nickels
    numOfNickels = value / 5;
    value = value % 5;
    // Calculating the maximum number of pennies
    numOfPennies = value;

    // Display amount of pennies and equivalent amount of dollars, quarters, dimes, nickels, and pennies
    cout << left << "Pennies you have: " << numOfPenniesYouHave << endl <<
        endl <<
        "You could have:" << endl <<
        " " << numOfDollars << " Dollars" << endl <<
        " " << numOfQuarters << " Quarters" << endl <<
        " " << numOfDimes << " Dimes" << endl <<
        " " << numOfNickels << " Nickels" << endl <<
        " " << numOfPennies << " Pennies" << endl <<
        endl;

    system("pause");
    return 0;
}
