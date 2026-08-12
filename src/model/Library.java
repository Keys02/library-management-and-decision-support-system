package model;
import java.util.ArrayList;
import java.util.List;

public class Library {
    private int id;
    private String libraryName;
    private String location;
    private String openHours;

    private final List<Book> books;
    private final List<Librarian> librarians;
    private final List<Patron> patrons;

    public Library(
            int id,
            String libraryName,
            String location,
            String openHours
    ) {
        this.id = id;
        this.libraryName = libraryName;
        this.location = location;
        this.openHours = openHours;

        this.books = new ArrayList<>();
        this.librarians = new ArrayList<>();
        this.patrons = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    public void addLibrarian(Librarian librarian) {
        librarians.add(librarian);
    }

    public void registerPatron(Patron patron) {
        patrons.add(patron);
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Librarian> getLibrarians() {
        return librarians;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }
}