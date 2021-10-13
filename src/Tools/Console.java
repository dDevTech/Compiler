/*
 * Copyright (c) 2021. Developed by dDev Tech. Website: https://www.retopall.com/
 */

package Tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Console {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    // High Intensity
    public static final String BLACK_BRIGHT = "\033[0;90m";  // BLACK
    public static final String RED_BRIGHT = "\033[0;91m";    // RED
    public static final String GREEN_BRIGHT = "\033[0;92m";  // GREEN
    public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
    public static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
    public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
    public static final String CYAN_BRIGHT = "\033[0;96m";   // CYAN
    public static final String WHITE_BRIGHT = "\033[0;97m";  // WHITE


    public static final String BLACK_BOLD = "\033[1;30m";  // BLACK
    public static final String RED_BOLD = "\033[1;31m";    // RED
    public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
    public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
    public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
    public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
    public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
    public static final String WHITE_BOLD = "\033[1;37m";  // WHITE
    public static void print(String prefix,String content){
        System.out.print(getCurrentTimeStamp()+"["+prefix+"]  "+content);
    }
    public static void printInfo(String prefix,String content){
        System.out.print(Console.ANSI_YELLOW+"["+getCurrentTimeStamp()+"]  "+BLUE_BOLD+"["+prefix+"]  "+ANSI_RESET+content+ANSI_RESET);
    }
    public static void printError(String prefix,String content){
        System.out.print(Console.ANSI_YELLOW+"["+getCurrentTimeStamp()+"]  "+RED_BOLD+"["+prefix+"]  "+ANSI_RESET+content+ANSI_RESET);
    }
    public static void print(String content){
        System.out.print(ANSI_RESET+content+ANSI_RESET);
    }
    public static void printlnInfo(String prefix,String content){
        printInfo(prefix,content+"\n");
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss.SSS");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }
    public static void printLegend() {
        Console.print(Console.ANSI_BLUE+"(_)    :  "+Console.ANSI_WHITE +"previous transition element\n");
        Console.print(Console.ANSI_YELLOW+"name_state   :  "+Console.ANSI_WHITE +"current state (name)\n");
        Console.print(Console.ANSI_PURPLE+"==>   :  "+Console.ANSI_WHITE +"transited available to node\n");
        Console.print(Console.ANSI_GREEN+"[W]   : "+Console.ANSI_WHITE +"Previous transition will concatenate element (WRITE)\n");
        Console.print(Console.ANSI_YELLOW+"<<NODE>> : "+Console.ANSI_WHITE +"Final state reached\n");
        Console.print(Console.ANSI_BLUE+"[IGNORE READ] : "+Console.ANSI_WHITE +"transited available to node but ignored and next character will be previous character\n");
    }
    public static void printCharacter(boolean debug,String character){
        if(debug){
            character = character.replace("\n","\\n").replace("\r","\\r").replace("\t","\\t");
            Console.print(Console.ANSI_CYAN+"("+character+") ");
        }
    }
}
