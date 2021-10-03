package FDA;

import Tools.Console;

public abstract class FDA<T>{
    private State<T> root;

    private boolean debug = false;
    public abstract T readNext() throws IndexOutOfBoundsException;
    public abstract boolean hasNext();
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
    public void execute(){
        FDAData<T>data = null ;
        do {
            if(data==null){
                data = root.feedForward(null,this,debug,null);
            }else{
                data = root.feedForward(data.getLastTransition(),this,debug,data.getLastElement());
            }

        }while(hasNext()&& data.getInternalCode()>0);
        if(data.getInternalCode()<0){
            Console.printlnInfo("FDA",Console.ANSI_RED+"Internal code: "+data.getInternalCode());
        }else{
            Console.printlnInfo("FDA",Console.ANSI_GREEN+"Internal code: "+data.getInternalCode());
        }



    }
}
