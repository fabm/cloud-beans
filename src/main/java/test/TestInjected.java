package test;

import java.io.PrintWriter;

public class TestInjected {
    PrintWriter printWriter;

    public TestInjected(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }
}
