package Tools;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class FileIterator<T> implements Iterator<T> {
     int column = 0;
     int line = 0;
     T current;
    public abstract void skipLine();
    public int getColumn() {
        return column;
    }

    public int getLine() {
        return line;
    }

    public T getCurrentElement() {
        return current;
    }
}
