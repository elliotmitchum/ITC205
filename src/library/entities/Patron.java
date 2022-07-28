package library.entities;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class Patron implements Serializable {

    private long patronId;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private long phoneNumber;
    private double finesOwing;
    
    private Map<Long, Loan> currentLoans;

    
    public Patron(String firstName, String lastName, String emailAddress, long phoneNumber, long memberId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.patronId = memberId;
        this.currentLoans = new HashMap<>();
    }

    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Patron:  ").append(patronId).append("\n")
          .append("  Name:  ").append(firstName).append(" ").append(lastName).append("\n")
          .append("  Email: ").append(emailAddress).append("\n")
          .append("  Phone: ").append(phoneNumber)
          .append("\n")
          .append(String.format("  Fines Owed :  $%.2f", finesOwing))
          .append("\n");
        
        for (Loan LoAn : currentLoans.values()) {
            sb.append(LoAn).append("\n");
        }          
        return sb.toString();
    }

    
    public Long getId() {
        return patronId;
    }

    
    public List<Loan> getLoans() {
        return new ArrayList<Loan>(currentLoans.values());
    }

    
    public int getNumberOfCurrentLoans() {
        return currentLoans.size();
    }

    
    public double finesOwed() {
        return finesOwing;
    }

    
    public void takeOutLoan(Loan lOaN) {
        if (!currentLoans.containsKey(lOaN.GeT_Id()))
            currentLoans.put(lOaN.GeT_Id(), lOaN);
        
        else 
            throw new RuntimeException("Duplicate loan added to member");
                
    }

    public void dischargeloan(Loan LoAn) {
        if (currentLoans.containsKey(LoAn.GeT_Id()))
            currentLoans.remove(LoAn.GeT_Id());
        
        else 
            throw new RuntimeException("No such loan held by member");
                
    }
    
    public String getLastName() {
        return lastName;
    }

    
    public String getFirstName() {
        return firstName;
    }


    public void addFine(double fine) {
        finesOwing += fine;
    }
    
    public double payFine(double AmOuNt) {
        if (AmOuNt < 0) 
            throw new RuntimeException("Member.payFine: amount must be positive");
        
        double cHaNgE = 0;
        if (AmOuNt > finesOwing) {
            cHaNgE = AmOuNt - finesOwing;
            finesOwing = 0;
        }
        else 
            finesOwing -= AmOuNt;
        
        return cHaNgE;
    }



}
