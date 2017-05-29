// John Meyer
// June 30, 2015
// Quiz Averager
// This program asks for students' quizzes, writes them to a file. 
//  Then it reads the quizes from the file and averages them.

#include <iostream>
#include <fstream>
#include <string>
#include <iomanip>

using namespace std;

int main()
{
    // Define Constants
    string FOLDER_PATH = "/Users/John Meyer/Documents/College/Sinclair/CIS 1111/";
    string FILE_PREFIX = "Averaging Quiz Grades ";
    int NUMBER_OF_QUIZZES = 4;

    // Define Variables
    unsigned int numberOfStudents;
    int quizGrade;
    ofstream quizDataOutput;
    string userPermissionToWrite;
    long studentID;

    // Define Flags
    bool allQuizInformationEntered = false;
    bool currentQuizConfirmedValid = false;

    // Warn user that file will be overwritten
    cout << FOLDER_PATH << FILE_PREFIX << "Student Quiz Data.txt" << endl <<
        "Will be overwritten if it exists. Is this O.K.? Y/N" << endl;
    cin >> userPermissionToWrite;
    if (userPermissionToWrite != "Y" && userPermissionToWrite != "y")
    {
        cout << "The program has stopped without touching the file" << endl;
        system("pause");
        exit(1);
    }

    // Open file
    quizDataOutput.open(FOLDER_PATH + FILE_PREFIX + "Student Quiz Data.txt");

    // Ask for information
    cout << "When done, enter zero for the student ID." << endl << "Enter the ";
    while (!allQuizInformationEntered)
    {
        // Ask for Student ID
        cout << "Student ID: " << endl;
        cin >> studentID;
        if (studentID)
        {
            // Write Student ID to file
            quizDataOutput << studentID;
            for (unsigned short quizNumber = 1; quizNumber < NUMBER_OF_QUIZZES + 1; quizNumber++)
            {
                // Ask for quiz info
                currentQuizConfirmedValid = false;
                while (!currentQuizConfirmedValid)
                {
                    cout << "Grade for quiz " << quizNumber << ": " << endl;
                    cin >> quizGrade;
                    if (quizGrade >= 0 && quizGrade <= 100)
                        currentQuizConfirmedValid = true;
                }
                // Write quiz grade to file
                quizDataOutput << " " << quizGrade;
            }
            // Write newline to file
            quizDataOutput << endl;
        }
        else
            allQuizInformationEntered = true;
    }

    // Clear Variables
    studentID = 0;
    quizGrade = 0;

    // Define Variables
    ifstream quizDataInput;

    long quizSum = 0;
    double quizAverage = 0.0;
    unsigned int quizzesRead;

    long classTotal = 0;
    double classAverage = 0.0;
    unsigned int classQuizzes = 0;


    // Define Flags
    bool allInformationRead = false;
    bool corruptedFile = false;
    bool moreDataInFile;

    // Insert Space
    cout << endl;

    // Open File
    quizDataInput.open(FOLDER_PATH + FILE_PREFIX + "Student Quiz Data.txt");

    // Read info from file
    while (!allInformationRead)
    {
        // Read Student ID
        moreDataInFile = quizDataInput >> studentID;
        if (moreDataInFile)
        {
            // Validate Student ID
            if (studentID > 0)
                ; // Nothing wrong here
            else
            {
                corruptedFile = true;
            }

            // Read quizzes
            quizSum = 0;
            quizGrade = 0;
            for (quizzesRead = 0; quizzesRead < NUMBER_OF_QUIZZES; quizzesRead++)
            {
                quizDataInput >> quizGrade;
                // Validate quiz grade
                if (quizGrade >= 0 && quizGrade <= 100)
                    ; // Nothing wrong here
                else
                {
                    corruptedFile = true;
                }
                quizSum += quizGrade;
            }

            // Add quizzes to class quizzes
            classTotal += quizSum;
            classQuizzes += quizzesRead;

            // Calculate/Display quiz average
            if (quizzesRead == 0)
                cout << "Student " << studentID << " did not take any quizzes" << endl;
            else
            {
                quizAverage = quizSum / (double) quizzesRead;
                cout << setprecision(2) << fixed << "Student " << studentID << 
                    " Quiz Average: " << quizAverage << endl;
            }
        }
        else
            allInformationRead = true;
    }

    // Calculate class average
    if (classQuizzes == 0)
        cout << "The entire class has not taken any quizzes!" << endl;
    else
    {
        classAverage = classTotal / (double) classQuizzes;
        cout << endl << "Class Average: " << classAverage;
    }

    // Display File Corruption Warning
    if (corruptedFile)
        cout << "---This file may be corrupted---" << endl;

    // Add space
    cout << endl << endl;

    system("pause");
    return 0;
}
