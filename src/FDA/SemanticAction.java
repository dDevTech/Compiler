package FDA;

public abstract class SemanticAction<T> {
    public abstract void onAction(State<T>state,T element);
}
