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

    private ItemState itemState;

    public Item(String author, String title, String callNumber, ItemType itemType, long id) {
        this.itemType = itemType;
        this.author = author;
        this.title = title;
        this.callNumber = callNumber;
        this.id = id;
        this.itemState = ItemState.Available;
    }

    public String toString() {
        StringBuilder Sb = new StringBuilder();
        Sb.append("Item: ").append(id).append("\n")
            .append("  Type:   ").append(itemType).append("\n")
            .append("  Title:  ").append(title).append("\n")
            .append("  Author: ").append(author).append("\n")
            .append("  CallNo: ").append(callNumber).append("\n")
            .append("  State:  ").append(itemState);

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
        return itemState == ItemState.Available;
    }

    public boolean isOnLoan() {
        return itemState == ItemState.OnLoan;
    }

    public boolean isDamaged() {
        return itemState == ItemState.Damaged;
    }

    public void takeOut() {
        if (itemState.equals(ItemState.Available)) {
            itemState = ItemState.OnLoan;
        } else {
            throw new RuntimeException(String.format("Item: cannot borrow item while item is in state: %s", itemState));
        }
    }

    public void takeBack(boolean isDamaged) {
        if (itemState.equals(ItemState.OnLoan)) {
            if (isDamaged) {
                itemState = ItemState.Damaged;
            } else {
                itemState = ItemState.Available;
            }
        } else {
            throw new RuntimeException(String.format("Item: cannot return item while item is in state: %s", itemState));
        }
    }

    public void repair() {
        if (itemState.equals(ItemState.Damaged)) {
            itemState = ItemState.Available;
        } else {
            throw new RuntimeException(String.format("Item: cannot repair while Item is in state: %s", itemState));
        }
    }

}
