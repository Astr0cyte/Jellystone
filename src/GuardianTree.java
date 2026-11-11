public class GuardianTree extends Tree {
    public static final int PROTECTION_RADIUS = 3;
    public static final double PROTECTED_SPREAD_MULTIPLIER = 0.02;

    public GuardianTree() {
        super(0.15, 450.0, 1.0);
    }

    static boolean isWithinRadius(int rowOffset, int columnOffset) {
        return rowOffset * rowOffset + columnOffset * columnOffset
                <= PROTECTION_RADIUS * PROTECTION_RADIUS;
    }
}
