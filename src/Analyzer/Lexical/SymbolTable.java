package Analyzer.Lexical;

import Tools.FileWrite;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private HashMap<String,Integer>ids = new HashMap<>();
    private static int ID_COUNTER = 0;
    public int add(String variable){
        if(!ids.containsKey(variable)){
            int id = ID_COUNTER;
            ids.put(variable,id);
            ID_COUNTER++;
            return id;
        }
        return -1;

    }
    public int get(String s){
        return  ids.get(s);
    }
    public void toFile(){
        FileWrite write = new FileWrite("files/tablaSimbolos.txt");
        write.writer().println("TABLA PRINCIPAL # 1:");
        for(Map.Entry<String,Integer>entry:ids.entrySet()){
            write.writer().println("* LEXEMA : '"+entry.getKey()+"'");
        }
        write.writer().flush();
        write.writer().close();
    }

}
