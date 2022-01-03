package Javascript;

import Analyzer.Lexical.LexicAnalyzer;
import Analyzer.Semantic.Atribute;
import Analyzer.Semantic.RuleData;
import Analyzer.Semantic.SemanticAction;
import Analyzer.Sintactic.Utils.EntryRef;
import Analyzer.Sintactic.Utils.IntRef;
import Analyzer.Sintactic.Grammar.Production;
import Analyzer.Sintactic.Grammar.Rule;
import Common.ErrorHandler;
import FDA.FDAException;
import Tools.Console;
import Tools.FileWrite;

import java.util.*;

public class SintaxSemanticAnalyzer {

    public LexicAnalyzer lexicAnalyzer;
    private ArrayList<Rule> rules = new ArrayList<>();
    private Rule initialRule;

    public boolean isContinueOnError() {
        return continueOnError;
    }

    public void setContinueOnError(boolean continueOnError) {
        this.continueOnError = continueOnError;
    }

    private boolean continueOnError = false;

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    private boolean debug = false;
    public ArrayList<Rule> getRules() {
        return rules;
    }



    protected void setup(LexicAnalyzer lexicAnalyzer) {
       this.lexicAnalyzer = lexicAnalyzer;
    }
    public void start(){
        execute();
        close();
    }
    public void close(){
        lexicAnalyzer.close();
    }
    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public void setInitialRule(Rule rule) {
        this.initialRule = rule;
    }

    /**
     * Check LL1 conditions of the current grammar. Will print FIRST-FIRST collisions and FIRST-FOLLOW collisions if lambda is in FIRST
     */
    public void LL1Collisions() {
        System.out.println();
        System.out.println();
        Console.print(Console.RED_BOLD + "CHECKING COLLISIONS IN GRAMMAR\n");
        int counter = 0;
        for (Rule rule : rules) {
            //ALL COMBINATIONS OF FIRST OF PRODUCTIONS OF A RULE MUST BE EMPTY
            for (int i = 0; i < rule.getProductions().size(); i++) {

                for (int j = i; j < rule.getProductions().size(); j++) {

                    Production prod = rule.getProductions().get(i);
                    Production prod2 = rule.getProductions().get(j);

                    if (prod != prod2) {
                        Set<String> set1 = first(new IntRef(0), false, prod);
                        Set<String> set2 = first(new IntRef(0), false, prod2);

                        Set<String> intersection = new HashSet<>(set1);
                        intersection.retainAll(set2);
                        if (intersection.size() > 0) {
                            Console.print(Console.RED_BOLD + "COLLISION FIRST: " + Console.PURPLE_BOLD + prod + Console.YELLOW_BOLD + "[" + prod.getRule() + "]" + Console.RED_BOLD + " <!> " + Console.PURPLE_BOLD + prod2 + Console.YELLOW_BOLD + "[" + prod2.getRule() + "] " + Console.WHITE_BOLD + intersection + "\n");
                            counter++;
                        } else {//FIRST WITH LAMBDA AND ASSOCIATED FOLLOW INTERSECTION MUST BE EMPTY
                            Set<String> setFollow = follow(new IntRef(0), false, rule);
                            Set<String> intersectionFollow = new HashSet<>(setFollow);
                            if (set1.contains("λ")) {
                                intersectionFollow.retainAll(set2);
                                if (intersectionFollow.size() > 0) {
                                    Console.print(Console.RED_BOLD + "COLLISION FOLLOW: " + Console.PURPLE_BOLD + prod + Console.YELLOW_BOLD + "[" + prod.getRule() + "]" + Console.RED_BOLD + " <!> " + Console.PURPLE_BOLD + prod2 + Console.YELLOW_BOLD + "[" + prod2.getRule() + "] " + Console.WHITE_BOLD + intersectionFollow + "\n");
                                    counter++;
                                }

                            } else if (set2.contains("λ")) {
                                intersectionFollow.retainAll(set1);
                                if (intersectionFollow.size() > 0) {
                                    Console.print(Console.RED_BOLD + "COLLISION FOLLOW: " + Console.PURPLE_BOLD + prod + Console.YELLOW_BOLD + "[" + prod.getRule() + "]" + Console.RED_BOLD + " <!> " + Console.PURPLE_BOLD + prod2 + Console.YELLOW_BOLD + "[" + prod2.getRule() + "] " + Console.WHITE_BOLD + intersectionFollow + "\n");
                                    counter++;
                                }
                            }

                        }

                    }
                }
            }
        }
        if (counter == 0) {
            Console.print(Console.GREEN_BOLD + "NO COLLISIONS FOUND\n");
            System.out.println();
            System.out.println();
        }
    }

