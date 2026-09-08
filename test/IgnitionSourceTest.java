public class IgnitionSourceTest {

    public static void main(String[] args) {
        testArsonDefaults();
        testArsonIgnitesTargetCell();
        testArsonDoesNotIgniteLowSpreadabilityTree();
        testLightningDefaults();
        testLightningIgnitesRandomCell();
        testBackburningDefaults();
        testBackburningIgnitesWaveColumns();
        testRejectsInvalidSeverity();
        testRejectsInvalidSpreadability();
        
        System.out.println("All IgnitionSource tests passed.");
    }

    private static void testArsonDefaults() {
        Arson arson = new Arson(0, 0);

        check(
                arson.severity == 0.55,
                "Arson should use severity 0.55"
        );
        check(
                arson.spreadability == 0.5,
                "Arson should use spreadability 0.5"
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

    private static void testArsonDoesNotIgniteLowSpreadabilityTree() {
        Forest forest = new Forest(2, 2);
        forest.getGrid()[0][0].plantTree(new Tree(0.1));

        Arson arson = new Arson(0, 0);
        arson.ignite(forest);

        check(
                !forest.getGrid()[0][0].getTree().isBurning(),
                "Arson should not ignite a tree below its flammability threshold"
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

    private static void testBackburningIgnitesWaveColumns() {
        Forest forest = new Forest(1, 6);

        for (int col = 0; col < 6; col++) {
            forest.getGrid()[0][col].plantTree(new Tree(0.9));
        }

        Backburning backburning = new Backburning(0);
        backburning.ignite(forest);

        // With a 3-column wavelength, only the wave troughs (columns 2
        // and 5) fall below the 0.4 threshold and ignite immediately.
        int[] troughColumns = {2, 5};
        int[] crestColumns = {0, 1, 3, 4};

        for (int col : troughColumns) {
            check(
                    forest.getGrid()[0][col].getTree().isBurning(),
                    "Backburning should ignite every tree in the selected row"
            );
        }
    }
private static void testRejectsInvalidSeverity() {
    boolean exceptionThrown = false;

    try {
        new TestIgnitionSource(1.1, 0.5);
    } catch (IllegalArgumentException exception) {
        exceptionThrown = true;
    }

    check(
            exceptionThrown,
            "Severity above 1.0 should throw an exception"
    );
}

private static void testRejectsInvalidSpreadability() {
    boolean exceptionThrown = false;

    try {
        new TestIgnitionSource(0.5, -0.1);
    } catch (IllegalArgumentException exception) {
        exceptionThrown = true;
    }

    check(
            exceptionThrown,
            "Spreadability below 0.0 should throw an exception"
    );
}

private static class TestIgnitionSource extends IgnitionSource {
    TestIgnitionSource(double severity, double spreadability) {
        super(severity, spreadability);
    }

    @Override
    public void ignite(Forest forest) {
        // No ignition behaviour is needed for constructor validation.
    }
}
    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
