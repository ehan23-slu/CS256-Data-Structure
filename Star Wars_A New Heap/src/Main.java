public class Main {
    public static void main(String[] args) {
        Config config = new Config(args);

        Simulation simulation = new Simulation(config);
        simulation.performWarfare();

    }
}
