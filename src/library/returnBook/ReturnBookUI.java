package library.returnBook;

import java.util.Scanner;


public class ReturnBookUI {

    private enum UiState {INITIALISED, READY, INSPECTING, COMPLETED};

    private rETURN_bOOK_cONTROL control;

    private Scanner input;

    private UiState state;


    public ReturnBookUI(rETURN_bOOK_cONTROL control) {
        this.control = control;
        input = new Scanner(System.in);
        state = UiState.INITIALISED;
        control.sEt_uI(this);
    }


    public void run() {
        displayOutput("Return Book Use Case UI\n");

        while (true) {

            switch (state) {

                case INITIALISED:
                    break;

                case READY:
                    String bookInputString = getInput("Scan Book (<enter> completes): ");
                    if (bookInputString.length() == 0) {
                        control.sCaNnInG_cOmPlEtEd();
                    }
                    else {
                        try {
                            long bookId = Long.valueOf(bookInputString).longValue();
                            control.bOoK_sCaNnEd(bookId);
                        } catch (NumberFormatException e) {
                            displayOutput("Invalid bookId");
                        }
                    }
                    break;

                case INSPECTING:
                    String answer = getInput("Is book damaged? (Y/N): ");
                    boolean isDamaged = false;
                    if (answer.toUpperCase().equals("Y"))
                        isDamaged = true;

                    control.dIsChArGe_lOaN(isDamaged);

                case COMPLETED:
                    displayOutput("Return processing complete");
                    return;

                default:
                    displayOutput("Unhandled state");
                    throw new RuntimeException("ReturnBookUI : unhandled state :" + state);
            }
        }
    }


    private String getInput(String prompt) {
        System.out.print(prompt);
        return input.nextLine();
    }


    private void displayOutput(Object object) {
        System.out.println(object);
    }


    public void display(Object object) {
        displayOutput(object);
    }

    public void setReady() {
        state = UiState.READY;
    }


    public void setInspecting() {
        state = UiState.INSPECTING;
    }


    public void setCompleted() {
        state = UiState.COMPLETED;
    }


}
