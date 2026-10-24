public class JediDeployment extends Deployment {
    // constructor for JediDeployment
    // initializes the deployment attributes for a Jedi deployment
    public JediDeployment(int id, long ts, int g, int p, int f, int q) {
        super(id, ts, g, p, f, q);
    }

    // method for identify deployment as Jedi
    @Override
    public boolean isSith() {
        // false since this is Jedi, not Sith
        return false;
    }

    // method for comparing JediDeployment with another deployment to determine priority
    @Override
    public int compareTo(Deployment d) {

        // compare force values
        // return a negative number if "this" is higher priority than d
        // return a positive number if "this" is lower priority than d
        if (this.force == d.force) {
            return this.id - d.id;
        }
        // if the forces are different
        return this.force - d.force;
    }
}
