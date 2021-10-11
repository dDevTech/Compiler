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
    private FDAData<T>previousData;

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
        if(debug){
            Console.printLegend();
        }
        Console.printlnInfo("FDA","Executing");
        do {
            if(previousData==null){
                previousData = root.feedForward(null,this,debug,null,new ArrayList<>());
            }else{
                previousData = root.feedForward(previousData.getLastTransition(),this,debug,previousData.getLastElement(),new ArrayList<>());
            }

        }while((iterator.hasNext()||!previousData.getLastTransition().isReadNext())&& previousData.getInternalCode()>0);
        if(previousData.getInternalCode()<0){
            Console.printlnInfo("FDA",Console.ANSI_RED+"Internal code: "+previousData.getInternalCode());
        }else{
            Console.printlnInfo("FDA",Console.ANSI_GREEN+"Internal code: "+previousData.getInternalCode());
        }



    }
    public int executeNext(){



        if(previousData==null){
            previousData = root.feedForward(null,this,debug,null,new ArrayList<>());
        }else{
            if((iterator.hasNext()||!previousData.getLastTransition().isReadNext())&& previousData.getInternalCode()>0){
                previousData = root.feedForward(previousData.getLastTransition(),this,debug,previousData.getLastElement(),new ArrayList<>());
            }

        }


        if(previousData.getInternalCode()<0){
            Console.printlnInfo("FDA",Console.ANSI_RED+"Internal code: "+previousData.getInternalCode());
        }
        return previousData.getInternalCode();


    }

    public FileIterator getIterator() {
        return iterator;
    }

    public void setIterator(FileIterator iterator) {
        this.iterator = iterator;
    }
}
