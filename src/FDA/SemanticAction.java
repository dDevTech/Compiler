package FDA;

import java.util.List;

public abstract class SemanticAction<T> {
    public abstract void onAction(State<T>state, T element, List<T> sequence) throws ProcessorError;
}
