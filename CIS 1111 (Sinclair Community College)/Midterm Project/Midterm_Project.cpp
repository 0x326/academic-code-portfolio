// John Meyer
// June 20, 2015
// Story Teller
// This program creates varying stories based on user input

#include <iostream>
#include <string>
#include <stdlib.h>
#include <cmath>

using namespace std;

int main ()
{
    // Define Variables
    string characterName = "JOE";
    string characterPronoun = "HE";
    string characterPossessivePronoun = "HIS";
    string characterObjectPronoun = "HIM";
    string characterGender = "M";
    string guestsPronoun = "They";
    string questLocation = "A faraway land";
    string typeOfGuests = "Elves";
    string guestAction = "to sing";
    string characterResponse = "did nothing";

    bool characterIsMale = true;

    short characterAge = 13;
    short hourOfDay = 4.0;
    
    int numberOfGuests = 13;
    
    long daysOfGuestActivity = 40;

    unsigned short calculation1 = 0;
    unsigned short calculation2 = 0;
    unsigned short calculation3 = 0;
    unsigned short calculation4 = 0;

    // Define Flag Variables
    bool errorNotValidGender = false;
    bool errorCharacterTooYoung = false;
    bool errorHourOfDayNotValid = false;
    bool errorNumberOfGuestsTooSmall = false;
    bool errorDaysOfGuestActivityTooShort = false;
   
    // Announce Program Title
    cout << "Story Teller by John Meyer" << endl;

    // Display Instructions
    cout << "When you see [square brackets], enter a number " << 
        "greater than zero." << endl << endl;

    // Ask for the character's name and gender
    cout << "What is the character's name?" << endl;
    getline(cin, characterName);
    cout << "Is the character male or female? M/F" << endl;
    cin >> characterGender;

    // Validate gender input (Name is assumed to be correct)
    if (characterGender[0] == 'M' || characterGender[0] == 'm')
        characterIsMale = true;
    else if (characterGender[0] == 'F' || characterGender[0] == 'f')
        characterIsMale = false;
    else
    {   // Error 1: Gender not valid
        errorNotValidGender = true;
        cout << "The gender you inputed is not valid" << endl;
        system("pause");
        exit(1);
    }

    // Change gender-specific parts of speech
    if (characterIsMale)
    {
        characterPronoun = "HE";
        characterPossessivePronoun = "HIS";
        characterObjectPronoun = "HIM";
    }
    else
    {
        characterPronoun = "SHE";
        characterPossessivePronoun = "HER";
        characterObjectPronoun = "HER";
    }

    ///// Begin the story /////
    // Display Beginning and Ask for age
    cout << "Once upon a time, there was an adventurer named " << characterName <<
        ". " << endl << characterPronoun << " was [ number ] years old." << endl;
    cin >> characterAge;

    // Validate Character Age
    if (characterAge > 0)
        // All is well
        ;
    else
    {   // Error 2: Negative Age
        errorCharacterTooYoung = true;
        cout << "The character has yet to be born! Give " << characterObjectPronoun << " a positive value." <<
            endl;
        system("pause");
        exit(2);
    }

    // Calculate Quest Location
    calculation1 = (characterAge * 70) % 3;
    switch (calculation1)
    {
        case 0:
            questLocation = "the Shire";
            break;
        case 1:
            questLocation = "Narnia";
            break;
        case 2:
            questLocation = "the Caribbean";
            break;
    }

    // Display Location and ask for the Hour of the Day
    cout << characterPronoun << " went on a quest to " << questLocation << ". " <<
        "At the early hour of [ number ]." << endl;
    cin >> hourOfDay;

    // Validate Hour of the Day
    if (hourOfDay > 0 && hourOfDay <= 12)
        // All is good
        ;
    else
    {   // Error 3: Hour of the Day Not Valid
        errorHourOfDayNotValid = true;
        cout << "The hour should in 12-hour-clock format.  It should be 1-12" << endl;
        system("pause");
        exit(3);
    }

    // Calculate the Type of Guests
    calculation2 = (long long) pow(hourOfDay, 5.0) % 3;
    switch (calculation2)
    {
        case 0:
            typeOfGuests = "dwarves";
            break;
        case 1:
            typeOfGuests = "fauns";
            break;
        case 2:
            typeOfGuests = "pirates";
            break;
    }

    // Display Type of Guests and Ask how many
    cout << characterPronoun << " had an encounter with " << typeOfGuests <<
        endl << " of which there were [ number ]. " << endl;
    cin >> numberOfGuests;

    // Validate Number of Guests
    if (numberOfGuests > 1) // EXCEPTION: Guests must be greater than one;
        // All is good
        ;
    else
    {   // Error 4: Too Few Guests
        errorNumberOfGuestsTooSmall = true;
        cout << "What a small party " << characterPronoun << 
            " is having! Enter a number greater than one. " << endl <<
            "This is an exception to the rule. " << endl;
        system("pause");
        exit(4);
    }

    // Calculate Guest Action
    calculation3 = (long long) pow(numberOfGuests, 3.0) % 3;
    switch (calculation3)
    {
        case 0:
            guestAction = "to eat all the food";
            break;
        case 1:
            guestAction = "to kidnap " + characterObjectPronoun;
            break;
        case 2:
            guestAction = "to steal all " + characterPossessivePronoun + " goods";
            break;
    }

    // Display Guest Action and Ask for the Duration
    cout << "They decided " << guestAction << ". " << endl << 
        "This happened over the course of [ number ] days" << endl;
    cin >> daysOfGuestActivity;

    // Validate Duration of Guest Activity
    if (daysOfGuestActivity > 0)
        // All is good
        ;
    else
    {   // Error 5: Duration of Guest Activity Too Short
        errorDaysOfGuestActivityTooShort = true;
        cout << "The duration of guest activity should be greater than zero days. " << endl;
        system("pause");
        exit(5);
    }

    // Calculate Character's Response
    calculation4 = (long long) pow(daysOfGuestActivity, 7.0) % 3;
    switch (calculation4)
    {
        case 0:
            characterResponse = "moved away";
            break;
        case 1:
            characterResponse = "was reimbursed";
            break;
        case 2:
            characterResponse = "came to an agreement with them";
            break;
    }

    // Display Character's Response
    cout << "but " << characterPronoun << " eventually " <<
        characterResponse << " and all was well. ";

    // Display Divider
    cout << endl << endl << "--------------------" << endl << endl;

    // Reprint Entire Story
    cout << "Once upon a time, there was an adventurer named " << characterName << ". " << endl << 
        characterPronoun << " was " << characterAge << " years old. " << endl <<
        characterPronoun << " went on a quest to " << questLocation << ". " << endl <<
        "At the early hour of " << hourOfDay << ", " << characterPronoun <<
        " had an encounter with " << typeOfGuests << endl << " of which there were " <<
        numberOfGuests << ". " << endl << "They decided " << guestAction << ". " << endl <<
        "This happened over the course of " << daysOfGuestActivity << " days " << endl <<
        " but " << characterPronoun << " eventually " << characterResponse << 
        " and all was well." << endl << endl << "The End" << endl << endl;

    system("pause");
    return 0;
}