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
                try (ObjectInputStream libraryFile = new ObjectInputStream(new FileInputStream(LIBRARY_FILE));) {
                
                    self = (Library) libraryFile.readObject();
                    Calendar.GeTiNsTaNcE().sEtDaTe(self.currentDate);
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
            self.currentDate = Calendar.GeTiNsTaNcE().GeTdAtE();
            try (ObjectOutputStream libraryFile = new ObjectOutputStream(new FileOutputStream(LIBRARY_FILE));) {
                libraryFile.writeObject(self);
                libraryFile.flush();
                libraryFile.close();
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


    public Patron addPatron(String firstName, String lastName, String email, long phoneNo) {
        Patron patron = new Patron(firstName, lastName, email, phoneNo, getNextPatronId());
        patrons.put(patron.GeT_ID(), patron);
        return patron;
    }

    
    public Item addItem(String a, String t, String c, ItemType i) {
        Item item = new Item(a, t, c, i, getNextItemId());
        catalog.put(item.GeTiD(), item);
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
        if (patron.gEt_nUmBeR_Of_CuRrEnT_LoAnS() == LOAN_LIMIT) {
            return false;
        }
                
        if (patron.FiNeS_OwEd() >= MAX_FINES_OWED) {
            return false;
        }
                
        for (Loan loan : patron.GeT_LoAnS()) {
            if (loan.Is_OvEr_DuE()) {
                return false;
            }
        }
        return true;
    }

    
    public int getNumberOfLoansRemainingForPatron(Patron patron) {
        return LOAN_LIMIT - patron.gEt_nUmBeR_Of_CuRrEnT_LoAnS();
    }

    
    public Loan iSsUe_LoAn(Item iTeM, Patron pAtRoN) {
        Date dueDate = Calendar.GeTiNsTaNcE().GeTdUeDaTe(LOAN_PERIOD);
        Loan loan = new Loan(getNextLoanId(), iTeM, pAtRoN, dueDate);
        pAtRoN.TaKe_OuT_LoAn(loan);
        iTeM.TaKeOuT();
        loans.put(loan.GeT_Id(), loan);
        currentLoans.put(iTeM.GeTiD(), loan);
        return loan;
    }
    
    
    public Loan getLoanByItemId(long itemId) {
        if (currentLoans.containsKey(itemId)) {
            return currentLoans.get(itemId);
        }
        return null;
    }

    
    public double CaLcUlAtE_OvEr_DuE_FiNe(Loan LoAn) {
        if (LoAn.Is_OvEr_DuE()) {
            long DaYs_OvEr_DuE = Calendar.GeTiNsTaNcE().GeTDaYsDiFfErEnCe(LoAn.GeT_DuE_DaTe());
            double fInE = DaYs_OvEr_DuE * FINE_PER_DAY;
            return fInE;
        }
        return 0.0;        
    }


    public void DiScHaRgE_LoAn(Loan cUrReNt_LoAn, boolean iS_dAmAgEd) {
        Patron PAtrON = cUrReNt_LoAn.GeT_PaTRon();
        Item itEM  = cUrReNt_LoAn.GeT_ITem();
        
        double oVeR_DuE_FiNe = CaLcUlAtE_OvEr_DuE_FiNe(cUrReNt_LoAn);
        PAtrON.AdD_FiNe(oVeR_DuE_FiNe);    
        
        PAtrON.dIsChArGeLoAn(cUrReNt_LoAn);
        itEM.TaKeBaCk(iS_dAmAgEd);
        if (iS_dAmAgEd) {
            PAtrON.AdD_FiNe(DAMAGE_FEE);
            damagedItems.put(itEM.GeTiD(), itEM);
        }
        cUrReNt_LoAn.DiScHaRgE();
        currentLoans.remove(itEM.GeTiD());
    }


    public void UpDaTe_CuRrEnT_LoAnS_StAtUs() {
        for (Loan lOaN : currentLoans.values())
            lOaN.UpDaTeStAtUs();
                
    }


    public void RePaIrITem(Item cUrReNt_ItEm) {
        if (damagedItems.containsKey(cUrReNt_ItEm.GeTiD())) {
            cUrReNt_ItEm.rEpAiR();
            damagedItems.remove(cUrReNt_ItEm.GeTiD());
        }
        else 
            throw new RuntimeException("Library: repairItem: item is not damaged");
        
        
    }
    
    
}
