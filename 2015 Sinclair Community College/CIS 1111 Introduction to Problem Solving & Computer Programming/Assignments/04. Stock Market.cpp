// John Meyer
// June 3, 2015
// Stock Tracker
// This program simulates a change in stock price and calculates the money gained or lost if the stocks were sold

#include <iostream>
#include <string>
#include <cstdlib>
#include <ctime>
#include <iomanip>

using namespace std;

int main()
{
    // Defining Variables
    string companyName, stockSymbol;
    int numberOfShares, coinFlip;
    double sharePrice, sharesCost, commission, commissionRate, totalCost, randomNumber, percentChange;
    double newSharePrice, newSharesCost, newCommission, newTotalCost, profit;
    double rateOfReturn;
    unsigned short FIELD_WIDTH = 30; // for setw stream manipulator

    // Announce Program Title
    cout << "----- Stock Tracker -----" << endl;

    // Asking user for stock information
    cout << "What is the company name? ";
    getline(cin, companyName);
    cout << "What is the stock symbol? ";
    cin >> stockSymbol;
    cout << "How many shares did you buy? ";
    cin >> numberOfShares;
    cout << "What was the price per share? ";
    cin >> sharePrice;
    cout << "What was the commission rate as a decimal? ";
    cin >> commissionRate;

    // Calculating
    sharesCost = numberOfShares * sharePrice;
    commission = sharesCost * commissionRate;
    totalCost = commission + sharesCost; // Simulated Buying of shares with commissionRate
    srand( (unsigned int) time(0) ); // Creating seed
    percentChange = ((rand() % (200 - 100 + 1 )) + 100) / 100.0;
    coinFlip = rand() % 2 + 1;
    if (coinFlip)
        percentChange *= 1;
    else
        percentChange *= -1;
    newSharePrice = sharePrice * percentChange;
    newSharesCost = newSharePrice * numberOfShares;
    newCommission = newSharesCost * commissionRate;
    newTotalCost = newSharesCost - newCommission; // Simulated Selling of shares with commssion
    profit = newTotalCost - totalCost;
    rateOfReturn = profit / totalCost;

    // Displaying data
    cout << endl << left << setprecision(2) << fixed << 
        "Receipt: " << endl <<
        setw(FIELD_WIDTH) << "Company Name: " << companyName << endl <<
        setw(FIELD_WIDTH) << "Stock Symbol: " << stockSymbol << endl <<
        setw(FIELD_WIDTH) << "Commission Rate: " << setprecision(4) << fixed << commissionRate << endl <<
        endl << setprecision(2) << fixed <<
        setw(FIELD_WIDTH) << "Shares bought: " << numberOfShares << endl <<
        setw(FIELD_WIDTH) << "Price Per Share at Purchase: " << "$" << sharePrice << endl <<
        setw(FIELD_WIDTH) << "Cost of Shares: " << "$" << sharesCost << endl <<
        " " << setw(FIELD_WIDTH) << "Commission: " << "$" << commission << endl <<
        setw(FIELD_WIDTH) << "Total Purchase Price: " << "$" << totalCost << endl <<
        endl << 
        setw(FIELD_WIDTH) << "Shares sold: " << numberOfShares << endl <<
        setw(FIELD_WIDTH) << "Price Per Share at Sell-time: " << "$" << newSharePrice << endl <<
        setw(FIELD_WIDTH) << "Cost of Shares: " << "$" << newSharesCost << endl <<
        " " << setw(FIELD_WIDTH) << "Commision: " << "$" << newCommission << endl <<
        setw(FIELD_WIDTH) << "Total Sell Price: " << "$" << newTotalCost << endl <<
        endl <<
        setw(FIELD_WIDTH) << "Profit: " << "$" << profit << endl <<
        setw(FIELD_WIDTH) << "Rate of Return: " << rateOfReturn << endl <<
        "";

    system("pause");
    return 0;
}