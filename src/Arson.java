public class Arson extends IgnitionSource {

    private int row;
    private int col;

    public Arson(int row, int col) {

        super(1.0, 1.0);

        this.row = row;
        this.col = col;
    }

    @Override
    public void ignite(Forest forest) {

        Cell[][] grid = forest.getGrid();

        if (row < 0 ||
            row >= grid.length ||
            col < 0 ||
            col >= grid[0].length) {

            return;
        }

        Cell cell = grid[row][col];

        if (cell.hasTree()) {
            cell.getTree().ignite(severity);
        }
    }
}
