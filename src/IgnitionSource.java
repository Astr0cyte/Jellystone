public abstract class IgnitionSource {

    protected double severity;
    protected double spreadability;

    public IgnitionSource(
            double severity,
            double spreadability) {

        validateValue("Severity", severity);
        validateValue("Spreadability", spreadability);

        this.severity = severity;
        this.spreadability = spreadability;
    }

    private void validateValue(String name, double value) {
        if (value < 0.0 || value > 1.0) {
            throw new IllegalArgumentException(
                    name + " must be between 0.0 and 1.0"
            );
        }
    }

    public abstract void ignite(Forest forest);
}
