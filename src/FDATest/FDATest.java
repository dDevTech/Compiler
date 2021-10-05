package FDATest;

import FDA.FDA;
import FDA.*;

import java.io.BufferedReader;
import java.util.Iterator;
import java.util.List;
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
            public void onReadSequence(FinalState<Character> finalState, List<Character> readSequence) {

            }
        };
        fda.setDebug(false);
        //creamos nodos
        State<Character> initial = new State<Character>("root");

        State<Character> nodeAsig1 = new State<Character>(":");
        FinalState<Character> nodeAsig2 = new FinalState<Character>("=");
        initial.addTransition(':',nodeAsig1,false,true);
        nodeAsig1.addTransition('=',nodeAsig2,false,true);

        FinalState<Character> sum = new FinalState<Character>("+");
        initial.addTransition('+',sum,false,true);

        State<Character> letterDigit = new State<Character>("letter");
        initial.addTransitionFunction(TransitionFunction::isLetter,letterDigit,false,true);


        letterDigit.addTransitionFunction(TransitionFunction::isLetterDigit,letterDigit,false,true);

        FinalState<Character> variable = new FinalState<Character>("variable");
        letterDigit.addOtherElementTransitionFunction(variable,true,true);

        FinalState<Character> eof = new FinalState<Character>("eof");
        initial.addTransition('\\',eof,false,false);

        initial.addTransitionFunction(TransitionFunction::isDelimiter,initial,false,true);

        fda.setRoot(initial);
        fda.execute(a);

    }


}