    public Stack<Integer> execute() {
        onSyntaxError= false;
        Stack<Integer> stack = new Stack<>();

        descentRecursive(null,debug, new IntRef(1), new EntryRef(lexicAnalyzer.readToken()), initialRule, stack);

        //Write PARSER to file
        FileWrite write = new FileWrite("files/parser.txt");
        write.writer().print("Descendente ");
        System.out.println("Total parse size: "+stack.size());
        for (Integer i : stack) {
            write.writer().print(i + " ");
        }
        write.writer().flush();
        write.writer().close();

        return stack;
        //*********************
    }
    boolean onSyntaxError = false;
    private RuleData descentRecursive(RuleData ruleData,boolean debug, IntRef increment, EntryRef entry, Rule rule, Stack<Integer> parse) {

        if (entry.getEntry() != null) {

            if (debug) spaces(increment.getInteger());
            if (debug) Console.print(Console.WHITE_BOLD + "Current rule: " + Console.PURPLE_BOLD + rule.getLetter() + Console.WHITE_BOLD + "  Token: " + Console.PURPLE_BOLD + entry.getEntry().getKey() + "\n");

            boolean foundProduction = false;
            Production withLambda = null;//neccesary to know which production of rule has lambda to calculate FOLLOW
            HashMap<String,Object> params = new HashMap<>();
            if(ruleData!=null){
                params.put(ruleData.getRule(), ruleData);
            }

            for (int i = 0; i < rule.getProductions().size(); i++) {
                Production prod = rule.getProductions().get(i);
                Set<String> set = first(new IntRef(increment.getInteger() + 1), false, prod);//first of each production of rule

                if (set.contains("λ")) withLambda = prod;//update withlambda if first contains lambda

                if (debug) spaces(increment.getInteger());
                if (debug) Console.print(Console.YELLOW_BOLD + "Production: " + prod + Console.PURPLE_BOLD + " FIRST -> " + Console.BLUE_BOLD + set);

                if (set.contains(entry.getEntry().getKey())) {
                    if (debug) Console.print("\n");
                    if (debug) spaces(increment.getInteger());
                    if (debug) Console.print(Console.WHITE_BOLD + "Production chosed " + Console.YELLOW_BOLD + prod + Console.WHITE_BOLD + " in " + Console.PURPLE_BOLD + rule + "\n");
                    if (debug) spaces(increment.getInteger());
                    if (debug) Console.print(Console.BLUE_BOLD + "Parse id: " + prod.getIdParse() + "\n");

                    increment.setInteger(increment.getInteger() + 1);
                    parse.push(prod.getIdParse());//add to parse current production id because current token is in FIRST
                    params = new HashMap<>();
                    for (int j = 0; j < prod.getAllElements().size(); j++) {//iterate through terminals and no terminals

                        if (entry.getEntry() != null) {

                            if (prod.getAllElements().get(j) instanceof String) {// terminal
                                if (!entry.getEntry().getKey().equals(prod.getAllElements().get(j))) {
                                    if (debug) spaces(increment.getInteger());
                                    if (debug)
                                        Console.print(Console.BLUE_BOLD + entry.getEntry().getKey() + Console.RED_BOLD + "  INVALID\n");
                                    onSyntaxError=true;
                                    ErrorHandler.showSintaxError(lexicAnalyzer.getFda().getIterator(), new FDAException(-1, "Expecting "  +Console.ANSI_YELLOW+ prod.getAllElements().get(j) +  Console.ANSI_WHITE+" in structure " +  Console.ANSI_YELLOW+rule+" -> "+prod + Console.ANSI_WHITE+ " but found " +Console.ANSI_YELLOW+ entry.getEntry().getKey()+Console.ANSI_WHITE));

                                } else {//if terminal is equal to current token
                                    params.put((String)prod.getAllElements().get(j),entry.getEntry().getValue());
                                    if (debug) spaces(increment.getInteger());
                                    if (debug)
                                        Console.print(Console.BLUE_BOLD + entry.getEntry().getKey() + Console.GREEN_BOLD + "  VALID\n");
                                }
                                entry.setEntry(lexicAnalyzer.readToken()); //read next token
                            } else if (prod.getAllElements().get(j) instanceof Rule) {//no terminal
                                RuleData data= descentRecursive((RuleData) params.get(prod.getAllElements().get(j)),debug, increment, entry, (Rule) prod.getAllElements().get(j), parse);//recursive of non terminal rule

                                if(data==null){

                                    if(isContinueOnError()){
                                        return new RuleData(null);
                                    }else{
                                        return null;
                                    }
                                }else{
                                    addListToParams(params,data.getRule(),data);
                                }
                            } else if (prod.getAllElements().get(j) instanceof SemanticAction&&!onSyntaxError) {


                                SemanticAction action = (SemanticAction) prod.getAllElements().get(j);
                                if (debug) spaces(increment.getInteger());
                                if (debug) Console.print(Console.WHITE_BOLD+"EXECUTING "+ Console.CYAN_BOLD +prod.getRule()+" -> "+prod.getElements()+" : {"+action.getInfo()+"}"+Console.WHITE_BOLD+" WITH PARAMS "+params+"\n");
                                List<RuleData> data=null;
                                try {
                                    data = action.apply(params, lexicAnalyzer.getHandler());
                                }catch(Exception e){
                                    ErrorHandler.showCompilerError(lexicAnalyzer.getIterator(), e);
                                }

                                if (debug) spaces(increment.getInteger());

                                if (debug) Console.print(Console.WHITE_BOLD+"AND RETURNED "+data+"\n");
                                for(RuleData element:data){
                                    addListToParams(params, element.getRule(),element);
                                }


                            }

                        }else{
                            System.err.println("END OF FILE!");
                            //return null;
                        }

                    }
                    if (debug)increment.setInteger(increment.getInteger() - 1);

                    foundProduction = true;//found a production where token is in PRODUCTION FIRST
                    break;//dont continue checking the rest (LL1)
                } else {//check next production for FIRST

                    if (debug) Console.print(Console.WHITE_BOLD + " -> SKIPPED\n");
                }
            }
            //TODO PARTE SEMANTICA LAMBDAS
            if (!foundProduction) {//not found production with  current token in FIRST
                if (rule.isLambda()) {//is there any lambda production?
                    Set<String> set2 = follow(new IntRef(increment.getInteger() + 1), false, rule);
                    if(set2.contains(entry.getEntry().getKey())){
                        if (debug) spaces(increment.getInteger());
                        if (debug) Console.print(Console.BLUE_BOLD + "PARSE ID: " + rule.getLambdaIDParse() + "\n");
                        if (debug) spaces(increment.getInteger()+1);
                        if (debug) Console.print(Console.BLUE_BOLD + "λ" + Console.GREEN_BOLD + "  VALID\n");
                        parse.push(rule.getLambdaIDParse()); //valorate as lambda then

                        HashMap<String,Object>map = new HashMap<>();
                        if(rule.getLambdaAction()!=null){
                            if (debug) spaces(increment.getInteger()+1);
                            if (debug) Console.print(Console.WHITE_BOLD+"EXECUTING "+ Console.CYAN_BOLD +rule.getLambdaAction().getInfo()+Console.WHITE_BOLD+" WITH PARAMS "+params+"\n");

                            List<RuleData>data=rule.getLambdaAction().apply(map, lexicAnalyzer.getHandler());

                            if (debug) spaces(increment.getInteger()+1);
                            if (debug) Console.print(Console.WHITE_BOLD+"AND RETURNED "+data+"\n");

                            for(RuleData element:data){
                                addListToParams(map, element.getRule(),element);
                            }

                        }

                        RuleData data = (RuleData) map.get("ret");

                        if(data==null) return new RuleData(null);
                        data.setRule(rule.getLetter());
                        return data;

                    }else{
                        onSyntaxError=true;
                        ErrorHandler.showSintaxError(lexicAnalyzer.getFda().getIterator(), new FDAException(-6, "Lambda production available in "+Console.ANSI_YELLOW+rule+Console.ANSI_WHITE+" but FOLLOW of rule ("  +Console.ANSI_YELLOW+set2+Console.ANSI_WHITE+") doesn't contain current token "+Console.ANSI_YELLOW+entry.getEntry().getKey()));
                        if(isContinueOnError()){

                            return new RuleData(null);
                        }else{

                            return null;
                        }
                    }

                } else {//lambdas productions
                    if (withLambda != null) {
                        System.out.println(Console.RED_BOLD + "ENTRADA UNEXPECTED");
                        Set<String> set2 = follow(new IntRef(increment.getInteger() + 1), false, rule);
                        if (debug) spaces(increment.getInteger());
                        if (debug) Console.print(Console.YELLOW_BOLD + "Rule: " + rule + Console.PURPLE_BOLD + " FOLLOW -> " + Console.BLUE_BOLD + set2 + "\n");
                        if (set2.contains(entry.getEntry().getKey())) {
                            if (debug) spaces(increment.getInteger());
                            if (debug) Console.print(Console.RED_BOLD + withLambda+"Contains λ\n");
                            for (int j = 0; j < withLambda.getAllElements().size(); j++) {//iterate through terminals and no terminals
                                if (entry.getEntry() != null) {
                                    if (withLambda.getAllElements().get(j) instanceof String) {// terminal
                                        if (!entry.getEntry().getKey().equals(withLambda.getAllElements().get(j))) {
                                            if (debug) spaces(increment.getInteger());
                                            if (debug) Console.print(Console.BLUE_BOLD + entry.getEntry().getKey() + Console.RED_BOLD + "  INVALID\n");

                                            ErrorHandler.showSintaxError(lexicAnalyzer.getFda().getIterator(), new FDAException(-1, "Expecting "  +Console.ANSI_YELLOW+ withLambda.getAllElements().get(j) +  Console.ANSI_WHITE+" in structure " +  Console.ANSI_YELLOW+rule+" -> "+withLambda + Console.ANSI_WHITE+ " but found " +Console.ANSI_YELLOW+ entry.getEntry().getKey()+Console.ANSI_WHITE));
                                        } else {//if terminal is equal to current token
                                            if (debug) spaces(increment.getInteger());
                                            if (debug) Console.print(Console.BLUE_BOLD + entry.getEntry().getKey() + Console.GREEN_BOLD + "  VALID\n");
                                        }
                                        entry.setEntry(lexicAnalyzer.readToken()); //read next token
                                    } else if (withLambda.getAllElements().get(j) instanceof Rule) {//no terminal
                                        if( descentRecursive((RuleData) params.get(((Rule) withLambda.getAllElements().get(j)).getLetter()),debug, increment, entry, (Rule) withLambda.getAllElements().get(j), parse)==null){
                                            if(isContinueOnError()){
                                                return new RuleData(null);
                                            }else{
                                                return null;
                                            }
                                        } //recursive of non terminal rule

                                    }
                                }else{
                                    return null;
                                }

                            }
                        } else {
                            onSyntaxError=true;
                            ErrorHandler.showSintaxError(lexicAnalyzer.getFda().getIterator(), new FDAException(-4, "Checking lambda transition but FOLLOW not contains it"));
                        }
                    } else {
                        onSyntaxError=true;
                        ErrorHandler.showSintaxError(lexicAnalyzer.getFda().getIterator(), new FDAException(-5, "No production available for current character and lambda transition is not available for token " + entry.getEntry().getKey() + "\n"));
                    }
                }


            } else {

                if (debug) spaces(increment.getInteger());
                if (debug) Console.print(Console.YELLOW_BOLD + "END\n");
                //return of data sintetized atributes
                RuleData data = (RuleData) params.get("ret");

                if(data==null) return new RuleData(null);
                data.setRule(rule.getLetter());
                return data;

            }


        } else {
            ErrorHandler.showSintaxError(lexicAnalyzer.getFda().getIterator(), new FDAException(-2, "Unexpected end of tokens"));
        }
        return new RuleData(null);

    }

