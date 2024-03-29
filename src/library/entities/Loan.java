package library.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial")
public class Loan implements Serializable {

    private enum LoanState {CURRENT, OVER_DUE, DISCHARGED}

    private long loanId;

    private Item item;

    private Patron patron;

    private Date dueDate;

    private LoanState state;

    public Loan(long loanId, Item item, Patron patron, Date dueDate) {
        this.loanId = loanId;
        this.item = item;
        this.patron = patron;
        this.dueDate = dueDate;
        this.state = LoanState.CURRENT;
    }


    public void updateStatus() {
        if (state == LoanState.CURRENT && Calendar.getInstance().getDate().after(dueDate)) {
            this.state = LoanState.OVER_DUE;
        }
    }


    public boolean isOverDue() {
        return state == LoanState.OVER_DUE;
    }


    public Long getId() {
        return loanId;
    }


    public Date getDueDate() {
        return dueDate;
    }


    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        StringBuilder output = new StringBuilder();
        output.append("Loan:  ").append(loanId).append("\n")
            .append("  Borrower ").append(patron.getId()).append(" : ")
            .append(patron.getFirstName()).append(" ").append(patron.getLastName()).append("\n")
            .append("  Item ").append(item.getId()).append(" : ")
            .append(item.getItemType()).append("\n")
            .append(item.getTitle()).append("\n")
            .append("  DueDate: ").append(dateFormat.format(dueDate)).append("\n")
            .append("  State: ").append(state);
        return output.toString();
    }


    public Patron getPatron() {
        return patron;
    }


    public Item getItem() {
        return item;
    }


    public void discharge() {
        state = LoanState.DISCHARGED;
    }

    
}
