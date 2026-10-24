public class SithDeployment extends Deployment {
    // constructor for SithDeployment
    // initializes the deployment attributes for a Sith deployment
    public SithDeployment(int id, long timestamp, int generalID, int planetID, int force, int numTroops) {
        super(id, timestamp, generalID, planetID, force, numTroops);
    }

    // method for identify deployment as Sith
    public boolean isSith() {
        return true; // if it's Sith then return ture
    }

    @Override
    public int compareTo(Deployment d) {
        // this
        // d
        // return a negative number if "this" is higher priority than d
        // return a positive number if "this" is lower priority than d
        // compare force values
        if (this.force == d.force) {
            return this.id - d.id;
        }
        // if the forces are different
        return d.force - this.force;
    }
}
