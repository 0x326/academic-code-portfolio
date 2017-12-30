// John Meyer
// July 20, 2015
// Journal to Ledger Converter
// This program converts an accounting General Journal to individual account ledgers.
//  This program saves the ledgers to individual files.  Then this program reads the
//  information from the files and displays the ledgers. 
//  This program supports up to 50 accounts.

#include <iostream>
#include <iomanip>
#include <string>
#include <fstream>
#include <cmath>

using namespace std;

int findInArray(const string[], string, long);
string stringOfThisManyChars(string, int);
string stringOfThisManyChars(char, int);
string getStreamUntilSeparator(ifstream &, long long = -1LL);

int main()
{
    // Display Progam Name
    cout << "Journal to Ledger Converter" << endl;
    cout << stringOfThisManyChars('-', 30) << endl << endl;

    // Define Constants
    const long MAX_NUMBER_OF_ACCOUNTS = 50;
    const int DEBIT_STATEMENT = 0;
    const int CREDIT_STATEMENT = 1;
    const int SOURCE_DOCUMENT = 2;
    const string OUTPUT_FILE_PREFIX = "J2L ";

    const int ERROR_FILENAME_INVALID = 1;
    const int ERROR_CANT_OPEN_FILE = 2;
    const int ERROR_GENERAL_JOURNAL_NOT_VALID = 3;

    ////////// READ AND TRANSLATE JOURNAL //////////
    // Define Variables
    ifstream generalJournal;
    string generalJournalFileLocation;
    bool invalidFilename;
    string column1OfJournal, column2OfJournal, column3OfJournal, column4OfJournal;
    bool generalJournalFileOpened = false;
    bool fileHeaderVerified = false;
    bool generalJournalFileHasMoreData = true;

    string listOfAccounts[MAX_NUMBER_OF_ACCOUNTS] = {""};
    string accountLedgers[MAX_NUMBER_OF_ACCOUNTS] = {""};
    long double accountBalances[MAX_NUMBER_OF_ACCOUNTS] = {0.0L};
    long numberOfAccounts = 0L;

    unsigned short typeOfJournalStatement; // Zero: Debit, One: Credit, Two: Source Document
    string transactionDate;
    string transactionAccountName;
    int transactionAccountArrayIndex;
    string transactionSourceDocument;
    long double transactionAmount; // Debits will be recorded as positive; credits will be recorded as negative

    // Ask for file location
    cout << "General Journal File Location: " << endl;
    getline(cin, generalJournalFileLocation);

    // Insert Space
    cout << endl;
    
    // Validate User Input
    if (generalJournalFileLocation.find('*') == -1)
        invalidFilename = false;
    else if (generalJournalFileLocation.find('?') == -1)
        invalidFilename = false;
    else if (generalJournalFileLocation.find('"') == -1)
        invalidFilename = false;
    else if (generalJournalFileLocation.find('<') == -1)
        invalidFilename = false;
    else if (generalJournalFileLocation.find('>') == -1)
        invalidFilename = false;
    else if (generalJournalFileLocation.find('|') == -1)
        invalidFilename = false;
    else
    {
        invalidFilename = true;
        cout << "Filename is not valid" << endl;
        cout << "Program is closing" << endl;
        system("pause");
        exit(ERROR_FILENAME_INVALID);
    }

    // Open General Journal
    generalJournal.open(generalJournalFileLocation);
    
    // Check if file successfully opened
    generalJournalFileOpened = !generalJournal.fail();
    if (generalJournalFileOpened)
    {
        ; // Continue
    }
    else
    {
        cout << "File did not open" << endl;
        cout << "Program is closing" << endl;
        system("pause");
        exit(ERROR_CANT_OPEN_FILE);
    }

    // Verify header in the file
    // Should be in the format: Date Description Debit Credit Debit Credit
    generalJournalFileHasMoreData = generalJournal >> column1OfJournal >> column2OfJournal >> column3OfJournal >> column4OfJournal;
    if (column1OfJournal == "Date" && column2OfJournal == "Description" && 
        column3OfJournal == "Debit" && column4OfJournal == "Credit")
    {
        fileHeaderVerified = true;
    }
    else
    {
        fileHeaderVerified = false;
        cout << "General Journal file is not valid." << endl;
        cout << "Program is closing" << endl;
        system("pause");
        exit(ERROR_GENERAL_JOURNAL_NOT_VALID);
    }

    // Read general journal - create ledgers in memory
    typeOfJournalStatement = 0; // Debit
    while (generalJournalFileHasMoreData)
    {
        // Get first column (date)
        if (typeOfJournalStatement == DEBIT_STATEMENT) // The date is only written on the debit line
            generalJournal >> transactionDate;
        // Get second column (account name/source document)
        if (typeOfJournalStatement == DEBIT_STATEMENT || typeOfJournalStatement == CREDIT_STATEMENT)
        {
            generalJournalFileHasMoreData = generalJournal >> transactionAccountName;
        }
        else
            generalJournalFileHasMoreData = generalJournal >> transactionSourceDocument;
        // Get third/fourth column (transaction ammount)
        if (typeOfJournalStatement == DEBIT_STATEMENT || typeOfJournalStatement == CREDIT_STATEMENT)
        {
            generalJournal >> transactionAmount;
            transactionAmount = abs(transactionAmount);
        }
        
        // Break if file is out of data
        if (!generalJournalFileHasMoreData)
            break;

        // Add to account ledger
        // Search List of Accounts - Check if this account this new
        if (typeOfJournalStatement == DEBIT_STATEMENT || typeOfJournalStatement == CREDIT_STATEMENT)
        {
            transactionAccountArrayIndex = findInArray(listOfAccounts, transactionAccountName, numberOfAccounts);
            switch (transactionAccountArrayIndex)
            {
                case -1:
                    // Announce that a new account has been found
                    cout << "Found New Account: " << transactionAccountName << endl;
                    cout << " Creating New Ledger" << endl;
                    // Create new account and note starting balance
                    transactionAccountArrayIndex = numberOfAccounts++;
                    listOfAccounts[transactionAccountArrayIndex] = transactionAccountName;
                    accountLedgers[transactionAccountArrayIndex] = (string) "Date" + "\t" + "Description" + "\t" + "Debit" + "\t" + "Credit" + "\t" + "Debit" + "\t" + "Credit" + "\n";
                    accountLedgers[transactionAccountArrayIndex] += transactionDate + "\t" + "Starting_Balance" + "\t\t\t" + to_string(accountBalances[transactionAccountArrayIndex]) + "\t\n";
                default:
                    // Create new line
                    if (typeOfJournalStatement == DEBIT_STATEMENT)
                    {
                        accountLedgers[transactionAccountArrayIndex] += transactionDate + "\t\t" + to_string(transactionAmount) + "\t\t"; // Now in fifth column
                        // Update account balance
                        accountBalances[transactionAccountArrayIndex] += transactionAmount;
                    }
                    else if (typeOfJournalStatement == CREDIT_STATEMENT)
                    {
                        accountLedgers[transactionAccountArrayIndex] += transactionDate + "\t\t\t" + to_string(transactionAmount) + "\t";
                        // Update account balance
                        accountBalances[transactionAccountArrayIndex] += -transactionAmount; // Credits will be recorded in memory as negative
                    }
                    // Add account balance to ledger
                    accountLedgers[transactionAccountArrayIndex] += accountBalances[transactionAccountArrayIndex] >= 0 ? // If the balance is less than zero it is a credit (in the sixth column)
                        to_string(accountBalances[transactionAccountArrayIndex]) + "\t" :
                        "\t" + to_string( -accountBalances[transactionAccountArrayIndex]); // Account balance is negated to undo previous negation for storage
                    // Add newline
                    accountLedgers[transactionAccountArrayIndex] += "\n";
            }
        }
        else
            ; // Do nothing.  Source Documents are not recorded in individual ledgers
        // Clear variables
        transactionDate; // This variable will persist so that credits can have the date
        transactionAccountName = transactionSourceDocument = "";
        transactionAmount = 0.00L;

        // Increment Counter (will cycle through 0, 1, 2)
        ++typeOfJournalStatement %= 3;
    }
    ////////// WRITE LEDGERS //////////
    // Define Variables
    ofstream ledgerOutputFile;
    string ledgerFileName;
    string ledgerOutputFolder;

    // Ask for output folder
    cout << endl << "In what folder would you like the ledger files to be?" << endl;
    getline(cin, ledgerOutputFolder);
    // Insert Space
    cout << endl;
    // Cycle through all the ledgers in memory
    for (int ledgersCompleted = 0; ledgersCompleted < numberOfAccounts; ledgersCompleted++)
    {
        // Pick filename
        ledgerFileName = OUTPUT_FILE_PREFIX + listOfAccounts[ledgersCompleted] + ".txt";
        // Announce which file is being written
        cout << "Writing File: " << ledgerFileName << endl;
        // Write ledger to file
        ledgerOutputFile.open(ledgerOutputFolder + ledgerFileName);
        ledgerOutputFile << accountLedgers[ledgersCompleted];
        // Close file
        ledgerOutputFile.close();
    }
    // Insert Space
    cout << endl;

    ////////// READ LEDGERS //////////
    // Define Variables
    ifstream ledgerInputFile;
    string ledgerLineDate, ledgerLineDescription, ledgerLineTransactionDebit,
        ledgerLineTransactionCredit, ledgerLineAccountBalanceDebit,
        ledgerLineAccountBalanceCredit;
    bool ledgerInputEndOfFile = false;
    int ledgerLine;

    // Open files
    for (int ledgersRead = 0; ledgersRead < numberOfAccounts; ledgersRead++)
    {
        // Open ledger file
        ledgerFileName = OUTPUT_FILE_PREFIX + listOfAccounts[ledgersRead] + ".txt";
        ledgerInputFile.open(ledgerOutputFolder + ledgerFileName);
        if (ledgerInputFile.fail())
        {
            // Announce that the file cannot be opened
            cout << "Error opening file: " << ledgerFileName << endl << endl;
            continue;
        }
        // Display file name
        cout << ledgerFileName << endl << stringOfThisManyChars("=====", 6) << endl;
        // Reset counter
        ledgerLine = 1;
        // Display Ledger
        do
        {
            // Get Ledger Details
            ledgerLineDate = getStreamUntilSeparator(ledgerInputFile);
            // Check if at the end of the file
            if (ledgerLineDate == "\0")
            {
                ledgerInputEndOfFile = true;
                break;
            }
            else
                ledgerInputEndOfFile = false;
            ledgerLineDescription = getStreamUntilSeparator(ledgerInputFile);
            ledgerLineTransactionDebit = getStreamUntilSeparator(ledgerInputFile);
            ledgerLineTransactionCredit = getStreamUntilSeparator(ledgerInputFile);
            ledgerLineAccountBalanceDebit = getStreamUntilSeparator(ledgerInputFile);
            ledgerLineAccountBalanceCredit = getStreamUntilSeparator(ledgerInputFile);
            // Add space to all values for minimum spacing
            ledgerLineDescription += " ";
            ledgerLineTransactionDebit += " ";
            ledgerLineTransactionCredit += " ";
            ledgerLineAccountBalanceDebit += " ";
            ledgerLineAccountBalanceCredit += " ";
            // Display Ledger Line
            cout << right; // Align text to the right
            cout << setw(8) << ledgerLineDate;
            cout << setw(20) << ledgerLineDescription;
            cout << setw(10) << ledgerLineTransactionDebit;
            cout << setw(10) << ledgerLineTransactionCredit;
            cout << setw(10) << ledgerLineAccountBalanceDebit;
            cout << setw(10) << ledgerLineAccountBalanceCredit;
            cout << endl;
            ledgerLine++;
        } while (!ledgerInputEndOfFile);
        // Close the ledger file
        ledgerInputFile.close();
        // Insert Space
        cout << endl << endl;
    }

    cout << endl << endl;
    system("pause");
}

