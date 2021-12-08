package Analyzer.SymbolTable;

public class SymbolEntry {
    private String lexeme;
    private Type type;
    private int displacement = 0;
    private int numParameters = 0;
    private Type[]typesParamters;
    private Type returnType;
    private String tag;
    private static int tagCounter = 0;
    public SymbolEntry(String lexeme){
        this.lexeme = lexeme;
    }

    public void setupAsFunction(String lexeme){

        tag = lexeme + "_"+tagCounter;
        tagCounter++;

    }
    public int setType(Type type,SymbolTable table){
        this.type = type;
        displacement =  table.getDisplacementCount();
        table.setDisplacementCount(displacement+type.getSize());
        return table.getDisplacementCount();
    }
    public void setParameters(Type ... types){
        numParameters = types.length;
        typesParamters = types;

    }
    public void setReturnType(Type type){
        returnType = type;
    }


    public int getNumParameters() {
        return numParameters;
    }


    public Type[] getTypesParamters() {
        return typesParamters;
    }


    public Type getReturnType() {
        return returnType;
    }

    public String getTag() {
        return tag;
    }


    public static int getTagCounter() {
        return tagCounter;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public Type getType() {
        return type;
    }


}
