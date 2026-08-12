package model;

public class Library {
    private int id;
    private String libraryName;
    private String location;
    private String openHours;

    public Library(int id, String libraryName, String location, String openHours) {
        this.id = id;
        this.libraryName = libraryName;
        this.location = location;
        this.openHours = openHours;
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
