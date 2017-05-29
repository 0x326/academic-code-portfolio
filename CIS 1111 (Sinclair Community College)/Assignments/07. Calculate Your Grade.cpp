// John Meyer
// June 27, 2015
// Grade Calculator
// This program calculates the user's grade by taking the average of quizes and assignments

#include <iostream>
#include <iomanip>

using namespace std;

int main ()
{
    // Define Constants
    const unsigned int TOTAL_POSSIBLE_QUIZ_POINTS = 40;
    const unsigned int TOTAL_POSSIBLE_ASSIGNMENT_POINTS = 160;

    // Define Variables
    double quizAverage = 0.0;
    double quizSum = 0.0;
    int thisQuizGrade = 0;

    double assignmentAverage = 0.0;
    double assignmentSum = 0.0;
    int thisAssignmentGrade = 0;
    unsigned short assignmentNumber = 0;

    double overallAverage = 0.0;
    double overallAveragePercent = 0.0;
    char overallLetterGrade;

    // Define Flags
    bool userInputValid = false;

    // Ask user for quiz grades
    for (unsigned short quizNumber = 0; quizNumber < 4; quizNumber++) 
        // quizNumber represents number of quizes the program has finsished asking for
    {
        do
        {
            userInputValid = false; // Reset flag
            cout << "Enter quiz " << quizNumber + 1 << " grade" << endl;
            cin >> thisQuizGrade;

            // Validate User Input
            if (thisQuizGrade >= 0 && thisQuizGrade <= 10)
                userInputValid = true;
            else
                cout << "That grade is not valid. It must be between 0 and 10." << endl;
        } while (!userInputValid);
        quizSum += thisQuizGrade;
    }

    // Display quiz sum
    cout << "Quiz Sum: " << quizSum << endl;

    // Calculate/Display quiz average
    quizAverage = quizSum / TOTAL_POSSIBLE_QUIZ_POINTS;
    cout << "Quiz Average: " << quizAverage << endl;

    // Insert Space
    cout << endl;

    // Ask user for assignment grades
    while (assignmentNumber < 8)
        // asignmentNumber represents the number of assignments the program has finished asking for
    {
        do
        {
            userInputValid = false; // Reset flag
            cout << "Enter assignment " << assignmentNumber + 1 << " grade" << endl;
            cin >> thisAssignmentGrade;

            // Validate User Input
            if (thisAssignmentGrade >= 0 && thisAssignmentGrade <= 20)
                userInputValid = true;
            else
                cout << "That grade is not valid. It must be between 0 and 20." << endl;
        } while (!userInputValid);
        assignmentSum += thisAssignmentGrade;
        assignmentNumber++;
    }

    // Display assignment sum
    cout << "Assignment Sum: " << assignmentSum << endl;

    // Calculate/Display assignment average
    assignmentAverage = assignmentSum / TOTAL_POSSIBLE_ASSIGNMENT_POINTS;
    cout << "Assignment Average: " << assignmentAverage << endl;

    // Insert Space
    cout << endl;

    // Calculate/Display overall average
    overallAverage = (quizSum + assignmentSum) / (TOTAL_POSSIBLE_QUIZ_POINTS + TOTAL_POSSIBLE_ASSIGNMENT_POINTS);
    overallAveragePercent = overallAverage * 100;
    cout << "Overall Average: " << setprecision(0) << fixed << overallAveragePercent << "%" << endl;

    // Calculate/Display overall letter grade
    if (overallAveragePercent < 60)
        overallLetterGrade = 'F';
    else
    {
        if (overallAveragePercent < 70)
            overallLetterGrade = 'D';
        else
        {
            if (overallAveragePercent < 80)
                overallLetterGrade = 'C';
            else
            {
                if (overallAveragePercent < 90)
                    overallLetterGrade = 'B';
                else
                    overallLetterGrade = 'A';
            }
        }
    }
    cout << "Letter Grade: " << overallLetterGrade << endl;

    // Insert Space
    cout << endl;

    system("pause");
    return 0;
}
