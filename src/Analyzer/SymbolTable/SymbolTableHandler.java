package Analyzer.SymbolTable;

import Common.ErrorHandler;
import FDA.FDAException;
import Tools.FileWrite;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SymbolTableHandler {
    public Stack<SymbolTable> getCurrentTables() {
        return currentTables;
    }

    private Stack<SymbolTable> currentTables;
    private List<SymbolTable> allTables;


    public boolean isZoneDeclaration() {
        return zoneDeclaration;
    }

    public void setZoneDeclaration(boolean zoneDeclaration) {

        this.zoneDeclaration = zoneDeclaration;
    }

    private boolean zoneDeclaration = false;
    public SymbolTableHandler(){
        SymbolTable table = new SymbolTable();
        currentTables = new Stack<>();
        currentTables.add(table);
        allTables = new ArrayList<>();
        allTables.add(table);//main
    }
    public int add(String lexeme){

        return currentTables.peek().add(lexeme,currentTables.peek());
    }
    public void createTable(){
        if(currentTables.size()>=2){
            ErrorHandler.showSymbolTableError("Function anidation is not supported in this language");
        }
        SymbolTable table = new SymbolTable();

        currentTables.push(table);
        allTables.add(table);
    }

    public SymbolTable removeLast(){
        return currentTables.pop();
    }
    public int find(String lexeme){
        for(int i = currentTables.size()-1; i>=0 ; i--){
            SymbolEntry entry = currentTables.get(i).get(lexeme);
            if(entry!=null){
                return entry.getId();
            }
        }
        return -1;

    }
    public SymbolEntry find(int id){
        for(int i = currentTables.size()-1; i>=0 ; i--){
            SymbolEntry entry = currentTables.get(i).get(id);
            if(entry!=null){
                return entry;
            }
        }

        return null;

    }
    public SymbolEntry findCurrentTable(int id){

        SymbolEntry entry = currentTables.get(currentTables.size()-1).get(id);
        if(entry!=null){
            return entry;
        }
        return null;

    }

    @Override
    public String toString() {
        return "SymbolTableHandler{" +
                "currentTables=" + currentTables +
                ", zoneDeclaration=" + zoneDeclaration +
                '}';
    }

    public void toFile(){
        FileWrite write = new FileWrite("files/tablaSimbolos.txt");
        for(SymbolTable table: allTables){

            table.toFile(write);
        }
        write.writer().flush();
        write.writer().close();
    }
}
