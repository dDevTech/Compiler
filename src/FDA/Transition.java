package FDA;

import Common.ErrorHandler;
import Tools.FileIterator;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class Transition<T>{
    private Function<T,Boolean> function;
    private T transition;
    private State<T>toTransit;
    private boolean readNext = true;
    private boolean write = false;
    private List<SemanticAction<T>> actions;
    public Transition(T transition,State<T>toTransit){
        this.toTransit = toTransit;
        this.transition = transition;
        actions = new ArrayList<>();
    }
    public Transition(Function<T,Boolean> transitionFunction,State<T>toTransit){
        this.toTransit = toTransit;
        this.function = transitionFunction;
        actions = new ArrayList<>();
    }
    public Transition(T transition,State<T>toTransit,boolean ignoreRead,boolean write){
        this.toTransit = toTransit;
        this.transition = transition;
        this.readNext = !ignoreRead;
        this.write = write;
        actions = new ArrayList<>();
    }
    public Transition(Function<T,Boolean> transitionFunction,State<T>toTransit,boolean ignoreRead,boolean write){

        this.toTransit = toTransit;
        this.function = transitionFunction;
        this.readNext = !ignoreRead;
        this.write = write;
        actions = new ArrayList<>();
    }

    public boolean isReadNext() {
        return readNext;
    }
    public void addSemanticAction(SemanticAction<T> action){
        actions.add(action);
    }
    public void setReadNext(boolean readNext) {
        this.readNext = readNext;
    }
    protected void callActions(T element, List<T>elements, FileIterator iterator) throws FDAException{
        Iterator<SemanticAction<T>>actionsIt =actions.stream().iterator();

        while(actionsIt.hasNext()){
            SemanticAction<T> action = actionsIt.next();
            action.onAction(toTransit,element,elements);
        }


    }
    public boolean apply(T element){

        if(isFunction()){
            return function.apply(element);
        }else{
            return transition.equals(element);
        }
    }

    public boolean isWrite() {
        return write;
    }

    public void setWrite(boolean write) {
        this.write = write;
    }

    public Function<T, Boolean> getFunction() {
        return function;
    }


    public T getTransition() {
        return transition;
    }



    public boolean isFunction() {
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
