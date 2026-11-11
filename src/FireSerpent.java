public class FireSerpent extends IgnitionSource {
    public FireSerpent() {
        super(0.8, 0.0);
    }

    @Override
    public void ignite(Forest forest) {
        Cell[][] grid = forest.getGrid();
        if (grid.length == 0 || grid[0].length == 0) {
            return;
        }

        int lastRow = grid.length - 1;
        int margin = grid[0].length / 12;
        int lastColumn = grid[0].length - 1;
        int headWidth = Math.max(1, grid[0].length / 15);
        int bodyEnd = Math.max(margin, lastColumn - margin - headWidth);
        int span = Math.max(1, bodyEnd - margin);
        int previousRow = (int) Math.round(lastRow * 0.5);

        for (int column = margin; column <= bodyEnd; column++) {
            double progress = (column - margin) / (double) span;
            double phase = 4 * Math.PI * progress + 0.45 * Math.sin(2 * Math.PI * progress);
            double amplitude = 0.22 + 0.04 * Math.sin(Math.PI * progress);
            int centerRow = (int) Math.round(lastRow * (0.5 + amplitude * Math.sin(phase)));
            int halfWidth = (int) Math.round(grid.length / 20.0 * Math.min(1.0, progress * 4));
            // Bridge steep bends so the path stays connected on a full grid.
            int top = Math.max(0, Math.min(centerRow - halfWidth, previousRow + 1));
            int bottom = Math.min(lastRow, Math.max(centerRow + halfWidth, previousRow - 1));

            for (int row = top; row <= bottom; row++) {
                igniteCell(grid[row][column]);
            }
            previousRow = centerRow;
        }

        int headColumn = Math.min(lastColumn, bodyEnd + 1);
        int headHeight = Math.max(1, grid.length / 12);
        for (int row = Math.max(0, previousRow - headHeight);
                row <= Math.min(lastRow, previousRow + headHeight); row++) {
            for (int col = Math.max(0, headColumn - headWidth);
                    col <= Math.min(lastColumn, headColumn + headWidth); col++) {
                double x = (col - headColumn) / (double) headWidth;
                double y = (row - previousRow) / (double) headHeight;
                if (x * x + y * y <= 1.0) {
                    igniteCell(grid[row][col]);
                }
            }
        }
    }

    private void igniteCell(Cell cell) {
        if (cell.hasTree()) {
            cell.getTree().ignite(severity, FireColor.MAGENTA);
        }
    }
}
