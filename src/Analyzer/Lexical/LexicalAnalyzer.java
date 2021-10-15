package Analyzer.Lexical;

import FDA.*;
import Tools.CharacterIterator;
import Tools.FileRead;
import Tools.Tools;
import Tools.FileWrite;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

public class LexicalAnalyzer {
    HashMap<String,Integer>reservedWords = new HashMap<>();
    PrintWriter tokenWriter;

    public void setup(){
        SymbolTable table = new SymbolTable();
        FileWrite write = new FileWrite("files/output.txt");
        tokenWriter = write.writer();
        readReserved();
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
        Transition<Character> transition2 = dif.addTransition('=',difeq,false,true);
        transition2.addSemanticAction(new SemanticAction<Character>() {
            @Override
            public void onAction(State<Character> state, Character element, List<Character> sequence) throws FDAException {
                generateToken(14,null);
            }
        });
       // FinalState<Character>difdif = new FinalState<Character>("--");
        //dif.addTransition('-',difdif,false,true);
        FinalState<Character>difOnly = new FinalState<Character>("-");
        Transition<Character> transition3= dif.addOtherElementTransitionFunction(difOnly,true,false);
        transition3.addSemanticAction(new SemanticAction<Character>() {
            @Override
            public void onAction(State<Character> state, Character element, List<Character> sequence) throws FDAException {
                generateToken(23,null);
            }
        });

        FinalState<Character>sum = new FinalState<Character>("+");
        Transition<Character> transition4=root.addTransition('+',sum,false,true);
        transition4.addSemanticAction(new SemanticAction<Character>() {
            @Override
            public void onAction(State<Character> state, Character element, List<Character> sequence) throws FDAException {
                generateToken(22,null);
            }
        });
       // FinalState<Character>sumeq = new FinalState<Character>("+=");
       // sum.addTransition('=',sumeq,false,true);
       // FinalState<Character>sumsum = new FinalState<Character>("++");
        //sum.addTransition('+',sumsum,false,true);
       // FinalState<Character>sumOnly = new FinalState<Character>("+");
        //sum.addOtherElementTransitionFunction(sumOnly,true,false);


        //OPERADORES LOGICOS
        State<Character>and = new State<Character>("and");
        root.addTransition('&',and,false,true);
        FinalState<Character>andand = new FinalState<Character>("&&");
        Transition<Character>transition5=and.addTransition('&',andand,false,true);
        transition5.addSemanticAction(new SemanticAction<Character>() {
            @Override
            public void onAction(State<Character> state, Character element, List<Character> sequence) throws FDAException {
                generateToken(24,null);
            }
        });

        State<Character>or = new State<Character>("or");
        root.addTransition('|',or,false,true);
        FinalState<Character>oror = new FinalState<Character>("||");
        Transition<Character>transition6=or.addTransition('|',oror,false,true);
        transition6.addSemanticAction(new SemanticAction<Character>() {
            @Override
            public void onAction(State<Character> state, Character element, List<Character> sequence) throws FDAException {
                generateToken(25,null);
            }
        });

      //  FinalState<Character>not = new FinalState<Character>("not");
       // root.addTransition('!',not,false,true);


        //OPERADORES RELACIONALES
        State<Character>equals = new State<Character>("equals");
        root.addTransition('=',equals,false,true);
        FinalState<Character>equalsequals = new FinalState<Character>("==");

        FinalState<Character>assignment = new FinalState<Character>("=");
        Transition<Character>transition8=equals.addOtherElementTransitionFunction(assignment,true,false);
        transition8.addSemanticAction(new SemanticAction<Character>() {
            @Override
            public void onAction(State<Character> state, Character element, List<Character> sequence) throws FDAException {
                generateToken(15,null);
            }
        });
        Transition<Character>transition7=equals.addTransition('=',equalsequals,false,true);
        transition7.addSemanticAction(new SemanticAction<Character>() {
            @Override
            public void onAction(State<Character> state, Character element, List<Character> sequence) throws FDAException {
                generateToken(27,null);
            }
        });

        State<Character>different = new State<Character>("notequals");
        root.addTransition('!',different,false,true);
        FinalState<Character>differentdifferent = new FinalState<Character>("!=");
        Transition<Character>transition9 = different.addTransition('=',differentdifferent,false,true);
        transition9.addSemanticAction(new SemanticAction<Character>() {
            @Override
            public void onAction(State<Character> state, Character element, List<Character> sequence) throws FDAException {
                generateToken(26,null);
            }
        });

        //FIN ARCHIVO
        FinalState<Character>eof = new FinalState<Character>("eof"); //fin de archivo

        Transition<Character>transition10=root.addTransition('\\',eof,false,true);
        transition10.addSemanticAction(new SemanticAction<Character>() {
            @Override
            public void onAction(State<Character> state, Character element, List<Character> sequence) throws FDAException {
                generateToken(30,null);
            }
        });

        //COMENTARIOS DE BLOQUE
        State<Character>comentario1 = new State<Character>("/");
        State<Character>comentario2 = new State<Character>("comment");
        State<Character>comentario3 = new State<Character>("/**/");
        root.addTransition('/',comentario1,false,false);
        comentario1.addTransition('*',comentario2,false,false);
        comentario2.addTransition('*',comentario3,false,false);
        comentario3.addTransition('*',comentario3,false,false);
        comentario3.addOtherElementTransitionFunction(comentario2,false,false);
        comentario2.addOtherElementTransitionFunction(comentario2,false,false);
        comentario3.addTransition('/',root,false,false);


        //VARIABLES - PALABRAS RESERVADAS
        State<Character>letterDigit = new State<Character>("letterDigit");
        FinalState<Character>variable = new FinalState<Character>("variable/reserved");
        root.addTransitionFunction(TransitionFunction::isLetter,letterDigit,false,true);
        letterDigit.addTransitionFunction(TransitionFunction::isLetterDigit,letterDigit,false,true);
        Transition<Character> transition =letterDigit.addOtherElementTransitionFunction(variable,true,false);
        transition.addSemanticAction(new SemanticAction<Character>() {
            @Override
            public void onAction(State<Character> state, Character element, List<Character> sequence) throws FDAException {
                String s = Tools.characterListToString(sequence);

                if(reservedWords.containsKey(s)){
                    generateToken(reservedWords.get(s),null);
                }else{
                    int id;
                    if((id=table.add(s))!=-1){
                        generateToken(13,id);
                    }else{
                        generateToken(13,table.get(s));
                        System.out.println(s+"Already created");
                    }

                }

            }
        });


        //STRINGS
        State<Character>stringStart = new State<Character>("str");
        State<Character>escape = new State<Character>("escape");
        FinalState<Character>stringEnd = new FinalState<Character>("string");
        root.addTransition('\"',stringStart,false,true);
        Transition<Character>transitionString = stringStart.addTransition('\"',stringEnd,false,true);
        stringStart.addTransition('\\',escape,false,true);
        escape.addTransitionFunction(TransitionFunction::isEscape,stringStart,false,true);
        stringStart.addOtherElementTransitionFunction(stringStart,false,true);
        transitionString.addSemanticAction(new SemanticAction<Character>() {
            @Override
            public void onAction(State<Character> state, Character element, List<Character> sequence) throws FDAException {
                String s = Tools.characterListToString(sequence);
                generateToken(12,s);
            }
        });


        //INTEGERS
        State<Character>integerStart = new State<Character>("int");
        FinalState<Character>integerEnd = new FinalState<Character>("integer");
        root.addTransitionFunction(TransitionFunction::isDigit,integerStart,false,true);
        integerStart.addTransitionFunction(TransitionFunction::isDigit,integerStart,false,true);
        Transition<Character>integersTransition=integerStart.addOtherElementTransitionFunction(integerEnd,true,false);
        integersTransition.addSemanticAction(new SemanticAction<Character>() {
            @Override
            public void onAction(State<Character> state, Character element, List<Character> sequence) throws FDAException {
                int a = Integer.parseInt(Tools.characterListToString(sequence));
                generateToken(11,a);
            }
        });
        //PARENTESIS Y LLAVES FIN LINEA
        FinalState<Character>par1 = new FinalState<Character>("(");
        Transition<Character>transition11=root.addTransition('(',par1,false,true);
        transition11.addSemanticAction(new SemanticAction<Character>() {
            @Override
            public void onAction(State<Character> state, Character element, List<Character> sequence) throws FDAException {
                generateToken(18,null);
            }
        });

        FinalState<Character>par2 = new FinalState<Character>(")");
        Transition<Character>transition12 = root.addTransition(')',par2,false,true);
        transition12.addSemanticAction(new SemanticAction<Character>() {
            @Override
            public void onAction(State<Character> state, Character element, List<Character> sequence) throws FDAException {
                generateToken(19,null);
            }
        });
        FinalState<Character>llav1 = new FinalState<Character>("{");
        Transition<Character>transition13 = root.addTransition('{',llav1,false,true);
        transition13.addSemanticAction(new SemanticAction<Character>() {
            @Override
            public void onAction(State<Character> state, Character element, List<Character> sequence) throws FDAException {
                generateToken(20,null);
            }
        });
        FinalState<Character>llav2 = new FinalState<Character>("}");
        Transition<Character>transition14 = root.addTransition('}',llav2,false,true);
        transition14.addSemanticAction(new SemanticAction<Character>() {
            @Override
            public void onAction(State<Character> state, Character element, List<Character> sequence) throws FDAException {
                generateToken(21,null);
            }
        });

        FinalState<Character>ptcoma = new FinalState<Character>(";");
        Transition<Character>transition15 = root.addTransition(';',ptcoma,false,true);
        transition15.addSemanticAction(new SemanticAction<Character>() {
            @Override
            public void onAction(State<Character> state, Character element, List<Character> sequence) throws FDAException {
                generateToken(17,null);
            }
        });

        FinalState<Character>coma = new FinalState<Character>(",");
        Transition<Character>transition16 = root.addTransition(',',coma,false,true);
        transition16.addSemanticAction(new SemanticAction<Character>() {
            @Override
            public void onAction(State<Character> state, Character element, List<Character> sequence) throws FDAException {
                generateToken(16,null);
            }
        });
        fda.setRoot(root);
        fda.setDebug(true);
        fda.setIterator(it);
        fda.setContinueOnError(false);
        fda.execute(it);
        tokenWriter.close();
    }
    public void readReserved(){
        FileRead read= new FileRead("files/reservedWords");
        String line = "";
        while((line=read.readNextLine())!=null){
            System.out.println(line);
            String[]keyValue = line.split("=");
            reservedWords.put(keyValue[0].trim(),Integer.parseInt(keyValue[1].trim()));
        }


    }
    public void generateToken(int id,Object value){

        if(value==null){
            tokenWriter.println("< "+id+" ,> ");
        }else{
            tokenWriter.println("< "+id+" , "+value+"> ");
        }
        tokenWriter.flush();
    }
}
