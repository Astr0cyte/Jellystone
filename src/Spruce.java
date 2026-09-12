public class Spruce extends Tree {

    public Spruce(double spreadability) {
        super(
                Math.min(1.0, spreadability * 1.2),
                90.0,
                1.15
        );
    }
}