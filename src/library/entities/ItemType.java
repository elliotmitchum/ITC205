package library.entities;

import java.util.HashMap;
import java.util.Map;

public enum ItemType {

    BOOK, DVD, VHS, CD, CASSETTE;

    private static final ItemType[] VALUES = values();

    private static final Map<ItemType, String> ITEM_TYPES = new HashMap<ItemType, String>();

    static {
        ITEM_TYPES.put(ItemType.BOOK, "Book");
        ITEM_TYPES.put(ItemType.DVD, "DVD");
        ITEM_TYPES.put(ItemType.VHS, "VHS Video cassette");
        ITEM_TYPES.put(ItemType.CD, "CD Audio disk");
        ITEM_TYPES.put(ItemType.CASSETTE, "Audio cassette");
    }

    public String toString() {
        return ITEM_TYPES.get(this);
    }

}
