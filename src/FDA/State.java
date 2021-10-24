package FDA;


import Tools.Console;


import java.util.*;
import java.util.function.Function;

public class State<T> {

    private ArrayList<Transition<T>> transitions = new ArrayList<>();
    private Transition<T> other = null;
    private String name = "Node";
    private String noTransitionError = "NOT AVAILABLE TRANSITION FOR ELEMENT ";


    public State(String name) {
        this.name = name;
    }

    public State() {
    }

    public String getNoTransitionError() {
        return noTransitionError;
    }

    public void setNoTransitionError(String noTransitionError) {
        this.noTransitionError = noTransitionError;
    }

    public Transition<T> addTransition(T transitionValue, State<T> toTransit, boolean ignoreRead, boolean write) {
        checkAddTransition(toTransit, ignoreRead);

        Transition<T> transition = new Transition<T>(transitionValue, toTransit, ignoreRead, write);
        transitions.add(transition);
        return transition;
    }

    public Transition<T> addTransitionFunction(Function<T, Boolean> transitionFunction, State<T> toTransit, boolean ignoreRead, boolean write) {
        checkAddTransition(toTransit, ignoreRead);

        Transition<T> transition = new Transition<T>(transitionFunction, toTransit, ignoreRead, write);
        transitions.add(transition);
        return transition;

    }

    public Transition<T> addOtherElementTransitionFunction(State<T> toTransit, boolean ignoreRead, boolean write) {
        checkAddTransition(toTransit, ignoreRead);

        other = new Transition<T>(this::otherFunction, toTransit, ignoreRead, write);
        return other;
    }

    private void checkAddTransition(State<T> toTransit, boolean ignoreRead) {
        if (toTransit == null) {
            throw new IllegalArgumentException("Transitioned node must be not null");
        }
        if (!(toTransit instanceof FinalState) && ignoreRead) {
            throw new IllegalArgumentException("Ignore read can only be used in FinalStates");
        }
    }

    private boolean otherFunction(T next) {

        for (Transition<T> transition : transitions) {
            if (transition.apply(next)) {
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

    protected FDAData<T> feedForward(FDA<T> fda, Transition<T> transition, List<T> sequence, T prev, boolean debug) {
        //Check first if current node is final
        if (this instanceof FinalState) {
            if (prev != null) {
                printPath(debug, prev.toString(), true, !transition.isReadNext(), transition.isWrite());
            }
            fda.printOnFinish(debug, true, prev.toString());
            fda.onReadSequence((FinalState<T>) this, sequence);

            //end read sequence
            return new FDAData<T>(1, transition, this, prev);
        }
        if (prev != null) {
            if (transition != null) {
                printPath(debug, prev.toString(), false, false, transition.isWrite());
            } else {
                printPath(debug, prev.toString(), false, false, false);
            }
        }else{
            printPath(debug, "", false, false, false);
        }


        //SELECTION OF NEXT CHARACTER
        T character;

        if (transition == null || transition.isReadNext()) {
            if (fda.getIterator().hasNext()) {
                character = (T) fda.getIterator().next();//read next character
            } else {

                FDAException exception = new FDAException(-1, "REACH END OF ITERATOR BUT NOT REACHED FINAL STATE");
                boolean notfinish = fda.onError(exception);
                int internalCode = notfinish?1:-1;
                return new FDAData<T>(internalCode, transition, this, prev);
            }
        } else {
            character = prev;
        }

        //Check available transition
        State<T> nextState = null;
        Transition<T> transitionUsed = null;

        for (Transition<T> element : transitions) {
            if (element.apply(character)) {
                nextState = element.getToTransit();
                transitionUsed = element;
            }
        }
        if (nextState == null && other != null) {//Other element transition
            nextState = other.getToTransit();
            transitionUsed = other;
        }

        if (nextState == null) {//If there is no available transition
            FDAException exception = new FDAException(-2, noTransitionError);

            boolean notfinish = fda.onError(exception);
            int internalCode = notfinish?1:-1;
            return new FDAData<T>(internalCode, null, this, fda.getIterator().getCurrentElement());
        }

        if (transitionUsed != null) {//If there is transition call semantic actions of transition
            if (transitionUsed.isWrite()) {
                sequence.add(character);
            }
            try {
                transitionUsed.callActions(character, sequence, fda.getIterator());
            } catch (FDAException e) {
                boolean notfinish = fda.onError(e);
                int internalCode = notfinish?1:-1;
                return new FDAData<T>(internalCode, transition, this, fda.getIterator().getCurrentElement());
            }

        }
        // go to next  state of transition
        return nextState.feedForward(fda, transitionUsed, sequence, character, debug);
    }

    private void printPath(boolean debug, String prev, boolean isFinal, boolean ignoreRead, boolean write) {
        if (!debug) return;
        String modifiers = "";
        prev = prev.replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");

        if (ignoreRead) {
            modifiers += Console.ANSI_BLUE + "[IGNORE READ]";
        }
        if (write) {
            modifiers += Console.ANSI_GREEN + "[W] ";
        }
        if (isFinal) {
            Console.print(Console.ANSI_CYAN + "(" + prev + ") " + modifiers + Console.ANSI_YELLOW + "<<" + name + ">>" + Console.ANSI_PURPLE + " ==> ");
        } else {
            Console.print(Console.ANSI_CYAN + "(" + prev + ") " + modifiers + Console.ANSI_YELLOW + name + Console.ANSI_PURPLE + " ==> ");
        }

    }


}
