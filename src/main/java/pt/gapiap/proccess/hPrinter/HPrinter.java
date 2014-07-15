package pt.gapiap.proccess.hPrinter;

import java.io.PrintWriter;

public class HPrinter {
    private int level = 0;
    private PrintWriter out;

    public HPrinter(PrintWriter out) {
        this.out = out;
    }

    private HPrinter() {
    }

    public void print(String string) {
        if (string.endsWith("\n")) {
            int i = 4;
            i *= level;
            for (int j = 0; j < i; j++) {
                out.println(" ");
            }
            out.print(string);
        }
    }

    public void printChild(Hprintable hprintable) {
        HPrinter childPrinter = new HPrinter();
        childPrinter.level = level + 1;
        childPrinter.out = out;
        out.println();
        hprintable.print(childPrinter);
    }

}
