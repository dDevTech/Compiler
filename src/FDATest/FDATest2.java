package FDATest;

import Analyzer.Lexical.LexicalAnalyzer;
import Analyzer.Sintactic.SintacticAnalyzer;

public class FDATest2 {
    public static void main(String[]args){
        SintacticAnalyzer analyzer  = new SintacticAnalyzer();
        analyzer.setup();
        analyzer.setDebug(true);
        analyzer.run();

    }
}
