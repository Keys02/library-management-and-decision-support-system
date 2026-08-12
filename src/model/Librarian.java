package model;

public class Librarian {
    private int id;
    private String name;
    private String email;
    private int libraryId;

    public Librarian(int id, String name, String email, int libraryId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.libraryId = libraryId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(int libraryId) {
        this.libraryId = libraryId;
    }
}
