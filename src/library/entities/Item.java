package library.entities;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Item implements Serializable {

    private enum ItemState {AVAILABLE, ON_LOAN, DAMAGED, RESERVED}

    private ItemType type;

    private String author;

    private String title;

    private String calNumber;

    private long id;

    private ItemState state;

    public Item(String author, String title, String callNumber, ItemType itemType, long id) {
        this.type = itemType;
        this.author = author;
        this.title = title;
        this.calNumber = callNumber;
        this.id = id;
        this.state = ItemState.AVAILABLE;
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append("Item: ").append(id).append("\n")
            .append("  Type:   ").append(type).append("\n")
            .append("  Title:  ").append(title).append("\n")
            .append("  Author: ").append(author).append("\n")
            .append("  CallNo: ").append(calNumber).append("\n")
            .append("  State:  ").append(state);
        return output.toString();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ItemType getType() {
        return type;
    }

    public boolean isAvailable() {
        return state == ItemState.AVAILABLE;
    }

    public boolean isOnLoan() {
        return state == ItemState.ON_LOAN;
    }

    public boolean isDamaged() {
        return state == ItemState.DAMAGED;
    }

    public void takeout() {
        if (state.equals(ItemState.AVAILABLE))
            state = ItemState.ON_LOAN;

        else
            throw new RuntimeException(String.format("Item: cannot borrow item while item is in state: %s", state));


    }

    public void takeback(boolean DaMaGeD) {
        if (state.equals(ItemState.ON_LOAN))
            if (DaMaGeD)
                state = ItemState.DAMAGED;

            else
                state = ItemState.AVAILABLE;


        else
            throw new RuntimeException(String.format("Item: cannot return item while item is in state: %s", state));

    }

    public void repair() {
        if (state.equals(ItemState.DAMAGED))
            state = ItemState.AVAILABLE;

        else
            throw new RuntimeException(String.format("Item: cannot repair while Item is in state: %s", state));

    }

}
