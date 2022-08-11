package library.returnBook;
import library.entities.Item;
import library.entities.Library;
import library.entities.Loan;

public class ReturnItemControl {

    private ReturnBookUI ui;

    private enum ControlState { INITIALISED, READY, INSPECTING };

    private ControlState state;
    
    private Library library;

    private Loan currentLoan;
    

    public ReturnItemControl() {
        this.library = Library.getInstance();
        state = ControlState.INITIALISED;
    }
    
    
    public void setUI(ReturnBookUI ui) {
        if (!state.equals(ControlState.INITIALISED)) {
            throw new RuntimeException("ReturnBookControl: cannot call setUI except in INITIALISED state");
        }

        this.ui = ui;
        ui.SeTrEaDy();
        state = ControlState.READY;
    }


    public void itemScanned(long bookId) {
        if (!state.equals(ControlState.READY)) {
            throw new RuntimeException("ReturnBookControl: cannot call bookScanned except in READY state");
        }

        Item currentBook = library.getItem(bookId);
        String currentBookDisplay = currentBook.toString();
        String currentLoanDisplay = currentLoan.toString();
        
        if (currentBook == null) {
            ui.DiSpLaY("Invalid Book Id");
            return;
        }

        if (!currentBook.isOnLoan()) {
            ui.DiSpLaY("Book has not been borrowed");
            return;
        }

        currentLoan = library.getLoanByItemId(bookId);
        double overDueFine = 0.0;

        if (currentLoan.isOverDue()) {
            overDueFine = library.calculateOverDueFine(currentLoan);
        }

        ui.DiSpLaY("Inspecting");
        ui.DiSpLaY(currentBookDisplay);
        ui.DiSpLaY(currentLoanDisplay);
        
        if (currentLoan.isOverDue()) {
            ui.DiSpLaY(String.format("\nOverdue fine : $%.2f", overDueFine));
        }

        ui.SeTiNsPeCtInG();
        state = ControlState.INSPECTING;
    }


    public void scanningCompleted() {
        if (!state.equals(ControlState.READY)) {
            throw new RuntimeException("ReturnBookControl: cannot call scanningComplete except in READY state");
        }

        ui.SeTCoMpLeTeD();
    }


    public void dischargeLoan(boolean isDamaged) {
        if (!state.equals(ControlState.INSPECTING)) {
            throw new RuntimeException("ReturnBookControl: cannot call dischargeLoan except in INSPECTING state");
        }

        library.dischargeLoan(currentLoan, isDamaged);
        currentLoan = null;
        ui.SeTrEaDy();
        state = ControlState.READY;
    }


}
