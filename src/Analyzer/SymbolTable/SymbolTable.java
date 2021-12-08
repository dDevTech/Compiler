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

    public int add(String lexeme){
        if(!entries.containsKey(lexeme)){
            int id = ID_COUNTER;
            SymbolEntry entry = new SymbolEntry(lexeme);

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
    public void toFile(){
        FileWrite write = new FileWrite("files/tablaSimbolos.txt");
        write.writer().println("TABLA PRINCIPAL # 1:");

        for(Map.Entry<Integer,SymbolEntry>entry:entries.entrySet()){
            write.writer().println("* LEXEMA : '"+entry.getValue().getLexeme()+"'");
        }
        write.writer().flush();
        write.writer().close();
    }

    public int getDisplacementCount() {
        return displacementCount;
    }

    public void setDisplacementCount(int displacementCount) {
        this.displacementCount = displacementCount;
    }
}
