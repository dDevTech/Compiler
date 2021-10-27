package Tools;

import java.util.List;
import java.util.stream.Collectors;

public class Tools {
    public static <T>String characterListToString(List<T> chars){
        return chars.stream().map(String::valueOf).collect(Collectors.joining());
    }

}