int findInArray(const string arrayToSearch[], string keyWord, long lengthOfArray)
{
    long searchResult = -1;
    for (long searchIndex = 0; searchIndex < lengthOfArray; searchIndex++)
    {
        if (arrayToSearch[searchIndex] == keyWord)
        {
            searchResult = searchIndex;
            break;
        }
    }
    return searchResult;
}

string stringOfThisManyChars(string stringWanted, int numberOfThemWanted)
{
    string result = "";
    for (int numberAdded = 0; numberAdded < numberOfThemWanted; numberAdded++)
        result += stringWanted;
    return result;
}
string stringOfThisManyChars(char characterWanted, int numberOfThemWanted)
{
    string result = "";
    char characterAsArray[2] = {characterWanted, '\0'}; // For easy conversion to a string
    for (int numberAdded = 0; numberAdded < numberOfThemWanted; numberAdded++)
        result += (string) characterAsArray;
    return result;
}
string getStreamUntilSeparator(ifstream &inputStream, long long limit)
{
    // Define Variables
    bool isCharSentinel = false;
    string streamExtract = "";
    long long charactersExtracted = 0LL;
    char inputChar[2] = {'\0', '\0'}; // Store character in char array so it can be coverted to string
    // Extract Stream
    do
    {
        // Get a character
        inputChar[0] = inputStream.get();
        charactersExtracted++;
        // Test if file is out of data
        if (inputChar[0] == (char) -1) // File is out of data
        {
            if (charactersExtracted == 1)
            {
                streamExtract = "\0"; // A sentinel for "no more data in stream"
                break;
            }
            else
                break; // Stop loop to return what is extracted so far
        }
        // Test if the character is a sentinel
        if (inputChar[0] >= 'a' && inputChar[0] <= 'z')
            isCharSentinel = false;
        else if (inputChar[0] >= 'A' && inputChar[0] <= 'Z')
            isCharSentinel = false;
        else if (inputChar[0] >= '0' && inputChar[0] <= '9')
            isCharSentinel = false;
        else if (inputChar[0] == '.' || inputChar[0] == '\\' || inputChar[0] == '/')
            isCharSentinel = false;
        else if (inputChar[0] == '_')
            isCharSentinel = false;
        else
            isCharSentinel = true;
        // If it is not a sentinel, add it to the string
        if (!isCharSentinel)
            streamExtract += (string) inputChar;
        // Check if the number of extracted characters goes beyond the limit
        if (charactersExtracted == limit)
            break;
    } while (!isCharSentinel);
    return streamExtract;
}
