package FDATest;

import FDA.FDA;
import FDA.*;

import java.util.Scanner;

public class FDATest {
    static int cont =0;
    public static void main(String[]args){
        Scanner sc = new Scanner(System.in);
        String analyze = sc.nextLine();

        FDA<Character>fda = new FDA<Character>() {
            @Override
            public Character readNext() throws IndexOutOfBoundsException {
                if(cont==analyze.length()){
                    throw new IndexOutOfBoundsException("Reach limit");
                }
                Character c = analyze.charAt(cont); //cogemos siguiente caracter de la linea
                cont++;
                return c;
            }
        };
        //creamos nodos
        State<Character> initial = new State<Character>();

        State<Character> nodeGuion = new State<Character>("-");
        FinalState<Character> nodeMayor = new FinalState<Character>(">");
        nodeMayor.addSemanticAction(new SemanticAction() {
            @Override
            public void onAction(State state) {
                System.out.println("Hemos llegado al mayor");
            }
        });
        initial.addTransition('-',nodeGuion);
        nodeGuion.addTransition('>',nodeMayor);

        fda.setRoot(initial);


        fda.execute();
         //crea afd
    }
}
