import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Statistics {
    private long totalTraffic = 0; // ← изменено на long
    private LocalDateTime minTime = null;
    private LocalDateTime maxTime = null;

    public Statistics() {}

    public void addEntry(LogEntry entry) {
        totalTraffic += entry.getResponseSize();

        LocalDateTime ts = entry.getTimestamp();
        if (minTime == null || ts.isBefore(minTime)) {
            minTime = ts;
        }
        if (maxTime == null || ts.isAfter(maxTime)) {
            maxTime = ts;
        }
    }

    public double getTrafficRate() {
        if (minTime == null || maxTime == null) return 0.0;
        if (minTime.equals(maxTime)) return totalTraffic; // за <1 часа

        long hours = ChronoUnit.HOURS.between(minTime, maxTime);
        if (hours == 0) hours = 1;
        return (double) totalTraffic / hours;
    }
}