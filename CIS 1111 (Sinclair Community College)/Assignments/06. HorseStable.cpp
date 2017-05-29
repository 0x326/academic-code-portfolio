// John Meyer
// June 9, 2015
// Horse Feed Calculator
// This program determines if a horse is overweight or underweight and
//  determines the amount of feed it needs

#include <iostream>
#include <string>
#include <iomanip>

using namespace std;

int main ()
{
    // Defining Variables
    const short LIGHT_RIDING_HORSE = 1,
        LARGE_RIDING_HORSE = 2,
        DRAFT_HORSE = 3;

    const int LIGHT_RIDING_HORSE_OPTIMAL_WEIGHT_RANGE_LOWER_END = 840,
        LIGHT_RIDING_HORSE_OPTIMAL_WEIGHT_RANGE_HIGHER_END = 1200;
    const int LARGE_RIDING_HORSE_OPTIMAL_WEIGHT_RANGE_LOWER_END = 1100,
        LARGE_RIDING_HORSE_OPTIMAL_WEIGHT_RANGE_HIGHER_END = 1300;
    const int DRAFT_HORSE_OPTIMAL_WEIGHT_RANGE_LOWER_END = 1500,
        DRAFT_HORSE_OPTIMAL_WEIGHT_RANGE_HIGHER_END = 2200;

    const float POUNDS_OF_FEED_FOR_UNDERWEIGHT = 3.3,
        POUNDS_OF_FEED_FOR_OPTIMAL_WEIGHT = 3.0,
        POUNDS_OF_FEED_FOR_OVERWEIGHT = 2.5;

    short horseType = 0;
    string horseTypeName = "";
    unsigned int horseWeight = 0;
    bool horseUnderweight = false, horseOptimalWeight = false,
        horseOverweight = false;
    string horseWeightName = "";
    float poundsOfFeedNeeded = 0.0;
    bool userInputIsConfirmedValid = false;
    char userInputConfirmation = ' '; // Used when the user is asked to confirm his output with 'Y' or 'N'

    // Display weight chart
    cout << "Optimal Weight Chart in Pounds:" << endl <<
        setw(20) << left << "Light riding horse" << 
            setw(5) << right << LIGHT_RIDING_HORSE_OPTIMAL_WEIGHT_RANGE_LOWER_END <<
            "-" <<
            setw(5) << left << LIGHT_RIDING_HORSE_OPTIMAL_WEIGHT_RANGE_HIGHER_END <<
            endl <<
        setw(20) << left << "Large riding horse  " <<
            setw(5) << right << LARGE_RIDING_HORSE_OPTIMAL_WEIGHT_RANGE_LOWER_END <<
            "-" <<
            setw(5) << left << LARGE_RIDING_HORSE_OPTIMAL_WEIGHT_RANGE_HIGHER_END <<
            endl <<
        setw(20) << left << "Draft horse" <<
            setw(5) << right << DRAFT_HORSE_OPTIMAL_WEIGHT_RANGE_LOWER_END <<
            "-" <<
            setw(5) << left << DRAFT_HORSE_OPTIMAL_WEIGHT_RANGE_HIGHER_END <<
            endl <<
        endl;

    // Ask user for information
    cout << "Is your horse a:" << endl <<
        " " << LIGHT_RIDING_HORSE << ". Light Riding Horse" << endl <<
        " " << LARGE_RIDING_HORSE << ". Large Riding Horse" << endl <<
        " " << DRAFT_HORSE << ". Draft Horse" << endl <<
        endl;
    while (!userInputIsConfirmedValid) // Continue asking until a valid answer is given
    {
        cin >> horseType;
        // Confirming User Input
        if (horseType == LIGHT_RIDING_HORSE)
            userInputIsConfirmedValid = true;
        else if (horseType == LARGE_RIDING_HORSE)
            userInputIsConfirmedValid = true;
        else if (horseType == DRAFT_HORSE)
            userInputIsConfirmedValid = true;
        else
            cout << "Please enter one of the characters above" << endl;
    }
    cout << "How many pounds does your horse weight?" << endl;
    userInputIsConfirmedValid = false;
    while (!userInputIsConfirmedValid) // Continue asking until a valid answer is given
    {
        cin >> horseWeight;
        if (horseWeight > 10000) // If the weight is greater than an improbable number
        {
            cout << "The number you entered was recognized as " << horseWeight << endl <<
                "Is this number correct? (Y/N)";
            cin.get(userInputConfirmation);
            cin.ignore(1,'\n');
            if (userInputConfirmation == 'Y' || userInputConfirmation == 'y')
                userInputIsConfirmedValid = true;
            else
                userInputIsConfirmedValid = false;
        }
        else
            userInputIsConfirmedValid = true;
    }
    
    // Remember the name of the horse type
    if (horseType == LIGHT_RIDING_HORSE)
        horseTypeName = "Light Riding Horse";
    else if (horseType == LARGE_RIDING_HORSE)
        horseTypeName = "Large Riding Horse";
    else if (horseType == DRAFT_HORSE)
        horseTypeName = "Draft Horse";

    // Calculate whether horse is overweight, underweight, or optimal
    if (horseType == LIGHT_RIDING_HORSE)
    {
        if (horseWeight < LIGHT_RIDING_HORSE_OPTIMAL_WEIGHT_RANGE_LOWER_END)
            horseUnderweight = true;
        else if (horseWeight <= LIGHT_RIDING_HORSE_OPTIMAL_WEIGHT_RANGE_HIGHER_END)
            horseOptimalWeight = true;
        else
            horseOverweight = true;
    }
    else if (horseType == LARGE_RIDING_HORSE)
    {
        if (horseWeight < LARGE_RIDING_HORSE_OPTIMAL_WEIGHT_RANGE_LOWER_END)
            horseUnderweight = true;
        else if (horseWeight <= LARGE_RIDING_HORSE_OPTIMAL_WEIGHT_RANGE_HIGHER_END)
            horseOptimalWeight = true;
        else
            horseOverweight = true;
    }
    else if (horseType == DRAFT_HORSE)
    {
        if (horseWeight < DRAFT_HORSE_OPTIMAL_WEIGHT_RANGE_LOWER_END)
            horseUnderweight = true;
        else if (horseWeight <= DRAFT_HORSE_OPTIMAL_WEIGHT_RANGE_HIGHER_END)
            horseOptimalWeight = true;
        else
            horseOverweight = true;
    }

    // Remember the name of the horse's weight
    if (horseUnderweight)
        horseWeightName = "Underweight";
    else if (horseOptimalWeight)
        horseWeightName = "Optimal Weight";
    else if (horseOverweight)
        horseWeightName = "Overweight";

    // Calculate how much feed is needed for the horse
    if (horseUnderweight)
        poundsOfFeedNeeded = POUNDS_OF_FEED_FOR_UNDERWEIGHT;
    else if (horseOptimalWeight)
        poundsOfFeedNeeded = POUNDS_OF_FEED_FOR_OPTIMAL_WEIGHT;
    else if (horseOverweight)
        poundsOfFeedNeeded = POUNDS_OF_FEED_FOR_OVERWEIGHT;


    // Display info
    cout << endl << "Info list:" << endl <<
        "Horse Type: " << horseTypeName << endl <<
        "Horse Weight: " << horseWeight << endl <<
        " Horse Weight Type: " << horseWeightName << endl <<
        "Horse Feed (lbs): " << poundsOfFeedNeeded << endl <<
        endl;

    cout << endl;
    system("pause");
    return 0;
}