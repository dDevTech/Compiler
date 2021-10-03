package FDA;

public class FDAData<T> {
    private int internalCode = 0;
    private Transition<T>lastTransition;
    private State<T>lastState;
    private T lastElement;

    public FDAData(int internalCode, Transition<T> lastTransition, State<T> lastState, T lastElement) {
        this.internalCode = internalCode;
        this.lastTransition = lastTransition;
        this.lastState = lastState;
        this.lastElement = lastElement;
    }

    public int getInternalCode() {
        return internalCode;
    }

    public void setInternalCode(int internalCode) {
        this.internalCode = internalCode;
    }

    public Transition<T> getLastTransition() {
        return lastTransition;
    }

    public void setLastTransition(Transition<T> lastTransition) {
        this.lastTransition = lastTransition;
    }

    public State<T> getLastState() {
        return lastState;
    }

    public void setLastState(State<T> lastState) {
        this.lastState = lastState;
    }

    public T getLastElement() {
        return lastElement;
    }

    public void setLastElement(T lastElement) {
        this.lastElement = lastElement;
    }
}
