public class Arson extends IgnitionSource {

    private int row;
    private int col;

    public Arson(int row, int col) {

        // Weaker than a lightning strike (severity 0.8): a deliberately
        // lit fire is smaller and only takes hold on sufficiently
        // flammable fuel.
        super(0.55, 0.5);

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

            // Unlike lightning, a small deliberate fire isn't guaranteed
            // to catch - the target needs to be flammable enough.
            if (cell.getTree().getSpreadability() >= spreadability) {
                cell.getTree().ignite(severity);
            }
        }
    }
}
