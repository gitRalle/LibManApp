package storingOfData;

import objects.Book;
import objects.Member;
import objects.Transaction;
import objects.User;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;



public class DB_Connection {

    private Statement statement;
    private ResultSet resultSet;

    public DB_Connection() {
        String url = "jdbc:mysql://127.0.0.1:3306/mydb?user=root&password=root&useSSL=false";
        Connection connection;

        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
        }

        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Inserts param Member Object into DB and returns 'complete' Member Object (including AUTO_INCREMENT value) to caller.
     *
     * @param createdMember
     * @return
     */
    public User insertMember(Member createdMember) {
        final String SQL = "INSERT into member (name, ssn, address, telephoneNumber) " +
                "VALUES ('" + createdMember.getName() + "', '" + createdMember.getSsn() + "', '" +
                createdMember.getAddress() + "', '" + createdMember.getPhoneNumber() + "');";
        final boolean VALIDATES = validateQuery(SQL);
        User createdUser = null;

        if (VALIDATES) {

            try {
                statement.executeUpdate(SQL);

                resultSet = statement.executeQuery("SELECT LAST_INSERT_ID();");
                resultSet.next();
                createdUser = new Member(createdMember.getUsername(), createdMember.getHashedPassword(),
                        resultSet.getInt(1), createdMember.getName(), createdMember.getSsn(),
                        createdMember.getAddress(), createdMember.getPhoneNumber());
            }

            catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return createdUser;
    }

    public int deleteMember(int memberId) {
        int returnVal = -1;

        try {
            returnVal = statement.executeUpdate("DELETE from member WHERE memberId = " + memberId + ";");
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return returnVal;
    }

    public Book insertBook(Book createdBook) {
        final String SQL = "INSERT into BOOK (title, author, numberOfPages, " +
                "language, publisher) VALUES ('" + createdBook.getTitle() + "', '" +
                createdBook.getAuthor() + "', '" + createdBook.getNumberOfPages() + "', '" +
                createdBook.getLanguage() + "', '" + createdBook.getPublisher() + "');";
        final boolean VALIDATES = validateQuery(SQL);
        Book insertedBook = null;

        if (VALIDATES) {

            try {
                statement.executeUpdate(SQL);

                resultSet = statement.executeQuery("SELECT LAST_INSERT_ID();");
                resultSet.next();
                insertedBook = new Book(resultSet.getInt(1), createdBook.getTitle(),
                        createdBook.getAuthor(), createdBook.getNumberOfPages(),
                        createdBook.getLanguage(), createdBook.getPublisher(), createdBook.isAvailable());
            }

            catch (SQLException ex) {
                ex.printStackTrace();
            }

        }

        return insertedBook;
    }

    public void setBookAvailability(int bookId, boolean isAvailable) {
        int tinyint;

        if (isAvailable) {
            tinyint = 1;
        }

        else {
            tinyint = 0;
        }

        try {
            statement.executeUpdate("UPDATE book SET isAvailable = " + tinyint + " WHERE bookId = " + bookId + ";");
        }

        catch (SQLException ex) {
            ex.printStackTrace();
        }

    }


    public Transaction insertTransaction(Transaction appTransaction) {
        Transaction dbTransaction = null;
        final String SQL = "INSERT into TRANSACTION (issueDate, dueDate, book_bookId, member_memberId) " +
                "VALUES ('" + appTransaction.getIssueDate() + "', '" + appTransaction.getDueDate() + "', '" +
                appTransaction.getBookId() + "', '" + appTransaction.getMemberId() + "');";
        final boolean VALIDATES = validateQuery(SQL);

        if (VALIDATES) {

            try {
                statement.executeUpdate(SQL);
                setBookAvailability(appTransaction.getBookId(), false);

                resultSet = statement.executeQuery("SELECT LAST_INSERT_ID();");
                resultSet.next();
                dbTransaction = new Transaction(resultSet.getInt(1), appTransaction.getIssueDate(),
                        appTransaction.getDueDate(), appTransaction.getBookId(), appTransaction.getMemberId());
            }

            catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return dbTransaction;
    }


    public ArrayList<Book> readTableBook() {
        ArrayList<Book> dbBooks = new ArrayList<>();

        try {
            resultSet = statement.executeQuery("SELECT * FROM book;");

            while (resultSet.next()) {
                dbBooks.add(new Book(resultSet.getInt(1), resultSet.getString(2),
                        resultSet.getString(3), resultSet.getInt(4),
                        resultSet.getString(5), resultSet.getString(6), resultSet.getInt(7) == 1));
            }
        }

        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return dbBooks;
    }

    public ArrayList<Book> readTableBook(final String SQL_WHERE) {
        ArrayList<Book> dbBooks = new ArrayList<>();
        final boolean VALIDATES = validateQuery(SQL_WHERE);

        if (VALIDATES) {

            try {
                resultSet = statement.executeQuery("SELECT * FROM book WHERE " + SQL_WHERE + ";");

                while (resultSet.next()) {
                    dbBooks.add(new Book(resultSet.getInt(1), resultSet.getString(2),
                            resultSet.getString(3), resultSet.getInt(4),
                            resultSet.getString(5), resultSet.getString(6), resultSet.getInt(7) == 1));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }

        return dbBooks;
    }


    public ArrayList<Member> readTableMember() {
        ArrayList<Member> dbMembers = new ArrayList<>();

        try {
            resultSet = statement.executeQuery("SELECT * FROM member;");

            while (resultSet.next()) {
                dbMembers.add(new Member(null, null,
                        resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getString(4), resultSet.getString(5)));
            }
        }

        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return dbMembers;
    }

    public ArrayList<Transaction> readTableTransaction() {
        ArrayList<Transaction> dbTransactions = new ArrayList<>();

        try {
            resultSet = statement.executeQuery("SELECT * FROM transaction;");

            while (resultSet.next()) {
                dbTransactions.add(new Transaction(resultSet.getInt(1), resultSet.getTimestamp(2),
                        resultSet.getTimestamp(3), resultSet.getTimestamp(4), resultSet.getDouble(5),
                        resultSet.getInt(6), resultSet.getInt(7)));
            }
        }

        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return dbTransactions;
    }


    private boolean validateQuery(@NotNull String sql) {
        char[] sqlQuery = sql.toCharArray();
        boolean validates = true;

        for (int i = 0; i < sqlQuery.length; i++) {
            if (sqlQuery[i] == ';' && i != sql.length() - 1) {
                validates = false;
            }
        }

        return validates;
    }



}
