package Analyzer.Semantic;

import Analyzer.SymbolTable.Type;
import Tools.Console;

public class Atribute {
    private Object content;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    @Override
    public String toString() {
        if(content.equals(Type.OK_TYPE)){
            return Console.GREEN_BOLD+ content+ Console.WHITE_BOLD;
        }else if(content.equals(Type.ERROR_TYPE)){
            return Console.RED_BOLD+ content+ Console.WHITE_BOLD;
        }
        return content.toString();
    }
}
