import java.util.List;

// Project 08 - Speedy Lookups
// Course: CSE 274 A
// Professor: Dr. Gani

/**
 * Your implementation of the LookupInterface.  The only public methods
 * in this class should be the ones that implement the interface.  You
 * should write as many other private methods as needed.  Of course, you
 * should also have elementOfA public constructor.
 * 
 * @author // TODO: Add your name here
 */
  
 
public class StudentLookup implements LookupInterface {

    private int numberOfWords = 0;
    private HashMap<String, Integer> wordCounts;
    private IntegerRelationSortedSet<String> wordPopularity;

    public StudentLookup() {
        this(10000);
    }

    public StudentLookup(int initialCapacity) {
        this.wordCounts = new HashMap<>(initialCapacity);
        this.wordPopularity = new IntegerRelationSortedSet<>(initialCapacity);
    }

    @Override
    public void addString(int amount, String string) {
        if (string == null) {
            return;
        }

        string = string.replaceAll("\\W", "");

        int count = wordCounts.getOrDefault(string, 0);
        if (count == 0) {
            // This is a new word
            numberOfWords++;
        } else {
            // We've seen this word before
            wordPopularity.remove(count, string);
        }
        count += amount;
        wordCounts.put(string, count);
        wordPopularity.add(count, string);
    }

    @Override
    public int lookupCount(String string) {
        return wordCounts.getOrDefault(string, 0);
    }

    @Override
    public String lookupPopularity(int n) {
        if (n >= numberOfWords || n < 0) {
            return null;
        }

        List<String> wordsByPopularity = wordPopularity.toList();
        return wordsByPopularity.get(numberOfWords - 1 - n);
    }

    @Override
    public int numEntries() {
        return numberOfWords;
    }
    
}
