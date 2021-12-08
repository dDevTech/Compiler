package Analyzer.Semantic;

import java.util.HashMap;
import java.util.List;

public class RuleData {
    private HashMap<String,Atribute> atributes= new HashMap<>();

    public void addAttribute(String name,Object content){
        Atribute atribute = new Atribute();
        atribute.setName(name);
        atribute.setContent(content);
        atributes.put(name,atribute);

    }
    public Object searchAtribute(String name){
        Atribute atribute = atributes.get(name);
        if(atribute!=null){
            return atribute.getContent();
        }else{
            return null;
        }

    }
}
