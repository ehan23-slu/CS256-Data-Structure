import java.sql.SQLOutput;
import java.util.*;

/**
 * Stores all of the dictionary data and provides methods for solving the puzzle
 */
public class Letterman {

    private Config c;

    // variable to store our dictionary
    private ArrayList<WordInfo> dictionary;
    private int beginIndex, endIndex;


    public Letterman(Config c) {
        this.c = c;
    }

    /**
     * read the dictionary from standard input
     */
    public void readDictionary() {
        // Scanner object
        Scanner in = new Scanner(System.in);

        // get number of words in dictionary
        int count = in.nextInt();

        // read to the end of the line
        in.nextLine();

        // construct our ArrayList
        // fixed size
        dictionary = new ArrayList<>(count);

        // read all of the words
        while (in.hasNextLine()) {
            String line = in.nextLine();

            // check for blank line
            if (line.length() == 0) {
                break;
            }

            // either have a word or a comment
            // comment begins with two slashes
            // consider a bug: IndexOutOfBoundsException
            if (line.length() >= 2 && line.charAt(0) == '/' && line.charAt(1) == '/') {
                // comment
                continue;
            }

            // check if this is the begin word
            if (line.equals(c.getBeginWord())) {
                beginIndex = dictionary.size(); // nothing in dictionary yet (size = 0) // mark beginIndex as 0
            }

            // we have a word
            dictionary.add(new WordInfo(line));
        }

        // print the size of the dictionary
        System.out.println("Words in dictionary: " + count);

        // check if the beginWord and endWord exist in the dictionary
        boolean beginWordExist = false;
        boolean endWordExist = false;

        for (WordInfo w : dictionary) {
            if (w.text.equals(c.getBeginWord())) {
                beginWordExist = true;
            }
            if (w.text.equals(c.getEndWord())) {
                endWordExist = true;
            }
            if (beginWordExist && endWordExist) { // if both words exist, escape the loop
                break;
            }
        }

        if (!beginWordExist) { // begin word doesn't exist
            System.err.println("The begin word does not exist in the dictionary.");
            System.exit(1);
        }

        if (!endWordExist) { // end word doesn't exist
            System.err.println("The end word does not exist in the dictionary.");
            System.exit(1);
        }
    }

