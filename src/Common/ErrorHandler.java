package Common;

import FDA.ProcessorError;
import Tools.Console;
import Tools.FileIterator;

public class ErrorHandler {


    public static void showLexicError(FileIterator iterator, ProcessorError exception){
        Console.printError("LEXICAL ERROR",Console.ANSI_WHITE+exception.getMessage()+ Console.ANSI_PURPLE+" in line "+ iterator.getLine() + " column "+iterator.getColumn()+Console.ANSI_BLUE +" ["+Console.avoidScapes(Character.toString((Character) iterator.getCurrentElement()))+"]"+Console.ANSI_WHITE+" with lexical code " +exception.getErrorCode()+"\n");

    }
    public static void showSintaxError(int line, ProcessorError exception){
        Console.printError("SYNTAX ERROR",Console.ANSI_WHITE+exception.getMessage()+ Console.ANSI_PURPLE+" in line "+ line +Console.ANSI_BLUE +Console.ANSI_WHITE+" with syntax code " +exception.getErrorCode()+"\n");
    }
    public static void showSemanticError(int line, ProcessorError exception){
        Console.printError("SEMANTIC ERROR",Console.ANSI_WHITE+exception.getMessage()+ Console.ANSI_PURPLE+" in line "+ line +Console.ANSI_BLUE +Console.ANSI_WHITE+" with semantic code " +exception.getErrorCode()+"\n");
    }
    public static void showSymbolTableError(String message){
        Console.printError("SYMBOL TABLE ERROR",Console.ANSI_WHITE+message+"\n");
    }
    public static void showError(String message){
        Console.printError("SEMANTIC ACTION ERROR",Console.ANSI_WHITE+message+"\n");
    }
    public static void showCompilerError(FileIterator iterator, Exception exception){
        Console.printError("COMPILER ERROR",Console.YELLOW_BOLD+"in semantic action "+Console.ANSI_WHITE+exception.getMessage()+"["+exception.getClass()+"]"+ Console.ANSI_PURPLE+" in line "+ iterator.getLine() +Console.ANSI_BLUE +" ["+Console.avoidScapes(Character.toString((Character) iterator.getCurrentElement()))+"]"+Console.ANSI_WHITE+"\n");
        Console.printError("EXIT","ABORTING AND PRINTING ERROR LINE\n");
        System.err.println("Please check semantic action: "+exception.getStackTrace()[0]);
        System.exit(0);
    }

    public static void showDeepSintaxError(int line, ProcessorError processorError) {
        Console.printError("DEEP EXPLANATION OF SYNTAX ERROR",Console.ANSI_WHITE+processorError.getMessage()+ Console.ANSI_PURPLE+" in line "+ line +Console.ANSI_BLUE +Console.ANSI_WHITE+" with syntax code " +processorError.getErrorCode()+"\n");
    }
}
