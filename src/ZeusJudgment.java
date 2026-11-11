import java.awt.geom.Path2D;

public class ZeusJudgment extends IgnitionSource {
    public ZeusJudgment() {
        super(0.9, 0.0);
    }

    @Override
    public void ignite(Forest forest) {
        Cell[][] grid = forest.getGrid();
        if (grid.length == 0 || grid[0].length == 0) {
            return;
        }

        int firstRow = grid.length / 16;
        int height = Math.max(1, (int) (grid.length * 0.8));
        int width = Math.max(1, grid[0].length / 2);
        int firstColumn = (grid[0].length - width) / 2;

        // The two ledges form the sharp bend between the top and the tapered tip.
        Path2D bolt = new Path2D.Double();
        bolt.moveTo(0.62, 0.0);
        bolt.lineTo(0.13, 0.55);
        bolt.lineTo(0.43, 0.55);
        bolt.lineTo(0.31, 1.0);
        bolt.lineTo(0.88, 0.38);
        bolt.lineTo(0.57, 0.38);
        bolt.closePath();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Cell cell = grid[firstRow + row][firstColumn + col];
                if (cell.hasTree() && bolt.contains((col + 0.5) / width, (row + 0.5) / height)) {
                    cell.getTree().ignite(severity, FireColor.BLUE);
                }
            }
        }
    }
}
