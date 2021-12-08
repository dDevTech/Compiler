package Analyzer.SymbolTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SymbolTableHandler {
    private Stack<SymbolTable> currentTables;
    private List<SymbolTable> allTables;
    private int displacementCount = 0;
    public SymbolTableHandler(){
        SymbolTable table = new SymbolTable();
        currentTables = new Stack<>();
        currentTables.add(table);
        allTables = new ArrayList<>();
        allTables.add(table);//main
    }
    public int add(String lexeme){
        return currentTables.peek().add(lexeme);
    }
    public void createTable(){
        SymbolTable table = new SymbolTable();
        table.setDisplacementCount(currentTables.peek().getDisplacementCount());
        currentTables.push(table);
    }

    public SymbolTable removeLast(){
        return currentTables.pop();
    }
    public SymbolEntry find(String lexeme){
        for(int i = 0; i< currentTables.size(); i++){
            SymbolEntry entry = currentTables.get(i).get(lexeme);
            if(entry!=null){
                return entry;
            }
        }
        return null;

    }
    public SymbolEntry find(int id){
        for(int i = 0; i< currentTables.size(); i++){
            SymbolEntry entry = currentTables.get(i).get(id);
            if(entry!=null){
                return entry;
            }
        }
        return null;

    }
    public void toFile(){
        for(SymbolTable table: allTables){
            table.toFile();
        }
    }
}
