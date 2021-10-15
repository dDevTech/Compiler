package Analyzer.Lexical;

import java.util.HashMap;

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
}
