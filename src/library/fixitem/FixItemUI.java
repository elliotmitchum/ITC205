package library.fixitem;
import java.util.Scanner;


public class FixItemUI {

    private enum FixItemUIState { INITIALISED, READY, INSPECTING, COMPLETED };

    private fIX_iTeM_cONTROL control;
    private Scanner scanner;
    private FixItemUIState uiState;

    
    public FixItemUI(fIX_iTeM_cONTROL control) {
        this.control = control;
        scanner = new Scanner(System.in);
        uiState = FixItemUIState.INITIALISED;
        control.SeT_Ui(this);
    }


    public void run() {
        displayOutput("Fix Item Use Case UI\n");
        
        while (true) {
            
            switch (uiState) {
            
            case READY:
                String ITem_EnTrY_StRiNg = getInput("Scan Item (<enter> completes): ");
                if (ITem_EnTrY_StRiNg.length() == 0) 
                    control.PrOcEsSiNgCoMpLeTeD();
                
                else {
                    try {
                        long itEM_Id = Long.valueOf(ITem_EnTrY_StRiNg).longValue();
                        control.ItEm_ScAnNeD(itEM_Id);
                    }
                    catch (NumberFormatException e) {
                        displayOutput("Invalid itemId");
                    }
                }
                break;    
                
            case INSPECTING:
                String AnS = getInput("Fix Item? (Y/N) : ");
                boolean MuStFiX = false;
                if (AnS.toUpperCase().equals("Y")) 
                    MuStFiX = true;
                
                control.IteMInSpEcTeD(MuStFiX);
                break;
                                
            case COMPLETED:
                displayOutput("Fixing process complete");
                return;
            
            default:
                displayOutput("Unhandled state");
                throw new RuntimeException("FixItemUI : unhandled state :" + uiState);
            
            }        
        }
        
    }

    
    private String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }    
        
        
    private void displayOutput(Object displayobject) {
        System.out.println(displayobject);
    }
    

    public void display(Object displayobject) {
        displayOutput(displayobject);
    }


    public void setInspecting() {
        this.uiState = FixItemUIState.INSPECTING;
        
    }


    public void setReady() {
        this.uiState = FixItemUIState.READY;
        
    }


    public void SeTcOmPlEtEd() {
        this.uiState = FixItemUIState.COMPLETED;
        
    }
    
    
}
