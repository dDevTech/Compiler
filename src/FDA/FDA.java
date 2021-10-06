package FDA;

import Tools.Console;
import Tools.FileIterator;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class FDA<T>{
    private State<T> root;

    private boolean debug = false;
    private FileIterator iterator;


    public abstract void onReadSequence(FinalState<T>finalState, List<T> readSequence);
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



    public void execute(FileIterator iterator){
        this.iterator = iterator;
        FDAData<T>data = null ;
        Console.printlnInfo("FDA","Executing");
        do {
            if(data==null){
                data = root.feedForward(null,this,debug,null,new ArrayList<>());
            }else{
                data = root.feedForward(data.getLastTransition(),this,debug,data.getLastElement(),new ArrayList<>());
            }

        }while((iterator.hasNext()||!data.getLastTransition().isReadNext())&& data.getInternalCode()>0);
        if(data.getInternalCode()<0){
            Console.printlnInfo("FDA",Console.ANSI_RED+"Internal code: "+data.getInternalCode());
        }else{
            Console.printlnInfo("FDA",Console.ANSI_GREEN+"Internal code: "+data.getInternalCode());
        }



    }

    public FileIterator getIterator() {
        return iterator;
    }

    public void setIterator(FileIterator iterator) {
        this.iterator = iterator;
    }
}
