public class Eucalyptus extends Tree {

    public Eucalyptus(double spreadability) {
        super(
                Math.min(1.0, spreadability * 1.6),
                240.0,
                1.4
        );
    }
}