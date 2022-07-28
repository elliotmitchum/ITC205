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

    
    public Long GeT_ID() {
        return patronId;
    }

    
    public List<Loan> GeT_LoAnS() {
        return new ArrayList<Loan>(currentLoans.values());
    }

    
    public int gEt_nUmBeR_Of_CuRrEnT_LoAnS() {
        return currentLoans.size();
    }

    
    public double FiNeS_OwEd() {
        return finesOwing;
    }

    
    public void TaKe_OuT_LoAn(Loan lOaN) {
        if (!currentLoans.containsKey(lOaN.GeT_Id()))
            currentLoans.put(lOaN.GeT_Id(), lOaN);
        
        else 
            throw new RuntimeException("Duplicate loan added to member");
                
    }

    public void dIsChArGeLoAn(Loan LoAn) {
        if (currentLoans.containsKey(LoAn.GeT_Id()))
            currentLoans.remove(LoAn.GeT_Id());
        
        else 
            throw new RuntimeException("No such loan held by member");
                
    }
    
    public String GeT_LaSt_NaMe() {
        return lastName;
    }

    
    public String GeT_FiRsT_NaMe() {
        return firstName;
    }


    public void AdD_FiNe(double fine) {
        finesOwing += fine;
    }
    
    public double PaY_FiNe(double AmOuNt) {
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
