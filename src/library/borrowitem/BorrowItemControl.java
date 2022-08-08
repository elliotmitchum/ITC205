package library.borrowitem;
import java.util.ArrayList;
import java.util.List;

import library.entities.Item;
import library.entities.Library;
import library.entities.Loan;
import library.entities.Patron;

public class BorrowItemControl {
    
    private BorrowItemUI ui;
    
    private Library library;
    private Patron patron;
    private enum CONTROL_STATE { INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED }
    private CONTROL_STATE state;
    
    private List<Item> pendingList;
    private List<Loan> completedList;
    private Item item;
    
    
    public BorrowItemControl() {
        this.library = Library.getInstance();
        state = CONTROL_STATE.INITIALISED;
    }
    

    public void setUI(BorrowItemUI ui) {
        if (!state.equals(CONTROL_STATE.INITIALISED)) {
            throw new RuntimeException("BorrowItemControl: cannot call setUI except in INITIALISED state");
        }
        this.ui = ui;
        this.ui.setReady();
        state = CONTROL_STATE.READY;
    }

        
    public void cardSwiped(long patronId) {
        if (!state.equals(CONTROL_STATE.READY)) {
            throw new RuntimeException("BorrowItemControl: cannot call cardSwiped except in READY state");
        }
        patron = library.getPatron(patronId);
        if (patron == null) {
            ui.DiSpLaY("Invalid patronId");
            return;
        }
        if (library.canPatronBorrow(patron)) {
            pendingList = new ArrayList<>();
            ui.setScanning();
            state = CONTROL_STATE.SCANNING;
        }
        else {
            ui.DiSpLaY("Patron cannot borrow at this time");
            ui.setRestricted();
        }
    }
    
    
    public void itemScanned(int itemId) {
        item = null;
        if (!state.equals(CONTROL_STATE.SCANNING)) {
            throw new RuntimeException("BorrowItemControl: cannot call itemScanned except in SCANNING state");
        }
        item = library.getItem(itemId);
        if (item == null) {
            ui.DiSpLaY("Invalid itemId");
            return;
        }
        if (!item.isAvailable()) {
            ui.DiSpLaY("Item cannot be borrowed");
            return;
        }
        pendingList.add(item);
        for (Item item : pendingList) {
            ui.DiSpLaY(item);
        }
        if (library.getNumberOfLoansRemainingForPatron(patron) - pendingList.size() == 0) {
            ui.DiSpLaY("Loan limit reached");
            borrowingCompleted();
        }
    }
    
    
    public void borrowingCompleted() {
        if (pendingList.size() == 0) {
            CaNcEl();
        }
        else {
            ui.DiSpLaY("\nFinal Borrowing List");
            for (Item item : pendingList) {
                ui.DiSpLaY(item);
            }
            completedList = new ArrayList<>();
            ui.setFinalising();
            state = CONTROL_STATE.FINALISING;
        }
    }


    public void commitLoans() {
        if (!state.equals(CONTROL_STATE.FINALISING)) {
            throw new RuntimeException("BorrowItemControl: cannot call commitLoans except in FINALISING state");
        }
        for (Item item : pendingList) {
            Loan loan = library.issueLoan(item, patron);
            completedList.add(loan);
        }
        ui.DiSpLaY("Completed Loan Slip");
        for (Loan loan : completedList) {
            ui.DiSpLaY(loan);
        }
        ui.setCompleted();
        state = CONTROL_STATE.COMPLETED;
    }

    
    public void CaNcEl() {
        ui.setCancelled();
        state = CONTROL_STATE.CANCELLED;
    }
    
    
}
