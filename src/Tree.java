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

    public Tree(double spreadability) {
        this.burning = false;
        this.spreadability = spreadability;
        this.treeType = TreeType.OAK;
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

    public TreeType getTreeType() {
        return treeType;
    }

    public void extinguish() {
        burning = false;
    }
}