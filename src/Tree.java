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

    public Tree(double spreadability) {
        this(TreeType.OAK, spreadability);
    }

    public Tree(TreeType treeType, double spreadability) {
        this.burning = false;
        this.spreadability = spreadability;
        this.treeType = treeType;
        this.burnIntensity = 0.0;
    }

    @Override
    public void ignite() {
        burning = true;
        burnIntensity = 1.0;
    }

    // OVERLOADED version
    public void ignite(double severity) {
        if (severity >= 0.5) {
            burning = true;
            burnIntensity = Math.max(burnIntensity, severity);
        }
    }

    @Override
    public boolean isBurning() {
        return burning;
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

    public void extinguish() {
        burning = false;
        burnIntensity = 0.0;
    }
}