import javax.swing.*;
import java.awt.*;

public class ForestPanel extends JPanel {

    private Forest forest;

    public ForestPanel(Forest forest) {
        this.forest = forest;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Cell[][] grid = forest.getGrid();

        int cellWidth = getWidth() / grid[0].length;
        int cellHeight = getHeight() / grid.length;

        for (int row = 0; row < grid.length; row++) {

            for (int col = 0; col < grid[row].length; col++) {

                Cell cell = grid[row][col];

                if (!cell.hasTree()) {
                    g.setColor(Color.BLACK);
                }
                else if (cell.getTree().isBurning()) {
                    g.setColor(Color.RED);
                }
                else {
                    g.setColor(Color.GREEN);
                }

                g.fillRect(
                    col * cellWidth,
                    row * cellHeight,
                    cellWidth,
                    cellHeight
                );
            }
        }
    }
}