    public void print(boolean compact) {
        if (compact) {
            Console.print(Console.PURPLE_BOLD + "\n\nSYNTAX GRAMMAR\n\n");
            for (Rule rule : rules) {
                Console.print(Console.WHITE_BOLD + rule.getLetter() + Console.PURPLE_BOLD + " => ");
                if (rule.isLambda()) {
                    Console.print(Console.YELLOW_BOLD + "λ");
                    if(rule.getLambdaAction()!=null){
                        Console.print(Console.ANSI_CYAN+" "+rule.getLambdaAction().toString());
                    }
                    Console.print(Console.YELLOW_BOLD + " | ");


                }
                int i = 0;
                for (Production prod : rule.getProductions()) {

                    for (Object o : prod.getAllElements()) {
                        if (o instanceof Rule) {
                            Console.print(Console.BLUE_BOLD + ((Rule) o).getLetter() + " ");
                        } else if(o instanceof String) {
                            Console.print(Console.GREEN_BOLD + o + " ");
                        } else if(o instanceof SemanticAction){
                            Console.print(Console.ANSI_CYAN + o + " ");
                        }
                    }
                    if (i < rule.getProductions().size() - 1) Console.print(Console.YELLOW_BOLD + " | ");
                    i++;
                }

                System.out.println();
            }
            System.out.println();
            System.out.println();
        } else {
            Console.print(Console.PURPLE_BOLD + "\n\nSYNTAX GRAMMAR\n\n");
            int count = 1;
            for (Rule rule : rules) {
                for (Production prod : rule.getProductions()) {

                    Console.print(Console.PURPLE_BOLD + count + ". " + Console.WHITE_BOLD + rule.getLetter() + Console.PURPLE_BOLD + " => ");
                    for (Object o : prod.getAllElements()) {
                        if (o instanceof Rule) {
                            Console.print(Console.BLUE_BOLD + ((Rule) o).getLetter() + " ");
                        } else {
                            Console.print(Console.GREEN_BOLD + o.toString() + " ");
                        }
                    }
                    count++;
                    Console.print("\n");

                }
                if (rule.isLambda()) {
                    Console.print(Console.PURPLE_BOLD + count + ". " + Console.WHITE_BOLD + rule.getLetter() + Console.PURPLE_BOLD + " => λ");
                    count++;
                }

                System.out.println();
            }
            System.out.println();
            System.out.println();
        }
    }

