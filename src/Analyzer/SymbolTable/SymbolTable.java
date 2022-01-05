package Analyzer.SymbolTable;

import Tools.FileWrite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {
    private int displacementCount = 0;
    private Map<Integer,SymbolEntry> entries = new HashMap<>();
    private static int ID_COUNTER = 0;
    private static int TABLE_COUNTER = 0 ;
    private int idTable = 0;
    public SymbolTable(){
        idTable=TABLE_COUNTER;
        TABLE_COUNTER++;

    }
    public int add(String lexeme,SymbolTable table){

        if(!entries.containsKey(lexeme)){
            int id = ID_COUNTER;
            SymbolEntry entry = new SymbolEntry(lexeme);
            entry.setId(id);
            entry.setTable(table);
            entries.put(id,entry);
            ID_COUNTER++;
            return id;
        }
        return -1;

    }

    public SymbolEntry get(String s){
        for(Map.Entry<Integer,SymbolEntry>entry:entries.entrySet()) {
            if(entry.getValue().getLexeme().equals(s)){
                return entry.getValue();
            }
        }
        return null;
    }
    public SymbolEntry get(int id){
        return  entries.get(id);
    }
    public void toFile(FileWrite write){

        write.writer().println("TABLA # "+idTable+":");

        for(Map.Entry<Integer,SymbolEntry>entry:entries.entrySet()){
            write.writer().println("* LEXEMA : '"+entry.getValue().getLexeme()+"'");
            write.writer().println("ATRIBUTOS:");
            if(entry.getValue().getType()!=null){
                write.writer().println("+ tipo : '"+entry.getValue().getType()+"'");
            }

            if(entry.getValue().getDisplacement()!=-1){
                write.writer().println("+ despl : "+entry.getValue().getDisplacement());
            }
            if(entry.getValue().getTypesParamters()!=null){
                write.writer().println("+ numParam : "+entry.getValue().getTypesParamters().size());
            }
            int counter = 1;
            if(entry.getValue().getTypesParamters()!=null){
               for(Type type:entry.getValue().getTypesParamters()){
                   write.writer().println(" + TipoParam"+counter+" : '"+type+"'");
                   counter++;
               }
            }
            if(entry.getValue().getReturnType()!=null){
                write.writer().println("+ TipoRetorno : '"+entry.getValue().getReturnType()+"'");
            }
            if(entry.getValue().getTag()!=null){
                write.writer().println("+ EtiqFuncion : '"+entry.getValue().getTag()+"'");
            }

        }


    }

    @Override
    public String toString() {
        return "SymbolTable{" +
                "displacementCount=" + displacementCount +
                ", entries=" + entries +
                ", idTable=" + idTable +
                '}';
    }

    public int getDisplacementCount() {
        return displacementCount;
    }

    public void setDisplacementCount(int displacementCount) {
        this.displacementCount = displacementCount;
    }
}
