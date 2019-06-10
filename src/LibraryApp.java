import logic.LibraryLogic;
import objects.Admin;
import objects.User;
import ui.Menu;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class LibraryApp {

    private static Scanner input = new Scanner(System.in);
    private static boolean cont = true;
    private static boolean stayLoggedIn = true;

    private static LibraryLogic logic;
    private static User currentUser;

    public static void main(String[] args) {
        logic = new LibraryLogic();

        while (cont) {
            stayLoggedIn = true;
            currentUser = logic.login();
            while (stayLoggedIn) {
                showStartMenu();
            }
        }


    }

    /**
     * Method which displays the start menu and asks the user for input and
     * handles the input
     */
    private static void showStartMenu() {

        if (currentUser instanceof Admin) {
            Menu startMenu = new Menu("LibManApp", currentUser.getClass().getSimpleName().toUpperCase() + " | " + currentUser.getUsername() + " | " +
                    "<HkrLib> | " + logic.getTime());
            startMenu.addMenuItem("Loans");
            startMenu.addMenuItem("Books");
            startMenu.addMenuItem("Members");
            startMenu.addMenuItem("Help");
            startMenu.addMenuItem("Settings");
            startMenu.addMenuItem("Logout Current User");
            startMenu.addMenuItem("Exit Application");
            startMenu.showMenu();

            String choice = input.nextLine();
            if (choice.equals("")) {
                choice = input.nextLine();
            }

            try {
                logic.parseInput(choice);

                switch (choice) {
                    case "1":
                        showTransactionManagementMenu();
                        break;
                    case "2":
                        showBookManagementMenu();
                        break;
                    case "3":
                        showMemberManagementMenu();
                        break;
                    case "6":
                        System.out.print("\n");
                        stayLoggedIn = false;
                        break;
                    case "7":
                        stayLoggedIn = false;
                        cont = false;
                        break;

                }
            }

            catch (NumberFormatException ex) {
                System.out.print("\n" + ex.getMessage() + "\n<|x|>");
                input.next();
            }

            catch (IllegalArgumentException ex) {
                System.out.print("\nIllegalArgumentException raised " + ex.getMessage() + "\n<|x|>");
                ex.printStackTrace();
                input.next();
            }

            catch (SQLException ex) {
                System.out.print("\nSQLException raised " + ex.getMessage() + "\n<|x|>");
                ex.printStackTrace();
                input.next();
            }

            catch (Exception ex) {
                System.out.print("\nException raised " + ex.getCause() + "\n<|x|>");
                ex.printStackTrace();
                input.next();
            }


        }





    }

    private static void showTransactionManagementMenu() throws IllegalArgumentException, NumberFormatException, SQLException, Exception
    {

        if (currentUser instanceof Admin) {
            Menu transactionMenu = new Menu("LibManApp/Loans", currentUser.getClass().getSimpleName().toUpperCase() + " | " +
                    currentUser.getUsername() + " | " + "<HkrLib> | " + logic.getTime());
            transactionMenu.addMenuItem("Issue Loan");
            transactionMenu.addMenuItem("Renew Loan");
            transactionMenu.addMenuItem("Return Issue");
            transactionMenu.addMenuItem("Show Active Loans");
            transactionMenu.addMenuItem("Show Overdue Loans");
            transactionMenu.addMenuItem("Show Returned Loans");
            transactionMenu.addMenuItem("Show Loan History (APPEND .TXT)");
            transactionMenu.showMenu();

            String choice = input.nextLine();
            logic.parseInput(choice);

            switch (choice) {
                case "1":
                    logic.issueLoan();
                    break;
            }

        }
    }

    private static void showBookManagementMenu() {

        if (currentUser instanceof Admin) {
            Menu bookMenu = new Menu("LibManApp/LibBookManagement", currentUser.getClass().getSimpleName().toUpperCase() + " | " +
                    currentUser.getUsername() + " | " + "<HkrLib> | " + logic.getTime());
            bookMenu.addMenuItem("Show Available Assortment");
            bookMenu.addMenuItem("Show Unavailable Assortment");
            bookMenu.addMenuItem("Show Full Assortment");
            bookMenu.addMenuItem("Show Removed Assortment (APPEND .DATA/.TXT)");
            bookMenu.addMenuItem("Goto Edit Menu");
            bookMenu.showMenu();
            input.next();
        }
    }

    private static void showMemberManagementMenu() {

        if (currentUser instanceof Admin) {
            Menu memberMenu = new Menu("LibManApp/MemBookManagement", currentUser.getClass().getSimpleName().toUpperCase() + " | " +
                    currentUser.getUsername() + " | " + "<HkrLib> | " + logic.getTime());
            memberMenu.addMenuItem("Add Member");
            memberMenu.addMenuItem("Remove Member");
            memberMenu.addMenuItem("Show Active Members");
            memberMenu.addMenuItem("Show Removed Members (APPEND .DATA/.TXT)");
            memberMenu.addMenuItem("Show Member History (APPEND .DATA/.TXT)");
            memberMenu.addMenuItem("Goto Edit Member Menu");
            memberMenu.showMenu();
            input.next();
        }
    }





}


