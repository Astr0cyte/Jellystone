public class Tree implements Burnable {

    public enum TreeType {
        OAK,
        PINE, 
        EUCALYPTUS,
        BIRCH,
        SPRUCE,
        JUNGLE
    }

    private boolean burning;
    private double spreadability;
    private TreeType treeType;
    private double burnIntensity;
    private int ignitionCount;
    private boolean alive;

    public Tree(double spreadability) {
        this(TreeType.OAK, spreadability);
    }

    public Tree(TreeType treeType, double spreadability) {
        this.burning = false;
        this.spreadability = spreadability;
        this.treeType = treeType;
        this.burnIntensity = 0.0;
        this.ignitionCount = 0;
        this.alive = true;
    }

    @Override
    public void ignite() {
        if (!alive) {
            return;
        }

        if (!burning) {
            ignitionCount++;
        }

        burning = true;
        burnIntensity = 1.0;
    }

    // OVERLOADED version
    public void ignite(double severity) {
        if (alive && severity >= 0.5) {
            if (!burning) {
                ignitionCount++;
            }

            burning = true;
            burnIntensity = Math.max(burnIntensity, severity);
        }
    }

    @Override
    public boolean isBurning() {
        return burning;
    }
    public boolean isAlive() {
        return alive;
    }

    public double getSpreadability() {
        return spreadability;
    }

    public TreeType getTreeType() {
        return treeType;
    }

    public double getBurnIntensity() {
        return burnIntensity;
    }

    public int getIgnitionCount() {
        return ignitionCount;
    }

    public void extinguish() {
        burning = false;
        burnIntensity = 0.0;
    }

    public void kill() {
        alive = false;
        burning = false;
        burnIntensity = 0.0;
    }
}