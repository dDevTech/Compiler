package Common;

import Tools.Console;

public class ErrorHandler {
    public static void showLexicError(boolean debug,String message, String value, int column, int line,int errorCode){
        Console.printError("LEXICAL ERROR",Console.ANSI_WHITE+message+ Console.ANSI_PURPLE+" in line "+ line + " column "+column+Console.ANSI_BLUE +" ["+value+"]"+Console.ANSI_WHITE+" with lexical code " +errorCode+"\n");
    }
    public static void printCharacter(boolean debug,String prev){
        if(debug){
            prev = prev.replace("\n","\\n").replace("\r","\\r").replace("\t","\\t");
            Console.print(Console.ANSI_CYAN+"("+prev+") ");
        }
    }
}
