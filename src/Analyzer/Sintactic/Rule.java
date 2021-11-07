package Analyzer.Sintactic;

import java.util.ArrayList;
import java.util.List;

public class Rule {
    private String letter;
    private ArrayList<Production>productions = new ArrayList<>();
    private boolean lambda = false;
    private int lambdaIDParse = -1;

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
    public Rule(String c){
        letter = c;
    }
    public void addProductions(Production... list){
        for(int i = 0;i<list.length;i++){
            list[i].setRule(this);
            this.productions.add(list[i]);
        }


    }
    public void addProduction(Production production){
        productions.add(production);

    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    @Override
    public String toString() {
        return letter.toString();
    }

    public int getLambdaIDParse() {
        return lambdaIDParse;
    }

    public void setLambdaIDParse(int lambdaIDParse) {
        this.lambdaIDParse = lambdaIDParse;
    }
}
