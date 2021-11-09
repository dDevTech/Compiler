package Analyzer.Sintactic;

import Analyzer.Lexical.LexicAnalyzer;
import Common.ErrorHandler;
import FDA.FDAException;
import Tools.Console;
import Tools.FileWrite;

import java.util.*;

public class SintaxAnalyzer {

    LexicAnalyzer lexicAnalyzer;
    private ArrayList<Rule> rules = new ArrayList<>();
    private Rule initialRule;
    private static List<Production> allProductions = new ArrayList<>();

    public ArrayList<Rule> getRules() {
        return rules;
    }

    public void setup() {
        lexicAnalyzer = new LexicAnalyzer();
        lexicAnalyzer.setup();

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

    public void execute(boolean debug) {
        Stack<Integer> stack = new Stack<>();

        descentRecursive(debug, new IntRef(1), new EntryRef(lexicAnalyzer.readToken()), initialRule, stack);

        //Write PARSER to file
        FileWrite write = new FileWrite("files/parser.txt");
        write.writer().print("Descendente ");
        System.out.println(stack.size());
        for (Integer i : stack) {
            write.writer().print(i + " ");
        }
        write.writer().flush();
        write.writer().close();
        //*********************
    }

    private void descentRecursive(boolean debug, IntRef increment, EntryRef entry, Rule rule, Stack<Integer> parse) {

        if (entry.getEntry() != null) {

            if (debug) spaces(increment.getInteger());
            if (debug) Console.print(Console.WHITE_BOLD + "Current rule: " + Console.PURPLE_BOLD + rule.getLetter() + Console.WHITE_BOLD + "  Token: " + Console.PURPLE_BOLD + entry.getEntry().getKey() + "\n");

            boolean foundProduction = false;
            Production withLambda = null;//neccesary to know which production of rule has lambda to calculate FOLLOW

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
                    for (int j = 0; j < prod.getElements().size(); j++) {//iterate through terminals and no terminals
                        if (entry.getEntry() != null) {
                            if (prod.getElements().get(j) instanceof String) {// terminal
                                if (!entry.getEntry().getKey().equals(prod.getElements().get(j))) {
                                    if (debug) spaces(increment.getInteger());
                                    if (debug)
                                        Console.print(Console.BLUE_BOLD + entry.getEntry().getKey() + Console.RED_BOLD + "  INVALID\n");

                                    ErrorHandler.showSintaxError(lexicAnalyzer.fda.getIterator(), new FDAException(-1, "Expecting "  +Console.ANSI_YELLOW+ prod.getElements().get(j) +  Console.ANSI_WHITE+" in structure " +  Console.ANSI_YELLOW+rule+" -> "+prod + Console.ANSI_WHITE+ " but found " +Console.ANSI_YELLOW+ entry.getEntry().getKey()+Console.ANSI_WHITE));
                                } else {//if terminal is equal to current token
                                    if (debug) spaces(increment.getInteger());
                                    if (debug)
                                        Console.print(Console.BLUE_BOLD + entry.getEntry().getKey() + Console.GREEN_BOLD + "  VALID\n");
                                }
                                entry.setEntry(lexicAnalyzer.readToken()); //read next token
                            } else if (prod.getElements().get(j) instanceof Rule) {//no terminal
                                descentRecursive(debug, increment, entry, (Rule) prod.getElements().get(j), parse);//recursive of non terminal rule
                            }
                        }else{
                            return;
                        }

                    }
                    increment.setInteger(increment.getInteger() - 1);

                    foundProduction = true;//found a production where token is in PRODUCTION FIRST
                    break;//dont continue checking the rest (LL1)
                } else {//check next production for FIRST
                    if (debug) Console.print(Console.WHITE_BOLD + " -> SKIPPED\n");
                }
            }

            if (!foundProduction) {//not found production with  current token in FIRST
                if (rule.isLambda()) {//is there any lambda production?
                    Set<String> set2 = follow(new IntRef(increment.getInteger() + 1), false, rule);
                    if(set2.contains(entry.getEntry().getKey())){
                        if (debug) spaces(increment.getInteger());
                        if (debug) Console.print(Console.BLUE_BOLD + "PARSE ID: " + rule.getLambdaIDParse() + "\n");
                        if (debug) spaces(increment.getInteger());
                        if (debug) Console.print(Console.BLUE_BOLD + "λ" + Console.GREEN_BOLD + "  VALID\n");
                        parse.push(rule.getLambdaIDParse()); //valorate as lambda then
                    }else{
                        ErrorHandler.showSintaxError(lexicAnalyzer.fda.getIterator(), new FDAException(-6, "Lambda production available in "+Console.ANSI_YELLOW+rule+Console.ANSI_WHITE+" but FOLLOW of rule ("  +Console.ANSI_YELLOW+set2+Console.ANSI_WHITE+") doesn't contain current token "+Console.ANSI_YELLOW+entry.getEntry().getKey()));
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
                            for (int j = 0; j < withLambda.getElements().size(); j++) {//iterate through terminals and no terminals
                                if (entry.getEntry() != null) {
                                    if (withLambda.getElements().get(j) instanceof String) {// terminal
                                        if (!entry.getEntry().getKey().equals(withLambda.getElements().get(j))) {
                                            if (debug) spaces(increment.getInteger());
                                            if (debug) Console.print(Console.BLUE_BOLD + entry.getEntry().getKey() + Console.RED_BOLD + "  INVALID\n");

                                            ErrorHandler.showSintaxError(lexicAnalyzer.fda.getIterator(), new FDAException(-1, "Expecting "  +Console.ANSI_YELLOW+ withLambda.getElements().get(j) +  Console.ANSI_WHITE+" in structure " +  Console.ANSI_YELLOW+rule+" -> "+withLambda + Console.ANSI_WHITE+ " but found " +Console.ANSI_YELLOW+ entry.getEntry().getKey()+Console.ANSI_WHITE));
                                        } else {//if terminal is equal to current token
                                            if (debug) spaces(increment.getInteger());
                                            if (debug) Console.print(Console.BLUE_BOLD + entry.getEntry().getKey() + Console.GREEN_BOLD + "  VALID\n");
                                        }
                                        entry.setEntry(lexicAnalyzer.readToken()); //read next token
                                    } else if (withLambda.getElements().get(j) instanceof Rule) {//no terminal
                                        descentRecursive(debug, increment, entry, (Rule) withLambda.getElements().get(j), parse);//recursive of non terminal rule
                                    }
                                }else{
                                    return;
                                }

                            }
                        } else {
                            ErrorHandler.showSintaxError(lexicAnalyzer.fda.getIterator(), new FDAException(-4, "Checking lambda transition but FOLLOW not contains it"));
                        }
                    } else {
                        ErrorHandler.showSintaxError(lexicAnalyzer.fda.getIterator(), new FDAException(-5, "No production available for current character and lambda transition is not available for token " + entry.getEntry().getKey() + "\n"));
                    }
                }


            } else {
                if (debug) spaces(increment.getInteger());
                if (debug) Console.print(Console.YELLOW_BOLD + "END\n");
            }


        } else {
            ErrorHandler.showSintaxError(lexicAnalyzer.fda.getIterator(), new FDAException(-2, "Unexpected end of tokens"));
        }


    }

    public void print(boolean compact) {
        if (compact) {
            Console.print(Console.PURPLE_BOLD + "\n\nSYNTAX GRAMMAR\n\n");
            for (Rule rule : rules) {
                Console.print(Console.WHITE_BOLD + rule.getLetter() + Console.PURPLE_BOLD + " => ");
                if (rule.isLambda()) {
                    Console.print(Console.YELLOW_BOLD + "λ");
                    Console.print(Console.YELLOW_BOLD + " | ");
                }
                int i = 0;
                for (Production prod : rule.getProductions()) {

                    for (Object o : prod.getElements()) {
                        if (o instanceof Rule) {
                            Console.print(Console.BLUE_BOLD + ((Rule) o).getLetter() + " ");
                        } else {
                            Console.print(Console.GREEN_BOLD + o.toString() + " ");
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
                    for (Object o : prod.getElements()) {
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
        increment.setInteger(increment.getInteger() + 1);
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

                        increment.setInteger(increment.getInteger() + 1);
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
                    Set<String> set = new HashSet<>();
                    if (debug) spaces(increment.getInteger());
                    if (debug)
                        Console.print(Console.PURPLE_BOLD + "=FIRST(" + Console.BLUE_BOLD + o + Console.PURPLE_BOLD + ")\n");

                    increment.setInteger(increment.getInteger() + 1);
                    firstRecursive(debug, increment, o, set, passedProductions);

                    containsLambda = set.contains("λ");
                    set.remove("λ");
                    list.addAll(set);

                    if (!containsLambda) {
                        break;
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
            throw new IllegalArgumentException("Must be rule or string");
        }

        increment.setInteger(increment.getInteger() - 1);
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

        increment.setInteger(increment.getInteger() + 1);
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
                                Console.print(Console.YELLOW_BOLD + " FOUND in " + r.getLetter() + " production" + Console.ANSI_WHITE + "  [" + production + "]\n");
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
                            p.getElements().add(production.getElements().get(j));
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
                                increment.setInteger(increment.getInteger() + 1);
                                HashSet<String> set = new HashSet<>();

                                followRecursive(debug, increment, r, set, passedProductions);

                                list.addAll(set);
                                increment.setInteger(increment.getInteger() - 1);
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
                            increment.setInteger(increment.getInteger() + 1);
                            HashSet<String> set = new HashSet<>();

                            followRecursive(debug, increment, r, set, passedProductions);
                            list.addAll(set);
                            increment.setInteger(increment.getInteger() - 1);
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

    public void spaces(int spaces) {
        for (int i = 0; i < spaces; i++) {
            System.out.print("\t");
        }
    }


}

