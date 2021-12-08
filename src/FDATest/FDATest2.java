package FDATest;

import Javascript.JavascriptLexical;
import Javascript.JavascriptSyntax;

public class FDATest2 {
    public static void main(String[]args){

        JavascriptLexical lexical = new JavascriptLexical();
        JavascriptSyntax syntax = new JavascriptSyntax(lexical);
        lexical.setup();
        syntax.setup();

        syntax.start();

        //OtherSintax sintax = new OtherSintax();


    }
}
