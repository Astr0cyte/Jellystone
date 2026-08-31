public class IgnitionSource {
    private int severity;

    public IgnitionSource() {
       this(1);
    }

    public IgnitionSource(int severity) {
        setSeverity(severity);
    }

    public int getSeverity() {
        return this.severity;
    }

    public void setSeverity(int severity) {
        if (severity < 1 || severity > 5) {
            throw new IllegalArgumentException(
                    "Severity must be between 1 and 5"
            );
        }

        this.severity = severity;
    }
}
