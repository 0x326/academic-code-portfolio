// John Meyer
// July 7, 2015
// Contest Winner Calculator
// This program asks for the contestants and for their scores 
//  then calculates the contestant with the highest score.

#include <iostream>
#include <iomanip>
#include <string>

using namespace std;

bool isScoreValid(short);
bool isNumber(string);
double calcAvgScore(short, short, short, short, short);
int findHighest(short, short, short, short, short);
int findLowest(short, short, short, short, short);

// Function main
int main()
{
    // Define Variables
    double highScore = 0.0;
    string highScorePerson = "";
    string contestantName;
    short judgeScore[5];
    double contestantScore;

    // Define Flags
    bool allContestantsEntered = false;
    bool judgeScoreValid;

    // Set decimal precision
    cout << setprecision(2) << fixed;

    // Give user instructions
    cout << "Enter a number when all contestants are entered" << endl;

    // Loop until all contestants are entered
    while (!allContestantsEntered)
    {
        // Ask for contestant's name
        cout << endl << "Contestant Name:" << endl;
        cin >> contestantName;
        if (isNumber(contestantName))
            allContestantsEntered = true;
        else
        {
            // Ask for judges scores
            for (unsigned short scoreNumber = 1; scoreNumber < 6; scoreNumber++)
            {
                judgeScoreValid = false;
                while (!judgeScoreValid)
                {
                    cout << "Enter judge " << scoreNumber << " score:" << endl;
                    cin >> judgeScore[scoreNumber - 1];
                    // Validate scores
                    if (isScoreValid(judgeScore[scoreNumber - 1]))
                        judgeScoreValid = true;
                    else
                        cout << "The score must not be less than 1 or greater than 10" << endl;
                }
            }
            // Calculate contestant's average score
            contestantScore = calcAvgScore(judgeScore[0], judgeScore[1], judgeScore[2], judgeScore[3], judgeScore[4]);
            // Check for a high score
            if (contestantScore > highScore)
            {
                highScore = contestantScore;
                highScorePerson = contestantName;
            }
        }
    }
    // Display winning contestant (high score)
    cout << endl << "The winner is: " << highScorePerson << " with a score of " << highScore << endl;


    cout << endl;
    system("pause");
    return 0;
}

// Function isScoreValid: Calculates whether a judges score is within the appropriate range
bool isScoreValid(short score)
{
    if (score <= 10 && score >= 1)
        return true;
    else
        return false;
}

// Function isNumber: determines whether the first character is a number
bool isNumber(string stringInQuestion)
{
    switch (stringInQuestion[0])
    {
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            return true;
            break;
        default:
            return false;
    }
}

// Function calcAvgScore: calculates the contestants average score 
//  (excluding the highest and lowest of the judges' scores)
double calcAvgScore(short score1, short score2, short score3, short score4, short score5)
{
    // Define Variables
    int scoreSum;
    double scoreAverage;

    // Begin calculations
    scoreSum = score1 + score2 + score3 + score4 + score5;
    scoreSum -= findLowest(score1, score2, score3, score4, score5);
    scoreSum -= findHighest(score1, score2, score3, score4, score5);
    scoreAverage = scoreSum / 3.0;
    return scoreAverage;
}

// Function findHighest: finds the highest score of the five scores passed to it
int findHighest(short score1, short score2, short score3, short score4, short score5)
{
    // Define Variables
    short highestScore = 0;

    // Begin Comparisons
    if (score1 > highestScore)
        highestScore = score1;
    if (score2 > highestScore)
        highestScore = score2;
    if (score3 > highestScore)
        highestScore = score3;
    if (score4 > highestScore)
        highestScore = score4;
    if (score5 > highestScore)
        highestScore = score5;
    return highestScore;
}

// Function findLowest: finds the lowest score of the five scores passed to it
int findLowest(short score1, short score2, short score3, short score4, short score5)
{
    // Define Variables
    short lowestScore = 10;

    // Begin Comparisons
    if (score1 < lowestScore)
        lowestScore = score1;
    if (score2 < lowestScore)
        lowestScore = score2;
    if (score3 < lowestScore)
        lowestScore = score3;
    if (score4 < lowestScore)
        lowestScore = score4;
    if (score5 < lowestScore)
        lowestScore = score5;
    return lowestScore;
}
