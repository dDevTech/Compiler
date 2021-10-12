package FDATest;

import FDA.FDA;
import FDA.*;
import Tools.CharacterIterator;

import java.util.List;

public class FDATest {
    static int cont =-1;
    static String analyze;
    static int sizeCounter = 0;
    public static void main(String[]args){
        CharacterIterator it = new CharacterIterator("files/javascript.txt");

       // Scanner sc = new Scanner(System.in);
        //analyze = sc.nextLine();
        //analyze = analyze+"\\";

      //  Iterator<Character> a =analyze.chars().mapToObj(i -> (char)i).iterator();


        FDA<Character>fda = new FDA<Character>() {

            @Override
            public void onReadSequence(FinalState<Character> finalState, List<Character> readSequence) {
                sizeCounter = 0;
            }
        };
        fda.setDebug(true);
        //creamos nodos
        State<Character> initial = new State<Character>("root");

        State<Character> nodeAsig1 = new State<Character>(":");
        nodeAsig1.setNoTransitionError("Assignments must be :=");
        FinalState<Character> nodeAsig2 = new FinalState<Character>("=");
        initial.addTransition(':',nodeAsig1,false,true);
        nodeAsig1.addTransition('=',nodeAsig2,false,true);

        FinalState<Character> sum = new FinalState<Character>("+");
        initial.addTransition('+',sum,false,true);

        State<Character> letterDigit = new State<Character>("letter");
        Transition<Character> transition = initial.addTransitionFunction(TransitionFunction::isLetter,letterDigit,false,true);
        transition.addSemanticAction(new SemanticAction<Character>() {
            @Override
            public void onAction(State<Character> state, Character element, List<Character> sequence){


            }
        });

        Transition<Character> transition2=letterDigit.addTransitionFunction(TransitionFunction::isLetterDigit,letterDigit,false,true);
        transition2.addSemanticAction(new SemanticAction<Character>() {
            @Override
            public void onAction(State<Character> state, Character element, List<Character> sequence) throws FDAException {
                //System.out.print(sequence);
                if(sequence.size()>5){
                    throw new FDAException(-3,"Variables must be max size of 5");
                }

            }
        });
        FinalState<Character> variable = new FinalState<Character>("variable");
        letterDigit.addOtherElementTransitionFunction(variable,true,true);

        FinalState<Character> eof = new FinalState<Character>("eof");
        initial.addTransition('\\',eof,false,false);

        initial.addTransitionFunction(TransitionFunction::isDelimiter,initial,false,false);
        //Console.printLegend();
        fda.setRoot(initial);
        fda.setIterator(it);
        fda.execute(fda.getIterator());



    }


}
