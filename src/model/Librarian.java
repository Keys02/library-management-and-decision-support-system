package model;

public class Librarian {
    private int id;
    private String name;
    private String email;
    private int libraryId;

    public Librarian(
            int id,
            String name,
            String email,
            int libraryId
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.libraryId = libraryId;
    }

    public String getDetails() {
        return String.format(
            "Librarian{id=%d, name='%s', email='%s', libraryId=%d}",
            id,
            name,
            email,
            libraryId
        );
    }

    public void updateEmail(String email) {
        this.email = email;
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

    public int getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(int libraryId) {
        this.libraryId = libraryId;
    }
}
