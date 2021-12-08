package Analyzer.Lexical;

import Analyzer.SymbolTable.SymbolTable;
import Analyzer.SymbolTable.SymbolTableHandler;
import FDA.*;
import Tools.CharacterIterator;
import Tools.FileRead;
import Tools.Tools;
import Tools.FileWrite;
import java.io.PrintWriter;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;

import Tools.Console;
public class LexicAnalyzer {
    private AbstractMap.SimpleEntry<Object,Object>token;
    HashMap<String,String>reservedWords = new HashMap<>();
    PrintWriter tokenWriter;
    SymbolTableHandler handler;
    private CharacterIterator it;
    private FDA<Character> fda;

    public FDA<Character> getFda() {
        return fda;
    }

    public void setFda(FDA<Character> fda) {

        this.fda = fda;

    }

    public CharacterIterator getIterator() {
        return it;
    }

    public void setIterator(CharacterIterator iterator) {
        this.it = iterator;
    }

    public HashMap<String, String> getReservedWords() {
        return reservedWords;
    }

    public PrintWriter getTokenWriter() {
        return tokenWriter;
    }

    public SymbolTableHandler getHandler() {
        return handler;
    }
    public LexicAnalyzer(){
        handler = new SymbolTableHandler();
        FileWrite write = new FileWrite("files/output.txt");
        tokenWriter = write.writer();
        readReserved("files/reservedWords");


    }
    public void setup(){

    }

    public void close(){
        tokenWriter.close();
        it.close();
        handler.toFile();
    }
    public AbstractMap.SimpleEntry readToken(){

        if(this.fda.executeNext()){
            return token;
        }else{
            return null;
        }
    }
    private void readReserved(String file){
        FileRead read= new FileRead(file);
        String line = "";
        while((line=read.readNextLine())!=null){

            String[]keyValue = line.split("=");
            reservedWords.put(keyValue[0].trim(),keyValue[1].trim());
        }
        read.close();
        Console.printlnInfo("RESERVED WORDS",Console.PURPLE_BOLD+reservedWords);
    }

    protected void generateToken(Object id, Object value){
        if(value==null){
            tokenWriter.println("< "+id+" ,> ");
            token = new AbstractMap.SimpleEntry<>(id,null);
        }else{
            if(value instanceof  String){
                tokenWriter.println("< "+id+" , \""+value+"\"> ");
                token = new AbstractMap.SimpleEntry<>(id,value);
            }else{
                tokenWriter.println("< "+id+" , "+value+"> ");
                token = new AbstractMap.SimpleEntry<>(id,value);
            }

        }
        tokenWriter.flush();
    }
}
