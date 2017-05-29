// John Meyer
// June 1, 2015
// College Info Display
// This program displays the value of four variables which contain my name, my major,
//  the number of credit hours I am taking, and the tution rate for those credit hours.

#include <iostream>
#include <string>

using namespace std;

int main ()
{
    // Defining Variables
    string myName = "John Meyer";
    string plannedMajor = "Computer Engineering";
    short creditHours = 3;
    float tutionRate = 146.28F;

    // Printing info to console
    cout << "Hello! My name is " << myName << endl;
    cout << "I am majoring in " << plannedMajor << endl;
    cout << "I am taking " << creditHours << " credit hours" << endl;
    cout << "I am paying $" << tutionRate << " per credit hour" << endl;

    // Creating pause so user can read the console
    cout << endl;
    system("pause");
    return 0;
}