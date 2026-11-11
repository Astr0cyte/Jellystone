public class PhoenixRising extends IgnitionSource {
    public PhoenixRising() {
        super(0.8, 0.0);
    }

    @Override
    public void ignite(Forest forest) {
        Cell[][] grid = forest.getGrid();
        if (grid.length == 0 || grid[0].length == 0) {
            return;
        }

        int centerRow = grid.length / 2;
        int centerColumn = grid[0].length / 2;
        int outerRadius = Math.max(1, Math.min(grid.length, grid[0].length) / 3);
        int innerRadius = Math.max(1, outerRadius - Math.max(1, outerRadius / 4));

        for (int row = 0; row < grid.length; row++) {
            int dy = row - centerRow;
            for (int column = 0; column < grid[row].length; column++) {
                int dx = column - centerColumn;
                int distanceSquared = dx * dx + dy * dy;
                Cell cell = grid[row][column];
                if (cell.hasTree() && distanceSquared >= innerRadius * innerRadius
                        && distanceSquared <= outerRadius * outerRadius) {
                    cell.getTree().ignite(severity, FireColor.PURPLE);
                }
            }
        }
    }

}
