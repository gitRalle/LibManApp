package objects;

import storingOfData.DB_Connection;

import java.util.ArrayList;

public class Book {

    private int bookId;
    private final String title;
    private final String author;
    private final int numberOfPages;
    private final String language;
    private final String publisher;
    private boolean isAvailable;
    private final static DB_Connection connection = new DB_Connection();

    /**
     * 1st CONSTRUCTOR - used temp - excludes bookId
     *
     * @param title
     * @param author
     * @param numberOfPages
     * @param language
     * @param publisher
     * @throws IllegalArgumentException
     */
    public Book(String title, String author, int numberOfPages,
                String language, String publisher) throws IllegalArgumentException
    {
        ArrayList<Book> appBooks = connection.readTableBook();
        for (Book currentBook : appBooks) {
            if (currentBook.getTitle().equalsIgnoreCase(title)) {
                throw new IllegalArgumentException(
                        "book already exists in db");
            }
        }

        this.title = title;
        this.author = author;
        this.numberOfPages = numberOfPages;
        this.language = language;
        this.publisher = publisher;
        this.isAvailable = true;
    }

    /**
     * 2nd CONSTRUCTOR - used after first dbInsert - includes bookId
     *
     * @param bookId
     * @param title
     * @param author
     * @param numberOfPages
     * @param language
     * @param publisher
     * @param isAvailable
     */
    public Book(int bookId, String title, String author, int numberOfPages,
                String language, String publisher, boolean isAvailable) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.numberOfPages = numberOfPages;
        this.language = language;
        this.publisher = publisher;
        this.isAvailable = isAvailable;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public String getLanguage() {
        return language;
    }

    public String getPublisher() {
        return publisher;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

}
