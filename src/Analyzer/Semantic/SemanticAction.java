package Analyzer.Semantic;

import Analyzer.Semantic.RuleData;
import Analyzer.SymbolTable.SymbolTableHandler;
import com.google.common.collect.Multimap;

import java.util.List;
import java.util.Map;

public abstract class SemanticAction{

    private String info;
    public SemanticAction( ){

    }
    public abstract List<RuleData> apply(int line,Map<String,Object> params, SymbolTableHandler handler);
    public SemanticAction(String info){
        this.info = info;
    }
    public Object getAttribute(Map<String,Object>params,String rule,String attributeName){
        Object o = params.get(rule);
        if(o!=null){
            if(o instanceof RuleData){

                return ((RuleData)o).searchAtribute(attributeName);

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
