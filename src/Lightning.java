import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Lightning extends IgnitionSource {
    private Random random = new Random();
    private List<Cell> struckCells = new ArrayList<>();
    private static final int MAX_ATTEMPTS = 10;

    public Lightning() {
        super(0.8, 0.7);
    }

    public void ignite(Forest forest) {
        Cell[][] grid = forest.getGrid();

        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            int row = random.nextInt(grid.length);
            int col = random.nextInt(grid[0].length);
            Cell cell = grid[row][col];

            if (struckCells.contains(cell)) {
                continue;
            }

            struckCells.add(cell);

            if (cell.hasTree()) {
                cell.getTree().ignite(this.severity);
                return;
            }
        }

        throw new IllegalStateException(
            "Lightning found no ignitable cell after " + MAX_ATTEMPTS + " attempts"
        );
    }
}