    /**
     * output all words in the dictionary
     */
    public void printDictionary() {
        // initialize variable to track the number of words
        int i = 0;
        // initialize variable to track number of usable words
        int usableWordsCount = 0;
        // get begin word
        String beginWord = c.getBeginWord();
        // track lengthMode
        boolean lengthMode = c.isLengthMode();

        // create new ArrayList for usable words
        ArrayList<String> usableWords = new ArrayList<>();
        // enhanced for loop (for-each/for-in loop)
        for (WordInfo w : dictionary) {
            if (lengthMode || w.text.length() == beginWord.length()) {
                usableWords.add(w.text);
                usableWordsCount++;
            }
        }
        // print the number of usable words
        System.out.println("Usable words: " + usableWordsCount);

        // print the usable words with index
        for (String s : usableWords) {
            System.out.println(i++ + " " + s);
        }

    }
    /**
     * Search from a beginning word to an end word
     */
    public void search() {
        // deque to keep track of our reachable collection
        // store the index of the word we're processing from our dictionary AL
        ArrayDeque<Integer> processing = new ArrayDeque<>(); // processing = reachable collection

        // initialize the variable to check how many words checked
        int checkedWords = 0;

        // create list for tracking prev Idx
        List<Integer> prev = new ArrayList<>(dictionary.size());
        for (int i = 0; i < dictionary.size(); i++) {
            prev.add(-1); // setting no previous Idx
        }
        // create list to add sufficiently similar word
        List<String> morph = new ArrayList<>();

        // initially populate this with the starting word
        // mark as visited and add to the deque
        dictionary.get(beginIndex).visited = true;
        // print message for adding word to our collection
        // System.out.println(" adding " + dictionary.get(beginIndex).text);

        // add begin word to the collection
        if (c.isStackMode()) {  // --stack
            processing.addFirst(beginIndex);
        } else { // -- queue
            processing.addLast(beginIndex);
        }
        // increment checkedWords for the initial word added
        checkedWords++;

        // while not empty
        while (!processing.isEmpty()) {
            // remove the next element
            int currIdx;
            if (c.isStackMode()) {
                currIdx = processing.removeFirst(); // --stack
            } else {
                currIdx = processing.removeFirst(); // --queue
            }
            // get the current word to compare with rest of the words in the dictionary
            WordInfo curr = dictionary.get(currIdx);
            // print out the processing message indicating process number
            // System.out.println(processCount++ + ": processing " + curr.text);

            // if the current word is the end word
            if (curr.text.equals(c.getEndWord())) {
                // print out the output
                System.out.println("Solution, " + checkedWords + " words checked.");
                for (int i = currIdx; i != -1; i = prev.get(i)) { // track the previous idx
                    morph.add(0, dictionary.get(i).text); // reverse order
                }
                morph.add(c.getEndWord()); // add the last word (endWord)
                System.out.println("Words in morph: " + morph.size()); // print morph message
                if(c.isWordOutput()) { // W word output mode
                    for (String word : morph) {
                        System.out.println(word); // print each sufficiently similar words
                    }
                } else { // M morph output mode
                    System.out.println(c.getBeginWord()); // show begin word which will be transformed
                    for(int j = 0; j < morph.size(); j++) {
                        modificationOutput(morph.get(j), morph.get(j + 1)); // keep call modification function
                    }
                }
                return; // exit
            }

            // loop through dictionary and check for sufficiently similar items
            for (int i = 0; i < dictionary.size(); i++) {
                // skip ourselves
                if (currIdx == i) {
                    continue;
                }
                WordInfo other = dictionary.get(i);
                // check if the word hasn't been visited and is sufficiently similar
                if (!other.visited && sufficientlySimilar(curr.text, other.text)) {
                    // now mark as true which means visited
                    other.visited = true;
                    // print the debug message when we adding the word in the queue/stack
                    // System.out.println(" adding " + other.text);


                    // add to the deque depends on stack / queue
                    if (c.isStackMode()) { // stack
                        processing.addFirst(i);
                    } else { // queue
                        processing.addLast(i);
                    }

                    // track the index of previous word
                    prev.set(i, currIdx);

                    // check if the added word is the end word
                    if (c.getEndWord().equals(other.text)) {
                        // increment the count of checked words
                        checkedWords++;
                        // printing the final message
                        System.out.println("Solution, " + checkedWords + " words checked.");
                        // same logic with line 176 -188
                        for (i = currIdx; i != -1; i = prev.get(i)) {
                            morph.add(0, dictionary.get(i).text);
                        }
                        morph.add(c.getEndWord());
                        System.out.println("Words in morph: " + morph.size()); // print morph message
                        if(c.isWordOutput()) {
                            for (String word : morph) {
                                System.out.println(word);
                            }
                        } else {
                            System.out.println(c.getBeginWord());
                            for (int j = 0; j < morph.size() - 1; j++) {
                                modificationOutput(morph.get(j), morph.get(j + 1));
                            }
                        }
                        return; // exit
                    }
                    // increment the count of checked words
                    checkedWords++;
                }
            }
        }
        // when we reach here, we cannot be letterman!
        System.out.println("No solution, " + checkedWords + " words checked.");
    }

