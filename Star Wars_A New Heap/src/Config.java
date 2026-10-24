import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

public class Config {
    // set boolean variables to track the choices
    boolean verbose;
    boolean median;
    boolean general;
    // boolean watcher; // This is optional

    // constructor
    public Config(String[] args) {
        LongOpt[] longOptions = {
                // define long options
                new LongOpt("verbose", LongOpt.NO_ARGUMENT, null, 'v'),
                new LongOpt("median", LongOpt.NO_ARGUMENT, null, 'm'),
                new LongOpt("general-eval", LongOpt.NO_ARGUMENT, null, 'g'),
                // new LongOpt("watcher", LongOpt.NO_ARGUMENT, null, 'w')
        };

        // initialize Getopt
        Getopt g = new Getopt("Project2",args,"vmg", longOptions);
        g.setOpterr(true);

        // purse options using getopt()
        int choice = 0;
        while((choice = g.getopt()) != -1) {
            // using switch handle options
            switch(choice) {
                case 'v':
                    verbose = true;
                    break;
                case 'm':
                    median = true;
                    break;
                case 'g':
                    general = true;
                    break;
                default:
                    printHelp();
                    break;
            }
        }

    }

    // Print help message
    private void printHelp() {
        System.out.print("Usage: java [options] \n");
        System.out.print("This program simulates galactic warfare. \n");
        System.out.print("Options: [-v, --verbose], [-m, --median], [-g, --general-eval] \n");
        System.out.print("verbose for battle output with details. \n");
        System.out.print("median for troop lost in battles. \n");
        System.out.print("general-eval for summary of battles at the end. \n");
        System.exit(0);
    }
}
