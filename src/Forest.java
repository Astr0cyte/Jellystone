import java.util.Random;

public class Forest {

    private Cell[][] grid;

    private double temperature;
    private double rainfall;
    private double aridity;

    // private Wind wind;

    private Random random = new Random();

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
    /*
    public void setWind(Wind wind) {
        this.wind = wind;
        wind.applyWind(this);
    }

    public Wind getWind() {
        return wind;
    }
    */

    public void update() {

        spreadFire();
    }

    private void spreadFire() {

        boolean[][] igniteNext =
                new boolean[grid.length][grid[0].length];

        for (int row = 0; row < grid.length; row++) {

            for (int col = 0; col < grid[row].length; col++) {

                Cell cell = grid[row][col];

                if (cell.hasTree() &&
                    cell.getTree().isBurning()) {

                    trySpread(row - 1, col, igniteNext);
                    trySpread(row + 1, col, igniteNext);
                    trySpread(row, col - 1, igniteNext);
                    trySpread(row, col + 1, igniteNext);
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

        // chance += target.getWindExposure() * 0.2;

        chance += aridity * 0.2;

        chance -= rainfall * 0.2;

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