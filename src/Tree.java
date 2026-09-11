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

        validateTreeType(treeType);
        validateSpreadability(spreadability);
        validateMaxHealth(maxHealth);
        validateBurnIntensityFactor(burnIntensityFactor);

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
        burnIntensity = burnIntensityFactor;
    }

    // OVERLOADED version
    public void ignite(double severity) {
        if (alive && severity >= 0.5) {
            burning = true;
            burnIntensity = Math.max(
                    burnIntensity,
                    severity * burnIntensityFactor
            );
        }
    }

    public double getHealth() {
        return health;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void damage(double amount) {
        if (!Double.isFinite(amount) || amount < 0.0) {
            throw new IllegalArgumentException(
                    "Damage must be a finite, non-negative value"
            );
        }

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

    public void advanceBurning() {
        if (!burning || !alive) {
            return;
        }

        damage(30.0 * burnIntensity);
    }

    //exceptions
    
    private static void validateTreeType(TreeType treeType) {
        if (treeType == null) {
            throw new IllegalArgumentException(
                    "Tree type must not be null"
            );
        }
    }

    private static void validateSpreadability(double spreadability) {
        if (!Double.isFinite(spreadability)
                || spreadability < 0.0
                || spreadability > 1.0) {
            throw new IllegalArgumentException(
                    "Spreadability must be between 0.0 and 1.0"
            );
        }
    }

    private static void validateMaxHealth(double maxHealth) {
        if (!Double.isFinite(maxHealth) || maxHealth <= 0.0) {
            throw new IllegalArgumentException(
                    "Maximum health must be positive"
            );
        }
    }

    private static void validateBurnIntensityFactor(
            double burnIntensityFactor) {

        if (!Double.isFinite(burnIntensityFactor)
                || burnIntensityFactor < 0.0) {
            throw new IllegalArgumentException(
                    "Burn intensity factor must be non-negative"
            );
        }
    }
}