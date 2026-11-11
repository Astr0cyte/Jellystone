import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FireReplay {
    private static final int ROWS = 40;
    private static final int COLUMNS = 60;

    record Result(long inside, long outside, int ticks) {
        long total() {
            return inside + outside;
        }
    }

    record Comparison(long startingTrees, Result withGuardians, Result withoutGuardians) {}

    static void run(String[] args) {
        long seed = 42;
        if (args.length > 1) {
            try {
                seed = Long.parseLong(args[1]);
            } catch (NumberFormatException exception) {
                System.out.println("Use a whole number for the replay seed, for example: Main replay 42");
                return;
            }
        }

        Comparison comparison = compare(seed);
        System.out.println("Phoenix Rising replay, seed " + seed);
        System.out.println("Starting trees in each run: " + comparison.startingTrees());
        printResult("With Guardians", comparison.withGuardians());
        printResult("Without Guardians", comparison.withoutGuardians());
        long difference = comparison.withGuardians().total() - comparison.withoutGuardians().total();
        System.out.printf("Difference in survivors: %+d%n", difference);
    }

    static Comparison compare(long seed) {
        Forest guarded = new Forest(ROWS, COLUMNS, seed);
        Forest control = new Forest(ROWS, COLUMNS, seed);
        guarded.setWind(new Wind(30, "EAST", 30, 20, 15));
        control.setWind(new Wind(30, "EAST", 30, 20, 15));
        Main.plantGuardians(guarded, new Random(seed));

        boolean[][] originalZones = new boolean[ROWS][COLUMNS];
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                originalZones[row][col] = guarded.isProtected(row, col);
                Cell controlCell = control.getGrid()[row][col];
                if (guarded.getGrid()[row][col].getTree() instanceof GuardianTree
                        && !controlCell.hasTree()) {
                    controlCell.plantTree(new Tree(0.55));
                }
            }
        }

        long startingTrees = trees(guarded).count();
        return new Comparison(startingTrees,
                runToEnd(guarded, originalZones), runToEnd(control, originalZones));
    }

    private static Result runToEnd(Forest forest, boolean[][] originalZones) {
        new PhoenixRising().ignite(forest);
        int ticks = 0;
        while (trees(forest).anyMatch(cell -> cell.getTree().isBurning())) {
            forest.update();
            ticks++;
        }

        Map<Boolean, Long> survivors = trees(forest).collect(Collectors.partitioningBy(
                cell -> originalZones[cell.getX()][cell.getY()], Collectors.counting()));
        return new Result(survivors.get(true), survivors.get(false), ticks);
    }

    private static Stream<Cell> trees(Forest forest) {
        return Arrays.stream(forest.getGrid())
                .flatMap(Arrays::stream)
                .filter(Cell::hasTree);
    }

    private static void printResult(String label, Result result) {
        System.out.printf("%s: %d survived (%d inside the original zones, %d outside), %d ticks%n",
                label, result.total(), result.inside(), result.outside(), result.ticks());
    }
}
