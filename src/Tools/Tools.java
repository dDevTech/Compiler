package Tools;

import Analyzer.SymbolTable.Type;

import java.util.List;
import java.util.stream.Collectors;

public class Tools {
    public static <T>String characterListToString(List<T> chars){
        return chars.stream().map(String::valueOf).collect(Collectors.joining());
    }
    public static boolean compareArrays(List<Type>types1, List<Type>types2){
        if(types1.size()!=types2.size()) return false;
        for(int i =0 ;i<types1.size();i++){
           if(!types1.get(i).equals(types2.get(i))){
               return false;
           }
        }

        return true;
    }

}