    private boolean sufficientlySimilar(String a, String b) {
        // change: 1 character difference
        // swap: 2 character different with the two character adjacent and swapped
        // length: 1 character different and 1 character length difference
        int charDifference = 0;
        // initialize the variables to track change index for swap mode
        // -1 indicate that no difference detected yet
        int firstChgIdx = -1;
        int secondChgIdx = -1;

        // Check if the lengths are the same for swap and change modes
        // performance tuning: are there ways to make this code faster?
        // --> maybe check for length mode first
        // length mode: check if exactly 1 character is different and 1 character length is different
        if (c.isLengthMode() && Math.abs(a.length() - b.length()) == 1) { // only 1 character length difference
            // loop through each string
            // off by -1
            // initialize the variables to compare the words that have different length
            String shorter;
            String longer;
            int shorterIdx = 0;
            int longerIdx = 0;
            boolean mismatch = false;

            // There are two cases
            if (a.length() > b.length()) { // 1: when length a > length b
                longer = a;
                shorter = b;
            } else { // 2: when length a < length b
                longer = b;
                shorter = a;
            }
            // while there is letter
            while (shorterIdx < shorter.length()) {
                if (shorter.charAt(shorterIdx) != longer.charAt(longerIdx)) { // have different letter
                    if (mismatch) {
                            return false; // don't put in the process // exit
                    }
                        mismatch = true;
                        longerIdx++; // increment to keep compare
                    } else {
                    // continue
                    shorterIdx++;
                    longerIdx++;
                }
            }
            return true; // can put in the process
        }

        // only check for change and swap if lengths are equal
        if (a.length() != b.length()) {
            return false;
        }
        // are there ways to return as soon as we know we've failed to be sufficiently similar

            // only swap and change will apply
            // go character by character to check for equivalence
            for (int i = 0; i < a.length(); i++) {
                if (a.charAt(i) != b.charAt(i)) {
                    charDifference++;
                    if (firstChgIdx == -1) {
                        firstChgIdx = i; // first difference is detected
                    } else if (secondChgIdx == -1) {
                        secondChgIdx = i; // second difference is detected
                    } else {
                        // for more than 2 character differences, exit
                        return false;
                    }
                }
            }

        // change mode: check if there is at most 1 character difference
        if (c.isChangeMode() && charDifference <= 1) {
            return true;
        }
        // Swap mode: check if exactly 2 characters are swapped
        // hint: might be helpful to keep track of the index of the change
        if (c.isSwapMode() && charDifference == 2) {
            // There should be exactly 2 differences and swapping should resolve it
            return firstChgIdx + 1 == secondChgIdx && a.charAt(firstChgIdx) == b.charAt(secondChgIdx) && a.charAt(secondChgIdx) == b.charAt(firstChgIdx);
        }
        return false; // If no condition matches, return false
    }

    /**
     * Output the modification required to go from string a to string b
     * At this point, we already know one of the morphs applies, we just have to determine which one
     * @param a starting word for the morph
     * @param b ending word for the morph
     */
    private void modificationOutput(String a, String b) {
        // need to find the first difference between string a and b
        int pos = 0;
        // length of the shorter string -1
        int maxPosition = Math.min(a.length(), b.length());

        while(pos < maxPosition) {
            // check for a difference
            if (a.charAt(pos) != b.charAt(pos)) {
                // we have found the position of the change
                break;
            }
            pos++;
        }
        // pos is either (1) the position of the change or (2) the index of the last character in the longer string
        // change, swap, insert, or delete?
        if (a.length() == b.length()) {
            // swap - when adjacent two letters are same
            // s, <position>
            if (pos < a.length() - 1 && a.charAt(pos) == b.charAt(pos + 1) && a.charAt(pos + 1) == b.charAt(pos)) {
                System.out.println("s," + pos);
                // increment skipping next character (already check it --> adjacent latter)
            } else {
                // change c, <position>, <letter>
                System.out.println("c," + pos + "," + b.charAt(pos));
            }
        } else if (a.length() < b.length()) {
            // insert
            // i,<position>,<letter>
            // string b will be longer, so we need the character from b at this position

            // Ex:0123
            // a: let
            // b: leet
            System.out.println("i," + pos + "," + b.charAt(pos));
        } else {
            // delete d, <position>
            System.out.println("d," + pos);
        }
    }

    // inner class (helper class) to store dictionary words
    private class WordInfo {
        String text;
        boolean visited; // to check if the word is already in the processing or not

        public WordInfo(String text) {
            this.text = text;
            this.visited = false;
        }
    }
}
