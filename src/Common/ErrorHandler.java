package Common;

import Tools.Console;
import Tools.FileIterator;

public class ErrorHandler {
    public static void showLexicError(String message, String value, int column, int line,int errorCode){
        System.out.println();
        Console.printInfo("ERROR",Console.ANSI_RED+message+ Console.ANSI_PURPLE+" in line "+ line + " column "+column+Console.ANSI_BLUE +" ["+value+"]"+Console.ANSI_RED+" with lexical code " +errorCode+"\n");

    }
}
