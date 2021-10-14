package FDA;

import Common.ErrorHandler;
import Tools.Console;

import Tools.FileIterator;


import java.util.ArrayList;
import java.util.List;


public abstract class FDA<T>{
    private State<T> root;
    private boolean debug = false;
    private FileIterator<T> iterator;
    private FDAData<T>previousData;
    private boolean continueOnError = true;
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



    public void execute(FileIterator<T> iterator){
        this.iterator = iterator;
        if(debug){
            Console.printLegend();
        }
        Console.printlnInfo("FDA","Executing");
        do{}while(executeNext());
    }
    public boolean executeNext(){
        if(previousData==null){
            if(iterator.hasNext()){
                previousData = root.feedForward(this,null,new ArrayList<>(),null,debug);
            }else{
                return false;
            }
        }else{
            if((iterator.hasNext()||!previousData.getLastTransition().isReadNext())&& previousData.getInternalCode()>0){
                previousData = root.feedForward(this,previousData.getLastTransition(),new ArrayList<>(),previousData.getLastElement(),debug);
            }else{
                return false;
            }

        }
        return true;


    }
    protected boolean onError(FDAException exception){
        printOnFinish(debug,false,iterator.getCurrentElement().toString());
        ErrorHandler.showLexicError(getIterator(),exception);
        if(continueOnError){
            try {
                getIterator().skipLine();
                return true;
            }catch(Exception e){
                return false;
            }

        }else{
            return false;
        }
    }
    protected void printOnFinish(boolean debug, boolean valid, String value){
        if(debug){
            if(valid){
                Console.print(Console.ANSI_GREEN+"VALID\n");
            }else{
                Console.printCharacter(true,value);
                Console.print(Console.ANSI_RED+"ERROR\n");
            }
        }
    }
    public FileIterator<T> getIterator() {
        return iterator;
    }

    public void setIterator(FileIterator<T> iterator) {
        this.iterator = iterator;
    }

    public boolean isContinueOnError() {
        return continueOnError;
    }

    public void setContinueOnError(boolean continueOnError) {
        this.continueOnError = continueOnError;
    }
}
