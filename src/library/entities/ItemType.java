package library.entities;

import java.util.HashMap;
import java.util.Map;

public enum ItemType {

    Book, Dvd, Vhs, Cd, Cassette;

    private static final ItemType[] VALUES = values();

    private static final Map<ItemType, String> ITEM_TYPES = new HashMap<ItemType, String>();

    static {
        ITEM_TYPES.put(ItemType.Book, "Book");
        ITEM_TYPES.put(ItemType.Dvd, "DVD");
        ITEM_TYPES.put(ItemType.Vhs, "VHS Video cassette");
        ITEM_TYPES.put(ItemType.Cd, "CD Audio disk");
        ITEM_TYPES.put(ItemType.Cassette, "Audio cassette");
    }

    public String toString() {
        return ITEM_TYPES.get(this);
    }

}
