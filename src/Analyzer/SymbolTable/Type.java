package Analyzer.SymbolTable;

public enum Type {
    BOOLEAN(1),INTEGER(2),STRING(32),FUNCTION(0);
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
