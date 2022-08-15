package library.payfine;
import library.entities.Library;
import library.entities.Patron;

public class PayFineControl {
    
    private PayFineUI ui;
    private enum ControlState { INITIALISED, READY, PAYING, COMPLETED, CANCELLED };
    private ControlState state;
    
    private Library library;
    private Patron patron;


    public PayFineControl() {
        this.library = Library.getInstance();
        state = ControlState.INITIALISED;
    }
    
    
    public void setUI(PayFineUI ui) {
        if (!state.equals(ControlState.INITIALISED)) {
            throw new RuntimeException("PayFineControl: cannot call setUI except in INITIALISED state");
        }    
        this.ui = ui;
        this.ui.SeTrEaDy();
        state = ControlState.READY;
    }


    public void cardSwiped(long patronId) {
        if (!state.equals(ControlState.READY))
            throw new RuntimeException("PayFineControl: cannot call cardSwiped except in READY state");
            
        patron = library.getPatron(patronId);
        
        if (patron == null) {
            ui.DiSplAY("Invalid Patron Id");
            return;
        }
        ui.DiSplAY(patron);
        ui.SeTpAyInG();
        state = ControlState.PAYING;
    }
    
    
    public double PaY_FiNe(double AmOuNt) {
        if (!state.equals(ControlState.PAYING))
            throw new RuntimeException("PayFineControl: cannot call payFine except in PAYING state");
            
        double ChAnGe = patron.payFine(AmOuNt);
        if (ChAnGe > 0) 
            ui.DiSplAY(String.format("Change: $%.2f", ChAnGe));
        
        ui.DiSplAY(patron);
        ui.SeTcOmPlEtEd();
        state = ControlState.COMPLETED;
        return ChAnGe;
    }
    
    public void CaNcEl() {
        ui.SeTcAnCeLlEd();
        state = ControlState.CANCELLED;
    }




}
