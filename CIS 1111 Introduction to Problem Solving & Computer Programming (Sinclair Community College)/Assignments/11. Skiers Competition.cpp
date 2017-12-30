// John Meyer
// July 7, 2015
// Skier Stat Storer
// This program temporarily stores skiers' times and 
// gives the user the option to perform operations such as: 
//   calculating an average time, 
//   finding the fastest skier, 
//   searching for a skier by name, 
//   and displaying a chart of skier times.

#include <iostream>
#include <iomanip>
#include <string>

using namespace std;

void displayFastestTime(const double[], const string[]);
void displayAverageTime(const double[]);
bool displayTimeByName(const double[], const string[]);
void displayTimes(const double[], const string[]);

// Declare global constants
const unsigned int SIZE = 5;

// Function main: asks for initial data then give the user a menu
int main()
{
    // Define Variables
    string skierName[SIZE];
    double skierTime[SIZE];
    int optionNumberUserChose;

    // Define Flags
    bool userWantsToQuit = false;

    // Set decimal precision and text alignment
    cout << setprecision(2) << fixed << left;

    // Ask for inital data
    cout << "Enter initial data in the format [Skier Name] [SPACE] [Skier Time] [ENTER]" << endl;
    for (unsigned short skiersEntered = 0; skiersEntered < SIZE; skiersEntered++)
    {
        // Get Skier's name
        cin >> skierName[skiersEntered];
        // Get Skier's time
        cin >> skierTime[skiersEntered];
    }

    // Insert Space
    cout << endl;

    // Begin menu loop
    while (!userWantsToQuit)
    {
        // Display menu
        cout << "Enter a number:" << endl <<
            "0. Quit the program" << endl <<
            "1. Determine the fastest skier" << endl <<
            "2. Calculate the average time" << endl <<
            "3. Search for a skier's time by name" << endl <<
            "4. Display the chart of skier times" << endl <<
            "Your Choice: ";
        // Get user input
        cin >> optionNumberUserChose;
        // Insert Space
        cout << endl;
        switch (optionNumberUserChose)
        {
            case 0:
                userWantsToQuit = true;
                break;
            case 1:
                displayFastestTime(skierTime, skierName);
                break;
            case 2:
                displayAverageTime(skierTime);
                break;
            case 3:
                displayTimeByName(skierTime, skierName);
                break;
            case 4:
                displayTimes(skierTime, skierName);
                break;
        }
    }

    cout << endl;
    system("pause");
    return 0;
}

// Function displayFastestTime: finds and displays the fastest time of the skiers
void displayFastestTime(const double skierTime[], const string skierName[])
{
    // Define Variables
    double fastestTime = skierTime[0];
    string fastestSkier = skierName[0];

    // Begin search
    for (unsigned short skiersSearched = 0; skiersSearched < SIZE; skiersSearched++)
    {
        if (skierTime[skiersSearched] < fastestTime)
        {
            fastestTime = skierTime[skiersSearched];
            fastestSkier = skierName[skiersSearched];
        }
    }

    // Display results
    cout << "The fastest skier is " << fastestSkier << " with a time of " << 
        fastestTime << endl << endl;
}
// Function displayAverageTime: calculates and displays the average time
void displayAverageTime(const double skierTime[])
{
    // Define variables
    double sumOfTimes = 0;
    double averageTime;

    // Begin calculations
    for (int numberOfTimesRead = 0; numberOfTimesRead < SIZE; numberOfTimesRead++)
        sumOfTimes += skierTime[numberOfTimesRead];
    averageTime = sumOfTimes / SIZE;

    // Display results
    cout << "The average time is " << averageTime << endl << endl;
}
// Function displayTimeByName: searches for a time with a name and displays that time
bool displayTimeByName(const double skierTime[], const string skierName[])
{
    // Define constants
    const bool SKIER_FOUND = true;
    const bool SKIER_NOT_FOUND = false;

    // Define variables
    string nameOfSkierWanted;
    double timeOfSkierWanted = 0.00;

    // Define flags
    bool skierFound;

    // Ask for the name to search with
    cout << "What is the name of the skier? ";
    cin >> nameOfSkierWanted;

    // Begin search
    for (int namesSearched = 0; namesSearched < SIZE; namesSearched++)
    {
        if (skierName[namesSearched] == nameOfSkierWanted)
        {
            timeOfSkierWanted = skierTime[namesSearched];
            break;
        }
    }

    // Display results
    if (timeOfSkierWanted)
    {
        cout << nameOfSkierWanted << "'s time was " << timeOfSkierWanted << endl << endl;
        skierFound = true;
        return SKIER_FOUND;
    }
    else
    {
        cout << nameOfSkierWanted << "'s time was not found." << endl << endl;
        skierFound = false;
        return SKIER_NOT_FOUND;
    }
}
// Function displayTimes: displays each skier's name and time
void displayTimes(const double skierTime[], const string skierName[])
{
    cout << "Skiers:" << endl;

    // Begin loop for each skier
    for (unsigned int skiersPrinted = 0; skiersPrinted < SIZE; skiersPrinted++)
        cout << setw(10) << skierName[skiersPrinted] << setw(10) << skierTime[skiersPrinted] << endl;

    // Insert Space
    cout << endl;
}
