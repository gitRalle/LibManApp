package logic;

import objects.*;
import storingOfData.DB_Connection;
import storingOfData.ReadWrite;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

public class LibraryLogic {
    private static Scanner input = new Scanner(System.in);
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private LinkedList<User> appUsers = new LinkedList<>();
    private DB_Connection connection = new DB_Connection();
    private String getAvailable() {return "IN STOCK";}
    private String getUnavailable() {return "ON LOAN";}


    public User login() {
        LinkedList<User> listOfUsers = ReadWrite.readUser();
        String username;
        String password;
        boolean check = false;
        int counter = 0;

        do {
            System.out.print("username: ");
            username = input.nextLine();
            System.out.print("password: ");
            password = input.nextLine();

            for (User currentUser : listOfUsers) {
                if (currentUser.getUsername().equals(username)) {
                    try {
                        check = Password.check(password, currentUser.getHashedPassword());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    if (check) {
                        return currentUser;
                    }

                }

            }

            System.out.println("incorrect username or pw\n");
            counter++;

        } while (counter != 3);

        System.exit(0);
        return null;
    }


    public void parseInput(String input) throws NumberFormatException, IllegalArgumentException, Exception {

        /**
         * parse input and look for general commands
         */
        switch (input) {
            case "help":
                DataFormatter formatter = new DataFormatter("LibManCommands", '-', '-', '|');
                formatter.formatData(new String[]{"1a: createUser(arg1, arg2) 1b: createUser(arg1, arg2, arg3, arg4, arg5, arg6)",
                        "2. createBook(arg1, arg2, arg3, arg4, arg5)",
                        "5: createTransaction(String: bookTitle, String: memberName)",
                        "3: getMember(String: name)",
                        "4: getMembers",
                        "5: getBook(String: title)",
                        "6: getBooks",
                        "7: getTransaction(String: bookTitle, String: memberName)",
                        "8: getTransactions"});
                formatter.showData();
                parseInput(LibraryLogic.input.nextLine());
                break;
            case "getBooks":
                printBooks();
                break;
            case "getMembers":
                printMembers();
                break;
            case "getTransactions":
                printTransactions();
                break;
        }


        String cmd = "";
        String arg = "";
        ArrayList<String> args = new ArrayList<>();
        int index = -1;

        // parse command
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) != '(') {
                cmd += input.charAt(i);
            } else {
                index = i;
                break;
            }

        }

        // if '(' isn't found or if input doesnt end with ')'
        if (index == -1 || input.charAt(input.length() - 1) != ')') {
            return;
        }

        // parse params
        for (int i = index + 1; i < input.length(); i++) {

            if (input.charAt(i) == ',') {
                //   System.out.println("arg=" + args);
                args.add(arg);
                arg = "";
                i++;
            } else if (input.charAt(i) != ',' && input.charAt(i) != ')') {
                arg += input.charAt(i);
            } else {
                //   System.out.println("arg=" + args);
                args.add(arg);
                arg = "";
            }
        }

        /**
         * input has been parsed and is a command with arguments
         */
        switch (cmd) {
            case "createUser":
                User createdUser = createUser(args);
                if (createdUser != null) {
                    DataFormatter formatter = new DataFormatter("CreatedUser");
                    if (createdUser instanceof Admin) {
                        formatter.formatData(new String[] {"(Access: [" + createdUser.getClass().getSimpleName().toUpperCase() + "]) " +
                                                           "(Username: [" + createdUser.getUsername() + "]) (Password: [" + createdUser.getHashedPassword() + "])"});

                    } else if (createdUser instanceof Member) {
                        formatter.formatData(new String[] {"(Access: [" + createdUser.getClass().getSimpleName().toUpperCase() + "]) " +
                                                           "(Username: [" + createdUser.getUsername() + "]) (ID: [" + ((Member) createdUser).getMemberId() +
                                                           "]) (Name: [" + ((Member) createdUser).getName() + "]) (SSN: [" + ((Member) createdUser).getSsn() +
                                                           "]) (Address: [" + ((Member) createdUser).getAddress() + "]) (Phone number: [" +
                                                           ((Member) createdUser).getPhoneNumber() + "])"});
                    }
                    formatter.showData();
                    parseInput(LibraryLogic.input.nextLine());
                }
                break;
            case "createBook":
                Book createdBook = createBook(args);
                if (createdBook != null) {
                    DataFormatter df = new DataFormatter("CreatedBook");
                    df.formatData(new String[] {"(ID: [" + createdBook.getBookId() + "]) (Title: [" + createdBook.getTitle() + "]) (Author: [" +
                                                createdBook.getAuthor() + "]) (Pages: [" + createdBook.getNumberOfPages() + "]) (Lang: [" +
                                                createdBook.getLanguage() + "]) (Publisher: [" + createdBook.getPublisher() + "])"});
                    df.showData();
                    parseInput(LibraryLogic.input.nextLine());
                }
                break;
            case "createTransaction":
                Transaction createdTransaction = createTransaction(args);
                if (createdTransaction != null) {
                    DataFormatter formatter = new DataFormatter("CreatedTransaction");
                    formatter.formatData(new String[]{"(ID: [" + String.valueOf(createdTransaction.getTransactionId()) + "]) (Issued: [" +
                                                        createdTransaction.getIssueDate().format(dtf) + "]) (Due: [" +
                                                        createdTransaction.getDueDate().format(dtf) + "]) (Title: [" +
                                                        getBookTitle(createdTransaction.getBookId()) + "]) (Name: [" +
                                                        getMemberName(createdTransaction.getMemberId()) + "])"});
                    formatter.showData();
                    parseInput(LibraryLogic.input.nextLine());
                }
                break;
            case "getMember":
                if (args.size() == 1) {
                    Member member = getMember(args.get(0));
                    if (member != null) {
                        DataFormatter noWrap = new DataFormatter("MemberInfo", '-', '-', '|');
                        noWrap.formatData(new String[]{"(ID: [" + String.valueOf(member.getMemberId()) + "]) (Name: [" + member.getName() + "]) (SSN: [" +
                                                         member.getSsn() + "]) (Address: [" + member.getAddress() + "]) (Phone number: [" + member.getPhoneNumber() + "])"});
                        noWrap.showData();
                        parseInput(LibraryLogic.input.nextLine());
                    }
                }
                break;
            case "getBook":
                if (args.size() == 1) {
                    Book book = getBook(args.get(0));
                    if (book != null) {
                        DataFormatter formatter = new DataFormatter("BookInfo", '-', '-', '|');
                        formatter.formatData(new String[]{"(ID: [" + String.valueOf(book.getBookId()) + "]) (Title: [" + book.getTitle() + "]) (Author: [" +
                                                           book.getAuthor() + "]) (Pages: [" + String.valueOf(book.getNumberOfPages()) + "]) (Lang: [" +
                                                           book.getLanguage() + "]) (Publisher: [" + book.getPublisher() + "]) (Status: [" +
                                                           String.format("%s", book.isAvailable() ? getAvailable() : getUnavailable()) + "])"});
                        formatter.showData();
                        parseInput(LibraryLogic.input.nextLine());
                    }
                }
                break;
            case "getTransaction":
                if (args.size() == 2) {
                    Transaction transaction = getTransaction(args.get(0), args.get(1));
                    if (transaction != null) {
                        String returnDate;
                        if (transaction.getReturnDate() == null) {
                            returnDate = "NOT RETURNED";
                        } else {
                            returnDate = transaction.getReturnDate().format(dtf);
                        }
                        DataFormatter formatter = new DataFormatter("TransactionInfo");
                        formatter.formatData(new String[]{"[" + String.valueOf(transaction.getTransactionId()) + "] + [" +
                                                           transaction.getIssueDate().format(dtf) + "] + [" + transaction.getDueDate().format(dtf) + "] + [" +
                                                           returnDate + "} + [" +
                                                           transaction.getLateFee() + " SEK} + [" + getBookTitle(transaction.getBookId()) + "] + [" +
                                                           getMemberName(transaction.getMemberId()) + "]"});
                        formatter.showData();
                        parseInput(LibraryLogic.input.nextLine());
                    }
                }
                break;
            case "deleteUser":
                if (args.size() == 1) {
                    User user = deleteUser(args.get(0));
                    if (user != null) {
                        DataFormatter formatter = new DataFormatter("DeletedUser");
                        if (user instanceof Admin) {
                            formatter.formatData(new String[] {"(Access: [" + user.getClass().getSimpleName().toUpperCase() + "]) (Username: [" +
                                                                user.getUsername() + "]) (Password: [" + user.getHashedPassword() + "])"});
                        }
                        formatter.showData();
                        parseInput(LibraryLogic.input.nextLine());
                    }
                }

        }
    }


    /**
     * INTERACTIVE LOGIC FUNCTIONS
     */

    public void issueLoan() throws SQLException, InputMismatchException, Exception
    {

        /*
         * SHOW MEMBERS -->
         */
        String[][] memberData = getMembers();
        ArrayList<Member> appMembers = connection.readTableMember();
        Member selectedMember = null;
        boolean found = false;

        DataFormatter df = new DataFormatter("Select Member");
        df.setCursor("<>");
        df.formatData(memberData);
        df.showData();

        try {

            do {

                int memberId = Integer.parseInt(input.nextLine());

                for (Member currentMember : appMembers) {
                    if (currentMember.getMemberId() == memberId) {
                        selectedMember = currentMember;
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    System.out.print("<!=>");
                }

            } while (!found);

        }

        catch (NumberFormatException ex) {
            throw new NumberFormatException(
                    "System expected an int value and threw a new NumberFormatException");
        }

        /*
        * SHOW BOOKS -->
        */

        String[][] bookData = getBooks("isAvailable = 1");
        ArrayList<Book> appBooks = connection.readTableBook();
        Book selectedBook = null;
        found = false;

        DataFormatter bookFormatter = new DataFormatter("Select Book");
        bookFormatter.setCursor("<>");
        bookFormatter.formatData(bookData);
        bookFormatter.showData();

        try {

            do {
                int bookId = Integer.parseInt(input.nextLine());

                for (Book currentBook : appBooks) {
                    if (currentBook.isAvailable()) {
                        if (currentBook.getBookId() == bookId) {
                            selectedBook = currentBook;
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    System.out.print("<!=>");
                }
            } while (!found);

        }

        catch (NumberFormatException ex) {
            throw new NumberFormatException(
                    "System expected an int value and threw a new NumberFormatException");
        }

    }




    /**
     * NON-INTERACTIVE LOGIC FUNCTIONS -->
     */

    private void printBooks() throws SQLException, Exception
    {
        ArrayList<Book> appBooks = connection.readTableBook();
        DataFormatter formatter = new DataFormatter("getBooks");
        String[][] array = new String[appBooks.size()][7];

        for (int i = 0; i < appBooks.size(); i++) {
            array[i][0] = "(ID: [" + String.valueOf(appBooks.get(i).getBookId()) + "]) ";
            array[i][1] = "(Title: [" + appBooks.get(i).getTitle() + "]) ";
            array[i][2] = "(Author: [" + appBooks.get(i).getAuthor() + "]) ";
            array[i][3] = "(Pages: [" + String.valueOf(appBooks.get(i).getNumberOfPages()) + "]) ";
            array[i][4] = "(Lang: [" + appBooks.get(i).getLanguage() + "]) ";
            array[i][5] = "(Publisher: [" + appBooks.get(i).getPublisher() + "]) ";
            array[i][6] = "(Status: [" + String.valueOf(appBooks.get(i).isAvailable()) + "]) ";
        }

        formatter.formatData(array);
        formatter.showData();
        parseInput(LibraryLogic.input.nextLine());
    }


    private void printMembers() throws SQLException, Exception
    {
        ArrayList<Member> appMembers = connection.readTableMember();
        DataFormatter formatter = new DataFormatter("GetMembers");
        String[][] data = new String[appMembers.size()][5];

        for (int i = 0; i < appMembers.size(); i++) {
            data[i][0] = "(ID: [" + String.valueOf(appMembers.get(i).getMemberId()) + "]) ";
            data[i][1] = "(Name: [" + appMembers.get(i).getName() + "]) ";
            data[i][2] = "(SSN: [" + appMembers.get(i).getSsn() + "]) ";
            data[i][3] = "(Address: [" + appMembers.get(i).getAddress() + "]) ";
            data[i][4] = "(Phone number: [" + appMembers.get(i).getPhoneNumber() + "]) ";
        }

        formatter.formatData(data);
        formatter.showData();
        parseInput(LibraryLogic.input.nextLine());
    }


    public void printTransactions() throws SQLException, Exception
    {
        ArrayList<Transaction> appTransactions = connection.readTableTransaction();
        DataFormatter formatter = new DataFormatter("GetTransactions", '-', '-', '|');
        String[][] data = new String[appTransactions.size()][7];
        String returnDate;

        for (int i = 0; i < appTransactions.size(); i++) {
            data[i][0] = "(ID: [" + String.valueOf(appTransactions.get(i).getTransactionId()) + "]) ";
            data[i][1] = "(Issued: [" + appTransactions.get(i).getIssueDate().format(dtf) + "]) ";
            data[i][2] = "(Due: [" + appTransactions.get(i).getDueDate().format(dtf) + "]) ";
            if (appTransactions.get(i).getReturnDate() == null) {
                returnDate = "NOT RETURNED";
            }
            else {
                returnDate = appTransactions.get(i).getReturnDate().format(dtf);
            }
            data[i][3] = "(Returned: [" + returnDate + "]) ";
            data[i][4] = "(Fee: [" + String.format("%.2f", appTransactions.get(i).getLateFee()) + " SEK" + "]) ";
            data[i][5] = "(Title: [" + getBookTitle(appTransactions.get(i).getBookId()) + "]) ";
            data[i][6] = "(Name: [" + getMemberName(appTransactions.get(i).getMemberId()) + "]) ";

            formatter.formatData(data);
            formatter.showData();
            parseInput(LibraryLogic.input.nextLine());
        }
    }


    private Book createBook(ArrayList<String> args) throws NumberFormatException {
        Book createdBook = null;
        Book temp;

        /* title, author, numberOfPages, language, publisher */
        if (args.size() == 5) {
            temp = new Book(args.get(0), args.get(1), Integer.parseInt(args.get(2)), args.get(3), args.get(4));
            createdBook = connection.insertBook(temp);
        }

        return createdBook;
    }


    private User createUser(ArrayList<String> args) throws IllegalArgumentException, Exception
    {
        User createdUser = null;
        Member temp;

        // create new admin user
        if (args.size() == 2) {
            appUsers = ReadWrite.readUser();
            appUsers.add(createdUser = new Admin(args.get(0), Password.getSaltedHash(args.get(1))));
            ReadWrite.writeUser(appUsers);

        }

        // create new member user
        else if (args.size() == 6) {
            temp = new Member(args.get(0), Password.getSaltedHash(args.get(1)),
                    args.get(2), args.get(3), args.get(4), args.get(5));
            createdUser = connection.insertMember(temp);

            if (createdUser != null) {
                appUsers = ReadWrite.readUser();
                appUsers.add(createdUser);
                ReadWrite.writeUser(appUsers);
            }
        }

        return createdUser;

    }

    private Transaction createTransaction(ArrayList<String> args) throws Exception {
        Transaction createdTransaction = null;
        Transaction tempTransaction;

        if (args.size() == 2) {
            if (getBook(args.get(0)).isAvailable()) {
                tempTransaction = new Transaction(getBookId(args.get(0)), getMemberId(args.get(1))); /* title, name */
                createdTransaction = connection.insertTransaction(tempTransaction);
            }
        }

        return createdTransaction;
    }

    private User deleteUser(String username) throws IOException
    {
        appUsers = ReadWrite.readUser();
        User returnUser = null;

        System.out.print("\n");
        User root = login();
        if (root instanceof Admin && root.getUsername().equals("root") ||
            root instanceof Admin && root.getUsername().equals("admin")) {

            for (User currentUser : appUsers) {
                if (currentUser.getUsername().equals(username) && !currentUser.getUsername().equals(root.getUsername())) {
                    if (currentUser instanceof Member) {
                        connection.deleteMember(((Member) currentUser).getMemberId());
                    }

                    returnUser = currentUser;
                    appUsers.remove(currentUser);
                    ReadWrite.writeUser(appUsers);
                    break;
                }
            }
        }

        return returnUser;
    }






    /**
     * GET FUNCTIONS -->
     */

    private String[][] getBooks() throws SQLException, Exception
    {
        ArrayList<Book> appBooks = connection.readTableBook();
        String[][] array = new String[appBooks.size()][7];

        for (int i = 0; i < appBooks.size(); i++) {
            array[i][0] = "(ID: [" + String.valueOf(appBooks.get(i).getBookId()) + "]) ";
            array[i][1] = "(Title: [" + appBooks.get(i).getTitle() + "]) ";
            array[i][2] = "(Author: [" + appBooks.get(i).getAuthor() + "]) ";
            array[i][3] = "(Pages: [" + String.valueOf(appBooks.get(i).getNumberOfPages()) + "]) ";
            array[i][4] = "(Lang: [" + appBooks.get(i).getLanguage() + "]) ";
            array[i][5] = "(Publisher: [" + appBooks.get(i).getPublisher() + "]) ";
            array[i][6] = "(Status: [" + String.valueOf(appBooks.get(i).isAvailable()) + "]) ";
        }

        return array;
    }


    private String[][] getBooks(final String SQL_WHERE) throws SQLException, Exception
    {
        ArrayList<Book> appBooks = connection.readTableBook(SQL_WHERE);
        String[][] data = new String[appBooks.size()][7];

        for (int i = 0; i < appBooks.size(); i++) {
            if (appBooks.get(i).isAvailable()) {
                data[i][0] = "(ID: [" + String.valueOf(appBooks.get(i).getBookId()) + "]) ";
                data[i][1] = "(Title: [" + appBooks.get(i).getTitle() + "]) ";
                data[i][2] = "(Author: [" + appBooks.get(i).getAuthor() + "]) ";
                data[i][3] = "(Pages: [" + String.valueOf(appBooks.get(i).getNumberOfPages()) + "]) ";
                data[i][4] = "(Lang: [" + appBooks.get(i).getLanguage() + "]) ";
                data[i][5] = "(Publisher: [" + appBooks.get(i).getPublisher() + "]) ";
                data[i][6] = "(Status: [" + String.format("%s", appBooks.get(i).isAvailable() ? getAvailable() : getUnavailable()) + "]) ";
            }
        }

        return data;
    }


    private String[][] getMembers() throws SQLException, Exception
    {
        ArrayList<Member> appMembers = connection.readTableMember();
        DataFormatter formatter = new DataFormatter("GetMembers");
        String[][] data = new String[appMembers.size()][5];

        for (int i = 0; i < appMembers.size(); i++) {
            data[i][0] = "(ID: [" + String.valueOf(appMembers.get(i).getMemberId()) + "]) ";
            data[i][1] = "(Name: [" + appMembers.get(i).getName() + "]) ";
            data[i][2] = "(SSN: [" + appMembers.get(i).getSsn() + "]) ";
            data[i][3] = "(Address: [" + appMembers.get(i).getAddress() + "]) ";
            data[i][4] = "(Phone number: [" + appMembers.get(i).getPhoneNumber() + "]) ";
        }

        return data;
    }


    public String[][] getTransactions() throws SQLException, Exception
    {
        ArrayList<Transaction> appTransactions = connection.readTableTransaction();
        DataFormatter formatter = new DataFormatter("GetTransactions", '-', '-', '|');
        String[][] data = new String[appTransactions.size()][7];
        String returnDate;

        for (int i = 0; i < appTransactions.size(); i++) {
            data[i][0] = "(ID: [" + String.valueOf(appTransactions.get(i).getTransactionId()) + "]) ";
            data[i][1] = "(Issued: [" + appTransactions.get(i).getIssueDate().format(dtf) + "]) ";
            data[i][2] = "(Due: [" + appTransactions.get(i).getDueDate().format(dtf) + "]) ";
            if (appTransactions.get(i).getReturnDate() == null) {
                returnDate = "NOT RETURNED";
            }
            else {
                returnDate = appTransactions.get(i).getReturnDate().format(dtf);
            }
            data[i][3] = "(Returned: [" + returnDate + "]) ";
            data[i][4] = "(Fee: [" + String.format("%.2f", appTransactions.get(i).getLateFee()) + " SEK" + "]) ";
            data[i][5] = "(Title: [" + getBookTitle(appTransactions.get(i).getBookId()) + "]) ";
            data[i][6] = "(Name: [" + getMemberName(appTransactions.get(i).getMemberId()) + "]) ";

        }

        return data;
    }


    private Member getMember(String name) {
        ArrayList<Member> appMembers = connection.readTableMember();
        Member returnMember = null;

        for (Member currentMember : appMembers) {
            if (currentMember.getName().equalsIgnoreCase(name)) {
                returnMember = currentMember;
            }
        }

        return returnMember;
    }

    public Member getMember(int memberId) {
        ArrayList<Member> appMembers = connection.readTableMember();
        Member returnMember = null;

        for (Member currentMember : appMembers) {
            if (currentMember.getMemberId() == memberId) {
                returnMember = currentMember;
                break;
            }
        }

        return returnMember;
    }

    private Book getBook(String title) {
        ArrayList<Book> appBooks = connection.readTableBook();
        Book returnBook = null;

        for (Book currentBook : appBooks) {
            if (currentBook.getTitle().equalsIgnoreCase(title)) {
                returnBook = currentBook;
                break;
            }
        }

        return returnBook;
    }

    public Book getBook(int bookId) {
        ArrayList<Book> appBooks = connection.readTableBook();
        Book returnBook = null;

        for (Book currentBook : appBooks) {
            if (currentBook.getBookId() == bookId) {
                returnBook = currentBook;
                break;
            }
        }

        return returnBook;
    }

    private Transaction getTransaction(String bookTitle, String memberName) {
        ArrayList<Transaction> appTransactions = connection.readTableTransaction();
        Transaction returnTransaction = null;

        /* will eat alot of time */
        for (Transaction currentTransaction : appTransactions) {
            if (getBookTitle(currentTransaction.getBookId()).equalsIgnoreCase(bookTitle) &&
                    getMemberName(currentTransaction.getMemberId()).equalsIgnoreCase(memberName)) {
                returnTransaction = currentTransaction;
                break;
            }
        }

        return returnTransaction;
    }


    private int getBookId(String title) {
        ArrayList<Book> appBooks = connection.readTableBook();
        int bookId = -1;

        for (Book currentBook : appBooks) {
            if (currentBook.getTitle().equalsIgnoreCase(title)) {
                bookId = currentBook.getBookId();
                break;
            }
        }

        return bookId;
    }

    private int getMemberId(String name) {
        ArrayList<Member> appMembers = connection.readTableMember();
        int memberId = -1;

        for (Member currentMember : appMembers) {
            if (currentMember.getName().equalsIgnoreCase(name)) {
                memberId = currentMember.getMemberId();
                break;
            }
        }

        return memberId;
    }

    private String getBookTitle(int bookId) {
        ArrayList<Book> appBooks = connection.readTableBook();
        String bookTitle = null;

        for (Book currentBook : appBooks) {
            if (currentBook.getBookId() == bookId) {
                bookTitle = currentBook.getTitle();
                break;
            }
        }

        return bookTitle;
    }

    private String getMemberName(int memberId) {
        ArrayList<Member> appMembers = connection.readTableMember();
        String memberName = null;

        for (Member currentMember : appMembers) {
            if (currentMember.getMemberId() == memberId) {
                memberName = currentMember.getName();
                break;
            }
        }

        return memberName;
    }





    /**
     * MISC HELPER METHODS -->
     */

    public String getTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

}

