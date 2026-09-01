public class IgnitionSourceTest {

    public static void main(String[] args) {
        testArsonDefaults();
        testArsonIgnitesTargetCell();
        testLightningDefaults();
        testLightningIgnitesRandomCell();
        testBackburningDefaults();
        testBackburningIgnitesWholeRow();

        System.out.println("All IgnitionSource tests passed.");
    }

    private static void testArsonDefaults() {
        Arson arson = new Arson(0, 0);

        check(
                arson.severity == 1.0,
                "Arson should use severity 1.0"
        );
        check(
                arson.spreadability == 1.0,
                "Arson should use spreadability 1.0"
        );
    }

    private static void testArsonIgnitesTargetCell() {
        Forest forest = new Forest(2, 2);
        forest.getGrid()[0][0].plantTree(new Tree(0.9));

        Arson arson = new Arson(0, 0);
        arson.ignite(forest);

        check(
                forest.getGrid()[0][0].getTree().isBurning(),
                "Arson should ignite the tree in its target cell"
        );
    }

    private static void testLightningDefaults() {
        Lightning lightning = new Lightning();

        check(
                lightning.severity == 0.8,
                "Lightning should use severity 0.8"
        );
        check(
                lightning.spreadability == 0.7,
                "Lightning should use spreadability 0.7"
        );
    }

    private static void testLightningIgnitesRandomCell() {
        Forest forest = new Forest(1, 1);
        forest.getGrid()[0][0].plantTree(new Tree(0.9));

        Lightning lightning = new Lightning();
        lightning.ignite(forest);

        check(
                forest.getGrid()[0][0].getTree().isBurning(),
                "Lightning should ignite a tree in the forest"
        );
    }

    private static void testBackburningDefaults() {
        Backburning backburning = new Backburning(0);

        check(
                backburning.severity == 0.7,
                "Backburning should use severity 0.7"
        );
        check(
                backburning.spreadability == 0.4,
                "Backburning should use spreadability 0.4"
        );
    }

    private static void testBackburningIgnitesWholeRow() {
        Forest forest = new Forest(3, 3);

        for (int col = 0; col < 3; col++) {
            forest.getGrid()[0][col].plantTree(new Tree(0.9));
        }

        Backburning backburning = new Backburning(0);
        backburning.ignite(forest);

        for (int col = 0; col < 3; col++) {
            check(
                    forest.getGrid()[0][col].getTree().isBurning(),
                    "Backburning should ignite every tree in the selected row"
            );
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
