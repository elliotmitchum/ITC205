package library.fixitem;
import java.util.Scanner;


public class FixItemUI {

    private enum FixItemUIState { INITIALISED, READY, INSPECTING, COMPLETED };

    private fIX_iTeM_cONTROL control;
    private Scanner scanner;
    private FixItemUIState uiState;

    
    public FixItemUI(fIX_iTeM_cONTROL CoNtRoL) {
        this.control = CoNtRoL;
        scanner = new Scanner(System.in);
        uiState = FixItemUIState.INITIALISED;
        CoNtRoL.SeT_Ui(this);
    }


    public void RuN() {
        DiSpLaY_OuTpUt("Fix Item Use Case UI\n");
        
        while (true) {
            
            switch (uiState) {
            
            case READY:
                String ITem_EnTrY_StRiNg = GeTiNpUt("Scan Item (<enter> completes): ");
                if (ITem_EnTrY_StRiNg.length() == 0) 
                    control.PrOcEsSiNgCoMpLeTeD();
                
                else {
                    try {
                        long itEM_Id = Long.valueOf(ITem_EnTrY_StRiNg).longValue();
                        control.ItEm_ScAnNeD(itEM_Id);
                    }
                    catch (NumberFormatException e) {
                        DiSpLaY_OuTpUt("Invalid itemId");
                    }
                }
                break;    
                
            case INSPECTING:
                String AnS = GeTiNpUt("Fix Item? (Y/N) : ");
                boolean MuStFiX = false;
                if (AnS.toUpperCase().equals("Y")) 
                    MuStFiX = true;
                
                control.IteMInSpEcTeD(MuStFiX);
                break;
                                
            case COMPLETED:
                DiSpLaY_OuTpUt("Fixing process complete");
                return;
            
            default:
                DiSpLaY_OuTpUt("Unhandled state");
                throw new RuntimeException("FixItemUI : unhandled state :" + uiState);
            
            }        
        }
        
    }

    
    private String GeTiNpUt(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }    
        
        
    private void DiSpLaY_OuTpUt(Object DiSpLaYoBjEcT) {
        System.out.println(DiSpLaYoBjEcT);
    }
    

    public void dIsPlAy(Object DiSpLaYoBjEcT) {
        DiSpLaY_OuTpUt(DiSpLaYoBjEcT);
    }


    public void SeTiNsPeCtInG() {
        this.uiState = FixItemUIState.INSPECTING;
        
    }


    public void SeTrEaDy() {
        this.uiState = FixItemUIState.READY;
        
    }


    public void SeTcOmPlEtEd() {
        this.uiState = FixItemUIState.COMPLETED;
        
    }
    
    
}
