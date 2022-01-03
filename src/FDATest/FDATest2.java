package FDATest;

import Javascript.JavascriptLexical;
import Javascript.JavascriptSyntaxSemantic;
import Tools.Console;

public class FDATest2 {
    public static void main(String[]args){

        JavascriptLexical lexical = new JavascriptLexical();
        JavascriptSyntaxSemantic syntax = new JavascriptSyntaxSemantic(lexical);
        lexical.setup();
        syntax.setup();

        syntax.start();
        Console.saveLogError();

        //OtherSintax sintax = new OtherSintax();


    }
}
