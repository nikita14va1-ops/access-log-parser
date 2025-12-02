import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {
    private final String ip;
    private final LocalDateTime timestamp;
    private final HttpMethod method;
    private final String requestPath;
    private final int statusCode;
    private final long responseSize;
    private final String referer;
    private final UserAgent userAgent;

    public LogEntry(String logLine) {
        // Проверка длины уже выполнена в Main

        // 1. IP — всё до первого пробела
        int firstSpace = logLine.indexOf(' ');
        if (firstSpace == -1) throw new IllegalArgumentException("Нет IP");
        this.ip = logLine.substring(0, firstSpace);

        // 2. Дата — между [ и ]
        int dateStart = logLine.indexOf('[');
        int dateEnd = logLine.indexOf(']');
        if (dateStart == -1 || dateEnd == -1) throw new IllegalArgumentException("Нет даты");
        String dateStr = logLine.substring(dateStart + 1, dateEnd).split(" ")[0]; // отрезаем часовой пояс
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss");
        try {
            this.timestamp = LocalDateTime.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Неверный формат даты: " + dateStr, e);
        }

        // 3. Запрос — между первыми кавычками после даты
        int reqStart = logLine.indexOf('"');
        int reqEnd = logLine.indexOf('"', reqStart + 1);
        if (reqStart == -1 || reqEnd == -1) throw new IllegalArgumentException("Нет запроса");
        String requestLine = logLine.substring(reqStart + 1, reqEnd);
        String[] reqParts = requestLine.split(" ", 3);
        if (reqParts.length < 2) throw new IllegalArgumentException("Плохой запрос: " + requestLine);
        this.method = parseHttpMethod(reqParts[0]);
        this.requestPath = reqParts[1];

        // 4. Код ответа и размер — после запроса, до referer
        int afterRequest = reqEnd + 1;
        String remaining = logLine.substring(afterRequest).trim();
        String[] statusSize = remaining.split(" ", 2);
        if (statusSize.length < 2) throw new IllegalArgumentException("Нет статуса/размера");

        this.statusCode = Integer.parseInt(statusSize[0]);
        String sizeStr = statusSize[1].split(" ")[0].trim();
        this.responseSize = "-".equals(sizeStr) ? 0 : Long.parseLong(sizeStr);

        // 5. Referer и User-Agent — последние две строки в кавычках
        int lastQuote = logLine.lastIndexOf('"');
        int secondLastQuote = logLine.lastIndexOf('"', lastQuote - 1);
        int thirdLastQuote = logLine.lastIndexOf('"', secondLastQuote - 1);
        int fourthLastQuote = logLine.lastIndexOf('"', thirdLastQuote - 1);

        if (fourthLastQuote == -1) {
            // Только один кавычный фрагмент — возможно, нет referer или UA
            if (thirdLastQuote == -1) {
                this.referer = "-";
                this.userAgent = new UserAgent("-");
            } else {
                this.referer = logLine.substring(thirdLastQuote + 1, secondLastQuote);
                this.userAgent = new UserAgent("-");
            }
        } else {
            this.referer = logLine.substring(thirdLastQuote + 1, secondLastQuote);
            String uaStr = logLine.substring(secondLastQuote + 1, lastQuote);
            this.userAgent = new UserAgent(uaStr);
        }
    }

    private HttpMethod parseHttpMethod(String methodStr) {
        try {
            return HttpMethod.valueOf(methodStr);
        } catch (IllegalArgumentException e) {
            return HttpMethod.GET;
        }
    }

    // Геттеры
    public String getIp() { return ip; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public HttpMethod getMethod() { return method; }
    public String getRequestPath() { return requestPath; }
    public int getStatusCode() { return statusCode; }
    public long getResponseSize() { return responseSize; }
    public String getReferer() { return referer; }
    public UserAgent getUserAgent() { return userAgent; }
}