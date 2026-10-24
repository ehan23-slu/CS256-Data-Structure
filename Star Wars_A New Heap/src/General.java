public class General {
    // initialize variables to track number of Jedi troops, Sith troops, and Survivors, and generalID
    private int numJedi, numSith, numSurvive, generalID;

    // constructor
    public General(int id){
        this.generalID = id;
        numJedi = 0;
        numSith = 0;
        numSurvive = 0;
    }

    // method for counting the number of Jedi or Sith troops based on the type of deployment
    public void addDeployment(Deployment d){
        // update general number of Sith or Jedi
        if (d.isSith()) {
            numSith += d.quantity;
        } else {
            numJedi += d.quantity;
        }
    }

    // method to count of surviving troops after battle performed
    public void addSurvivors(int s){
        numSurvive += s;
    }

    // method for printing message for general evaluation
    public void printStats() {
        System.out.print("General " + generalID + " deployed " + numJedi + " Jedi troops and " + numSith + " Sith troops, and "
                + numSurvive + "/" + (numJedi + numSith) + " troops survived.\n");
    }
}