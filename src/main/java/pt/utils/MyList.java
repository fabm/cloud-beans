package pt.utils;

import java.util.Iterator;

public class MyList<T extends Iterable>{
    T my;

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    String separator = ",";

    public MyList(T my) {
        this.my = my;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator it = my.iterator();
        sb.append("(");
        while (it.hasNext()){
                sb.append(it.next().toString());
            if(it.hasNext())
                sb.append(separator);
        }
        sb.append(")");
        return sb.toString();
    }
}
