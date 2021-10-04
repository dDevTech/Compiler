package FDA;

import Tools.Console;

import java.util.Iterator;

public abstract class FDA<T>{
    private State<T> root;

    private boolean debug = false;
    private Iterator<T> iterator;


    public abstract void onReadSequence(FinalState<T>finalState);
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



    public void execute(Iterator iterator){
        this.iterator = iterator;
        FDAData<T>data = null ;
        do {
            if(data==null){
                data = root.feedForward(null,this,debug,null);
            }else{
                data = root.feedForward(data.getLastTransition(),this,debug,data.getLastElement());
            }

        }while(iterator.hasNext()&& data.getInternalCode()>0);
        if(data.getInternalCode()<0){
            Console.printlnInfo("FDA",Console.ANSI_RED+"Internal code: "+data.getInternalCode());
        }else{
            Console.printlnInfo("FDA",Console.ANSI_GREEN+"Internal code: "+data.getInternalCode());
        }



    }

    public Iterator getIterator() {
        return iterator;
    }

    public void setIterator(Iterator<T> iterator) {
        this.iterator = iterator;
    }
}
