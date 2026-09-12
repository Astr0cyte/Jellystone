public class Jungle extends Tree {

    public Jungle(double spreadability) {
        super(
                Math.min(1.0, spreadability * 0.8),
                240.0,
                0.7
        );
    }
}