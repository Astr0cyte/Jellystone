public class IgnitionSourceTest {
    public static void main(String[] args) {
        testDefaultSeverity();
        testChosenSeverity();
        testSeverityUpdate();
        testInvalidSeverity();

        System.out.println("All IgnitionSource tests passed.");
    }

    private static void testDefaultSeverity() {
        IgnitionSource source = new IgnitionSource();

        check(
                source.getSeverity() == 1,
                "Default severity should be 1"
        );
    }

    private static void testChosenSeverity() {
        IgnitionSource source = new IgnitionSource(3);

        check(
                source.getSeverity() == 3,
                "Constructor should store severity"
        );
    }

    private static void testSeverityUpdate() {
        IgnitionSource source = new IgnitionSource(2);
        source.setSeverity(5);

        check(
                source.getSeverity() == 5,
                "Setter should update severity"
        );
    }

    private static void testInvalidSeverity() {
        boolean exceptionThrown = false;

        try {
            new IgnitionSource(6);
        } catch (IllegalArgumentException exception) {
            exceptionThrown = true;
        }

        check(
                exceptionThrown,
                "Invalid severity should throw an exception"
        );
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
