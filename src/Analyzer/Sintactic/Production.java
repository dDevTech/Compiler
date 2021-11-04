package Analyzer.Sintactic;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Production {
    public ArrayList<Object> getElements() {
        return elements;
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
}
