package objects;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Transaction {
    private int transactionId;
    private final LocalDateTime issueDate;
    private final LocalDateTime dueDate;
    private LocalDateTime returnDate = null;
    private double lateFee;

    private final int bookId;
    private final int memberId;


    /**
     * CONSTRUCTOR 1: TEMP
     */
    public Transaction(int bookId, int memberId) {
        issueDate = LocalDateTime.now();
        dueDate = issueDate.plusMonths(3);
        returnDate = null;
        this.bookId = bookId;
        this.memberId = memberId;

    }

    /**
     * CONSTRUCTOR 2: 'REAL'
     *
     * @param transactionId
     * @param issueDate
     * @param dueDate
     * @param bookId
     * @param memberId
     */
    public Transaction(int transactionId, LocalDateTime issueDate, LocalDateTime dueDate,
                        int bookId, int memberId) {
        this.transactionId = transactionId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.bookId = bookId;
        this.memberId = memberId;
    }

    /**
     * READ FROM DB - ('FULL CONSTRUCTOR')
     *
     * @param transactionId
     * @param issueDate
     * @param dueDate
     * @param returnDate
     * @param lateFee
     * @param bookId
     * @param memberId
     */
    public Transaction(int transactionId, Timestamp issueDate, Timestamp dueDate, Timestamp returnDate,
                       double lateFee, int bookId, int memberId) {

        this.transactionId = transactionId;
        this.issueDate = issueDate.toLocalDateTime();
        this.dueDate = dueDate.toLocalDateTime();
        if (returnDate != null) {
            this.returnDate = returnDate.toLocalDateTime();
        }
        this.lateFee = lateFee;
        this.bookId = bookId;
        this.memberId = memberId;
    }


    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public double getLateFee() {
        return lateFee;
    }

    public LocalDateTime getReturnDate() {
       return returnDate;
    }

    public int getBookId() {
        return bookId;
    }

    public int getMemberId() {
        return memberId;
    }
}
