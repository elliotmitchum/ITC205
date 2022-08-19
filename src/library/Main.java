package library;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import library.borrowitem.BorrowItemControl;
import library.entities.Item;
import library.entities.ItemType;
import library.borrowitem.BorrowItemUI;
import library.entities.Calendar;
import library.entities.Library;
import library.entities.Loan;
import library.entities.Patron;
import library.fixitem.FixItemUI;
import library.fixitem.FixItemControl;
import library.payfine.PayFineUI;
import library.payfine.PayFineControl;
import library.returnItem.ReturnItemControl;
import library.returnItem.ReturnItemUI;


public class Main {

    private static Scanner SCANNER;

    private static Library LIBRARY;

    private static Calendar CALENDAR;

    private static SimpleDateFormat SIMPLE_DATE_FORMAT;

    private static String MENU = """
        Library Main Menu
                
            AP  : add patron
            LP : list patrons
                
            AI  : add item
            LI : list items
            FI : fix item
                
            B  : borrow an item
            R  : return an item
            L  : list loans
                
            P  : pay fine
                
            T  : increment date
            Q  : quit
                
        Choice : 
        """;


    public static void main(String[] args) {
        try {
            SCANNER = new Scanner(System.in);
            LIBRARY = Library.getInstance();
            CALENDAR = Calendar.getInstance();
            SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

            for (Patron patron : LIBRARY.listPatrons()) {
                output(patron);
            }

            output(" ");

            for (Item item : LIBRARY.listItems()) {
                output(item);
            }

            boolean isFinished = false;

            while (!isFinished) {
                Date currentDate = CALENDAR.getDate();
                output("\n" + SIMPLE_DATE_FORMAT.format(currentDate));
                String choice = input(MENU);

                switch (choice.toUpperCase()) {

                    case "AP":
                        addPatron();
                        break;

                    case "LP":
                        listPatrons();
                        break;

                    case "AI":
                        addItem();
                        break;

                    case "LI":
                        listItems();
                        break;

                    case "FI":
                        fixItems();
                        break;

                    case "B":
                        borrowItem();
                        break;

                    case "R":
                        returnItem();
                        break;

                    case "L":
                        listCurrentLoans();
                        break;

                    case "P":
                        payFines();
                        break;

                    case "T":
                        incrementDate();
                        break;

                    case "Q":
                        isFinished = true;
                        break;

                    default:
                        output("\nInvalid option\n");
                        break;
                }

                Library.save();
            }
        }
        catch (RuntimeException e) {
            output(e);
        }

        output("\nEnded\n");
    }


    private static void payFines() {
        new PayFineUI(new PayFineControl()).run();
    }


    private static void listCurrentLoans() {
        output("");
        for (Loan loan : LIBRARY.listCurrentLoans()) {
            output(loan + "\n");
        }
    }


    private static void listItems() {
        output("");
        for (Item book : LIBRARY.listItems()) {
            output(book + "\n");
        }
    }


    private static void listPatrons() {
        output("");
        for (Patron member : LIBRARY.listPatrons()) {
            output(member + "\n");
        }
    }


    private static void borrowItem() {
        new BorrowItemUI(new BorrowItemControl()).run();
    }


    private static void returnItem() {
        new ReturnItemUI(new ReturnItemControl()).run();
    }


    private static void fixItems() {
        new FixItemUI(new FixItemControl()).run();
    }


    private static void incrementDate() {
        try {
            Date currentDate = CALENDAR.getDate();
            String answer = input("Enter number of days: ");
            int days = Integer.valueOf(answer).intValue();
            CALENDAR.incrementDate(days);
            LIBRARY.updateCurrentLoansStatus();
            String currentDateDisplay = SIMPLE_DATE_FORMAT.format(currentDate);
            output(currentDateDisplay);
        }
        catch (NumberFormatException e) {
            output("\nInvalid number of days\n");
        }
    }


    private static void addItem() {
        ItemType itemType = null;
        String typeMenu = """
            Select item type:
                B : Book
                D : DVD video disk
                V : VHS video cassette
                C : CD audio disk
                A : Audio cassette
               Choice <Enter quits> : """;

        while (itemType == null) {
            String type = input(typeMenu);

            switch (type.toUpperCase()) {
                case "B":
                    itemType = ItemType.BOOK;
                    break;

                case "D":
                    itemType = ItemType.DVD;
                    break;

                case "V":
                    itemType = ItemType.VHS;
                    break;

                case "C":
                    itemType = ItemType.CD;
                    break;

                case "A":
                    itemType = ItemType.CASSETTE;
                    break;

                case "":
                    return;

                default:
                    output(type + " is not a recognised Item type");

            }
        }

        String author = input("Enter author: ");
        String title = input("Enter title: ");
        String callNumber = input("Enter call number: ");
        Item book = LIBRARY.addItem(author, title, callNumber, itemType);
        output("\n" + book + "\n");
    }


    private static void addPatron() {
        try {
            String firstName = input("Enter first name: ");
            String lastName = input("Enter last name: ");
            String emailAddress = input("Enter email address: ");
            String phoneNumberDisplay = input("Enter phone number: ");
            long phoneNumber = Long.valueOf(phoneNumberDisplay).intValue();
            Patron patron = LIBRARY.addPatron(firstName, lastName, emailAddress, phoneNumber);
            output("\n" + patron + "\n");
        }
        catch (NumberFormatException e) {
            output("\nInvalid phone number\n");
        }
    }


    private static String input(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine();
    }


    private static void output(Object object) {
        System.out.println(object);
    }


}
