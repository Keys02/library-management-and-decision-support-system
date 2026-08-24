import repository.LibraryRepository;
import model.Library;

import java.util.List;

public class TestDb {
    public static void main(String[] args) {
        repository.LibraryRepository repo = new repository.LibraryRepository();

        Library accraCentral = new Library(0, "Accra Central Library", "Accra", "8am - 6pm");
        repo.save(accraCentral);

        List<Library> all = repo.findAll();
        System.out.println("Libraries in database: " + all.size());
        for (Library library : all) {
            System.out.println(
                "Library{id=" + library.getId()
                + ", name='" + library.getLibraryName() + "'"
                + ", location='" + library.getLocation() + "'"
                + ", openHours='" + library.getOpenHours() + "'}"
            );
        }
    }
}