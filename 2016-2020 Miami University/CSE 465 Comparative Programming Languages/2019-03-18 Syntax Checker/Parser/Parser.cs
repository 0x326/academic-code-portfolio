using System;
using System.IO;

namespace Parser
{
    public enum ParseStatus
    {
        Valid,
        InValid,
        InValidPrefix
    }

    public class Parser
    {
        private const int EndOfReader = -1;
        private readonly TextReader _source;

#if DEBUG
        private int _parseTreeDepth;
        private const int IndentSize = 4;

        private void WriteParseTreeLocation(string node)
        {
            for (var i = 0; i < _parseTreeDepth; i++)
            {
                Console.Write('|');
                Console.Write(new string(' ', IndentSize - 1));
            }

            Console.WriteLine(node);
        }

        private void WriteParseTreeTerminal(char node)
        {
            WriteParseTreeLocation("\"" + node.ToString() + "\"");
        }
#endif

        public static int Main(string[] args)
        {
            Console.WriteLine("Enter a simplified Lisp program:");

            using (var stdin = Console.OpenStandardInput())
            {
                var parser = new Parser(stdin);

                var isValid = parser.ValidateSource();

                if (isValid)
                {
                    Console.WriteLine("Syntax OK");
                    return 0;
                }

                Console.WriteLine("Syntax Error");
                return 1;
            }
        }

        public Parser(TextReader source)
        {
            _source = source;
        }

        public Parser(Stream source)
        {
            _source = new StreamReader(source);
        }

        public Parser(string source)
        {
            _source = new StringReader(source);
        }

        private bool Accept(Func<char, bool> condition)
        {
#if DEBUG
            _parseTreeDepth++;
#endif

            var token = (char) _source.Peek();

            if (token == EndOfReader)
            {
                return false;
            }

            if (!condition(token))
            {
#if DEBUG
                WriteParseTreeLocation("(FAILED)");
                _parseTreeDepth--;
#endif
                return false;
            }

#if DEBUG
            WriteParseTreeTerminal(token);
            _parseTreeDepth--;
#endif

            _source.Read();

            return true;
        }

        private bool AcceptCharacter(char c)
        {
            return Accept(token => token == c);
        }

        private ParseStatus ValidateEmpty()
        {
#if DEBUG
            _parseTreeDepth++;
            WriteParseTreeLocation("<empty>");
#endif

            var isValid = Accept(char.IsWhiteSpace);

#if DEBUG
            _parseTreeDepth--;
#endif

            return isValid ? ParseStatus.Valid : ParseStatus.InValid;
        }

        private ParseStatus ValidateNumber()
        {
#if DEBUG
            _parseTreeDepth++;
            WriteParseTreeLocation("<number>");
#endif

            var isValid = Accept(char.IsDigit);

#if DEBUG
            _parseTreeDepth--;
#endif

            return isValid ? ParseStatus.Valid : ParseStatus.InValid;
        }

        private ParseStatus ValidateLetter()
        {
#if DEBUG
            _parseTreeDepth++;
            WriteParseTreeLocation("<letter>");
#endif

            var isValid = Accept(char.IsLetter);

#if DEBUG
            _parseTreeDepth--;
#endif

            return isValid ? ParseStatus.Valid : ParseStatus.InValid;
        }

        private ParseStatus ValidateAtomPart()
        {
#if DEBUG
            _parseTreeDepth++;
            WriteParseTreeLocation("<atom_part>");
#endif

            if (ValidateEmpty() == ParseStatus.Valid)
            {
#if DEBUG
                _parseTreeDepth--;
#endif

                return ParseStatus.Valid;
            }

            if (ValidateLetter() != ParseStatus.Valid && ValidateNumber() != ParseStatus.Valid)
            {
#if DEBUG
                _parseTreeDepth--;
#endif

                return ParseStatus.InValidPrefix;
            }

            var isValidateAtomPart = ValidateAtomPart();

#if DEBUG
            _parseTreeDepth--;
#endif

            return isValidateAtomPart == ParseStatus.Valid ? ParseStatus.Valid : ParseStatus.InValid;
        }

        private ParseStatus ValidateAtomicSymbol()
        {
#if DEBUG
            _parseTreeDepth++;
            WriteParseTreeLocation("<atomic_symbol>");
#endif

            if (ValidateLetter() != ParseStatus.Valid)
            {
#if DEBUG
                _parseTreeDepth--;
#endif

                return ParseStatus.InValidPrefix;
            }

            var isValidateAtomPart = ValidateAtomPart();

#if DEBUG
            _parseTreeDepth--;
#endif

            return isValidateAtomPart == ParseStatus.Valid ? ParseStatus.Valid : ParseStatus.InValid;
        }

        private ParseStatus ValidateListPart()
        {
#if DEBUG
            _parseTreeDepth++;
            WriteParseTreeLocation("<list_part>");
#endif

            if (ValidateSExpression() != ParseStatus.Valid)
            {
#if DEBUG
                _parseTreeDepth--;
#endif

                return ParseStatus.InValidPrefix;
            }

            var isValidList = ValidateListPart();

#if DEBUG
            _parseTreeDepth--;
#endif

            return isValidList == ParseStatus.InValid ? ParseStatus.InValid : ParseStatus.Valid;
        }

        private ParseStatus ValidateList()
        {
#if DEBUG
            _parseTreeDepth++;
            WriteParseTreeLocation("<list>");
#endif

            if (!AcceptCharacter('('))
            {
#if DEBUG
                _parseTreeDepth--;
#endif

                return ParseStatus.InValidPrefix;
            }

            var isValidList = ValidateSExpression() == ParseStatus.Valid && ValidateListPart() == ParseStatus.Valid &&
                              AcceptCharacter(')');

#if DEBUG
            _parseTreeDepth--;
#endif

            return isValidList ? ParseStatus.Valid : ParseStatus.InValid;
        }

        private ParseStatus ValidateSExpression()
        {
#if DEBUG
            _parseTreeDepth++;
            WriteParseTreeLocation("<s_expression>");
#endif

            var isAtomicSymbol = ValidateAtomicSymbol();

            var isValidSExpression = isAtomicSymbol == ParseStatus.InValidPrefix ? ValidateList() : isAtomicSymbol;

#if DEBUG
            _parseTreeDepth--;
#endif

            return isValidSExpression;
        }

        public bool ValidateSource()
        {
            var isValid = true;

            while (isValid && _source.Peek() != EndOfReader)
            {
                isValid &= ValidateSExpression() == ParseStatus.Valid;

                // Remove trailing newlines
                while (AcceptCharacter('\n'))
                {
                    // Do nothing
                }
            }

            return isValid;
        }
    }
}
