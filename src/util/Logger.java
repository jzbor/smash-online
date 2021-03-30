package util;

public class Logger {
    private static final long startTime = System.currentTimeMillis();
    private static final String[] levels = new String[] {
            "debug",
            "info",
            "warning",
            "error",
    };

    public static void log(String message) {
        log(getContextString(), message, 0);
    }

    public static void log(String message, int level) {
        log(getContextString(), message, level);
    }

    public static void log(String context, String message, int level) {
        System.out.println((System.currentTimeMillis() - startTime) + ":\t[" + levels[level] + "]\t" + context + " | " + message);
    }

    private static String getContextString() {
        StackTraceElement context = Thread.currentThread().getStackTrace()[3];
        return context.getClassName() + "." + context.getMethodName() + ":" + context.getLineNumber();
    }
}
