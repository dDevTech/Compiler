package Analyzer.Sintactic;

public class OtherSintax extends SintaxAnalyzer{
    public OtherSintax(){
        Rule S = new Rule("S");
        Rule A = new Rule("A");
        Rule B = new Rule("B");
        Rule C = new Rule("C");
        Rule D = new Rule("D");
        Rule E = new Rule("E");
        Rule F = new Rule("F");

        Production p1 = new Production(E,A,B,"g");
        Production p2 = new Production(D,B,"a");

        S.addProductions(p1,p2);

        Production p3 = new Production(A,"a");
        Production p4 = new Production("a");

        A.addProductions(p3,p4);

        Production p5 = new Production("b",B);
        B.setLambda(true);
        B.addProductions(p5);

        Production p6 = new Production(E,F);
        C.addProductions(p6);

        Production p7 = new Production("k",D);
        Production p8 = new Production(E);
        D.addProductions(p7,p8);

        Production p9 = new Production(E,"q");
        Production p10 = new Production("h");
        E.setLambda(true);
        E.addProductions(p9,p10);

        Production p11 = new Production("i","j",F);
        F.setLambda(true);
        F.addProductions(p11);

        addRule(S);
        addRule(A);
        addRule(B);
        addRule(C);
        addRule(D);
        addRule(E);
        addRule(F);
        setInitialRule(S);

        setup();
        print(true);
        LL1Collisions();

        for(Rule rule:getRules()){
            System.out.println(rule);
            System.out.println(first(new IntRef(0),true, rule));
        }

    }
}
