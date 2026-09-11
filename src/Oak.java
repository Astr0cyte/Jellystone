public class Oak extends Tree {

    public Oak(double spreadability) {
        super(
                TreeType.OAK,
                Math.min(1.0, spreadability),
                100.0,
                1.0
        );
    }
}