    public void assignIds() {

        int count = 1;
        for (Rule rule : rules) {
            for (Production prod : rule.getProductions()) {
                prod.setIdParse(count);
                count++;
            }
            if (rule.isLambda()) {
                rule.setLambdaIDParse(count);
                count++;
            }
        }
    }


    public Set<String> first(IntRef increment, boolean debug, Object o) {
        if (increment == null) {
            increment = new IntRef(1);
        }
        Set<String> set = new HashSet<>();
        Set<Production> productions = new HashSet<>();
        if (debug) spaces(increment.getInteger());
        if (debug) Console.print(Console.PURPLE_BOLD + "FIRST: " + o + "\n");
       /* if (o instanceof Production) {//If we want to calculate the first of a production the add the other productions of the selected rule to the PASSED productions to prevent calculating its first
            for (Production prod : ((Production) o).getRule().getProductions()) {
                if (o != prod) {
                    productions.add(prod);
                }
            }
        }*/
        if (debug)increment.setInteger(increment.getInteger() + 1);
        firstRecursive(debug, increment, o, set, productions);

        return set;
    }

    private void firstRecursive(boolean debug, IntRef increment, Object object, Set<String> list, Set<Production> passedProductions) {

        if (object instanceof String) {//Terminal
            if (debug) spaces(increment.getInteger());
            if (debug) Console.print(Console.BLUE_BOLD + object + "\n");
            list.add((String) object);
        } else if (object instanceof Rule) {//No terminal
            Rule rule = (Rule) object;
            if (rule.isLambda()) {//production of type lambda
                if (debug) spaces(increment.getInteger());
                if (debug) Console.print(Console.RED_BOLD + "λ" + "\n");
                list.add("λ");
            }
            for (Production Y : rule.getProductions()) {//Rest of productions
                if (debug) spaces(increment.getInteger());
                if (debug)
                    Console.print(Console.YELLOW_BOLD + Y + Console.ANSI_WHITE + "  USED: " + passedProductions + "\n");
                boolean containsLambda = false;
                if (!passedProductions.contains(Y)) {//list to prevent infinite iterations - ALREADY  PASSED PRODUCTIONS
                    passedProductions.add(Y);
                    for (Object o : Y.getElements()) {//Go to elements of production and calculate their first and append to the FIRST list except lambda that is removed

                            Set<String> set = new HashSet<>();
                            if (debug) spaces(increment.getInteger());
                            if (debug)
                                Console.print(Console.PURPLE_BOLD + "=FIRST(" + Console.BLUE_BOLD + o + Console.PURPLE_BOLD + ")\n");

                            if (debug)increment.setInteger(increment.getInteger() + 1);


                            firstRecursive(debug, increment, o, set, passedProductions);

                            containsLambda = set.contains("λ");
                            set.remove("λ");
                            list.addAll(set);

                            if (!containsLambda) {//not contain lambda
                                break;
                            }

                    }
                    if (containsLambda) {//All elements contain lambda so lambda is added to FIRST list
                        if (debug) spaces(increment.getInteger());
                        if (debug) Console.print(Console.YELLOW_BOLD + "λ (all lambdas)" + "\n");
                        list.add("λ");
                    }


                    passedProductions.remove(Y);
                } else {
                    if (debug) spaces(increment.getInteger());
                    if (debug) Console.print(Console.ANSI_RED + "Already Used\n");
                }
            }

        } else if (object instanceof Production) {//Only if we want to calculate the first of a production. Same as Rule production
            Production Y = (Production) object;
            if (debug) spaces(increment.getInteger());
            if (debug)
                Console.print(Console.YELLOW_BOLD + Y + Console.ANSI_WHITE + "  USED: " + passedProductions + "\n");
            boolean containsLambda = false;
            if (!passedProductions.contains(Y)) {
                passedProductions.add(Y);
                for (Object o : Y.getElements()) {
                    if(!(o instanceof SemanticAction)) {
                        Set<String> set = new HashSet<>();
                        if (debug) spaces(increment.getInteger());
                        if (debug)
                            Console.print(Console.PURPLE_BOLD + "=FIRST(" + Console.BLUE_BOLD + o + Console.PURPLE_BOLD + ")\n");

                        if (debug)increment.setInteger(increment.getInteger() + 1);
                        firstRecursive(debug, increment, o, set, passedProductions);

                        containsLambda = set.contains("λ");
                        set.remove("λ");
                        list.addAll(set);

                        if (!containsLambda) {
                            break;
                        }
                    }

                }
                if (containsLambda) {
                    if (debug) spaces(increment.getInteger());
                    if (debug) Console.print(Console.YELLOW_BOLD + "λ (all lambdas)" + "\n");
                    list.add("λ");
                }


                passedProductions.remove(Y);
            } else {
                spaces(increment.getInteger());
                Console.print(Console.ANSI_RED + "Already Used\n");
            }
        } else {
            //throw new IllegalArgumentException("Must be rule or string");
        }

        if (debug)increment.setInteger(increment.getInteger() - 1);
        return;

    }

