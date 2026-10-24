public abstract class Deployment implements Comparable<Deployment> {
    int id; // unique identifier for the deployment
    long timestamp; // Timestamp; time that deployment order is issued
    int general; // General ID
    int planet; // planet ID
    int force; // Force-sensitivity; the avg Force-sensitivity of the troops being deployed
    int quantity; // Quantity; the number of troops being deployed

    // constructor
    public Deployment(int id, long ts, int g, int p, int f, int q) {
        this.id = id;
        timestamp = ts;
        general = g;
        planet = p;
        force = f;
        quantity = q;
    }

    // method for checking whether the Sith deployment or Jedi deployment
    public abstract boolean isSith();

    // method for comparing deployments
    @Override
    public abstract int compareTo(Deployment o);
}
