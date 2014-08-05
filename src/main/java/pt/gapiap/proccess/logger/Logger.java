package pt.gapiap.proccess.logger;


import java.io.PrintWriter;

public class Logger {
    private PrintWriter printWriter;
    private boolean debug = true;
    private boolean trace = true;

    public Logger(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isTrace() {
        return trace;
    }

    public void setTrace(boolean trace) {
        this.trace = trace;
    }

    private void traceLog(int position) {
        if (trace) {
            StackTraceElement sTrace = Thread.currentThread().getStackTrace()[position];
            printWriter.printf("%20s#%30s %3d:", sTrace.getFileName(), sTrace.getMethodName(), sTrace.getLineNumber());
        }
    }

    private void log(String s, int position) {
        if (debug) {
            if (trace) {
                traceLog(position);
            }
            printWriter.print(s);
        }
    }

    public void log(String s) {
        log(s, 4);
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    public void close() {
        printWriter.close();
    }

    public void log(Exception exception) {
        StackTraceElement[] strace = exception.getStackTrace();
        StringBuilder stringBuilder = new StringBuilder("exception " + exception.getClass().getCanonicalName() + "\n");
        String message = exception.getMessage();
        message = message == null ? "" : "message:" + message + "\n";
        stringBuilder.append(message);

        for (int i = 0; i < strace.length; i++) {
            stringBuilder.append(
                    strace[i].getClassName() +
                            ":" + strace[i].getLineNumber() + "\n"
            );
        }
        log(stringBuilder.toString(), 4);
    }

    public void logStack(boolean printall) {
        StackTraceElement[] st = Thread.currentThread().getStackTrace();
        printWriter.println("stack size:" + st.length);
        if (printall) {
            for (StackTraceElement ste : st) {
                printWriter.println(ste.getClassName() + "#" + ste.getMethodName() + ":" + ste.getLineNumber());
            }
        }
    }

    public void logStack() {
        logStack(false);
    }

}
