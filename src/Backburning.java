import java.util.stream.IntStream;

public class Backburning extends IgnitionSource {

    private static final double WAVELENGTH = 3.0;

    private int row;

    public Backburning(int row) {

        // Controlled and deterministic (unlike Arson/Lightning): lit in a
        // staggered, wavelike pattern along the row rather than all at once.
        super(0.7, 0.4);

        this.row = row;
    }

    @Override
    public void ignite(Forest forest) {

        Cell[][] grid = forest.getGrid();

        if (row < 0 || row >= grid.length) {
            return;
        }

        double frequency = 2 * Math.PI / WAVELENGTH;

        Cell[] selectedRow = grid[row];
        // Only trees in the troughs catch immediately.
        IntStream.range(0, selectedRow.length)
                .filter(col -> selectedRow[col].hasTree())
                .filter(col -> (Math.sin(col * frequency) + 1) / 2 < spreadability)
                .mapToObj(col -> selectedRow[col].getTree())
                .forEachOrdered(tree -> tree.ignite(severity));
    }
}

