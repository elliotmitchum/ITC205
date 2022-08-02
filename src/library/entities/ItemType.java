package library.entities;

import java.util.HashMap;
import java.util.Map;

public enum ItemType {

    BOOK, DVD, VHS, CD, CASSETTE;

    private static final ItemType[] VALUES = values();

    private static final Map<ItemType, String> LABELS = new HashMap<ItemType, String>();

    static {
        LABELS.put(ItemType.BOOK, "Book");
        LABELS.put(ItemType.DVD, "DVD");
        LABELS.put(ItemType.VHS, "VHS Video cassette");
        LABELS.put(ItemType.CD, "CD Audio disk");
        LABELS.put(ItemType.CASSETTE, "Audio cassette");
    }

    public String toString() {
        return LABELS.get(this);
    }

}
