using System;
using NUnit.Framework;

namespace ParserTests
{
    [TestFixture]
    public class Tests
    {

        [Test]
        public void TestValidateSource()
        {
            var validSExpressions = new[]
            {
                "(a b c )",
                "((a b )c )",
                "((a123 b456 )c789 )",
                "a ",
                "(a b )",
                "(list a b )",
                "(function123 a b )",
            };

            foreach (var validSExpression in validSExpressions)
            {
                Console.WriteLine("Testing: {0}", validSExpression);
                Assert.That(new Parser.Parser(validSExpression).ValidateSource(), Is.True);
            }

            var invalidSExpressions = new[]
            {
                "a",
                "(a)",
                "()",
                "((a123 b456 )",
                "9"
            };

            foreach (var invalidSExpression in invalidSExpressions)
            {
                Console.WriteLine("Testing: {0}", invalidSExpression);
                Assert.That(new Parser.Parser(invalidSExpression).ValidateSource(), Is.False);
            }
        }
    }
}
