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
        int lastColumn = grid[0].length - 1 - margin;
        int span = Math.max(1, lastColumn - margin);
        int halfWidth = grid.length / 30;
        int bandWidth = Math.max(1, (lastColumn - margin + 1) / 16);

        for (int column = margin; column <= lastColumn; column++) {
            double progress = (column - margin) / (double) span;
            double bend = Math.sin(2 * Math.PI * progress);
            int centerRow = (int) Math.round(lastRow * (0.5 + 0.28 * bend));
            int top = Math.max(0, centerRow - halfWidth);
            int bottom = Math.min(lastRow, centerRow + halfWidth);
            int band = (column - margin) / bandWidth;
            FireColor color = band % 2 == 0 ? FireColor.BLACK : FireColor.WHITE;

            for (int row = top; row <= bottom; row++) {
                Cell cell = grid[row][column];
                if (cell.hasTree()) {
                    cell.getTree().ignite(severity, color);
                }
            }
        }
    }
}
