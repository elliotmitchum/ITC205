package library.entities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class Library implements Serializable {
    
    private static final String LIBRARY_FILE = "library.obj";
    private static final int LOAN_LIMIT = 2;
    private static final int LOAN_PERIOD = 2;
    private static final double FINE_PER_DAY = 1.0;
    private static final double MAX_FINES_OWED = 1.0;
    private static final double DAMAGE_FEE = 2.0;
    
    private static Library self;
    private long nextItemId;
    private long nextPatronId;
    private long nextLoanId;
    private Date currentDate;
    
    private Map<Long, Item> catalog;
    private Map<Long, Patron> patrons;
    private Map<Long, Loan> loans;
    private Map<Long, Loan> currentLoans;
    private Map<Long, Item> damagedItems;
    

    private Library() {
        catalog = new HashMap<>();
        patrons = new HashMap<>();
        loans = new HashMap<>();
        currentLoans = new HashMap<>();
        damagedItems = new HashMap<>();
        nextItemId = 1;
        nextPatronId = 1;
        nextLoanId = 1;
    }

    
    public static synchronized Library getInstance() {
        if (self == null) {
            Path PATH = Paths.get(LIBRARY_FILE);
            if (Files.exists(PATH)) {    
                try (ObjectInputStream libraryFile = new ObjectInputStream(new FileInputStream(LIBRARY_FILE))) {
                
                    self = (Library) libraryFile.readObject();
                    Calendar.getInstance().setDate(self.currentDate);
                    libraryFile.close();
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                self = new Library();
            }
        }
        return self;
    }

    
    public static synchronized void save() {
        if (self != null) {
            self.currentDate = Calendar.getInstance().getDate();
            try (ObjectOutputStream libraryFile = new ObjectOutputStream(new FileOutputStream(LIBRARY_FILE))) {
                libraryFile.writeObject(self);
                libraryFile.flush();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    
    private long getNextItemId() {
        return nextItemId++;
    }

    
    private long getNextPatronId() {
        return nextPatronId++;
    }

    
    private long getNextLoanId() {
        return nextLoanId++;
    }

    
    public List<Patron> listPatrons() {
        return new ArrayList<Patron>(patrons.values());
    }


    public List<Item> listItems() {
        return new ArrayList<Item>(catalog.values());
    }


    public List<Loan> listCurrentLoans() {
        return new ArrayList<Loan>(currentLoans.values());
    }


    public Patron addPatron(String firstName, String lastName, String email, long phoneNumber) {
        Patron patron = new Patron(firstName, lastName, email, phoneNumber, getNextPatronId());
        patrons.put(patron.getId(), patron);
        return patron;
    }

    
    public Item addItem(String author, String title, String callNumber, ItemType itemType) {
        Item item = new Item(author, title, callNumber, itemType, getNextItemId());
        long id = item.getId();
        catalog.put(id, item);
        return item;
    }

    
    public Patron getPatron(long patronId) {
        if (patrons.containsKey(patronId)) {
            return patrons.get(patronId);
        }
        return null;
    }

    
    public Item getItem(long itemId) {
        if (catalog.containsKey(itemId)) {
            return catalog.get(itemId);
        }
        return null;
    }

    
    public int getLoanLimit() {
        return LOAN_LIMIT;
    }

    
    public boolean canPatronBorrow(Patron patron) {
        if (patron.getNumberOfCurrentLoans() == LOAN_LIMIT) {
            return false;
        }
                
        if (patron.getFinesOwed() >= MAX_FINES_OWED) {
            return false;
        }
                
        for (Loan loan : patron.getLoans()) {
            if (loan.isOverDue()) {
                return false;
            }
        }
        return true;
    }

    
    public int getNumberOfLoansRemainingForPatron(Patron patron) {
        return LOAN_LIMIT - patron.getNumberOfCurrentLoans();
    }

    
    public Loan issueLoan(Item item, Patron patron) {
        Date dueDate = Calendar.getInstance().getDueDate(LOAN_PERIOD);
        Loan loan = new Loan(getNextLoanId(), item, patron, dueDate);
        patron.takeOutLoan(loan);
        item.takeOut();
        long id = loan.getId();
        loans.put(id, loan);
        long itemId = item.getId();
        currentLoans.put(itemId, loan);
        return loan;
    }
    
    
    public Loan getLoanByItemId(long itemId) {
        if (currentLoans.containsKey(itemId)) {
            return currentLoans.get(itemId);
        }
        return null;
    }

    
    public double calculateOverDueFine(Loan loan) {
        if (loan.isOverDue()) {
            Date loanDueDate = loan.getDueDate();
            long daysOverDue = Calendar.getInstance().getDaysOfDifference(loanDueDate);
            double fine = daysOverDue * FINE_PER_DAY;
            return fine;
        }
        return 0.0;        
    }


    public void dischargeLoan(Loan currentLoan, boolean isDamaged) {
        Patron patron = currentLoan.getPatron();
        Item item  = currentLoan.getItem();
        
        double overDueFine = calculateOverDueFine(currentLoan);
        patron.addFine(overDueFine);
        
        patron.dischargeloan(currentLoan);
        item.takeBack(isDamaged);
        if (isDamaged) {
            patron.addFine(DAMAGE_FEE);
            long itemId = item.getId();
            damagedItems.put(itemId, item);
        }
        currentLoan.discharge();
        long itemId = item.getId();
        currentLoans.remove(itemId);
    }


    public void updateCurrentLoansStatus() {
        for (Loan loan : currentLoans.values()) {
            loan.updateStatus();
        }
    }


    public void repairItem(Item currentItem) {
        long currentItemId = currentItem.getId();
        if (damagedItems.containsKey(currentItemId)) {
            currentItem.repair();
            damagedItems.remove(currentItemId);
        }
        else {
            throw new RuntimeException("Library: repairItem: item is not damaged");
        }
    }
}
