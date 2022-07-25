package library.entities;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Item implements Serializable {

    private ItemType itemType;

    private String author;

    private String title;

    private String callNumber;

    private long id;

    private enum ItemState {Available, OnLoan, Damaged, Reserved};

    private ItemState state;

    public Item(String author, String title, String callNumber, ItemType itemType, long id) {
        this.itemType = itemType;
        this.author = author;
        this.title = title;
        this.callNumber = callNumber;
        this.id = id;
        this.state = ItemState.Available;
    }

    public String toString() {
        StringBuilder Sb = new StringBuilder();
        Sb.append("Item: ").append(id).append("\n")
            .append("  Type:   ").append(itemType).append("\n")
            .append("  Title:  ").append(title).append("\n")
            .append("  Author: ").append(author).append("\n")
            .append("  CallNo: ").append(callNumber).append("\n")
            .append("  State:  ").append(state);

        return Sb.toString();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public boolean isAvailable() {
        return state == ItemState.Available;
    }

    public boolean isOnLoan() {
        return state == ItemState.OnLoan;
    }

    public boolean isDamaged() {
        return state == ItemState.Damaged;
    }

    public void takeOut() {
        if (state.equals(ItemState.Available)) {
            state = ItemState.OnLoan;
        } else {
            throw new RuntimeException(String.format("Item: cannot borrow item while item is in state: %s", state));
        }
    }

    public void takeBack(boolean isDamaged) {
        if (state.equals(ItemState.OnLoan)) {
            if (isDamaged) {
                state = ItemState.Damaged;
            } else {
                state = ItemState.Available;
            }
        } else {
            throw new RuntimeException(String.format("Item: cannot return item while item is in state: %s", state));
        }
    }

    public void repair() {
        if (state.equals(ItemState.Damaged)) {
            state = ItemState.Available;
        } else {
            throw new RuntimeException(String.format("Item: cannot repair while Item is in state: %s", state));
        }
    }

}
