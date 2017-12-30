// John Meyer
// June 4, 2015
// Shipping Charge Calculator
// This program calculates the shipping cost of items based on their weight

#include <iostream>
#include <iomanip>

using namespace std;

int main ()
{
    // Defining Variables
    const float RATE_FOR_FIVE_POUNDS = 1.10F, RATE_FOR_FIFTEEN_POUNDS = 2.20F, RATE_FOR_THIRTY_POUNDS = 3.70F, \
        RATE_FOR_FIFTY_POUNDS = 4.80F;
    double shippingRate = 0.00, shippingCost = 0.00;
    unsigned int weightOfPackage = 0, shippingDistance = 0;
    unsigned short shippingSegments = 0;

    unsigned short fieldWidth;

    bool errorWeightTooMuch = false, errorDistanceTooLarge = false, errorDistanceTooSmall = false;



    // Display Rates
    fieldWidth = 38;
    cout << setprecision(2) << fixed << "Freight Shipping Company Rates" << endl <<
        "_______________________________________________________________" << endl <<
        setw(fieldWidth) << left << "Weight of Package (pounds)" << "Rate per 500 miles shipped" << endl <<
        "_______________________________________________________________" << endl <<
        setw(fieldWidth) << "5 pounds or less" << "$" << RATE_FOR_FIVE_POUNDS << endl <<
        setw(fieldWidth) << "Over 5 pounds but not more than 15" << "$" << RATE_FOR_FIFTEEN_POUNDS << endl <<
        setw(fieldWidth) << "Over 15 pounds but not more than 30" << "$" << RATE_FOR_THIRTY_POUNDS << endl <<
        setw(fieldWidth) << "Over 30 pounds but not more than 50" << "$" << RATE_FOR_FIFTY_POUNDS << endl <<
        "_______________________________________________________________" << endl <<
        "NOTICE: We do not ship packages over 50 pounds" << endl <<
	    "        We do not ship less than 10 miles or more than 3,000 miles" << endl <<
        endl;

    // Ask for information
    cout << "How many pounds does your package weigh?" << endl;
    cin >> weightOfPackage;
    cout << "How many miles does your package need to be shipped?" << endl;
    cin >> shippingDistance;

    // Calculate shipping rate
    if (weightOfPackage <= 5)
        shippingRate = RATE_FOR_FIVE_POUNDS;
    else if (weightOfPackage <= 15)
        shippingRate = RATE_FOR_FIFTEEN_POUNDS;
    else if (weightOfPackage <= 30)
        shippingRate = RATE_FOR_THIRTY_POUNDS;
    else if (weightOfPackage <= 50)
        shippingRate = RATE_FOR_FIFTY_POUNDS;
    else
    {
        errorWeightTooMuch = true;
        cout << "The package needs to be less than or equal to 50 pounds" << endl;
    }
    if (shippingDistance < 10)
    {
        errorDistanceTooSmall = true;
        cout << "We don't ship packages under 10 miles" << endl;
    }
    else if (shippingDistance > 3000)
    {
        errorDistanceTooLarge = true;
        cout << "We don't ship packages over 3,000 miles" << endl;
    }
    shippingSegments = shippingDistance / 500;
    if (shippingDistance % 500 != 0) // Round up
        shippingSegments++;
    shippingCost = shippingSegments * shippingRate;

    // Display Receipt
    if (!(errorWeightTooMuch || errorDistanceTooSmall || errorDistanceTooLarge))
    {
        fieldWidth = 45;
        cout << endl << setprecision(0) << fixed << "Receipt: " << endl << 
            setw(fieldWidth) << left << "Package Weight (lbs): " << weightOfPackage << endl <<
            setw(fieldWidth) << setprecision(2) << fixed << " Shipping Rate: " << "$" << shippingRate << endl <<
            setw(fieldWidth) << setprecision(0) << fixed << "Shipping Distance in multiples of 500 mi: " << shippingSegments << endl <<
            setw(fieldWidth) << " Shipping Distance (mi): " << shippingDistance << endl <<
            setw(fieldWidth) << setprecision(2) << fixed << "Shipping Cost: " << "$" << shippingCost << endl <<
            endl;
    }
    system("pause");
    return 0;
}