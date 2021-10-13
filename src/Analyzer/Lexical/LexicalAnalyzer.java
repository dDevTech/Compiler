package Analyzer.Lexical;

import FDA.*;
import Tools.CharacterIterator;

import java.util.List;

public class LexicalAnalyzer {
    public void setup(){
        CharacterIterator it = new CharacterIterator("files/javascript.txt"); //iterador que lee el archivo
        FDA<Character> fda = new FDA<Character>() {
            @Override
            public void onReadSequence(FinalState<Character> finalState, List<Character> readSequence) {

            }
        };

        State<Character>root = new State<Character>("root");
        root.addTransitionFunction(TransitionFunction::isDelimiter,root,false,false);

        //operadores aritmeticos (+, -), de asignacion (+=, -=) y de incremento/decremento
        State<Character>dif = new State<Character>("difChar");
        root.addTransition('-',dif,false,true);

        FinalState<Character>difeq = new FinalState<Character>("-=");
        dif.addTransition('=',difeq,false,true);
        FinalState<Character>difdif = new FinalState<Character>("--");
        dif.addTransition('-',difdif,false,true);
        FinalState<Character>difOnly = new FinalState<Character>("-");
        dif.addOtherElementTransitionFunction(difOnly,true,false);

        State<Character>sum = new State<Character>("sumChar");
        root.addTransition('+',sum,false,true);
        FinalState<Character>sumeq = new FinalState<Character>("+=");
        sum.addTransition('=',sumeq,false,true);
        FinalState<Character>sumsum = new FinalState<Character>("++");
        sum.addTransition('+',sumsum,false,true);
        FinalState<Character>sumOnly = new FinalState<Character>("+");
        sum.addOtherElementTransitionFunction(sumOnly,true,false);


        //OPERADORES LOGICOS
        State<Character>and = new State<Character>("and");
        root.addTransition('&',and,false,true);
        FinalState<Character>andand = new FinalState<Character>("&&");
        and.addTransition('&',andand,false,true);

        State<Character>or = new State<Character>("or");
        root.addTransition('|',or,false,true);
        FinalState<Character>oror = new FinalState<Character>("||");
        or.addTransition('|',oror,false,true);

        FinalState<Character>not = new FinalState<Character>("not");
        root.addTransition('!',not,false,true);


        //OPERADORES RELACIONALES
        State<Character>equals = new State<Character>("equals");
        root.addTransition('=',equals,false,true);
        FinalState<Character>equalsequals = new FinalState<Character>("==");
        FinalState<Character>assignment = new FinalState<Character>("=");
        equals.addOtherElementTransitionFunction(assignment,true,false);
        equals.addTransition('=',equalsequals,false,true);

        State<Character>different = new State<Character>("notequals");
        root.addTransition('!',different,false,true);
        FinalState<Character>differentdifferent = new FinalState<Character>("!=");
        different.addTransition('=',differentdifferent,false,true);


        //FIN ARCHIVO
        FinalState<Character>eof = new FinalState<Character>("eof"); //fin de archivo
        root.addTransition('\\',eof,false,true);


        //COMENTARIOS DE BLOQUE
        State<Character>comentario1 = new State<Character>("/");
        State<Character>comentario2 = new State<Character>("comment");
        State<Character>comentario3 = new State<Character>("/**/");
        root.addTransition('/',comentario1,false,true);
        comentario1.addTransition('*',comentario2,false,true);
        comentario2.addTransition('*',comentario3,false,true);
        comentario3.addOtherElementTransitionFunction(comentario2,false,true);
        comentario2.addOtherElementTransitionFunction(comentario2,false,true);
        comentario3.addTransition('/',root,false,true);


        //VARIABLES
        State<Character>letterDigit = new State<Character>("letterDigit");
        FinalState<Character>variable = new FinalState<Character>("variable/reserved");
        root.addTransitionFunction(TransitionFunction::isLetter,letterDigit,false,true);
        letterDigit.addTransitionFunction(TransitionFunction::isLetterDigit,letterDigit,false,true);
        letterDigit.addOtherElementTransitionFunction(variable,true,false);

        //STRINGS
        State<Character>stringStart = new State<Character>("str");
        State<Character>escape = new State<Character>("escape");
        FinalState<Character>stringEnd = new FinalState<Character>("string");
        root.addTransition('\"',stringStart,false,true);
        stringStart.addTransition('\"',stringEnd,false,true);
        stringStart.addTransition('\\',escape,false,true);
        escape.addTransitionFunction(TransitionFunction::isEscape,stringStart,false,true);
        stringStart.addOtherElementTransitionFunction(stringStart,false,true);

        //INTEGERS
        State<Character>integerStart = new State<Character>("int");
        FinalState<Character>integerEnd = new FinalState<Character>("integer");
        root.addTransitionFunction(TransitionFunction::isDigit,integerStart,false,true);
        integerStart.addTransitionFunction(TransitionFunction::isDigit,integerStart,false,true);
        integerStart.addOtherElementTransitionFunction(integerEnd,true,false);

        //PARENTESIS Y LLAVES FIN LINEA
        FinalState<Character>par1 = new FinalState<Character>("(");
        root.addTransition('(',par1,false,true);
        FinalState<Character>par2 = new FinalState<Character>(")");
        root.addTransition(')',par2,false,true);
        FinalState<Character>llav1 = new FinalState<Character>("{");
        root.addTransition('{',llav1,false,true);
        FinalState<Character>llav2 = new FinalState<Character>("}");
        root.addTransition('}',llav2,false,true);
        FinalState<Character>ptcoma = new FinalState<Character>(";");
        root.addTransition(';',ptcoma,false,true);


        fda.setRoot(root);
        fda.setDebug(true);
        fda.setIterator(it);
        fda.setContinueOnError(false);
        fda.execute(it);
    }
}
