package Tools;

import java.util.List;
import java.util.stream.Collectors;

public class Tools {
    public static String characterListToString(List<Character> chars){
        return chars.stream().map(String::valueOf).collect(Collectors.joining());
    }
}
