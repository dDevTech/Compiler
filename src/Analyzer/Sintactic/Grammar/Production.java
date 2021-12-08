package Analyzer.Sintactic.Grammar;

import java.util.ArrayList;

public class Production {
    private Rule rule;
    private int idParse = -1;
    public ArrayList<Object> getElements() {
        return elements;
    }
    public void setRule(Rule rule){
        this.rule = rule;
    }



    public Rule getRule() {
        return rule;
    }

    private ArrayList<Object>elements=  new ArrayList<>();

    public Production(Object... elements){
        for(int i = 0;i<elements.length;i++){
            this.elements.add(elements[i]);
        }

    }
    public void addNoTerminal(Rule rule){
        elements.add(rule);
    }
    public void addTerminal(String token){
        elements.add(token);
    }

    @Override
    public String toString() {
        return elements.toString();
    }

    public int getIdParse() {
        return idParse;
    }

    public void setIdParse(int idParse) {
        this.idParse = idParse;
    }
}