    public Set<String> follow(IntRef increment, boolean debug, Rule rule) {

        if (increment == null) {
            increment = new IntRef(1);
        }
        Set<String> set = new HashSet<>();
        Set<Rule> productions = new HashSet<>();
        if (debug) spaces(increment.getInteger());
        if (debug) Console.print(Console.PURPLE_BOLD + "FOLLOW: " + rule + "\n");

        if (debug)increment.setInteger(increment.getInteger() + 1);
        followRecursive(debug, increment, rule, set, productions);

        return set;
    }

    //TODO review
    private void followRecursive(boolean debug, IntRef increment, Rule rule, Set<String> list, Set<Rule> passedProductions) {

        if (rule == initialRule) {
            list.add("$");
            if (debug) spaces(increment.getInteger());
            if (debug) Console.print(Console.RED_BOLD + "$\n");
        }
        for (Rule r : rules) {

            for (Production production : r.getProductions()) {

                int i;
                boolean found = false;
                for (i = 0; i < production.getElements().size(); i++) {
                    if (production.getElements().get(i) instanceof Rule) {
                        if (production.getElements().get(i) == rule) {
                            if (debug) spaces(increment.getInteger());
                            if (debug)
                                Console.print(Console.YELLOW_BOLD + " FOUND in " + r.getLetter() + " production" + Console.ANSI_WHITE + "  [" + production.getElements() + "]\n");
                            found = true;
                            break;
                        }
                    }
                }
                if (found) {

                    if (production.getElements().size() > i + 1) {
                        Production p = new Production();
                        p.setRule(r);

                        //Calcular next elements production
                        for (int j = i + 1; j < production.getElements().size(); j++) {

                            p.getAllElements().add(production.getElements().get(j));


                        }
                        if (debug) spaces(increment.getInteger());
                        if (debug)
                            Console.print(Console.PURPLE_BOLD + "=FIRST(" + Console.BLUE_BOLD + p + Console.PURPLE_BOLD + ")");
                        Set<String> elements = first(new IntRef(increment.getInteger() + 1), false, p);
                        boolean containsLambda = elements.contains("λ");
                        if (debug) Console.print(Console.ANSI_WHITE + " -> " + elements + "\n");
                        elements.remove("λ");

                        list.addAll(elements);
                        if (containsLambda) {
                            if (!passedProductions.contains(r)) {

                                passedProductions.add(r);
                                if (debug) spaces(increment.getInteger());
                                if (debug)
                                    Console.print(Console.PURPLE_BOLD + "=FOLLOW(" + Console.BLUE_BOLD + r + Console.PURPLE_BOLD + ")\n");
                                if (debug)increment.setInteger(increment.getInteger() + 1);
                                HashSet<String> set = new HashSet<>();

                                followRecursive(debug, increment, r, set, passedProductions);

                                list.addAll(set);
                                if (debug)increment.setInteger(increment.getInteger() - 1);
                                passedProductions.remove(r);
                            } else {
                                if (debug) spaces(increment.getInteger());
                                if (debug) Console.print(Console.ANSI_RED + "Already Used\n");
                            }
                        }
                    } else {

                        if (!passedProductions.contains(r)) {
                            passedProductions.add(r);
                            if (debug) spaces(increment.getInteger());
                            if (debug)
                                Console.print(Console.PURPLE_BOLD + "=FOLLOW(" + Console.BLUE_BOLD + r + Console.PURPLE_BOLD + ")\n");
                            if (debug)increment.setInteger(increment.getInteger() + 1);
                            HashSet<String> set = new HashSet<>();

                            followRecursive(debug, increment, r, set, passedProductions);
                            list.addAll(set);
                            if (debug)increment.setInteger(increment.getInteger() - 1);
                            passedProductions.remove(r);
                        } else {
                            if (debug) spaces(increment.getInteger());
                            if (debug) Console.print(Console.ANSI_RED + "Already Used\n");
                        }

                    }
                }
            }
        }
    }
    public void addParamToKey(HashMap<String,Object>map,String rule,String name ,Object content){

        if(map.get(rule)!=null){
            if(map.get(rule) instanceof RuleData) {
                ((RuleData) map.get(rule)).addAttribute(name,content);
            }
        }else{
            RuleData data =new RuleData(rule);
            map.put(rule,data);
            data.addAttribute(name,content);
        }
    }
    public void addListToParams(HashMap<String,Object>map,String rule,RuleData data){
        for(Map.Entry<String,Atribute>entry:data.getAtributes().entrySet()){

            addParamToKey(map,rule,entry.getKey(),entry.getValue().getContent());
        }
    }
    public void spaces(int spaces) {
        for (int i = 0; i < spaces; i++) {
            System.out.print("\t");
        }
    }


}

