package Javascript;

import Analyzer.Semantic.RuleData;
import Analyzer.Semantic.SemanticAction;
import Analyzer.Semantic.Type;
import Analyzer.Sintactic.Grammar.*;
import Analyzer.Sintactic.SintaxAnalyzer;
import Analyzer.SymbolTable.SymbolTableHandler;
import Tools.FileWrite;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.List;

public class JavascriptSyntax extends SintaxAnalyzer {
    private JavascriptLexical lexical;

    public JavascriptSyntax(JavascriptLexical lexical ){

        this.lexical = lexical;
    }

    public void setup(){

        super.setup(lexical);
        Rule P = new Rule("P");
        Rule B= new Rule("B");
        Rule T = new Rule("T");
        Rule S = new Rule("S");
        Rule S_ = new Rule("S'");
        Rule X = new Rule("X");
        Rule C = new Rule("C");
        Rule L = new Rule("L");
        Rule Q = new Rule("Q");
        Rule F = new Rule("F");
        Rule H = new Rule("H");
        Rule A = new Rule("A");
        Rule K = new Rule("K");
        Rule E = new Rule("E");
        Rule E_ = new Rule("E'");
        Rule R = new Rule("R");
        Rule R_ = new Rule("R'");
        Rule U = new Rule("U");
        Rule U_ = new Rule("U'");
        Rule W = new Rule("W");
        Rule W_ = new Rule("W'");
        Rule V = new Rule("V");
        Rule V_ = new Rule("V'");

        Rule Z= new Rule("Z");
        Rule Z_= new Rule("Z'");

        addRule(P);
        addRule(B);
        addRule(T);
        addRule(S);
        addRule(S_);
        addRule(X);
        addRule(C);
        addRule(L);
        addRule(Q);
        addRule(F);
        addRule(H);
        addRule(A);
        addRule(K);
        addRule(E);
        addRule(E_);
        addRule(R);
        addRule(R_);
        addRule(U);
        addRule(U_);
        addRule(W);
        addRule(W_);
        addRule(V);
        addRule(V_);
        addRule(Z);
        addRule(Z_);



        Production pP1 = new Production(B,P);
        Production pP2 = new Production(F,P);
        Production pP3 = new Production("eof");
        P.addProductions(pP1,pP2,pP3);


        SemanticAction actionpPB1 = new SemanticAction("T.id") {
            @Override
            public List<RuleData> apply(Multimap<String, Object> params, SymbolTableHandler handler) {

                System.out.println(this.getAttribute(params,"B","id"));
                return new ArrayList<>();
            }
        };
        Production pB1 = new Production("let",T,actionpPB1,"id","puntoycoma");
        Production pB2 = new Production("if","abrePar",E,"cierraPar",Z);
        Production pB3 = new Production(S);

        B.addProductions(pB1,pB2,pB3);


        SemanticAction actionpT1 = new SemanticAction("T.id = int") {
            @Override
            public List<RuleData> apply(Multimap<String, Object> params, SymbolTableHandler handler) {
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData();
                ruleSet.addAttribute("id",Type.INTEGER);
                data.add(ruleSet);
                return data;
            }
        };
        Production pT1 = new Production("int",actionpT1);
        Production pT2 = new Production("string");
        Production pT3 = new Production("boolean");
        T.addProductions(pT1,pT2,pT3);

        Production pS1 = new Production("id",S_);
        Production pS2 = new Production("print","abrePar",E,"cierraPar","puntoycoma");
        Production pS3 = new Production("input","abrePar","id","cierraPar","puntoycoma");
        Production pS4 = new Production("return",X,"puntoycoma");
        S.addProductions(pS1, pS2,pS3,pS4);

        Production pS_1 = new Production("asig",E,"puntoycoma");
        Production pS_2 = new Production("abrePar",L,"cierraPar","puntoycoma");
        Production pS_3 = new Production("menosigual",E,"puntoycoma");
        S_.addProductions(pS_1, pS_2,pS_3);

        Production pX1 = new Production(E);
        X.setLambda(true);
        X.addProductions(pX1);

        Production pC1 = new Production(B, C);
        C.setLambda(true);
        C.addProductions(pC1);

        Production pL1 = new Production(E, Q);
        L.setLambda(true);
        L.addProductions(pL1);

        Production pQ1 = new Production("coma", E, Q);
        Q.setLambda(true);
        Q.addProductions(pQ1);

        Production pF1 = new Production("function", "id", H, "abrePar", A, "cierraPar", "abreLlave", C, "cierraLlave");
        F.addProductions(pF1);

        Production pH1 = new Production(T);
        H.setLambda(true);
        H.addProductions(pH1);

        Production pA1 = new Production(T, "id", K);
        A.setLambda(true);
        A.addProductions(pA1);

        Production pK1= new Production ("coma", T, "id", K);
        K.setLambda(true);
        K.addProductions(pK1);

        Production pE1= new Production (R,E_);
        E.addProductions(pE1);

        Production pE_1= new Production ("suma",R,E_);
        Production pE_2= new Production ("resta",R,E_);
        E_.setLambda(true);
        E_.addProductions(pE_1,pE_2);


        Production pR1= new Production (U,R_);
        R.addProductions(pR1);

        Production pR_1= new Production ("igual",U,R_);
        Production pR_2= new Production ("distinto",U,R_);
        R_.setLambda(true);
        R_.addProductions(pR_1,pR_2);

        Production pU1= new Production (W,U_);
        U.addProductions(pU1);

        Production pU_1= new Production ("and",W,U_);

        U_.setLambda(true);
        U_.addProductions(pU_1);


        Production pW1= new Production (V,W_);
        W.addProductions(pW1);

        Production pW_1= new Production ("or",V,W_);

        W_.setLambda(true);
        W_.addProductions(pW_1);




        Production pV1= new Production ("id" ,V_);
        Production pV2= new Production ("abrePar",E,"cierraPar");
        Production pV4= new Production ("constanteEntera");
        Production pV5= new Production ("cadena");
        Production pV6= new Production ("true");
        Production pV7= new Production ("false");
        V.addProductions(pV1,pV2,pV4,pV5,pV6,pV7);

        Production pV_1 = new Production("abrePar",L,"cierraPar");
        V_.setLambda(true);
        V_.addProductions(pV_1);

        Production pZ = new Production(S);
        Production pZ2 = new Production("abreLlave",C,"cierraLlave",Z_);

        Z.addProductions(pZ,pZ2);

        Production pZ_1 = new Production("else","abreLlave",C,"cierraLlave");
        Z_.setLambda(true);
        Z_.addProductions(pZ_1);

        setInitialRule(P);
        print(true);
        assignIds();
        /*for(int i =0;i<getRules().size();i++){
            for(int j =0;j<getRules().get(i).getProductions().size();j++){
                System.out.println(first(new IntRef(0),false,getRules().get(i).getProductions().get(j)));
            }

        }*/
        /*for(int i =0;i<getRules().size();i++){
            System.out.println(first(new IntRef(0),false,getRules().get(i)));
        }*/

        //  System.out.println(first(true,B));
        /*Map.Entry<Object,Object> s ;
        while((s=ana.readToken())!=null){
            System.out.println(s);
        }
        */
        setDebug(true);
        setContinueOnError(false);
        LL1Collisions();

        FileWrite write = new FileWrite("files/productions");

        for(Rule rule : getRules()){
            for(Production prod: rule.getProductions()){
                write.writer().print(rule.toString().replace("'","_1")+" -> ");
                for(Object o : prod.getElements()){

                    write.writer().print(o.toString().replace("'","_1")+" ");
                }
                write.writer().println();
            }
            if(rule.isLambda()){
                write.writer().println(rule.toString().replace("'","_1")+" -> "+" lambda");
            }

        }
        write.writer().flush();
        write.writer().close();


    }


}
