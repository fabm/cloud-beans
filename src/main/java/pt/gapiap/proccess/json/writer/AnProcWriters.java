package pt.gapiap.proccess.json.writer;

import pt.gapiap.proccess.logger.Logger;

import java.io.PrintWriter;

public interface AnProcWriters {
    PrintWriter getJsonWriter();
    Logger getlogger();
    void close();
}
