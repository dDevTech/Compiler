package FDATest;

import FDA.FDA;
import FDA.*;

import java.util.Iterator;
import java.util.Scanner;

public class FDATest {
    static int cont =-1;
    static String analyze;
    public static void main(String[]args){

        Scanner sc = new Scanner(System.in);
        analyze = sc.nextLine();
        analyze = analyze+"\\";

        Iterator<Character> a =analyze.chars().mapToObj(i -> (char)i).iterator();

        FDA<Character>fda = new FDA<Character>() {

            @Override
            public void onReadSequence(FinalState<Character> finalState) {

            }
        };
        fda.setDebug(true);
        //creamos nodos
        State<Character> initial = new State<Character>("root");

        State<Character> nodeAsig1 = new State<Character>(":");
        FinalState<Character> nodeAsig2 = new FinalState<Character>("=");
        initial.addTransition(':',nodeAsig1,false);
        nodeAsig1.addTransition('=',nodeAsig2,false);

        FinalState<Character> sum = new FinalState<Character>("+");
        initial.addTransition('+',sum,false);

        State<Character> letterDigit = new State<Character>("letter");
        initial.addTransitionFunction(FDATest::isLetter,letterDigit,false);


        letterDigit.addTransitionFunction(FDATest::isLetterDigit,letterDigit,false);

        FinalState<Character> variable = new FinalState<Character>("variable");
        letterDigit.addOtherElementTransitionFunction(variable,true);

        FinalState<Character> eof = new FinalState<Character>("eof");
        initial.addTransition('\\',eof,false);

        initial.addTransitionFunction(FDATest::isDelimiter,initial,false);

        fda.setRoot(initial);
        fda.execute(a);

    }

    public static boolean isLetter(Character character){
        return Character.isLetter(character);
    }
    public static boolean isDigit(Character character){
        return Character.isDigit(character);
    }
    public static boolean isLetterDigit(Character character){
        return isLetter(character)||isDigit(character);
    }
    public static boolean isDelimiter(Character character){
        return Character.isWhitespace(character);
    }
}
