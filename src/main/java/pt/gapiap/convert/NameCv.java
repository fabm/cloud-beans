package pt.gapiap.convert;

import javax.lang.model.element.Name;

public class NameCv implements Name{
    private String string;

    public NameCv(String string) {
        this.string = string;
    }

    @Override
    public boolean contentEquals(CharSequence cs) {
        return string.contentEquals(cs);
    }

    @Override
    public int length() {
        return string.length();
    }

    @Override
    public char charAt(int index) {
        return string.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return string.subSequence(start, end);
    }

    @Override
    public String toString() {
        return string;
    }
}
