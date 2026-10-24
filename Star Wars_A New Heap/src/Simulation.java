import java.util.ArrayList;
import java.util.Scanner;

public class Simulation {
    private ArrayList<General> generals;
    private ArrayList<Planet> planets;
    private Config config;
    private Scanner in;
    private int currId = 0;
    static long currentTime = 0; // make a global variable

    public Simulation(Config c){
        config = c;

        in = new Scanner(System.in);

        // skip first line
        in.nextLine();

        // skip the header
        in.next();
        String mode = in.next();

        // read the number of generals
        in.next();
        int numGenerals = in.nextInt();

        // read the number of planets
        in.next();
        int numPlanets = in.nextInt();

        generals = new ArrayList<>(numGenerals);
        for(int i = 0; i < numGenerals; i++){
            // track the general id using increment i
            generals.add(new General(i));
        }

        planets = new ArrayList<>(numPlanets);
        for (int i = 0; i < numPlanets; i++){
            planets.add(new Planet(i));
        }

        // if PR mode, finalize our setup
        if (mode.equals("PR")){
            // random seed
            in.next();
            int seed = in.nextInt();

            // num deployments
            in.next();
            int numDeployments = in.nextInt();

            // arrival rate
            in.next();
            int arrivalRate = in.nextInt();

            // create our random deployment generator
            in = P2Random.PRInit(seed, numGenerals, numPlanets, numDeployments, arrivalRate);
        }
        // in scanner
        // DL: System.in
        // PR: P2Random
    }

    public void performWarfare(){

        // print out the simulation starting message
        System.out.println("Deploying troops...");

        while (in.hasNextInt()){
            Deployment d = getNextDeployment();

            // check the time
            if (currentTime < d.timestamp){

                if (config.median){
                    // median mode is on
                    for (Planet p : planets){
                        p.printMedian();
                    }
                }
                currentTime = d.timestamp;
            }

            // checks if the time stamps are valid
            if (currentTime > d.timestamp){
                System.out.print("Error: Deployment timestamp is out of order.\n");
                System.exit(1);
            }
            // track the deployment for the general eval mode
            if (config.general) {
                generals.get(d.general).addDeployment(d);
            }

            Planet p = planets.get(d.planet);
            p.addDeployment(d);
            p.performBattles(config);

        }

        // print out our running medians
        if (config.median){
            // median mode
            for (Planet p : planets){
                p.printMedian();
            }
        }

        // print out summary
        System.out.print("---End of Day---\n");

        // count battles
        int totalBattles = 0;
        for (Planet p : planets){
            totalBattles += p.getNumBattles();
        }
        System.out.print("Battles: " + totalBattles + "\n");

        // general eval output
        if (config.general){
            for (Planet p : planets){
                // count up the survivors
                // add them to the appropriate general
                p.countSurvivors(generals);
            }
            // print head message for general evaluation
            System.out.println("---General Evaluation---");
            for (General g : generals){
                g.printStats();
            }
        }
    }

    private Deployment getNextDeployment() {
        // read all of this and construct a deployment object to represent the deployment
        long timestamp = in.nextLong();
        String type = in.next();

        // Integer.parseInt()
        int generalId = Integer.parseInt(in.next().substring(1));
        int planetId = Integer.parseInt(in.next().substring(1));
        int force = Integer.parseInt(in.next().substring(1));
        int numTroops = Integer.parseInt(in.next().substring(1));

        // Error Checking
        if (timestamp < 0){
            System.err.print("Error: Invalid Timestamp.\n");
            System.exit(1);
        }
        if (numTroops < 1){
            System.err.print("Error: Invalid number of Troops.\n");
            System.exit(1);
        }
        if (force < 1){
            System.err.print("Invalid force.\n");
            System.exit(1);
        }
        if (generals.size() <= generalId){
            System.err.print("Invalid General ID.\n");
            System.exit(1);
        }
        // checks if the Planet ID integers are in range
        if (planets.size() <= planetId){
            System.err.print("Invalid Planet ID.\n");
            System.exit(1);
        }
        if (type.equals("SITH")) {
            return new SithDeployment(currId++, timestamp, generalId, planetId, force, numTroops);
        } else {
            return new JediDeployment(currId++, timestamp, generalId, planetId, force, numTroops);
        }
    }
}
