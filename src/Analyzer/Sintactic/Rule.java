package Analyzer.Sintactic;

import java.util.ArrayList;

public class Rule {
    private Character letter;
    private ArrayList<Production>productions = new ArrayList<>();
    private boolean lambda = false;

    public boolean isLambda() {
        return lambda;
    }

    public void setLambda(boolean lambda) {
        this.lambda = lambda;
    }

    public ArrayList<Production> getProductions() {
        return productions;
    }

    public Rule(){

    }
    public Rule(Character c){
        letter = c;
    }
    public void addProductions(Production... list){
        for(int i = 0;i<list.length;i++){
            this.productions.add(list[i]);
        }
    }
    public void addProduction(Production production){
        productions.add(production);
    }

    public Character getLetter() {
        return letter;
    }

    public void setLetter(Character letter) {
        this.letter = letter;
    }

    @Override
    public String toString() {
        return letter.toString();
    }
}
