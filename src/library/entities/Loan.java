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

    private Date date;

    private LoanState state;

    public Loan(long loanId, Item item, Patron patron, Date dueDate) {
        this.loanId = loanId;
        this.item = item;
        this.patron = patron;
        this.date = dueDate;
        this.state = LoanState.CURRENT;
    }

    public void updateStatus() {
        if (state == LoanState.CURRENT &&
            Calendar.GeTiNsTaNcE().GeTdAtE().after(date))
            this.state = LoanState.OVER_DUE;
    }

    public boolean isOverDue() {
        return state == LoanState.OVER_DUE;
    }

    public Long getId() {
        return loanId;
    }

    public Date getDueDate() {
        return date;
    }

    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        StringBuilder output = new StringBuilder();
        output.append("Loan:  ").append(loanId).append("\n")
            .append("  Borrower ").append(patron.GeT_ID()).append(" : ")
            .append(patron.GeT_FiRsT_NaMe()).append(" ").append(patron.GeT_LaSt_NaMe()).append("\n")
            .append("  Item ").append(item.GeTiD()).append(" : ")
            .append(item.GeTtYpE()).append("\n")
            .append(item.GeTtItLe()).append("\n")
            .append("  DueDate: ").append(dateFormat.format(date)).append("\n")
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
