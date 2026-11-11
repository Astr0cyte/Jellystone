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
        testIgnitionSourcePolymorphism();
        testZeusIgnitesBlueBoltAndSpreadsOrange();
        testPhoenixIgnitesColouredRing();
        testFireSerpentIgnitesMagentaSnake();
        
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
                    "Backburning should ignite trees in the wave troughs"
            );
        }
        for (int col : crestColumns) {
            check(!forest.getGrid()[0][col].getTree().isBurning(),
                    "Backburning should leave the other columns untouched");
        }

        forest.getGrid()[0][2].removeTree();
        forest.getGrid()[0][5].getTree().extinguish();
        backburning.ignite(forest);
        check(!forest.getGrid()[0][2].hasTree() && forest.getGrid()[0][5].getTree().isBurning(),
                "An empty trough should not prevent other troughs from igniting");
    }

    private static void testIgnitionSourcePolymorphism() {
        checkIgnitesThroughBaseType(new Arson(0, 0), 0);
        checkIgnitesThroughBaseType(new Lightning(), 0);
        checkIgnitesThroughBaseType(new Backburning(0), 2);
    }

    private static void testZeusIgnitesBlueBoltAndSpreadsOrange() {
        Forest forest = new Forest(40, 60);
        Cell[][] grid = forest.getGrid();
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                cell.plantTree(new Tree(1.0));
            }
        }

        grid[14][32].removeTree();
        new ZeusJudgment().ignite(forest);

        check(!grid[14][32].hasTree(), "Zeus should skip the gap in the bolt");
        int blueStrikes = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                Cell cell = grid[row][col];
                if (!cell.hasTree()) {
                    continue;
                }
                Tree tree = cell.getTree();
                if (tree.isBurning()) {
                    check(tree.getFireColor() == FireColor.BLUE, "Direct Zeus strikes should be blue");
                    blueStrikes++;
                }
            }
        }
        check(blueStrikes > 150, "The filled bolt should ignite many cells");
        check(grid[14][24].getTree().isBurning() && grid[14][39].getTree().isBurning(),
                "The bend should span multiple columns");
        check(grid[33][24].getTree().isBurning() && !grid[33][25].getTree().isBurning(),
                "The bolt should taper to a narrow tip");
        check(!grid[2][32].getTree().isBurning(), "A cell outside the bolt should stay unlit");

        forest.update();
        check(grid[2][33].getTree().getFireColor() == FireColor.BLUE,
                "The original strike should stay blue while burning");
        Tree neighbour = grid[2][32].getTree();
        check(neighbour.isBurning() && neighbour.getFireColor() == FireColor.ORANGE,
                "Fire spreading from a Zeus strike should be orange");
    }

    private static void testPhoenixIgnitesColouredRing() {
        Forest forest = new Forest(40, 60);
        Cell[][] grid = forest.getGrid();
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                cell.plantTree(new Tree(1.0));
            }
        }

        grid[20][42].removeTree();
        new PhoenixRising().ignite(forest);

        check(!grid[20][42].hasTree(), "Phoenix should skip empty cells");
        check(!grid[20][30].getTree().isBurning(), "The centre should start untouched");
        check(!grid[20][44].getTree().isBurning(), "Trees outside the ring should start untouched");
        check(grid[8][30].getTree().isBurning()
                        && grid[8][30].getTree().getFireColor() == FireColor.PURPLE,
                "The top of the ring should burn purple");
        check(grid[20][41].getTree().isBurning()
                        && grid[20][41].getTree().getFireColor() == FireColor.PURPLE,
                "The right side of the ring should burn purple");
        check(grid[32][30].getTree().isBurning()
                        && grid[32][30].getTree().getFireColor() == FireColor.PURPLE,
                "The bottom of the ring should burn purple");

        forest.update();
        Tree neighbour = grid[20][44].getTree();
        check(neighbour.isBurning() && neighbour.getFireColor() == FireColor.ORANGE,
                "Fire spreading beyond the ring should burn orange");
    }

    private static void testFireSerpentIgnitesMagentaSnake() {
        Forest forest = new Forest(40, 60);
        Cell[][] grid = forest.getGrid();
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                cell.plantTree(new Tree(1.0));
            }
        }

        grid[29][10].removeTree();
        new FireSerpent().ignite(forest);

        check(!grid[29][10].hasTree(), "Fire Serpent should skip empty cells");
        check(grid[28][11].getTree().isBurning() && grid[10][20].getTree().isBurning()
                        && grid[29][34].getTree().isBurning() && grid[11][44].getTree().isBurning(),
                "The body should have multiple bends");
        check(grid[20][5].getTree().isBurning() && !grid[21][5].getTree().isBurning(),
                "The tail should start one cell wide");
        check(grid[16][51].getTree().isBurning() && grid[22][51].getTree().isBurning()
                        && grid[19][55].getTree().isBurning(),
                "The head should be wider than the body and extend past it");
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                if (cell.hasTree() && cell.getTree().isBurning()) {
                    check(cell.getTree().getFireColor() == FireColor.MAGENTA,
                            "The whole serpent should burn magenta");
                }
            }
        }
        Tree neighbour = grid[19][5].getTree();
        check(!neighbour.isBurning(), "Trees outside the trail should start untouched");

        forest.update();
        check(neighbour.isBurning() && neighbour.getFireColor() == FireColor.ORANGE,
                "Fire spreading from the trail should burn orange");
    }

    private static void checkIgnitesThroughBaseType(
            IgnitionSource source, int expectedColumn) {
        Forest forest = new Forest(1, expectedColumn + 1);
        Cell target = forest.getGrid()[0][expectedColumn];
        target.plantTree(new Tree(0.9));

        source.ignite(forest);

        check(
                target.getTree().isBurning(),
                source.getClass().getSimpleName()
                        + " should ignite through an IgnitionSource reference"
        );
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
