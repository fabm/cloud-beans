package pt.gapiap.cloud.tests;

public class StringBuilderWithIdentation {
    private StringBuilder stringBuilder;
    private int identation;

    public StringBuilderWithIdentation(int identation) {
        this.stringBuilder = new StringBuilder();
        this.identation = identation;
    }

    private void ident() {
        for (int i = 0; i < identation; i++) {
            stringBuilder.append(' ');
        }
    }

    public void addLine(String string) {
        ident();
        stringBuilder.append(string);
        stringBuilder.append('\n');
    }
    public void addLineF(String string,Object...args){
        addLine(String.format(string, args));
    }

    public void addString(String string) {
        stringBuilder.append(string);
    }

    public void lineOfRepeatedChars(char c, int times) {
        ident();
        for (int i = 0; i < times; i++) {
            stringBuilder.append(c);
        }
        stringBuilder.append('\n');
    }
    public void append(StringBuilderWithIdentation stringBuilderWithIdentation){
        addString(stringBuilderWithIdentation.toString());
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }
}
