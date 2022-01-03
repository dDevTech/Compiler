package Analyzer.SymbolTable;

import java.util.ArrayList;
import java.util.Arrays;

public class SymbolEntry {
    private String lexeme;
    private Type type;



    private int displacement = -1;
    private int numParameters = 0;
    private ArrayList<Type> types;
    private Type returnType;
    private String tag;
    private static int tagCounter = 0;
    private SymbolTable table;
    private int id;

    public SymbolEntry(String lexeme){


        this.lexeme = lexeme;
    }

    public void setupAsFunction(String lexeme){

        this.type = Type.FUNCTION;
        tag = lexeme + "_"+tagCounter;
        tagCounter++;


    }
    public void setTable(SymbolTable table){

        this.table = table;
    }
    public int setType(Type type){

        this.type = type;
        displacement =  table.getDisplacementCount();
        table.setDisplacementCount(displacement+type.getSize());
        System.out.println(this);

        return table.getDisplacementCount();
    }
    public void setParameters(ArrayList<Type>types){
        numParameters = types.size();
        this.types = types;
        System.out.println(this);

    }
    public void setReturnType(Type type){
        returnType = type;
    }


    public int getNumParameters() {
        return numParameters;
    }


    public ArrayList<Type> getTypesParamters() {
        return types;
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


    public void setId(int id) {

        this.id = id;
    }
    public int getDisplacement() {
        return displacement;
    }

    public void setDisplacement(int displacement) {
        this.displacement = displacement;
    }
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "SymbolEntry{" +
                "lexeme='" + lexeme + '\'' +
                ", type=" + type +
                ", displacement=" + displacement +
                ", numParameters=" + numParameters +
                ", typesParamters=" + types +
                ", returnType=" + returnType +
                ", tag='" + tag + '\'' +

                ", id=" + id +
                '}';
    }
}
