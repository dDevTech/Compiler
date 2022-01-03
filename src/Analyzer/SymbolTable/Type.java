package Analyzer.SymbolTable;

public enum Type {
    BOOLEAN(1),INTEGER(1),STRING(64),FUNCTION(0),ERROR_TYPE(-1),EMPTY(-1),OK_TYPE(-1);
    int size = 0;
    private Type(int size){
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
