import java.util.Arrays;
import java.util.Random;

public class SpreadBenchmark {
    private static final long SEED = 42;
    private static final int SAMPLES = 7;

    record Run(long nanos, int ticks, long survivors) {}

    static Forest createScenario(boolean parallel) {
        Forest forest = new Forest(40, 60, SEED, parallel);
        forest.setWind(new Wind(30, "EAST", 30, 20, 15));
        Main.plantGuardians(forest, new Random(SEED));
        new PhoenixRising().ignite(forest);
        return forest;
    }

    static void run() {
        for (int warmup = 0; warmup < 2; warmup++) {
            timeRun(false);
            timeRun(true);
        }

        long[] sequentialTimes = new long[SAMPLES];
        long[] parallelTimes = new long[SAMPLES];
        Run result = null;
        for (int sample = 0; sample < SAMPLES; sample++) {
            boolean parallelFirst = sample % 2 != 0;
            Run first = timeRun(parallelFirst);
            Run second = timeRun(!parallelFirst);
            if (first.ticks() != second.ticks() || first.survivors() != second.survivors()) {
                throw new IllegalStateException("Sequential and parallel results differ");
            }
            sequentialTimes[sample] = parallelFirst ? second.nanos() : first.nanos();
            parallelTimes[sample] = parallelFirst ? first.nanos() : second.nanos();
            result = first;
        }

        Arrays.sort(sequentialTimes);
        Arrays.sort(parallelTimes);
        double sequentialMs = sequentialTimes[SAMPLES / 2] / 1_000_000.0;
        double parallelMs = parallelTimes[SAMPLES / 2] / 1_000_000.0;
        System.out.printf("Phoenix, seed %d: both modes finished in %d ticks with %d survivors%n",
                SEED, result.ticks(), result.survivors());
        System.out.printf("Median across %d runs: sequential %.2f ms, parallel %.2f ms (%.2fx speedup)%n",
                SAMPLES, sequentialMs, parallelMs, sequentialMs / parallelMs);
    }

    private static Run timeRun(boolean parallel) {
        Forest forest = createScenario(parallel);
        int ticks = 0;
        long start = System.nanoTime();
        while (forest.hasBurningTrees()) {
            forest.update();
            ticks++;
        }
        long elapsed = System.nanoTime() - start;
        long survivors = Arrays.stream(forest.getGrid())
                .flatMap(Arrays::stream)
                .filter(Cell::hasTree)
                .count();
        return new Run(elapsed, ticks, survivors);
    }
}
