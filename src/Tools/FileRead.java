package Tools;

import java.io.*;

public class FileRead {
    private  BufferedReader  reader;
    public FileRead(String path){
        setup(path);

    }
    public FileRead(){}
    public boolean setup(String path){
        File f = new File(path);

        try {
            reader= new BufferedReader(new FileReader(f));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }{}
    public String readNextLine(){
        if(reader!=null){
            try {
                return reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
