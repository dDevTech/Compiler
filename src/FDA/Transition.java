package FDA;

import jdk.swing.interop.SwingInterOpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Transition<T>{
    private Function<T,Boolean> function;
    private T transition;
    private State<T>toTransit;
    private boolean readNext = true;
    private List<SemanticAction<T>> actions = new ArrayList<>(); //conjunto acciones semanticas
    public Transition(T transition,State<T>toTransit){
        this.toTransit = toTransit;
        this.transition = transition;
    }
    public Transition(Function<T,Boolean> transitionFunction,State<T>toTransit){
        this.toTransit = toTransit;
        this.function = transitionFunction;
    }
    public Transition(T transition,State<T>toTransit,boolean ignoreRead){
        this.toTransit = toTransit;
        this.transition = transition;
        this.readNext = !ignoreRead;
    }
    public Transition(Function<T,Boolean> transitionFunction,State<T>toTransit,boolean ignoreRead){

        this.toTransit = toTransit;
        this.function = transitionFunction;
        this.readNext = !ignoreRead;
    }

    public boolean isReadNext() {
        return readNext;
    }
    public void addSemanticAction(SemanticAction action){
        actions.add(action);
    }
    public void setReadNext(boolean readNext) {
        this.readNext = readNext;
    }
    protected void callActions(T element){
        actions.stream().iterator().forEachRemaining((SemanticAction action)->action.onAction(toTransit,element));
    }
    public boolean apply(T element){

        if(isFunct()){
            return function.apply(element);
        }else{
            return transition.equals(element);
        }
    }

    public Function<T, Boolean> getFunction() {
        return function;
    }


    public T getTransition() {
        return transition;
    }



    public boolean isFunct() {
        return (function!=null);
    }

    public State<T> getToTransit() {
        return toTransit;
    }

    public void setToTransit(State<T> toTransit) {
        this.toTransit = toTransit;
    }

    @Override
    public String toString() {
        return "Transition{" +
                "function=" + function +
                ", transition=" + transition +
                ", readNext=" + readNext +
                ", actions=" + actions +
                '}';
    }
}
