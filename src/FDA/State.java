package FDA;

import Tools.Console;

import java.util.*;
import java.util.function.Function;

public class State <T>{
    private Map<T, State<T>> transitions = new HashMap<>();
    private Map<Function<T,Boolean>,State<T>> transitionFunctions = new HashMap<>();
    private List<SemanticAction<T>> actions = new ArrayList<>(); //conjunto acciones semanticas

    private String name = "Node";

    public State(String name){
        this.name = name;
    }
    public State(){

    }


    public void addTransition(T transitionValue, State<T> toTransit){
        if(toTransit == null){
            throw new IllegalArgumentException("Transitioned node must be not null");
        }

        if(transitions.get(transitionValue)==toTransit){ //asegurar que no hay dos iguales, es determinista
            throw new IllegalArgumentException("Repeated transition value");
        }
        transitions.put(transitionValue,toTransit);
    }
    public void addTransitionFunction(Function<T,Boolean>transitionFunction, State<T> toTransit){
        if(toTransit== null){
            throw new IllegalArgumentException("Transitioned node must be not null");
        }
        if(transitionFunctions.get(transitionFunction)==toTransit){
            throw new IllegalArgumentException("Repeated transition function");
        }
        transitionFunctions.put(transitionFunction,toTransit);

    }
    protected void checkTransitions(FDA<T>fda,T next,boolean debug){
        callActions();
        if(this instanceof FinalState){

        }

        State<T>nextState = null;
        for(Map.Entry<T, State<T>>entry:transitions.entrySet()){
            if(entry.getKey().equals(next)){
                nextState = entry.getValue();
            }
        }
        if(nextState==null){
            for(Map.Entry<Function<T,Boolean>, State<T>>entry:transitionFunctions.entrySet()){ //recorremos las funciones

                if(entry.getKey().apply(next)){
                    nextState = entry.getValue();
                }
            }
            if(debug){
                Console.print(Console.ANSI_RED+"Not available transition. Not recognized by FDA ");
                return;
            }
        }
        try {
            nextState.checkTransitions(fda,fda.readNext(), debug);
        }catch(IndexOutOfBoundsException exception){
            Console.print(Console.ANSI_RED+"Reach final");
        }

    }
    public void addSemanticAction(SemanticAction action){
        actions.add(action);
    }
    private void callActions(){
        actions.stream().iterator().forEachRemaining((SemanticAction action)->action.onAction(this));
    }

}
