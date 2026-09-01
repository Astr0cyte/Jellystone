public class Backburning extends IgnitionSource {

    private int row;

    public Backburning(int row) {

        super(0.7, 0.4);

        this.row = row;
    }

    @Override
    public void ignite(Forest forest) {

        Cell[][] grid = forest.getGrid();

        if (row < 0 || row >= grid.length) {
            return;
        }

        for (int col = 0;
             col < grid[row].length;
             col++) {

            Cell cell = grid[row][col];

            if (cell.hasTree()) {
                cell.getTree().ignite(severity);
            }
        }
    }
}
