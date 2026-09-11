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
    private boolean alive;
    private double health;
    private double maxHealth;
    private double burnIntensityFactor;

    public Tree(double spreadability) {
        this(TreeType.OAK, spreadability, 100.0, 0.75);
    }

    protected Tree(
            TreeType treeType,
            double spreadability,
            double maxHealth,
            double burnIntensityFactor) {

        this.burning = false;
        this.spreadability = spreadability;
        this.treeType = treeType;
        this.burnIntensity = 0.0;
        this.burnIntensityFactor = burnIntensityFactor;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.alive = true;
    }

    @Override
    public void ignite() {
        if (!alive) {
            return;
        }

        burning = true;
        burnIntensity = 1.0;
    }

    // OVERLOADED version
    public void ignite(double severity) {
        if (alive && severity >= 0.5) {
            burning = true;
            burnIntensity = Math.max(burnIntensity, severity);
        }
    }

    public double getHealth() {
        return health;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void damage(double amount) {
        if (!alive) {
            return;
        }

        health = Math.max(0.0, health - amount);

        if (health == 0.0) {
            kill();
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