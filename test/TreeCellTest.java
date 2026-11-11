import java.util.Random;

public class TreeCellTest {

    public static void main(String[] args) {
        testDefaultTreeType();
        testSubclassTreeTypes();
        testBurning();
        testDamageAndDeath();
        testCellRemoval();
        testGuardianProtection();
        testGuardianLastStand();
        testValidation();

        System.out.println("All Tree/Cell tests passed.");
    }

    private static void testDefaultTreeType() {
        Tree tree = new Tree(0.5);

        check(
                "OAK".equals(tree.getTreeType()),
                "Default Tree should report OAK"
        );

        check(
                tree.getHealth() == tree.getMaxHealth(),
                "New tree should start at full health"
        );

        check(
                tree.isAlive(),
                "New tree should be alive"
        );
    }

    private static void testSubclassTreeTypes() {
        Tree oak = new Oak(0.5);
        Tree birch = new Birch(0.5);
        Tree eucalyptus = new Eucalyptus(0.5);
        Tree spruce = new Spruce(0.5);
        Tree jungle = new Jungle(0.5);

        check(
                "OAK".equals(oak.getTreeType()),
                "Oak should report OAK"
        );

        check(
                "BIRCH".equals(birch.getTreeType()),
                "Birch should report BIRCH"
        );

        check(
                "EUCALYPTUS".equals(eucalyptus.getTreeType()),
                "Eucalyptus should report EUCALYPTUS"
        );

        check(
                "SPRUCE".equals(spruce.getTreeType()),
                "Spruce should report SPRUCE"
        );

        check(
                "JUNGLE".equals(jungle.getTreeType()),
                "Jungle should report JUNGLE"
        );
    }

    private static void testBurning() {
        Tree tree = new Eucalyptus(0.5);

        check(
                !tree.isBurning(),
                "New tree should not be burning"
        );

        tree.ignite();

        check(
                tree.isBurning(),
                "ignite() should start burning"
        );

        check(
                tree.getBurnIntensity() > 0.0,
                "Burning tree should have burn intensity"
        );

        double healthBefore = tree.getHealth();

        tree.advanceBurning();

        check(
                tree.getHealth() < healthBefore,
                "Burning should reduce health"
        );
    }

    private static void testDamageAndDeath() {
        Tree tree = new Oak(0.5);

        tree.damage(tree.getMaxHealth());

        check(
                !tree.isAlive(),
                "Tree should die at zero health"
        );

        check(
                !tree.isBurning(),
                "Dead tree should not be burning"
        );

        tree.ignite();

        check(
                !tree.isBurning(),
                "Dead tree should not be able to ignite"
        );
    }

    private static void testCellRemoval() {
        Cell cell = new Cell(0, 0);
        Tree tree = new Birch(0.5);

        cell.plantTree(tree);

        check(
                cell.hasTree(),
                "Cell should contain planted tree"
        );

        Tree removed = cell.removeTree();

        check(
                removed == tree,
                "removeTree() should return removed tree"
        );

        check(
                cell.isEmpty(),
                "Cell should be empty after removal"
        );
    }

    private static void testGuardianProtection() {
        Forest scenario = new Forest(40, 60);
        Main.plantGuardians(scenario, new Random(42));
        Forest otherScenario = new Forest(40, 60);
        Main.plantGuardians(otherScenario, new Random(43));
        int[][] guardiansPerQuarter = new int[2][2];
        boolean differentPlacement = false;
        for (int row = 0; row < 40; row++) {
            for (int col = 0; col < 60; col++) {
                if (scenario.getGrid()[row][col].getTree() instanceof GuardianTree) {
                    guardiansPerQuarter[row / 20][col / 30]++;
                    if (!(otherScenario.getGrid()[row][col].getTree() instanceof GuardianTree)) {
                        differentPlacement = true;
                    }
                }
            }
        }
        for (int[] half : guardiansPerQuarter) {
            for (int guardians : half) {
                check(guardians == 1, "Each quarter should have one Guardian");
            }
        }
        check(differentPlacement, "Different random seeds should change Guardian placement");

        Forest forest = new Forest(9, 9);
        Cell[][] grid = forest.getGrid();
        GuardianTree first = new GuardianTree();
        GuardianTree second = new GuardianTree();
        grid[4][2].plantTree(first);
        grid[4][6].plantTree(second);

        check(!forest.isProtected(4, 2), "A Guardian should not protect itself");
        check(forest.isProtected(7, 2), "Protection should reach three cells");
        check(!forest.isProtected(7, 3), "Protection should follow a circular radius");
        check(Math.abs(forest.applyGuardianProtection(0.8, 4, 4) - 0.016) < 1e-9,
                "Overlapping zones should apply the 98% reduction once");

        Tree target = new Tree(0.9);
        grid[4][4].plantTree(target);
        new Arson(4, 4).ignite(forest);
        check(target.isBurning(), "Direct ignition should reach protected trees");

        first.kill();
        check(!forest.isProtected(7, 2), "A dead Guardian should lose its zone");
        check(forest.isProtected(4, 4), "The surviving Guardian should still protect the overlap");
        second.kill();
        check(forest.applyGuardianProtection(0.8, 4, 4) == 0.8,
                "Spread chance should return to normal after both Guardians die");
    }

    private static void testGuardianLastStand() {
        Forest forest = new Forest(9, 9);
        Cell[][] grid = forest.getGrid();
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                cell.removeTree();
            }
        }

        GuardianTree guardian = new GuardianTree();
        guardian.damage(guardian.getMaxHealth() - 20);
        guardian.ignite();
        grid[4][4].plantTree(guardian);
        Tree inside = new Tree(0.5);
        inside.ignite();
        grid[4][7].plantTree(inside);
        Tree outside = new Tree(0.5);
        outside.ignite();
        grid[7][5].plantTree(outside);

        forest.update();
        check(!grid[4][4].hasTree(), "The dead Guardian should be removed");
        check(!forest.isProtected(4, 7), "The pulse should not keep the protection zone active");
        check(!inside.isBurning() && inside.getHealth() == 70.0,
                "The pulse should extinguish the nearby tree without healing it");
        check(outside.isBurning(), "A fire outside the circular radius should keep burning");
        check(forest.getGuardianPulses().equals(java.util.List.of(new Forest.GuardianPulse(4, 4))),
                "The death should show one pulse at the Guardian's cell");

        inside.ignite();
        forest.update();
        check(inside.isBurning() && forest.getGuardianPulses().isEmpty(),
                "A dead Guardian should not pulse again or block reignition");
    }

    private static void testValidation() {
        boolean exceptionThrown = false;

        try {
            new Tree(-0.1);
        } catch (IllegalArgumentException exception) {
            exceptionThrown = true;
        }

        check(
                exceptionThrown,
                "Negative spreadability should be rejected"
        );
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
