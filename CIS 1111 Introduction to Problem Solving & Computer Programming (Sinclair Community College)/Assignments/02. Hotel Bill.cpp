// John Meyer
// June 1, 2015
// Hotel Bill Calculator
// This program calculates a hotel bill based on hotel rates and the number of nights stayed

#include <iostream>
#include <string>

using namespace std;

int main()
{
    // Defining Given Variables
    int roomRate = 100, resortRate = 10;
    float depositRate = 0.10F, taxRate = 0.085F;
    short nightsStayed = 6;

    // Defining Variables used for Calculations
    int roomCharge, resortFee, subTotal;
    double tax, total, depositAmount, amountDueAtCheckIn;

    // Begin Calculations
    roomCharge = roomRate * nightsStayed;
    resortFee = resortRate * nightsStayed;
    subTotal = roomCharge + resortFee;
    tax = subTotal * taxRate;
    total = subTotal + tax;
    depositAmount = subTotal * depositRate;
    amountDueAtCheckIn = total - depositAmount;

    // Display Receipt
    cout <<
        "Length of stay:            " << nightsStayed << " days" << endl <<
        "Room rate:                 " << "$" << roomRate << endl <<
        "Resort rate:               " << "$" << resortRate << endl <<
        "Tax Rate:                  " << taxRate << endl <<
        "--------------------------------" << endl <<
        "Total room charge:         " << "$" << roomCharge << endl <<
        "Total resort fee:          " << "$" << resortFee << endl <<
        "  Subtotal:                " << "$" << subTotal << endl <<
        "  Tax:                     " << "$" << tax << endl <<
        "Deposit:                   " << "$" << depositAmount << endl <<
        "--------------------------------" << endl <<
        "Amount due at check-in:    " << "$" << amountDueAtCheckIn << endl
        << endl;

    // Pause
    system("pause");
    return 0;
}