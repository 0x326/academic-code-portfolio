import java.util.Iterator;
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
    private HashMap<Integer, SortedLinkedList<String>> wordPopularity;

    public StudentLookup() {
        this(10000);
    }

    public StudentLookup(int initialCapacity) {
        this.wordCounts = new HashMap<>(initialCapacity);
        this.wordPopularity = new HashMap<>(initialCapacity);
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
        }

        // Remove from old popularity list
        SortedLinkedList oldPopularityList = wordPopularity.get(count);
        if (oldPopularityList != null) {
            oldPopularityList.remove(string);
        }

        count += amount;

        // Add to dictionary
        wordCounts.put(string, count);

        // Add to new popularity list
        SortedLinkedList newPopularityList = wordPopularity.get(count);
        if (newPopularityList == null) {
            newPopularityList = new SortedLinkedList(false);
        }
        newPopularityList.add(string);
        wordPopularity.put(count, newPopularityList);
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

        int mostPopularIndex = 0;

        Iterator<Integer> popularities = wordPopularity.iterKeys();
        while (popularities.hasNext()) {
            SortedLinkedList<String> words = wordPopularity.get(popularities.next());
            int numberOfWords = words.size();

            if (mostPopularIndex == n) {
                return words.get(mostPopularIndex);
            } else {
                mostPopularIndex += numberOfWords;
            }
        }

        return null;
    }

    @Override
    public int numEntries() {
        return numberOfWords;
    }
    
}
