package model;

import datastructures.linear.LinkedList;

public class Library {
    private int id;
    private String libraryName;
    private String location;
    private String openHours;

    private final LinkedList<Book> books;
    private final LinkedList<Librarian> librarians;
    private final LinkedList<Patron> patrons;

    public Library(int id, String libraryName, String location, String openHours) {
        this.id = id;
        this.libraryName = libraryName;
        this.location = location;
        this.openHours = openHours;
        this.books = new LinkedList<>();
        this.librarians = new LinkedList<>();
        this.patrons = new LinkedList<>();
    }

    public void addBook(Book book)           { books.addLast(book); }
    public void removeBook(Book book)        { books.remove(books.indexOf(book)); }
    public void addLibrarian(Librarian l)    { librarians.addLast(l); }
    public void registerPatron(Patron p)     { patrons.addLast(p); }
    public LinkedList<Book> getBooks()       { return books; }
    public LinkedList<Librarian> getLibrarians() { return librarians; }

    public int getId()                  { return id; }
    public void setId(int id)           { this.id = id; }
    public String getLibraryName()      { return libraryName; }
    public void setLibraryName(String n){ this.libraryName = n; }
    public String getLocation()         { return location; }
    public void setLocation(String l)   { this.location = l; }
    public String getOpenHours()        { return openHours; }
    public void setOpenHours(String h)  { this.openHours = h; }
}
