package Common;

import Tools.Console;
import Tools.FileIterator;

public class ErrorHandler {
    public static void showLexicError(boolean debug,String message, String value, int column, int line,int errorCode){
        if(debug){printCharacter(debug,value);Console.print(Console.ANSI_RED+"ERROR\n");}

        Console.printInfo("ERROR",Console.ANSI_RED+message+ Console.ANSI_PURPLE+" in line "+ line + " column "+column+Console.ANSI_BLUE +" ["+value+"]"+Console.ANSI_RED+" with lexical code " +errorCode+"\n");

    }
    public static void printCharacter(boolean debug,String prev){
        if(debug){
            prev = prev.replace("\n","\\n").replace("\r","\\r").replace("\t","\\t");
            Console.print(Console.ANSI_CYAN+"("+prev+") ");
        }

    }
}
