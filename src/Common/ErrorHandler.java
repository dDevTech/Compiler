package Common;

import FDA.FDAException;
import Tools.Console;
import Tools.FileIterator;

import java.util.HashMap;

public class ErrorHandler {
    public static void showLexicError(FileIterator iterator, FDAException exception){
        Console.printError("LEXICAL ERROR",Console.ANSI_WHITE+exception.getMessage()+ Console.ANSI_PURPLE+" in line "+ iterator.getLine() + " column "+iterator.getColumn()+Console.ANSI_BLUE +" ["+Console.avoidScapes(Character.toString((Character) iterator.getCurrentElement()))+"]"+Console.ANSI_WHITE+" with lexical code " +exception.getErrorCode()+"\n");
    }

}
