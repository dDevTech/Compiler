package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileWrite {
    private PrintWriter writer;
    public FileWrite(String path){
        setup(path);
    }
    public FileWrite(){}
    public boolean setup(String path){
        try {
            writer = new PrintWriter(new FileWriter(path));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public PrintWriter writer(){
        return writer;
    }
}
