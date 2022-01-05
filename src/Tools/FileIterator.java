package Tools;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class FileIterator<T> implements Iterator<T> {
     int column = 0;
     int line = 1;

    public int getProductionLine() {
        return productionLine;
    }

    public void setProductionLine(int productionLine) {
        this.productionLine = productionLine;
    }

    int productionLine = -1;
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
    public abstract void close();
}
