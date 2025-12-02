public class UserAgent {
    private final String os;
    private final String browser;

    public UserAgent(String userAgentString) {
        this.os = detectOS(userAgentString);
        this.browser = detectBrowser(userAgentString);
    }

    private String detectOS(String ua) {
        ua = ua.toLowerCase();
        if (ua.contains("windows")) return "Windows";
        if (ua.contains("mac")) return "macOS";
        if (ua.contains("linux") || ua.contains("android") || ua.contains("ubuntu")) return "Linux";
        return "Other";
    }

    private String detectBrowser(String ua) {
        ua = ua.toLowerCase();
        if (ua.contains("edg/") || ua.contains("edge")) return "Edge";
        if (ua.contains("firefox")) return "Firefox";
        if (ua.contains("opr/") || ua.contains("opera")) return "Opera";
        if (ua.contains("chrome") && !ua.contains("edg/") && !ua.contains("opr/")) return "Chrome";
        if (ua.contains("safari") && !ua.contains("chrome")) return "Safari";
        return "Other";
    }

    public String getOs() { return os; }
    public String getBrowser() { return browser; }
}