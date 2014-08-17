package utils;

public class Printer {
    int ident = 0;

    public Printer(int ident) {
        this.ident = ident;
    }

    private StringBuilder stringBuilder = new StringBuilder();

    public static String repeat(int n,String s) {
        if (s == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(s);
        }
        return sb.toString();
    }



    public void print(Object object){
        stringBuilder.append(object.toString());
    }

    public void sprintln(Object object){
        print(object);
        print('\n');
    }
}
