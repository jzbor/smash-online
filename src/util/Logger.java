package util;

public class Logger {
    private static final long startTime = System.currentTimeMillis();
    public static final int DEBUG = 0, INFO = 1, WARNING = 2, ERROR = 3;
    private static final String[] levels = new String[] {
            "debug",
            "info",
            "warning",
            "error",
    };

    public static void log(String message) {
        log(getContextString(0), message, 0);
    }

    public static void log(String message, int level) {
        log(getContextString(level), message, level);
    }

    public static void log(String context, String message, int level) {
        System.out.println((System.currentTimeMillis() - startTime) + ":\t[" + levels[level] + "]\t" + context + " | " + message);
    }

    private static String getContextString(int level) {
        StackTraceElement context = Thread.currentThread().getStackTrace()[3];
        return context.getClassName() + "." + context.getMethodName() + (level == 0 ? ":" + context.getLineNumber() : "");
    }
}
