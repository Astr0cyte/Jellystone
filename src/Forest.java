import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Forest {

    private Cell[][] grid;

    private double temperature;
    private double rainfall;
    private double aridity;

    private Wind wind;

    private Random random = new Random();
    private final List<GuardianPulse> guardianPulses = new ArrayList<>();

    public record GuardianPulse(int row, int column) {}

    public Forest(int rows, int columns) {

        grid = new Cell[rows][columns];

        temperature = 25.0;
        rainfall = 0.2;
        aridity = 0.5;

        for (int row = 0; row < rows; row++) {

            for (int col = 0; col < columns; col++) {

                grid[row][col] = new Cell(row, col);

                // 80% chance of there being a tree
                if (random.nextDouble() < 0.8) {

                    double spreadability =
                            0.3 + random.nextDouble() * 0.7;

                    grid[row][col].plantTree(
                            new Tree(spreadability)
                    );
                }
            }
        }
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public boolean isProtected(int row, int column) {
        int radius = GuardianTree.PROTECTION_RADIUS;
        for (int r = Math.max(0, row - radius); r <= Math.min(grid.length - 1, row + radius); r++) {
            for (int c = Math.max(0, column - radius); c <= Math.min(grid[r].length - 1, column + radius); c++) {
                int dy = r - row;
                int dx = c - column;
                if ((dx != 0 || dy != 0) && GuardianTree.isWithinRadius(dy, dx)
                        && grid[r][c].getTree() instanceof GuardianTree guardian && guardian.isAlive()) {
                    return true;
                }
            }
        }
        return false;
    }

    double applyGuardianProtection(double chance, int row, int column) {
        return isProtected(row, column)
                ? chance * GuardianTree.PROTECTED_SPREAD_MULTIPLIER
                : chance;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
        wind.applyWind(this);
    }

    public Wind getWind() {
        return wind;
    }

    public void update() {
        guardianPulses.clear();
        spreadFire();
        advanceBurningTrees();
        for (GuardianPulse pulse : guardianPulses) {
            extinguishAround(pulse);
        }
    }

    public List<GuardianPulse> getGuardianPulses() {
        return List.copyOf(guardianPulses);
    }

    private void advanceBurningTrees() {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                Cell cell = grid[row][col];

                if (!cell.hasTree()) {
                    continue;
                }

                Tree tree = cell.getTree();

                if (tree.isBurning()) {
                    tree.advanceBurning();
                }

                if (!tree.isAlive()) {
                    cell.removeTree();
                    if (tree instanceof GuardianTree) {
                        guardianPulses.add(new GuardianPulse(row, col));
                    }
                }
            }
        }
    }

    private void extinguishAround(GuardianPulse pulse) {
        int radius = GuardianTree.PROTECTION_RADIUS;
        for (int row = Math.max(0, pulse.row() - radius);
                row <= Math.min(grid.length - 1, pulse.row() + radius); row++) {
            for (int col = Math.max(0, pulse.column() - radius);
                    col <= Math.min(grid[row].length - 1, pulse.column() + radius); col++) {
                Tree tree = grid[row][col].getTree();
                if (tree != null && tree.isBurning()
                        && GuardianTree.isWithinRadius(row - pulse.row(), col - pulse.column())) {
                    tree.extinguish();
                }
            }
        }
    }

    
    private void spreadFire() {

        boolean[][] igniteNext =
                new boolean[grid.length][grid[0].length];

        for (int row = 0; row < grid.length; row++) {

            for (int col = 0; col < grid[row].length; col++) {

                Cell cell = grid[row][col];

                if (cell.hasTree() &&
                    cell.getTree().isBurning()) {

                    trySpread(row, col, row - 1, col, igniteNext);
                    trySpread(row, col, row + 1, col, igniteNext);
                    trySpread(row, col, row, col - 1, igniteNext);
                    trySpread(row, col, row, col + 1, igniteNext);
                    trySpread(row, col, row - 1, col - 1, igniteNext);
                    trySpread(row, col, row - 1, col + 1, igniteNext);
                    trySpread(row, col, row + 1, col - 1, igniteNext);
                    trySpread(row, col, row + 1, col + 1, igniteNext);
                }
            }
        }

        // Ignite AFTER checking the entire grid
        for (int row = 0; row < grid.length; row++) {

            for (int col = 0; col < grid[row].length; col++) {

                if (igniteNext[row][col]) {
                    grid[row][col].getTree().ignite();
                }
            }
        }
    }

    private void trySpread(
        int sourceRow,
        int sourceCol,
        int row,
        int col,
        boolean[][] igniteNext) {

        if (row < 0 ||
            row >= grid.length ||
            col < 0 ||
            col >= grid[0].length) {

            return;
        }

        Cell target = grid[row][col];

        if (!target.hasTree()) {
            return;
        }

        if (target.getTree().isBurning()) {
            return;
        }

        double chance =
                target.getTree().getSpreadability();

        if (wind != null) {

            chance += wind.getSpreadModifier(
                    sourceCol,
                    sourceRow,
                    col,
                    row
            );
}

        chance += aridity * 0.2;

        chance += (temperature / 100.0) * 0.2;

        chance -= rainfall * 0.2;

        chance = Math.max(0.0, Math.min(1.0, chance));
        chance = applyGuardianProtection(chance, row, col);

        if (random.nextDouble() < chance) {
            igniteNext[row][col] = true;
        }
    }

    public void display() {

        for (Cell[] row : grid) {

            for (Cell cell : row) {

                if (!cell.hasTree()) {
                    System.out.print(". ");

                } else if (cell.getTree().isBurning()) {
                    System.out.print("X ");

                } else {
                    System.out.print("T ");
                }
            }

            System.out.println();
        }
    }
}
