package Analyzer.Sintactic;

import Tools.FileWrite;

public class JavascriptSyntax extends SintaxAnalyzer{
    public void setup(){

        super.setup();
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


        Production pP1 = new Production(B,P);
        Production pP2 = new Production(F,P);
        Production pP3 = new Production("eof");
        P.addProductions(pP1,pP2,pP3);

        Production pB1 = new Production("let",T,"id","puntoycoma");
        Production pB2 = new Production("if","abrePar",E,"cierraPar",S,Z);
        Production pB3 = new Production(S);

        B.addProductions(pB1,pB2,pB3);

        Production pT1 = new Production("int");
        Production pT2 = new Production("string");
        Production pT3 = new Production("boolean");
        T.addProductions(pT1,pT2,pT3);

        Production pS1 = new Production("id",S_);
        Production pS2 = new Production("print","abrePar",E,"cierraPar","puntoycoma");
        Production pS3 = new Production("input","abrePar",E,"cierraPar","puntoycoma");
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

        Production pE_1= new Production ("or",R,E_);
        E_.setLambda(true);
        E_.addProductions(pE_1);


        Production pR1= new Production (U,R_);
        R.addProductions(pR1);

        Production pR_1= new Production ("and",U,R_);
        R_.setLambda(true);
        R_.addProductions(pR_1);

        Production pU1= new Production (W,U_);
        U.addProductions(pU1);

        Production pU_1= new Production ("igual",W,U_);
        Production pU_2= new Production ("distinto",W,U_);
        U_.setLambda(true);
        U_.addProductions(pU_1,pU_2);


        Production pW1= new Production (V,W_);
        W.addProductions(pW1);

        Production pW_1= new Production ("suma",V,W_);
        Production pW_2= new Production ("resta",V,W_);
        W_.setLambda(true);
        W_.addProductions(pW_1,pW_2);



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

        Production pZ = new Production("else",S);
        Z.setLambda(true);
        Z.addProductions(pZ);

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
        LL1Collisions();
        first(new IntRef(1),true,pP1);
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
        execute(true);
    }
}
