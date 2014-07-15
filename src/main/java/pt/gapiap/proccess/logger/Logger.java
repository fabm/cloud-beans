package pt.gapiap.proccess.logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Logger {
    private static PrintWriter printWriter;
    private boolean debug = true;
    private boolean trace = true;

    public Logger(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }


    private Logger() {
    }

    public static void create(PrintWriter printWriter) {
        Logger.printWriter = printWriter;
    }

    public static Logger get() {
        return new Logger();
    }

    private void traceLog(int position) {
        if (trace) {
            StackTraceElement sTrace = Thread.currentThread().getStackTrace()[position];
            printWriter.println("-->trace in:" + sTrace.getClassName() + "#" + sTrace.getMethodName() + ":" + sTrace.getLineNumber());
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

    private String listStringify(List<?> list) {
        List<String> strList = new ArrayList<>(list.size());
        for (Object object : list) {
            if (object instanceof List<?>) {
                strList.add(listStringify((List<?>) object));
            } else {
                strList.add(object.toString());
            }
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(strList) + "\n";
    }

    private String mapStringify(Map<?, ?> map) {
        Map<String, String> strMap = new HashMap<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map<?, ?>) {
                strMap.put(key.toString(), mapStringify((Map<?, ?>) value));
            } else if (value instanceof List<?>) {
                strMap.put(key.toString(), listStringify((List<?>) value));
            } else {
                strMap.put(key.toString(), value.toString());
            }
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(strMap) + "\n";
    }

    public void log(List<?> list) {
        log(listStringify(list), 4);
    }

    public void close() {
        printWriter.close();
    }

    public void log(Map<?, ?> map) {
        log(mapStringify(map), 4);
    }

    public void log(Exception exception) {
        StackTraceElement[] strace = exception.getStackTrace();
        StringBuilder stringBuilder = new StringBuilder("exception " + exception.getClass().getCanonicalName()+"\n");
        String message = exception.getMessage();
        message = message == null ? "" : "message:" + message+"\n";
        stringBuilder.append(message);

        for (int i = 0; i < strace.length; i++) {
            stringBuilder.append(
                    strace[i].getClassName() +
                            ":" + strace[i].getLineNumber() + "\n"
            );
        }
        log(stringBuilder.toString(),4);
    }
}
