package FDA;

public abstract class FDA<T>{
    private State<T> root;
    private boolean debug = false;
    public abstract T readNext() throws IndexOutOfBoundsException;
    public State<T> getRoot() {
        return root;
    }

    public void setRoot(State<T> root) {
        this.root = root;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    public void execute(){
        root.checkTransitions(this,readNext(),debug);
    }
}
