import java.util.Random;

public class Lightning extends IgnitionSource {

    private Random random = new Random();

    public Lightning() {
        super(0.8, 0.7);
    }

    @Override
    public void ignite(Forest forest) {

        Cell[][] grid = forest.getGrid();

        int row = random.nextInt(grid.length);
        int col = random.nextInt(grid[0].length);

        Cell cell = grid[row][col];

        if (cell.hasTree()) {
            cell.getTree().ignite(severity);
        }
    }
}
