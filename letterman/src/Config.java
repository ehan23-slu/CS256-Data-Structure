import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;


/**
 * Store and process all configuration options for this project
 */
public class Config {
    // member variables to store setting

    // stack vs. queue
    private boolean stackMode; // true for stack // false for queue

    // store which routing scheme is set
    boolean setStack = false;
    boolean setQueue = false;

    // morph modes
    private boolean changeMode;
    private boolean swapMode;
    private boolean length;

    // output mode
    private boolean wordOutput = true; // false for modification mode

    // begin word
    private String beginWord;

    // end word
    private String endWord;

    // checkpoints
    private boolean checkpoint1;
    private boolean checkpoint2;

    // Dictionary object
    private Letterman letterman;

    /**
     * Construct our configuration object and process the command line arguments
     * @param args string of the command line arguments
     */
    public Config(String[] args) {
        LongOpt[] longOptions = {
                new LongOpt("stack", LongOpt.NO_ARGUMENT, null, 's'),
                new LongOpt("queue", LongOpt.NO_ARGUMENT, null, 'q'),
                new LongOpt("change", LongOpt.NO_ARGUMENT, null, 'c'),
                new LongOpt("swap", LongOpt.NO_ARGUMENT, null, 'p'),
                new LongOpt("length", LongOpt.NO_ARGUMENT, null, 'l'),
                new LongOpt("output", LongOpt.REQUIRED_ARGUMENT, null, 'o'),
                new LongOpt("begin", LongOpt.REQUIRED_ARGUMENT, null, 'b'),
                new LongOpt("end", LongOpt.REQUIRED_ARGUMENT, null, 'e'),
                new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h'),
                new LongOpt("checkpoint1", LongOpt.NO_ARGUMENT, null, 'x'),
                new LongOpt("checkpoint2", LongOpt.NO_ARGUMENT, null, 'y'),
        };
        // construct the Getopt object
        Getopt g = new Getopt("Letterman", args, "sqcplo:b:e:hxy", longOptions);
        g.setOpterr(true);

        // store the required string argument
        String mode = "";

        // local variable to keep track of the current argument we are processing
        int choice;

        // error checking variable
        boolean routingModeSet = false;

        // loop through all of the arguments
        while((choice = g.getopt()) != -1) {
            switch (choice) {
                case 's':
                    setStack = true;
                    stackMode = true;
                    routingModeSet = true;
                    break;
                case 'q':
                    setQueue = true;
                    stackMode = false;
                    routingModeSet = true;
                    break;
                case 'c':
                    changeMode = true;
                    break;
                case 'p':
                    swapMode = true;
                    break;
                case 'l':
                    length = true;
                    break;
                case 'o':
                    // read the required string argument
                    mode = g.getOptarg();
                    if (!mode.equals("M") && !mode.equals("W")) {
                        // we have an error since we don't have valid argument
                        System.err.println("Only W and M are supported for modes");
                        System.exit(1);
                    }

                    wordOutput = mode.equals("W");

                    break;
                case 'b':
                    beginWord = g.getOptarg();
                    break;
                case 'e':
                    endWord = g.getOptarg();
                    break;
                case 'h':
                    printHelp();
                    break;
                case 'x':
                    checkpoint1 = true;
                    break;
                case 'y':
                    checkpoint2 = true;
                    break;

                default:
                    System.err.println("Unknown command line argument option: " + choice);
                    System.exit(1);
            }
        } // while

        if (setStack && setQueue) { // when there are both stack and queue
            System.err.println("ONLY ONE of stack or queue mode must be set");
            System.exit(1);
        }

        // check that all required argument are provided
        if (!routingModeSet) { // when there is no stack or queue
            System.err.println("One of stack or queue mode must be specified");
            System.exit(1);
        }

        if (beginWord == null || beginWord.length() == 0) { // when there is no begin word
            System.err.println("The begin word must be specified");
            System.exit(1);
        }

        if (endWord == null || endWord.length() == 0) { // when there is no end word
            System.err.println("The end word must be specified");
            System.exit(1);
        }

        if (!changeMode && !swapMode && !length) { // when there is no morph modes
            System.err.println("At least one of [--change/-c], [--swap/-p], and [--length/-l] must be specified");
            System.exit(1);
        }

        if ((changeMode || swapMode) && !length && (endWord.length() != beginWord.length())) { // endWord and beginWord don't match without length flag
            System.err.println("The --change/-c and --swap/-p flags are specified, but --length/-l is not");
            System.err.println("and the start/end words do not match in length");
            System.exit(1);
        }

    }


    private void printHelp() {
        // print valid help message
        System.out.println("Usage: java [options] ");
        System.out.println("This program reads a dictionary of words and allows to transform a starting word to an ending words based on the options.");
        System.out.println("The program prints the number of words in the dictionary followed by the number of usable words.");
        System.out.println(" ");
        System.out.println("options:");
        System.out.println("[--stack/-s] [--queue/-q] [--change/-c] [--swap/-p] [--length/-l] [--output (W|M)/-o (W|M)] [--begin <word>/-b <word>] [--end <word>/-e <end>] [--help/-h] [--checkpoint1/-x] [--checkpoint2/-y]");
        System.out.println(" ");
        System.out.println("--stack/-s use the stack-based routing scheme.");
        System.out.println("--queue/-q use the queue-based routing scheme.");
        System.out.println("--change/-c allow to change one letter into another.");
        System.out.println("--swap/-p allow to swap any two adjacent characters.");
        System.out.println("--length/-l allow to modify the length of a word by inserting/deleting a single letter.");
        System.out.println("--output (W|M)/-o (W|M) indicate the output file format; W supports word format; M supports modification format. If the --output option is not specified, default to W.");
        System.out.println("--begin <word>/-b <word> specify the word starts with. This flag should be specified on the command line, and when it is specified a word must follow it.");
        System.out.println("--end <word>/-e <end> specify the word must reach. This flag must be specified on the command line, and when it is specified a word must follow it.");
        System.out.println("--help/-h argument prints this message");
        System.out.println("--checkpoint1/-x stop the program after completing checkpoint 1.");
        System.out.println("--checkpoint2/-y stop the program after completing checkpoint 2.");
        System.exit(0);
    }

    public boolean isChangeMode() {
        return changeMode;
    }

    public boolean isSwapMode() {
        return swapMode;
    }

    public boolean isLengthMode() {
        return length;
    }

    public boolean isCheckpoint1() {
        return checkpoint1;
    }

    public boolean isCheckpoint2() {
        return checkpoint2;
    }

    public boolean isStackMode() {
        return stackMode;
    }

    public boolean isWordOutput() {
        return wordOutput;
    }

    public String getBeginWord() {
        return beginWord;
    }

    public String getEndWord() {
        return endWord;
    }

}
