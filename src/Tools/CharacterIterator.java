package Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CharacterIterator extends FileIterator<Character> {
    private Scanner sc;

    public CharacterIterator(String path){
        try {
            sc = new Scanner(new File(path));
            sc.useDelimiter("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    public CharacterIterator(){
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
            current = '\\';
            return '\\';//EOF

        }else if(sc.hasNext()){
            Character character = sc.next().charAt(0);
            if(character=='\r'){
                line++;
                column=0;
            }else if(character!='\n'){
                column+=1;
            }
            current = character;
            return character;
        }else{
            throw new NoSuchElementException();
        }

    }
    public void skipLine() {

        sc.nextLine();
        column=0;
        line++;


    }

    @Override
    public void close() {
        sc.close();
    }


}
