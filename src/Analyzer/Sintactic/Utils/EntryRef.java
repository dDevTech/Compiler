package Analyzer.Sintactic.Utils;

import java.util.Map;

public class EntryRef {
    private Map.Entry<Object,Object> entry;

    public EntryRef(Map.Entry<Object, Object> entry) {
        this.entry = entry;
    }

    public Map.Entry<Object, Object> getEntry() {
        return entry;
    }

    public void setEntry(Map.Entry<Object, Object> entry) {
        this.entry = entry;
    }
}
