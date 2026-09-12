import javax.swing.JPanel;
import java.awt.*;

public class WindVisualization extends JPanel {

    private Forest forest;

    public WindVisualization(Forest forest) {
        if (forest == null) {
            throw new WindArgumentException("Forest must not be null");
        }

        this.forest = forest;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Cell[][] grid = forest.getGrid();

        if (grid.length == 0 || grid[0].length == 0) {
            return;
        }

        Graphics2D borderGraphics = (Graphics2D) graphics.create();
        borderGraphics.setColor(new Color(0, 160, 255));
        borderGraphics.setStroke(new BasicStroke(2.0f));

        int cellWidth = getWidth() / grid[0].length;
        int cellHeight = getHeight() / grid.length;

        for (int row = 0; row < grid.length; row++) {

            for (int col = 0; col < grid[row].length; col++) {

                if (grid[row][col].getWindExposure() <= 0.0) {
                    continue;
                }

                int x = col * cellWidth;
                int y = row * cellHeight;

                borderGraphics.drawRect(x, y, cellWidth - 1, cellHeight - 1);
            }
        }

        borderGraphics.dispose();
    }
}
