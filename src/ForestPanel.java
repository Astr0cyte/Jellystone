import java.awt.*;
import javax.swing.*;

public class ForestPanel extends JPanel {

    private Forest forest;

    private Image treeImage;
    private Image fireImage;

    public ForestPanel(Forest forest) {
        this.forest = forest;

        System.out.println(new java.io.File("images/tree.png").exists());
        System.out.println(new java.io.File("images/fire.png").exists());
        
        treeImage = new ImageIcon("images/tree.png").getImage();
        fireImage = new ImageIcon("images/fire.png").getImage();
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

                g.setColor(Color.BLACK);

                g.fillRect(
                    col * cellWidth,
                    row * cellHeight,
                    cellWidth,
                    cellHeight
                );

                if (!cell.hasTree()) {
                    
                }
                else if (cell.getTree().isBurning()) {
                    g.drawImage(
                        fireImage,
                        col * cellWidth + 2,
                        row * cellHeight + 2,
                        cellWidth - 4,
                        cellHeight - 4,
                        this
                    );
                }
                else {
                    g.drawImage(
                        treeImage,
                        col * cellWidth + 2,
                        row * cellHeight + 2,
                        cellWidth - 4,
                        cellHeight - 4,
                        this
                    );
                }
            }
        }
    }
}