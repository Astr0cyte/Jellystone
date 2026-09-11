public class Birch extends Tree {

    public Birch(double spreadability) {
        super(
                TreeType.BIRCH,
                Math.min(1.0, spreadability * 0.9),
                60.0,
                0.6
        );
    }
}