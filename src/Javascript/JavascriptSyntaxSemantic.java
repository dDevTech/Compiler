package Javascript;

import Analyzer.Semantic.RuleData;
import Analyzer.Semantic.SemanticAction;
import Analyzer.Sintactic.Grammar.*;
import Analyzer.SymbolTable.SymbolEntry;
import Analyzer.SymbolTable.SymbolTableHandler;
import Analyzer.SymbolTable.Type;
import Common.ErrorHandler;
import FDA.FDAException;
import Tools.FileWrite;
import Tools.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JavascriptSyntaxSemantic extends SintaxSemanticAnalyzer {
    private JavascriptLexical lexical;

    public JavascriptSyntaxSemantic(JavascriptLexical lexical ){

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

        SemanticAction actionDeclTrue = new SemanticAction("zoneDeclTrue") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                handler.setZoneDeclaration(true);

                return new ArrayList<>();
            }
        };
        SemanticAction actionDeclFalse = new SemanticAction("zoneDeclFalse") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                handler.setZoneDeclaration(false);

                return new ArrayList<>();
            }
        };
        SemanticAction actionEnd= new SemanticAction("end") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                handler.removeLast();
                return new ArrayList<>();
            }
        };

        Production pP1 = new Production(B,P);
        Production pP2 = new Production(F,P);
        Production pP3 = new Production("eof",actionEnd);
        P.addProductions(pP1,pP2,pP3);


        SemanticAction actionpPB1 = new SemanticAction("insertaTipoTS;B.type= T.type") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                SymbolEntry entry = handler.find((int)params.get("id"));//lexema
                RuleData ruleT = ((RuleData)params.get("T"));
                entry.setType(ruleT.getType());

                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("type", Type.OK_TYPE);
                data.add(ruleSet);
                return data;

            }
        };
        Production pB1 = new Production(actionDeclTrue,"let",T,"id",actionDeclFalse,"puntoycoma",actionpPB1);
        SemanticAction actionpPB2 = new SemanticAction("check if") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData ruleE = ((RuleData)params.get("E"));
                Type type = ruleE.getType();
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                if(type.equals(Type.BOOLEAN)){
                    ruleSet.addAttribute("type", Type.OK_TYPE);
                }else{
                    ruleSet.addAttribute("type", Type.ERROR_TYPE);
                }
                data.add(ruleSet);
                return data;
            }
        };
        Production pB2 = new Production("if","abrePar",E,"cierraPar",Z,actionpPB2);

        SemanticAction actionpPB3 = new SemanticAction("B=S") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData ruleS = ((RuleData)params.get("S"));

                Type type = ruleS.getType();
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("type", type);
                if(ruleS.get("returnType")!=null){
                    ruleSet.addAttribute("returnType", ruleS.get("returnType").getContent());
                }
                data.add(ruleSet);
                return data;
            }
        };
        Production pB3 = new Production(S,actionpPB3);

        B.addProductions(pB1,pB2,pB3);


        SemanticAction actionpT1 = new SemanticAction("T.type = int") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {

                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("type", Type.INTEGER);
                data.add(ruleSet);

                return data;
            }
        };
        Production pT1 = new Production("int",actionpT1);
        SemanticAction actionpT2 = new SemanticAction("T.type = string") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {

                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("type", Type.STRING);
                data.add(ruleSet);

                return data;
            }
        };
        Production pT2 = new Production("string",actionpT2);
        SemanticAction actionpT3 = new SemanticAction("T.type = boolean") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {

                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("type", Type.BOOLEAN);
                data.add(ruleSet);

                return data;
            }
        };
        Production pT3 = new Production("boolean",actionpT3);
        T.addProductions(pT1,pT2,pT3);


        SemanticAction actionpPS1 = new SemanticAction("S.type = if(buscaTipo concide) FUNCTION CHECK PARAMS") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData ruleS_1 = ((RuleData)params.get("S'"));
                SymbolEntry entry = handler.find((int)params.get("id"));//lexema


                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                if(entry.getType().equals(Type.FUNCTION)){
                    if(ruleS_1.get("list")!=null) {
                        Object o = ruleS_1.get("list").getContent();
                        if (o instanceof ArrayList<?>) {
                            if (Tools.compareArrays(entry.getTypesParamters(), (ArrayList<Type>) o)) {
                                ruleSet.addAttribute("type", Type.OK_TYPE);
                            } else {
                                ruleSet.addAttribute("type", Type.ERROR_TYPE);
                                ErrorHandler.showSemanticError(lexicAnalyzer.getIterator(), new FDAException(-1, "Params of function must be the same type as function parameters"));
                            }
                        } else {
                            ErrorHandler.showSemanticError(lexicAnalyzer.getIterator(), new FDAException(-1, "Function must be called"));
                            ruleSet.addAttribute("type", Type.ERROR_TYPE);
                        }
                    }else{
                        ErrorHandler.showSemanticError(lexicAnalyzer.getIterator(), new FDAException(-1, "Function can only be called"));
                        ruleSet.addAttribute("type", Type.ERROR_TYPE);
                    }
                }else{
                    if(ruleS_1.getType().equals(entry.getType())){
                        ruleSet.addAttribute("type", Type.OK_TYPE);
                    }else{
                        ErrorHandler.showSemanticError(lexicAnalyzer.getIterator(),new FDAException(-1,"Variable id must be the same type as other statement"));
                        ruleSet.addAttribute("type", Type.ERROR_TYPE);

                    }
                }



                data.add(ruleSet);

                return data;
            }
        };
        Production pS1 = new Production("id",S_,actionpPS1);

        SemanticAction actionpPS2 = new SemanticAction("string or integer") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData ruleE = ((RuleData)params.get("E"));
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                if(ruleE.getType().equals(Type.INTEGER) || ruleE.getType().equals(Type.STRING)){
                    ruleSet.addAttribute("type", Type.OK_TYPE);
                }else{
                    ErrorHandler.showSemanticError(lexicAnalyzer.getIterator(),new FDAException(-1,"Print statement must be of string or integer"));
                    ruleSet.addAttribute("type", Type.ERROR_TYPE);

                }


                data.add(ruleSet);

                return data;
            }
        };
        Production pS2 = new Production("print","abrePar",E,"cierraPar","puntoycoma",actionpPS2);

        SemanticAction actionpPS3 = new SemanticAction("string or integer") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                SymbolEntry entry = handler.find((int)params.get("id"));//lexema

                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                if(entry.getType().equals(Type.INTEGER) || entry.getType().equals(Type.STRING)){
                    ruleSet.addAttribute("type", Type.OK_TYPE);
                }else{
                    ErrorHandler.showSemanticError(lexicAnalyzer.getIterator(),new FDAException(-1,"Input statement must be of string or integer"));
                    ruleSet.addAttribute("type", Type.ERROR_TYPE);

                }


                data.add(ruleSet);

                return data;
            }
        };
        Production pS3 = new Production("input","abrePar","id","cierraPar","puntoycoma",actionpPS3);

        SemanticAction actionPS_4 = new SemanticAction("S.type =  X.type") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData ruleData = (RuleData) params.get("X");
                Type type = (Type)ruleData.get("returnType").getContent();
                List<RuleData>returns = new ArrayList<>();
                RuleData data = new RuleData("ret");
                data.addAttribute("returnType", type);
                data.addAttribute("type", Type.OK_TYPE);
                returns.add(data);
                return returns;
            }
        };
        Production pS4 = new Production("return",X,"puntoycoma",actionPS_4);
        S.addProductions(pS1, pS2,pS3,pS4);




        SemanticAction actionPS_1 = new SemanticAction("S'.type =  E.type") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData ruleData = (RuleData) params.get("E");
                Type type = ruleData.getType();
                List<RuleData>returns = new ArrayList<>();
                RuleData data = new RuleData("ret");
                data.addAttribute("type", type);
                returns.add(data);
                return returns;
            }
        };
        Production pS_1 = new Production("asig",E,"puntoycoma",actionPS_1);


        SemanticAction actionpPS_2 = new SemanticAction("S'.list = L.tipo") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData rule = (RuleData)params.get("L");

                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("list", rule.get("list").getContent());
                data.add(ruleSet);
                return data;
            }
        };
        Production pS_2 = new Production("abrePar",L,"cierraPar","puntoycoma",actionpPS_2);

        SemanticAction actionPS_3 = new SemanticAction("E.type = int then E.type else tipoerror") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData ruleData = (RuleData) params.get("E");
                Type type = ruleData.getType();
                List<RuleData>returns = new ArrayList<>();
                RuleData data = new RuleData("ret");
                if(type.equals(Type.INTEGER)){
                    data.addAttribute("type", Type.INTEGER);
                }else{
                    ErrorHandler.showSemanticError(lexicAnalyzer.getIterator(),new FDAException(-1,"-= must be an integer"));
                    data.addAttribute("type", Type.ERROR_TYPE);
                }
                returns.add(data);
                return returns;
            }
        };
        Production pS_3 = new Production("menosigual",E,"puntoycoma",actionPS_3);
        S_.addProductions(pS_1, pS_2,pS_3);


        SemanticAction actionpPX = new SemanticAction("X.returnType = E.tipo") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData ruleData = (RuleData) params.get("E");
                Type type = ruleData.getType();

                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("returnType",type);
                data.add(ruleSet);
                return data;
            }
        };
        Production pX1 = new Production(E,actionpPX);
        X.setLambda(true);
        X.setLambdaAction(new SemanticAction("X.returnType = empty") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {

                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("type",Type.EMPTY);
                ruleSet.addAttribute("returnType",Type.EMPTY);
                data.add(ruleSet);
                return data;
            }
        });
        X.addProductions(pX1);


        SemanticAction actionC = new SemanticAction("B typo ok y C tipo ok") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData ruleB = (RuleData)params.get("B");
                RuleData ruleC = (RuleData)params.get("C");
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ArrayList<Type>returnTypes=((ArrayList<Type>)ruleC.get("returnList").getContent());;
                if(ruleB.get("returnType")!=null){

                    returnTypes.add((Type)ruleB.get("returnType").getContent());
                }
                if(ruleB.getType().equals(Type.OK_TYPE)&&ruleC.getType().equals(Type.OK_TYPE)){
                    ruleSet.addAttribute("type",Type.OK_TYPE);
                    ruleSet.addAttribute("returnList",returnTypes);
                }else{
                    ErrorHandler.showSemanticError(lexicAnalyzer.getIterator(),new FDAException(-1,"all statements must be ok"));
                    ruleSet.addAttribute("type",Type.ERROR_TYPE);
                    ruleSet.addAttribute("returnList",returnTypes);
                }



                data.add(ruleSet);
                return data;

            }
        };

        Production pC1 = new Production(B, C,actionC);

        C.setLambda(true);
        C.setLambdaAction(new SemanticAction("C.type = tipook") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("type",Type.OK_TYPE);
                ruleSet.addAttribute("returnList",new ArrayList<>());
                data.add(ruleSet);
                return data;
            }
        });


        C.addProductions(pC1);

        SemanticAction actionPL1 = new SemanticAction("add to list of E type and sytentize") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData ruleE= (RuleData)params.get("E");
                RuleData ruleQ = (RuleData)params.get("Q");

                ArrayList<Type>types = (ArrayList<Type>) ruleQ.get("list").getContent();
                types.add(ruleE.getType());


                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("list",types);
                data.add(ruleSet);
                return data;
            }
        };
        Production pL1 = new Production(E, Q,actionPL1);
        L.setLambda(true);
        L.setLambdaAction(new SemanticAction("L.type = empty") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("list",new ArrayList<>());
                data.add(ruleSet);
                return data;
            }
        });
        L.addProductions(pL1);


        SemanticAction actionPQ1 = new SemanticAction("add to list of E type and sytentize") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData ruleE= (RuleData)params.get("E");
                RuleData ruleQ = (RuleData)params.get("Q");

                ArrayList<Type>types = (ArrayList<Type>) ruleQ.get("list").getContent();
                types.add(ruleE.getType());


                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("list",types);
                data.add(ruleSet);
                return data;
            }
        };
        Production pQ1 = new Production("coma", E, Q,actionPQ1);
        Q.setLambda(true);
        Q.setLambdaAction(new SemanticAction("Q.type = empty") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("list",new ArrayList<>());
                data.add(ruleSet);
                return data;
            }
        });
        Q.addProductions(pQ1);


        SemanticAction actionPF10 = new SemanticAction("zone decl true") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                handler.setZoneDeclaration(true);
                List<RuleData>data =new ArrayList<>();


                return data;
            }
        };
        SemanticAction actionPF11 = new SemanticAction("set return type of H") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                SymbolEntry entry = handler.find((int)params.get("id"));//lexema
                RuleData ruleH = (RuleData)params.get("H");
                handler.createTable();

                entry.setReturnType(ruleH.getType());
                List<RuleData>data =new ArrayList<>();


                return data;
            }
        };
        SemanticAction actionPF12 = new SemanticAction("add type parameters ZONE DECL= FALSE") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                SymbolEntry entry = handler.find((int)params.get("id"));//lexema
                entry.setupAsFunction(entry.getLexeme());
                RuleData ruleA = (RuleData)params.get("A");
                ArrayList<Type>types = (ArrayList<Type>) ruleA.get("list").getContent();
                entry.setParameters(types);
                List<RuleData>data =new ArrayList<>();
                handler.setZoneDeclaration(false);

                return data;
            }
        };
        SemanticAction actionPF13 = new SemanticAction("F.type = C.type ") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                List<RuleData>data =new ArrayList<>();
                RuleData ruleC = (RuleData)params.get("C");
                RuleData ruleSet = new RuleData("ret");
                SymbolEntry entry = handler.find((int)params.get("id"));
                for(Type type :(ArrayList<Type>)ruleC.get("returnList").getContent()){
                    if(!entry.getReturnType().equals(type)) ErrorHandler.showSemanticError(lexicAnalyzer.getIterator(),new FDAException(-5,"Return types in function "+entry.getLexeme()+" must be of type " +entry.getReturnType()));
                }
                ruleSet.addAttribute("type",ruleC.getType());

                data.add(ruleSet);
                handler.removeLast();

                return data;

            }
        };
        Production pF1 = new Production(actionPF10,"function", "id", H, actionPF11,"abrePar", A,actionPF12, "cierraPar", "abreLlave", C, "cierraLlave",actionPF13);
        F.addProductions(pF1);


        SemanticAction actionPH1 = new SemanticAction("H.type = T.type") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData ruleT = (RuleData)params.get("T");
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("type",ruleT.getType());
                data.add(ruleSet);
                return data;
            }
        };
        Production pH1 = new Production(T,actionPH1);
        H.setLambda(true);
        H.setLambdaAction(new SemanticAction("H.type = empty") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("type",Type.EMPTY);
                data.add(ruleSet);
                return data;
            }
        });
        H.addProductions(pH1);


        SemanticAction actionPA1 = new SemanticAction("add to list of K type and sytentize") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData ruleT = (RuleData)params.get("T");
                RuleData ruleK = (RuleData)params.get("K");
                SymbolEntry entry = handler.find((int)params.get("id"));
                ArrayList<Type>types = (ArrayList<Type>) ruleK.get("list").getContent();
                types.add(ruleT.getType());
                entry.setType(ruleT.getType());

                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("list",types);
                data.add(ruleSet);
                return data;
            }
        };
        Production pA1 = new Production(T, "id", K,actionPA1);
        A.setLambda(true);
        A.setLambdaAction(new SemanticAction("A.type = empty") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("type",Type.EMPTY);

                ruleSet.addAttribute("list",new ArrayList<>());
                data.add(ruleSet);
                return data;
            }
        });
        A.addProductions(pA1);

        SemanticAction actionPK1 = new SemanticAction("add elements") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData ruleT = (RuleData)params.get("T");
                RuleData ruleK = (RuleData)params.get("K");
                SymbolEntry entry = handler.find((int)params.get("id"));
                ArrayList<Type>types = (ArrayList<Type>) ruleK.get("list").getContent();
                types.add(ruleT.getType());
                entry.setType(ruleT.getType());

                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("list",types);
                data.add(ruleSet);
                return data;
            }
        };
        Production pK1= new Production ("coma", T, "id", K,actionPK1);


        K.setLambda(true);
        K.setLambdaAction(new SemanticAction("K.type = empty") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("list",new ArrayList<Type>());
                data.add(ruleSet);
                return data;
            }
        });
        K.addProductions(pK1);


        SemanticAction actionpPE = new SemanticAction("R E'") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData ruleV = (RuleData)params.get("R");
                RuleData ruleW_1 = (RuleData)params.get("E'");
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                if(ruleW_1.getType().equals(Type.EMPTY)){
                    ruleSet.addAttribute("type",  ruleV.getType());
                }else if(ruleW_1.getType().equals(ruleV.getType())){
                    ruleSet.addAttribute("type",  ruleV.getType());
                }else{
                    ErrorHandler.showSemanticError(lexicAnalyzer.getIterator(),new FDAException(-1,"sum must be of the same types"));
                    ruleSet.addAttribute("type",  Type.ERROR_TYPE);
                }


                data.add(ruleSet);
                return data;
            }
        };
        Production pE1= new Production (R,E_,actionpPE);
        E.addProductions(pE1);

        SemanticAction actionpPE_1 = new SemanticAction("+ R E'") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData ruleV = (RuleData)params.get("R");
                RuleData ruleW_1 = (RuleData)params.get("E'");
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                if(ruleW_1.getType().equals(Type.EMPTY)){
                    ruleSet.addAttribute("type",  ruleV.getType());
                }else if(ruleW_1.getType().equals(ruleV.getType())&&ruleW_1.getType().equals(Type.INTEGER)){

                    ruleSet.addAttribute("type",  ruleV.getType());
                }else{
                    ErrorHandler.showSemanticError(lexicAnalyzer.getIterator(),new FDAException(-1,"sum must be of integers"));
                    ruleSet.addAttribute("type",  Type.ERROR_TYPE);
                }


                data.add(ruleSet);
                return data;
            }
        };
        Production pE_1= new Production ("suma",R,E_,actionpPE_1);

        SemanticAction actionpPE_2 = new SemanticAction("- R E'") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData ruleV = (RuleData)params.get("R");
                RuleData ruleW_1 = (RuleData)params.get("E'");
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                if(ruleW_1.getType().equals(Type.EMPTY)){
                    ruleSet.addAttribute("type",  ruleV.getType());
                }else if(ruleW_1.getType().equals(ruleV.getType())&&ruleW_1.getType().equals(Type.INTEGER)){
                    ruleSet.addAttribute("type",  ruleV.getType());
                }else{
                    ErrorHandler.showSemanticError(lexicAnalyzer.getIterator(),new FDAException(-1,"difference must be of integers"));
                    ruleSet.addAttribute("type",  Type.ERROR_TYPE);
                }


                data.add(ruleSet);
                return data;
            }
        };
        Production pE_2= new Production ("resta",R,E_,actionpPE_2);
        E_.setLambda(true);
        E_.setLambdaAction(new SemanticAction("R_1.type = empty") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("type",Type.EMPTY);
                data.add(ruleSet);
                return data;
            }
        });
        E_.addProductions(pE_1,pE_2);


        SemanticAction actionpPR = new SemanticAction("U R'") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData ruleV = (RuleData)params.get("U");
                RuleData ruleW_1 = (RuleData)params.get("R'");
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                if(ruleW_1.getType().equals(Type.EMPTY)){
                    ruleSet.addAttribute("type", ruleV.getType());
                }else if(ruleW_1.getType().equals(ruleV.getType())&&ruleW_1.getType().equals(Type.INTEGER)){
                    ruleSet.addAttribute("type",  Type.BOOLEAN);
                }else{

                    ruleSet.addAttribute("type",  Type.ERROR_TYPE);
                }


                data.add(ruleSet);
                return data;
            }
        };
        Production pR1= new Production (U,R_,actionpPR);
        R.addProductions(pR1);

        SemanticAction actionpPR_1 = new SemanticAction("== U R'") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData ruleV = (RuleData)params.get("U");
                RuleData ruleW_1 = (RuleData)params.get("R'");
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                if(ruleW_1.getType().equals(Type.EMPTY)){
                    ruleSet.addAttribute("type",  ruleV.getType());
                }else if(ruleW_1.getType().equals(ruleV.getType())&&ruleW_1.getType().equals(Type.INTEGER)){
                    ruleSet.addAttribute("type",  ruleV.getType());
                }else{
                    ruleSet.addAttribute("type",  Type.ERROR_TYPE);
                }


                data.add(ruleSet);
                return data;
            }
        };
        Production pR_1= new Production ("igual",U,R_,actionpPR_1);

        SemanticAction actionpPR_2 = new SemanticAction("!= U R'") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData ruleV = (RuleData)params.get("U");
                RuleData ruleW_1 = (RuleData)params.get("R'");
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                if(ruleW_1.getType().equals(Type.EMPTY)){
                    ruleSet.addAttribute("type",  ruleV.getType());
                }else if(ruleW_1.getType().equals(ruleV.getType())&&ruleW_1.getType().equals(Type.INTEGER)){
                    ruleSet.addAttribute("type",  ruleV.getType());
                }else{
                    ruleSet.addAttribute("type",  Type.ERROR_TYPE);
                }


                data.add(ruleSet);
                return data;
            }
        };
        Production pR_2= new Production ("distinto",U,R_,actionpPR_2);
        R_.setLambda(true);
        R_.setLambdaAction(new SemanticAction("R_1.type = empty") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("type",Type.EMPTY);
                data.add(ruleSet);
                return data;
            }
        });

        R_.addProductions(pR_1,pR_2);

        SemanticAction actionpPU = new SemanticAction("W U'") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData ruleV = (RuleData)params.get("W");
                RuleData ruleW_1 = (RuleData)params.get("U'");
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                if(ruleW_1.getType().equals(Type.EMPTY)){
                    ruleSet.addAttribute("type",  ruleV.getType());
                }else if(ruleW_1.getType().equals(ruleV.getType())){
                    ruleSet.addAttribute("type",  ruleV.getType());
                }else{
                    ruleSet.addAttribute("type",  Type.ERROR_TYPE);
                }


                data.add(ruleSet);
                return data;
            }
        };
        Production pU1= new Production (W,U_,actionpPU);
        U.addProductions(pU1);


        SemanticAction actionpPU_1 = new SemanticAction("and W U'") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData ruleV = (RuleData)params.get("W");
                RuleData ruleW_1 = (RuleData)params.get("U'");
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                if(ruleW_1.getType().equals(Type.EMPTY)){
                    ruleSet.addAttribute("type",  ruleV.getType());
                }else if(ruleW_1.getType().equals(ruleV.getType())&&ruleW_1.getType().equals(Type.BOOLEAN)){
                    ruleSet.addAttribute("type",  ruleV.getType());
                }else{
                    ruleSet.addAttribute("type",  Type.ERROR_TYPE);
                }


                data.add(ruleSet);
                return data;
            }
        };
        Production pU_1= new Production ("and",W,U_,actionpPU_1);

        U_.setLambda(true);
        U_.setLambdaAction(new SemanticAction("U_1.type = empty") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("type",Type.EMPTY);
                data.add(ruleSet);
                return data;
            }
        });
        U_.addProductions(pU_1);

        SemanticAction actionpPW = new SemanticAction("V W'") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData ruleV = (RuleData)params.get("V");
                RuleData ruleW_1 = (RuleData)params.get("W'");
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                if(ruleW_1.getType().equals(Type.EMPTY)){
                    ruleSet.addAttribute("type",  ruleV.getType());
                }else if(ruleW_1.getType().equals(ruleV.getType())){
                    ruleSet.addAttribute("type",  ruleV.getType());
                }else{
                    ruleSet.addAttribute("type",  Type.ERROR_TYPE);
                }


                data.add(ruleSet);
                return data;
            }
        };
        Production pW1= new Production (V,W_,actionpPW);
        W.addProductions(pW1);


        SemanticAction actionpPW_1 = new SemanticAction("or V W'") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData ruleV = (RuleData)params.get("V");
                RuleData ruleW_1 = (RuleData)params.get("W'");
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                if(ruleW_1.getType().equals(Type.EMPTY)){
                    ruleSet.addAttribute("type",  ruleV.getType());
                }else if(ruleW_1.getType().equals(ruleV.getType())&&ruleW_1.getType().equals(Type.BOOLEAN)){
                    ruleSet.addAttribute("type",  ruleV.getType());
                }else{
                    ruleSet.addAttribute("type",  Type.ERROR_TYPE);
                }


                data.add(ruleSet);
                return data;
            }
        };
        Production pW_1= new Production ("or",V,W_,actionpPW_1);

        W_.setLambda(true);
        W_.setLambdaAction(new SemanticAction("W_1.type = empty") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("type",Type.EMPTY);
                data.add(ruleSet);
                return data;
            }
        });
        W_.addProductions(pW_1);

        SemanticAction actionpPV1 = new SemanticAction("V.tipo = V'.tipo") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData rule = (RuleData)params.get("V'");

                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                SymbolEntry entry = handler.find((int)params.get("id"));
                if(rule.getType()== null){
                    Object o = rule.get("list").getContent();
                    if(o instanceof ArrayList<?>){
                        if(Tools.compareArrays(entry.getTypesParamters(),(ArrayList<Type>)o)){
                            ruleSet.addAttribute("type", entry.getReturnType());
                        }else{
                            ruleSet.addAttribute("type", Type.ERROR_TYPE);
                            ErrorHandler.showSemanticError(lexicAnalyzer.getIterator(),new FDAException(-1,"Params of function must be the same type as function parameters"));
                        }
                    }else{
                        ErrorHandler.showSemanticError(lexicAnalyzer.getIterator(),new FDAException(-1,"Function must be called"));
                        ruleSet.addAttribute("type", Type.ERROR_TYPE);
                    }
                }else
                if(rule.getType().equals(Type.EMPTY)){

                    ruleSet.addAttribute("type", entry.getType());
                }
                data.add(ruleSet);
                return data;
            }
        };


        Production pV1= new Production ("id" ,V_,actionpPV1);

        SemanticAction actionpPV2 = new SemanticAction("V.tipo = E.tipo") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData rule = (RuleData)params.get("E");

                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("type",  rule.getType());
                data.add(ruleSet);
                return data;
            }
        };
        Production pV2= new Production ("abrePar",E,"cierraPar",actionpPV2);
        SemanticAction actionpPV4 = new SemanticAction("V.tipo = ent") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("type", Type.INTEGER);
                data.add(ruleSet);
                return data;
            }
        };
        Production pV4= new Production ("constanteEntera",actionpPV4);

        SemanticAction actionpPV5 = new SemanticAction("V.tipo = string") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("type", Type.STRING);
                data.add(ruleSet);
                return data;
            }
        };
        Production pV5= new Production ("cadena",actionpPV5);

        SemanticAction actionpPV6 = new SemanticAction("V.tipo = log") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("type", Type.BOOLEAN);
                data.add(ruleSet);
                return data;
            }
        };
        Production pV6= new Production ("true",actionpPV6);
        SemanticAction actionpPV7 = new SemanticAction("V.tipo = log") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("type", Type.BOOLEAN);
                data.add(ruleSet);
                return data;
            }
        };
        Production pV7= new Production ("false",actionpPV7);
        V.addProductions(pV1,pV2,pV4,pV5,pV6,pV7);



        SemanticAction actionpPV_1 = new SemanticAction("V'.list = L.tipo") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData rule = (RuleData)params.get("L");

                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("list", rule.get("list").getContent());
                data.add(ruleSet);
                return data;
            }
        };
        Production pV_1 = new Production("abrePar",L,"cierraPar",actionpPV_1);
        V_.setLambda(true);
        V_.setLambdaAction(new SemanticAction("V'.type= empty") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("type",Type.EMPTY);
                data.add(ruleSet);
                return data;
            }
        });
        V_.addProductions(pV_1);
        SemanticAction actionPZ = new SemanticAction("Z.type = S.type") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData rule = (RuleData)params.get("S");

                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("type",rule.getType());
                data.add(ruleSet);
                return data;
            }
        };
        Production pZ = new Production(S,actionPZ);


        SemanticAction actionPZ_2 = new SemanticAction("Z.type = C.tipo || Z'.tipo") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData rule = (RuleData)params.get("C");
                RuleData rule2 = (RuleData)params.get("Z'");
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                if(rule.getType().equals(Type.OK_TYPE)&&rule2.getType().equals(Type.OK_TYPE)){
                    ruleSet.addAttribute("type",rule.getType());
                }else{
                    ruleSet.addAttribute("type",Type.ERROR_TYPE);
                }


                data.add(ruleSet);
                return data;
            }
        };
        Production pZ2 = new Production("abreLlave",C,"cierraLlave",Z_,actionPZ_2);

        Z.addProductions(pZ,pZ2);


        SemanticAction actionPZ_1 = new SemanticAction("Z'.TYPE = C.type") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                RuleData rule = (RuleData)params.get("C");

                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("type",rule.getType());
                data.add(ruleSet);
                return data;
            }
        };
        Production pZ_1 = new Production("else","abreLlave",C,"cierraLlave",actionPZ_1);
        Z_.setLambda(true);
        Z_.setLambdaAction(new SemanticAction("Z'.tipo = tipoOk") {
            @Override
            public List<RuleData> apply(Map<String, Object> params, SymbolTableHandler handler) {
                List<RuleData>data =new ArrayList<>();
                RuleData ruleSet = new RuleData("ret");
                ruleSet.addAttribute("type",Type.OK_TYPE);
                data.add(ruleSet);
                return data;
            }
        });
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
