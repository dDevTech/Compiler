package FDA;

import Tools.Console;


import java.util.*;
import java.util.function.Function;

public class State <T>{

    private ArrayList<Transition<T>> transitions = new ArrayList<>();
    private Transition other =null;
    private String name = "Node";


    public State(String name){
        this.name = name;
    }
    public State(){}


    public Transition addTransition(T transitionValue, State toTransit,boolean ignoreRead,boolean write){
        checkAddTransition(toTransit,ignoreRead);

        Transition transition =new Transition(transitionValue,toTransit,ignoreRead,write);
        transitions.add(transition);
        return transition;
    }
    public Transition addTransitionFunction(Function<T,Boolean>transitionFunction, State toTransit,boolean ignoreRead,boolean write){
        checkAddTransition(toTransit,ignoreRead);

        Transition transition = new Transition(transitionFunction,toTransit,ignoreRead,write);
        transitions.add(transition);
        return transition;

    }
    public void addOtherElementTransitionFunction(State toTransit, boolean ignoreRead,boolean write){
        checkAddTransition(toTransit,ignoreRead);

        Transition transition = new Transition<T>(this::otherFunction,toTransit,ignoreRead,write);
        other = transition;

    }
    private void checkAddTransition(State toTransit,boolean ignoreRead){
        if(toTransit== null){
            throw new IllegalArgumentException("Transitioned node must be not null");
        }
        if(!(toTransit instanceof FinalState) && ignoreRead == true){
            throw new IllegalArgumentException("Ignore read can only be used in FinalStates");
        }
    }
    private boolean otherFunction(T next){

        for(Transition transition:transitions){
            if(transition.apply(next)){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "State{" +
                "transitions=" + transitions +
                ", name='" + name + '\'' +
                '}';
    }

    protected FDAData<T> feedForward(Transition<T>transition, FDA<T>fda, boolean debug, T prev,List<T>sequence){
        //Check first if current node is final
        if(this instanceof FinalState){
            if(transition.isReadNext()){
                printPath(debug,prev,true,false);
            }else{
                printPath(debug,prev,true,true);
            }

            if(debug)Console.print(Console.ANSI_GREEN+" VALID \n");

            fda.onReadSequence((FinalState<T>) this,sequence);

            //end read sequence
            return new FDAData<T>(1,transition,this,prev);
        }
        printPath(debug,prev,false,false);

        //SELECTION OF NEXT CHARACTER
        T character;

        if(transition==null || transition.isReadNext()){
            if(fda.getIterator().hasNext()){
                character = (T)fda.getIterator().next();//read next character
            }else{
                Console.print(Console.ANSI_RED+"REACH END OF FILE BUT NOT REACHED FINAL STATE\n");
                return new FDAData<T>(-1,transition,this,prev);
            }

        }else{
            character = prev;
        }

        //Check available transition
        State<T>nextState = null;
        Transition<T>transitionUsed = null;

        for(Transition element :transitions){
            if(element.apply(character)){
                nextState = element.getToTransit();
                transitionUsed = element;
            }
        }
        if(nextState == null && other!=null){//Other element transition
            nextState = other.getToTransit();
            transitionUsed = other;
        }

        if(nextState == null){//If there is not available transition
            Console.print(Console.ANSI_RED+"NON AVAILABLE TRANSITION FOR ELEMENT "+Console.ANSI_PURPLE+"["+character+"]\n");
            return new FDAData<T>(-2,transition,this,character);
        }

        if(transitionUsed!=null){//If there is transition call semantic actions of transition
            if(transitionUsed.isWrite()){
                sequence.add(character);
            }
            transitionUsed.callActions(character,sequence);
        }
        // go to next  state of transition
        return nextState.feedForward(transitionUsed,fda, debug,character,sequence);



    }
    public void printPath(boolean debug,T prev,boolean isFinal,boolean ignoreRead){
        if(!debug) return;
        if(isFinal){
            if(ignoreRead){
               Console.print(Console.ANSI_CYAN+"("+prev+") "+Console.ANSI_BLUE+"IGNORE READ "+Console.ANSI_YELLOW+"<<"+name+">>"+Console.ANSI_PURPLE+" ==> ");
            }else{
               Console.print(Console.ANSI_CYAN+"("+prev+") "+Console.ANSI_YELLOW+"<<"+name+">>"+Console.ANSI_PURPLE+" ==> ");
            }

        }else{
            Console.print(Console.ANSI_CYAN+"("+prev+") "+Console.ANSI_YELLOW+name+Console.ANSI_PURPLE+" ==> ");
        }

    }



}
