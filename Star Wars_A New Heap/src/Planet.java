import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

public class Planet {
    // initializes the planet
    private PriorityQueue<Deployment> jediDeployments;
    private PriorityQueue<Deployment> sithDeployments;

    // 2 priority queues for the median
    private PriorityQueue<Integer> lowerMod;
    private PriorityQueue<Integer> upperMod;

    private int id;
    private int numBattles;

    // constructor
    public Planet(int id) {
        this.id = id;
        numBattles = 0;

        jediDeployments = new PriorityQueue<>();
        sithDeployments = new PriorityQueue<>();

        // create PQs for median
        lowerMod = new PriorityQueue<>(Collections.reverseOrder());
        upperMod = new PriorityQueue<>();
    }

    // add each deployment based on type of deployment
    public void addDeployment(Deployment d) {
        if (d.isSith()) {
            sithDeployments.add(d);
        } else {
            jediDeployments.add(d);
        }
    }

    public boolean canBattle() {
        // check to see if there is a valid battle
        // we need both a jedi and a sith
        if (jediDeployments.isEmpty() || sithDeployments.isEmpty()) {
            return false;
        }
        return jediDeployments.peek().force <= sithDeployments.peek().force;
    }

    public void performBattles(Config c) {
        // check if battles can be performed and do as many battles as possible
        while (canBattle()) {
            // a battle can occur
            // subtract the same number of troops from both sith and jedi deployments
            int troopsLost = Math.min(jediDeployments.peek().quantity,
                    sithDeployments.peek().quantity);

            // subtract these from both deployments
            jediDeployments.peek().quantity -= troopsLost;
            sithDeployments.peek().quantity -= troopsLost;

            // print output BEFORE removing from the PQs
            if (c.verbose) {
                System.out.print("General " + sithDeployments.peek().general + "'s battalion " +
                        "attacked General " + jediDeployments.peek().general + "'s battalion on " +
                        "planet " + id + ". " + (troopsLost * 2) + " troops were lost.\n");
            }

            // one of these deployments will have lost all the troops
            // remove that deployment from the PQ
            if (jediDeployments.peek().quantity == 0) {
                jediDeployments.remove();
            }
            if (sithDeployments.peek().quantity == 0) {
                sithDeployments.remove();
            }
            // update the count of our battles
            numBattles++;

            if (c.median) {
                updateMedian(troopsLost * 2);
            }
        }
    }

    private void updateMedian(int troopsLost) {
        // add to the PQs
        // make sure they're balanced...
        // if PQs are empty, then this is the first number
        // when lowerMed is empty or first element is greater than or equal to troopsLost
        // add to lowerMed which is tracking Max heap
        if (lowerMod.isEmpty() || troopsLost <= lowerMod.peek()) {
            lowerMod.add(troopsLost);
        } else {
            upperMod.add(troopsLost);
        }

        if (upperMod.size() - lowerMod.size() == 2) { // upper is bigger
            // move one element from upperMed to lowerMed
            lowerMod.add(upperMod.remove());
        } else if (lowerMod.size() - upperMod.size() == 2) { // lowerMed is bigger
            // move one element from lowerMed to upperMed
            upperMod.add(lowerMod.remove());
        }
    }

    public int getNumBattles() {
        return numBattles;
    }

    public void printMedian() {
        // when there is no battle
        if (getNumBattles() == 0) {
            return;
        }
        // prints out the current median value
        int median;
        if (lowerMod.size() == upperMod.size()) {
            median = (lowerMod.peek() + upperMod.peek()) / 2;
        } else if (upperMod.size() > lowerMod.size()) {
            median = upperMod.peek();
        } else {
            median = lowerMod.peek();
        }
        // prints out the median
        System.out.println("Median troops lost on planet " + id + " at time " + Simulation.currentTime + " is " + median + ".");
    }


    public void countSurvivors(ArrayList<General> generals) {
        // go through the Sith PQ
        // pop each deployment...add the remaining troops to the survivor
        // count for the appropriate general

        while (!sithDeployments.isEmpty()) {
            Deployment sith_d = sithDeployments.poll();
            generals.get(sith_d.general).addSurvivors(sith_d.quantity);
        }
        // pop each JEDI PQ deployment and do the same thing
        while (!jediDeployments.isEmpty()){
            Deployment jedi_d = jediDeployments.poll();
            generals.get(jedi_d.general).addSurvivors(jedi_d.quantity);
        }
    }
}
