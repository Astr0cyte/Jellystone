public class Tree implements Burnable {

    private boolean burning;
    private double spreadability;

    public Tree(double spreadability) {
        this.burning = false;
        this.spreadability = spreadability;
    }

    @Override
    public void ignite() {
        burning = true;
    }

    // OVERLOADED version
    public void ignite(double severity) {
        if (severity >= 0.5) {
            burning = true;
        }
    }

    @Override
    public boolean isBurning() {
        return burning;
    }

    public double getSpreadability() {
        return spreadability;
    }

    public void extinguish() {
        burning = false;
    }
}