package Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class FileIterator implements Iterator<Character> {
    private Scanner sc;
    private int column = 0;
    private int line = 0;

    public FileIterator(String path){
        try {
            sc = new Scanner(new File(path));
            sc.useDelimiter("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    public FileIterator(){
        sc = new Scanner(System.in);
    }
    boolean end = false;
    @Override
    public boolean hasNext() {
        return sc.hasNext()||!end;
    }

    @Override
    public Character next() {
        if(!sc.hasNext()&&!end){
            end = true;

            return '\\';//EOF

        }else if(sc.hasNext()){
            Character character = sc.next().charAt(0);
            if(character=='\r'){
                line++;
                column=0;
            }else if(character!='\n'){
                column+=1;
            }

            return character;
        }else{
            throw new NoSuchElementException();
        }

    }

    public int getColumn() {
        return column;
    }

    public int getLine() {
        return line;
    }
}
