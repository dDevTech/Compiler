package Analyzer.Semantic;

import Analyzer.SymbolTable.Type;
import Common.ErrorHandler;
import Tools.Console;

import java.util.HashMap;
import java.util.List;

public class RuleData {
    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    private String rule;

    public HashMap<String, Atribute> getAtributes() {
        return atributes;
    }
    public RuleData(String rule){

        this.rule = rule;
    }
    private HashMap<String,Atribute> atributes= new HashMap<>();

    public void addAttribute(String name,Object content){
        Atribute atribute = new Atribute();
        atribute.setName(name);
        atribute.setContent(content);
        atributes.put(name,atribute);

    }
    public Atribute get(String name){
        return getAtributes().get(name);
    }
    public Type getType(){
        if(getAtributes().get("type")==null){
            return null;
        }
        Type type = (Type)getAtributes().get("type").getContent();

        return type;
    }

    public Object searchAtribute(String name){
        Atribute atribute = atributes.get(name);
        if(atribute!=null){
            return atribute.getContent();
        }else{
            return null;
        }

    }

    @Override
    public String toString() {
        if(rule.equals("ret")){
            return Console.WHITE_BOLD+"syntetized :"+atributes.toString();
        }
        return rule+":"+atributes.toString();
    }
}
