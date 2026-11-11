import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.*;

public class ForestPanel extends JPanel {

    private Forest forest;

    private Image treeImage;
    private Image seedlingImage;
    private Image guardianImage;
    private static final Color PROTECTED_SOIL = new Color(171, 190, 131);
    private static final int GUARDIAN_SPRITE_SCALE = 4;
    private final Map<FireColor, Image> fireImages = new EnumMap<>(FireColor.class);
    private final Map<Class<? extends Tree>, Image> speciesImages;

    private Lightning lightning;
    private Image lightningImage;

    private Cell lastLightningCell;
    private long lightningStartTime;

    public ForestPanel(Forest forest, Lightning lightning) {
        this.forest = forest;
        this.lightning = lightning;
        setBackground(Color.BLACK);

        System.out.println(new java.io.File("images/tree.png").exists());
        System.out.println(new java.io.File("images/fire.png").exists());
        System.out.println(new java.io.File("images/lightning.png").exists());

        ImageIcon treeIcon = new ImageIcon("images/tree.png");
        treeImage = treeIcon.getImage();
        speciesImages = Map.of(
                Oak.class, tintSprite(treeIcon, new Color(112, 49, 20), new Color(0xB85A2B), true),
                Birch.class, tintSprite(treeIcon, new Color(67, 102, 19), new Color(0xA7CF46), true),
                Eucalyptus.class, tintSprite(treeIcon, new Color(44, 91, 84), new Color(0x78BDB5), true),
                Spruce.class, tintSprite(treeIcon, new Color(16, 45, 30), new Color(0x24533B), true));
        seedlingImage = new ImageIcon("images/ash-bloom-seedling.png").getImage();
        guardianImage = new ImageIcon("images/guardian-tree-gold.png").getImage();
        ImageIcon fireIcon = new ImageIcon("images/fire.png");
        fireImages.put(FireColor.ORANGE, fireIcon.getImage());
        fireImages.put(FireColor.BLUE, tintFire(fireIcon, new Color(0, 0, 128), new Color(127, 255, 255)));
        fireImages.put(FireColor.PURPLE, tintFire(fireIcon, new Color(52, 0, 105), new Color(194, 138, 255)));
        fireImages.put(FireColor.MAGENTA, tintFire(fireIcon, new Color(145, 0, 103), new Color(255, 79, 236)));
        lightningImage = new ImageIcon("images/lightning.png").getImage();
    }

    private Image tintFire(ImageIcon fireIcon, Color shadow, Color highlight) {
        return tintSprite(fireIcon, shadow, highlight, false);
    }

    private Image tintSprite(ImageIcon icon, Color shadow, Color highlight, boolean canopyOnly) {
        if (icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) {
            return icon.getImage();
        }

        int width = canopyOnly ? Math.min(128, icon.getIconWidth()) : icon.getIconWidth();
        int height = canopyOnly ? Math.min(128, icon.getIconHeight()) : icon.getIconHeight();
        BufferedImage image = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.drawImage(icon.getImage(), 0, 0, width, height, null);
        graphics.dispose();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                int alpha = pixel >>> 24;
                int red = (pixel >>> 16) & 255;
                int green = (pixel >>> 8) & 255;
                int blue = pixel & 255;
                if (canopyOnly && (green <= red || green <= blue)) {
                    continue;
                }
                int brightness = (3 * red + 6 * green + blue) / 10;
                Color tint = new Color(
                        blend(shadow.getRed(), highlight.getRed(), brightness),
                        blend(shadow.getGreen(), highlight.getGreen(), brightness),
                        blend(shadow.getBlue(), highlight.getBlue(), brightness),
                        alpha);
                image.setRGB(x, y, tint.getRGB());
            }
        }
        return image;
    }

    private int blend(int shadow, int highlight, int brightness) {
        return shadow + (highlight - shadow) * brightness / 255;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Cell[][] grid = forest.getGrid();

        int cellWidth = getWidth() / grid[0].length;
        int cellHeight = getHeight() / grid.length;

        Cell currentLightningCell = null;

        if (lightning != null) {
            currentLightningCell = lightning.getLastStruckCell();

            if (currentLightningCell != lastLightningCell) {
                lastLightningCell = currentLightningCell;
                lightningStartTime = System.currentTimeMillis();
            }
        }
        boolean showLightning = currentLightningCell != null
                && System.currentTimeMillis() - lightningStartTime < 3000;

        for (int row = 0; row < grid.length; row++) {

            for (int col = 0; col < grid[row].length; col++) {

                Cell cell = grid[row][col];

                g.setColor(forest.isProtected(row, col) ? PROTECTED_SOIL : getBackground());

                g.fillRect(
                    col * cellWidth,
                    row * cellHeight,
                    cellWidth,
                    cellHeight
                );

                if (cell.getTree() instanceof GuardianTree) {
                    continue;
                }

                if (showLightning && cell == currentLightningCell) {
                    g.drawImage(
                        lightningImage,
                        col * cellWidth + 2,
                        row * cellHeight + 2,
                        cellWidth - 4,
                        cellHeight - 4,
                        this
                    );
                }
                else {
                    if (!cell.hasTree()) {

                        // Empty cells show the soil background.

                    }
                    else if (cell.getTree().isBurning()) {

                        g.drawImage(
                            fireImages.get(cell.getTree().getFireColor()),
                            col * cellWidth + 2,
                            row * cellHeight + 2,
                            cellWidth - 4,
                            cellHeight - 4,
                            this
                        );
                    }
                    else {

                        g.drawImage(
                            cell.getTree() instanceof Seedling ? seedlingImage
                                    : speciesImages.getOrDefault(cell.getTree().getClass(), treeImage),
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

        // Draw the larger sprites after the grid so neighbouring cells cannot clip them.
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                Cell cell = grid[row][col];
                if (!(cell.getTree() instanceof GuardianTree guardian)) {
                    continue;
                }

                int x = col * cellWidth;
                int y = row * cellHeight;
                int treeWidth = Math.max(1, cellWidth - 4);
                int treeHeight = Math.max(1, cellHeight - 4);
                int guardianWidth = treeWidth * GUARDIAN_SPRITE_SCALE;
                int guardianHeight = treeHeight * GUARDIAN_SPRITE_SCALE;
                g.drawImage(guardianImage,
                        x + (cellWidth - guardianWidth) / 2,
                        y + (cellHeight - guardianHeight) / 2,
                        guardianWidth, guardianHeight, this);

                if (showLightning && cell == currentLightningCell) {
                    g.drawImage(lightningImage, x + 2, y + 2, treeWidth, treeHeight, this);
                } else if (guardian.isBurning()) {
                    g.drawImage(fireImages.get(guardian.getFireColor()),
                            x + 2, y + 2, treeWidth, treeHeight, this);
                }
            }
        }
        drawGuardianPulses(g, cellWidth, cellHeight);
    }

    private void drawGuardianPulses(Graphics g, int cellWidth, int cellHeight) {
        int radius = GuardianTree.PROTECTION_RADIUS;
        for (Forest.GuardianPulse pulse : forest.getGuardianPulses()) {
            int x = pulse.column() * cellWidth + cellWidth / 2 - radius * cellWidth;
            int y = pulse.row() * cellHeight + cellHeight / 2 - radius * cellHeight;
            int width = 2 * radius * cellWidth;
            int height = 2 * radius * cellHeight;
            g.setColor(new Color(255, 215, 80, 100));
            g.fillOval(x, y, width, height);
            g.setColor(new Color(255, 235, 150));
            g.drawOval(x, y, width, height);
        }
    }
}
