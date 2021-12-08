package Analyzer.Semantic;

import Analyzer.Semantic.RuleData;
import Analyzer.SymbolTable.SymbolTableHandler;
import com.google.common.collect.Multimap;

import java.util.List;

public abstract class SemanticAction{

    private String info;
    public SemanticAction( ){

    }
    public abstract List<RuleData> apply(Multimap<String,Object>params, SymbolTableHandler handler);
    public SemanticAction(String info){
        this.info = info;
    }
    public Object getAttribute(Multimap<String,Object>params,String rule,String attributeName){
        Object o = params.get(rule);
        if(o!=null){
            if(o instanceof List<?>){
                List<?> ruleDatas=(List<?>)o;
                for(Object ruleData : ruleDatas){
                    if(ruleData instanceof RuleData){
                        Object content;
                        if((content= ((RuleData)ruleData).searchAtribute(attributeName))!=null){
                            return content;
                        }
                    }

                }

            }
        }
        return null;

    }




    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "{"+info+"}";
    }
}
