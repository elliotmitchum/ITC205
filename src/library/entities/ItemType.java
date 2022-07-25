package library.entities;

import java.util.HashMap;
import java.util.Map;

public enum ItemType {

    Book, Dvd, Vhs, Cd, Cassette;

    private static final ItemType[] values = values();

    private static final Map<ItemType, String> itemTypes = new HashMap<ItemType, String>();

    static {
        itemTypes.put(ItemType.Book, "Book");
        itemTypes.put(ItemType.Dvd, "DVD");
        itemTypes.put(ItemType.Vhs, "VHS Video cassette");
        itemTypes.put(ItemType.Cd, "CD Audio disk");
        itemTypes.put(ItemType.Cassette, "Audio cassette");
    }

    public String toString() {
        return itemTypes.get(this);
    }

}
