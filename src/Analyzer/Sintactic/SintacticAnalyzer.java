package Analyzer.Sintactic;

import Analyzer.Lexical.LexicalAnalyzer;
import Tools.Console;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SintacticAnalyzer {
    private boolean debug = true;
    LexicalAnalyzer ana;
    private ArrayList<Rule>rules = new ArrayList<>();
    private Rule initialRule;
    public void setup(){
        ana = new LexicalAnalyzer();
        ana.setup();

        Rule P = new Rule('P');
        Rule B= new Rule('B');
        Rule T = new Rule('T');
        Rule S = new Rule('S');
        Rule X = new Rule('X');
        Rule C = new Rule('C');
        Rule L = new Rule('L');
        Rule Q = new Rule('Q');
        Rule F = new Rule('F');
        Rule H = new Rule('H');
        Rule A = new Rule('A');
        Rule K = new Rule('K');
        Rule E = new Rule('E');
        Rule R = new Rule('R');
        Rule U = new Rule('U');
        Rule V = new Rule('V');

        rules.add(P);
        rules.add(B);
        rules.add(T);
        rules.add(S);
        rules.add(X);
        rules.add(C);
        rules.add(L);
        rules.add(Q);
        rules.add(F);
        rules.add(H);
        rules.add(A);
        rules.add(K);
        rules.add(E);
        rules.add(R);
        rules.add(U);
        rules.add(V);




        Production pP1 = new Production(B,P);
        Production pP2 = new Production(F,P);
        Production pP3 = new Production("eof");
        P.addProductions(pP1,pP2,pP3);

        Production pB1 = new Production("let",T,"id","puntoycoma");
        Production pB2 = new Production("if","abrePar",E,"cierraPar",S);
        Production pB3 = new Production(S);
        Production pB4 = new Production(C);
        B.addProductions(pB1,pB2,pB3,pB4);

        Production pT1 = new Production("int");
        Production pT2 = new Production("string");
        Production pT3 = new Production("boolean");
        T.addProductions(pT1,pT2,pT3);

        Production pS1 = new Production("id","asig",E,"puntoycoma");
        Production pS2 = new Production("print","abrePar",E,"cierraPar","puntoycoma");
        Production pS3 = new Production("input","abrePar",E,"id","puntoycoma");
        Production pS4 = new Production("id","abrePar",L,"cierraPar","puntoycoma");
        Production pS5 = new Production("return",X,"puntoycoma");
      //  Production pS6 = new Production("menosigual");
        S.addProductions(pS1, pS2,pS3,pS4,pS5);



        Production pX1 = new Production(E);
        X.setLambda(true);
        X.addProductions(pX1);

        Production pC1 = new Production(B, C);
        C.setLambda(true);
        C.addProductions(pC1);

        Production pL1 = new Production(E, Q);
        L.setLambda(true);
        L.addProductions(pL1);

        Production pQ1 = new Production("coma", E, Q);
        Q.setLambda(true);
        Q.addProductions(pQ1);

        Production pF1 = new Production("function", "id", H, "abrePar", A, "cierraPar", "abreLlave", C, "cierraLlave");
        F.addProductions(pF1);

        Production pH1 = new Production(T);
        H.setLambda(true);
        H.addProductions(pH1);

        Production pA1 = new Production(T, "id", K);
        A.setLambda(true);
        A.addProductions(pA1);

        Production pK1= new Production ("coma", T, "id", K);
        K.setLambda(true);
        K.addProductions(pK1);

        initialRule =S;
        print();
        HashSet<String>rules=new HashSet<>();
        first(P);
        System.out.println(rules);


    }
    public void print(){
        for(Rule rule:rules){
            Console.print(rule.getLetter()+Console.ANSI_PURPLE+" -> ");
            if(rule.isLambda()){
                Console.print(Console.YELLOW_BOLD+"λ");
                Console.print(Console.ANSI_CYAN+" | ");
            }
            for(Production prod:rule.getProductions()){

                for(Object o:prod.getElements()){
                    if(o instanceof Rule){
                        Console.print(Console.BLUE_BOLD+((Rule)o).getLetter()+" ");
                    }else{
                        Console.print(Console.GREEN_BOLD+o.toString()+" ");
                    }
                }

                Console.print(Console.ANSI_CYAN+" | ");
            }

            System.out.println();
        }
    }
    public void run(){
        System.out.println(ana.readToken());

    }
    public Set<String> first(Object o){
        Set<String>set = new HashSet<>();
        Set<Production>productions = new HashSet<>();

        if(debug)Console.print(Console.PURPLE_BOLD+"FIRST: "+o+"\n");
        firstRecursive(new IntRef(1),o,set,productions);
        System.out.println(set);
        return set;
    }
    private void firstRecursive(IntRef increment,Object object, Set<String> list,Set<Production> passedProductions){
        if(object instanceof String){
            if(debug)spaces(increment.getInteger());
            if(debug)Console.print(Console.BLUE_BOLD+object+"\n");
            list.add((String)object);
        }
        else if(object instanceof Rule){
            Rule rule = (Rule)object;
            if(rule.isLambda()){
                if(debug)spaces(increment.getInteger());
                if(debug)Console.print(Console.RED_BOLD+"λ"+"\n");
                list.add("-1");
            }
            for(Production Y:rule.getProductions()){
                if(debug)spaces(increment.getInteger());
                if(debug)Console.print(Console.YELLOW_BOLD +Y+ Console.ANSI_WHITE+"  USED: " + passedProductions + "\n");
                boolean containsLambda= false;
                if(!passedProductions.contains(Y)) {
                    passedProductions.add(Y);
                    for (Object o : Y.getElements()) {
                        Set<String> set = new HashSet<>();
                        if (debug) spaces(increment.getInteger());
                        if (debug) Console.print(Console.PURPLE_BOLD + "=FIRST(" + Console.BLUE_BOLD + o + Console.PURPLE_BOLD + ")\n");

                        increment.setInteger(increment.getInteger() + 1);
                        firstRecursive(increment, o, set, passedProductions);

                        containsLambda = set.contains("-1");
                        set.remove("-1");
                        list.addAll(set);

                        if (!containsLambda) {
                            break;
                        }

                    }
                    if (containsLambda) {
                        if (debug) spaces(increment.getInteger());
                        if (debug) Console.print(Console.YELLOW_BOLD + "λ (all lambdas)" + "\n");
                        list.add("-1");
                    }


                    passedProductions.remove(Y);
                }else{
                    spaces(increment.getInteger());
                    Console.print(Console.ANSI_RED+"Already Used\n");
                }
            }

        }else{
            throw new IllegalArgumentException("Must be rule or string");
        }

        increment.setInteger(increment.getInteger()-1);
        return;

    }
    public void follow(Rule rule,Set<String>list){
        if(rule == initialRule){
            list.add("$");
        }
        for(Rule r:rules){

            for(Production production:r.getProductions()){
                int i;
                boolean found = false;
                for(i =0;i<production.getElements().size();i++){
                    if(production.getElements().get(i) instanceof  Rule){
                        if(production.getElements().get(i) == r){

                            found=true;
                            break;
                        }
                    }
                }
                if(found) {
                    if (production.getElements().size() > i + 1) {
                        Set<String> elements = first(production.getElements().get(i + 1));
                        if (elements.contains("-1")) {
                                follow(r, list);
                        } else {
                            list.addAll(elements);
                        }
                    } else {
                        if (r != production.getElements().get(i)) {
                            follow(r, list);
                        }
                    }
                }
            }
        }
    }
    public void spaces(int spaces){
        for(int i=0;i<spaces;i++){
            System.out.print("\t");
        }
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}

