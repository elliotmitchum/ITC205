package library.returnItem;

import java.util.Scanner;


public class ReturnItemUI {

    private enum ReturnItemUIState {INITIALISED, READY, INSPECTING, COMPLETED};

    private ReturnItemControl control;

    private Scanner scanner;

    private ReturnItemUIState uiState;


    public ReturnItemUI(ReturnItemControl control) {
        this.control = control;
        scanner = new Scanner(System.in);
        uiState = ReturnItemUIState.INITIALISED;
        control.setUI(this);
    }


    public void run() {
        displayOutput("Return Book Use Case UI\n");

        while (true) {

            switch (uiState) {

                case INITIALISED:
                    break;

                case READY:
                    String bookInputString = getInput("Scan Book (<enter> completes): ");
                    if (bookInputString.length() == 0) {
                        control.scanningCompleted();
                    }
                    else {
                        try {
                            long bookId = Long.valueOf(bookInputString).longValue();
                            control.itemScanned(bookId);
                        } catch (NumberFormatException e) {
                            displayOutput("Invalid bookId");
                        }
                    }
                    break;

                case INSPECTING:
                    String answer = getInput("Is book damaged? (Y/N): ");
                    boolean isDamaged = false;
                    if (answer.toUpperCase().equals("Y")) {
                        isDamaged = true;
                    }

                    control.dischargeLoan(isDamaged);

                case COMPLETED:
                    displayOutput("Return processing complete");
                    return;

                default:
                    displayOutput("Unhandled state");
                    throw new RuntimeException("ReturnBookUI : unhandled state :" + uiState);
            }
        }
    }


    private String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }


    private void displayOutput(Object object) {
        System.out.println(object);
    }


    public void display(Object object) {
        displayOutput(object);
    }

    public void setReady() {
        uiState = ReturnItemUIState.READY;
    }


    public void setInspecting() {
        uiState = ReturnItemUIState.INSPECTING;
    }


    public void setCompleted() {
        uiState = ReturnItemUIState.COMPLETED;
    }


}
