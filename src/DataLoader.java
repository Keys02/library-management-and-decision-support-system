import db.DatabaseManager;
import model.*;
import repository.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Loads seed data from CSV files into the database.
 * Run this ONCE to populate the database before starting the application.
 *
 * Usage:
 *   java -cp "out;lib\sqlite-jdbc-3.53.2.1.jar" DataLoader
 */
public class DataLoader {

    private static final String DATA_DIR = "data/";

    public static void main(String[] args) {
        System.out.println("=== DataLoader starting ===");

        // Clear existing data first to avoid duplicates on re-runs
        clearAllTables();

        loadLibraries();
        loadRoads();
        loadResources();
        loadPatrons();
        loadBooks();
        loadServiceRequests();

        System.out.println("=== DataLoader complete ===");
    }

    // ── Clear tables in correct order (foreign keys) ────

    private static void clearAllTables() {
        System.out.println("Clearing existing data...");
        Connection conn = DatabaseManager.getInstance().getConnection();
        String[] tables = {
            "service_requests", "books", "patrons",
            "resources", "roads", "librarians", "libraries"
        };
        for (String table : tables) {
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + table)) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("  Warning clearing " + table + ": " + e.getMessage());
            }
        }
        // Reset auto-increment counters
        for (String table : tables) {
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM sqlite_sequence WHERE name=?")) {
                stmt.setString(1, table);
                stmt.executeUpdate();
            } catch (SQLException e) {
                // sqlite_sequence may not exist yet — that's fine
            }
        }
        System.out.println("  Done.");
    }

    // ── Libraries ───────────────────────────────────────

    private static void loadLibraries() {
        System.out.println("Loading libraries...");
        LibraryRepository repo = new LibraryRepository();
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(DATA_DIR + "libraries.csv"))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(",", -1);
                if (cols.length < 3) continue;
                Library lib = new Library(0,
                    cols[0].trim(),
                    cols[1].trim(),
                    cols[2].trim()
                );
                repo.save(lib);
                count++;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load libraries CSV.", e);
        }
        System.out.println("  Loaded " + count + " libraries.");
    }

    // ── Roads ───────────────────────────────────────────

    private static void loadRoads() {
        System.out.println("Loading roads...");
        RoadRepository repo = new RoadRepository();
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(DATA_DIR + "roads.csv"))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(",", -1);
                if (cols.length < 4) continue;
                Road road = new Road(0,
                    Integer.parseInt(cols[0].trim()),
                    Integer.parseInt(cols[1].trim()),
                    Double.parseDouble(cols[2].trim()),
                    Double.parseDouble(cols[3].trim())
                );
                repo.save(road);
                count++;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load roads CSV.", e);
        }
        System.out.println("  Loaded " + count + " roads.");
    }

    // ── Resources ───────────────────────────────────────

    private static void loadResources() {
        System.out.println("Loading resources...");
        ResourceRepository repo = new ResourceRepository();
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(DATA_DIR + "resources.csv"))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(",", -1);
                if (cols.length < 5) continue;
                Resource resource = new Resource(0,
                    cols[0].trim(),
                    ResourceType.valueOf(cols[1].trim()),
                    Double.parseDouble(cols[2].trim()),
                    Integer.parseInt(cols[3].trim()),
                    Integer.parseInt(cols[4].trim())
                );
                repo.save(resource);
                count++;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load resources CSV.", e);
        }
        System.out.println("  Loaded " + count + " resources.");
    }

    // ── Patrons (generated inline — no CSV needed) ──────

    private static void loadPatrons() {
        System.out.println("Loading patrons...");
        PatronRepository repo = new PatronRepository();
        String[] names = {
            "Kwame Mensah","Ama Owusu","Kofi Asante","Abena Boateng","Yaw Darko",
            "Akosua Frimpong","Kweku Agyei","Adwoa Osei","Kojo Amoah","Efua Appiah",
            "Nana Adjei","Abiba Yakubu","Alhassan Suleiman","Fatima Ibrahim","Mohammed Baba",
            "Akua Dankwa","Kwabena Poku","Maame Asare","Opoku Ware","Nana Yaa Serwaa",
            "Kwesi Brew","Adoa Antwi","Fiifi Bondzie","Esi Hanson","Kwadwo Twum",
            "Abigail Quaye","Emmanuel Tetteh","Grace Larbi","Isaac Ofori","Janet Acheampong",
            "Kwame Peprah","Linda Asante","Michael Boadu","Nancy Darko","Obed Frimpong",
            "Patricia Agyei","Quame Osei","Rita Amoah","Samuel Appiah","Theresa Adjei",
            "Usman Yakubu","Victoria Suleiman","Walter Ibrahim","Xorlali Baba","Yayra Dankwa",
            "Zenabu Poku","Ama Asare","Baaba Ware","Comfort Serwaa","Daniel Brew"
        };
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            String email = name.toLowerCase().replace(" ", ".") + "@gmail.com";
            String phone = "024" + String.format("%07d", 1000000 + i);
            repo.save(new Patron(0, name, email, phone));
        }
        System.out.println("  Loaded " + names.length + " patrons.");
    }

    // ── Books (generated inline — no CSV needed) ────────

    private static void loadBooks() {
        System.out.println("Loading books...");
        BookRepository repo = new BookRepository();
        String[][] books = {
            {"Introduction to Algorithms","Cormen et al","978-0262033848"},
            {"Data Structures in Java","Goodrich","978-1118771334"},
            {"Clean Code","Robert Martin","978-0132350884"},
            {"The Pragmatic Programmer","Hunt & Thomas","978-0135957059"},
            {"Design Patterns","Gang of Four","978-0201633610"},
            {"Ghana: A History","Adu Boahen","978-0333267882"},
            {"Things Fall Apart","Chinua Achebe","978-0385474542"},
            {"Purple Hibiscus","Chimamanda Adichie","978-1616953508"},
            {"The Beautiful Ones Are Not Yet Born","Ayi Kwei Armah","978-0435900106"},
            {"Our Sister Killjoy","Ama Ata Aidoo","978-0582642102"},
            {"Database System Concepts","Silberschatz","978-0078022159"},
            {"Computer Networks","Tanenbaum","978-0132126953"},
            {"Operating System Concepts","Silberschatz","978-1119320913"},
            {"Artificial Intelligence","Russell & Norvig","978-0136042594"},
            {"Machine Learning","Tom Mitchell","978-0070428072"},
            {"Calculus","James Stewart","978-1285740621"},
            {"Linear Algebra","Gilbert Strang","978-0980232776"},
            {"Discrete Mathematics","Rosen","978-0073383095"},
            {"Statistics for Engineers","Montgomery","978-1119585596"},
            {"Physics for Scientists","Serway","978-1337553490"},
            {"Chemistry: Central Science","Brown","978-0134414232"},
            {"Biology","Campbell & Reece","978-0321775658"},
            {"Economics","Samuelson","978-0071285544"},
            {"Principles of Management","Robbins","978-0133073409"},
            {"Accounting Principles","Weygandt","978-1119491040"},
            {"English Grammar","Murphy","978-1107539006"},
            {"Oxford English Dictionary","Oxford Press","978-0198613428"},
            {"French for Beginners","Berlitz","978-9812685551"},
            {"Atlas of West Africa","National Geographic","978-0792295549"},
            {"World History Encyclopedia","DK Books","978-0241225752"},
            {"The Kite Runner","Khaled Hosseini","978-1594631931"},
            {"To Kill a Mockingbird","Harper Lee","978-0061935466"},
            {"1984","George Orwell","978-0451524935"},
            {"Brave New World","Aldous Huxley","978-0060850524"},
            {"Animal Farm","George Orwell","978-0451526342"},
            {"Python Programming","John Zelle","978-1590282755"},
            {"Java: How to Program","Deitel","978-0134743356"},
            {"JavaScript: The Good Parts","Douglas Crockford","978-0596517748"},
            {"Learning SQL","Alan Beaulieu","978-1492057611"},
            {"Web Design","Jennifer Robbins","978-1491960196"},
            {"Project Management","Harold Kerzner","978-1119165354"},
            {"Research Methods","Creswell","978-1506386706"},
            {"Academic Writing","Bailey","978-0230377363"},
            {"Study Skills","Burns","978-0230220850"},
            {"Critical Thinking","Bassham","978-0078038310"},
            {"Public Health in Ghana","MOH Ghana","978-9988550042"},
            {"Ghanaian Cuisine","Fran Osseo-Asare","978-1558765078"},
            {"Traditional Medicine Africa","WHO","978-9241547536"},
            {"Environmental Science","Miller","978-1305090446"},
            {"Climate Change","IPCC","978-1107054820"}
        };
        for (int i = 0; i < books.length; i++) {
            int libraryId = (i % 50) + 1;
            repo.save(new Book(0, books[i][0], books[i][1], books[i][2], true, libraryId));
        }
        System.out.println("  Loaded " + books.length + " books.");
    }

    // ── Service Requests ────────────────────────────────

    private static void loadServiceRequests() {
        System.out.println("Loading service requests...");
        ServiceRequestRepository repo = new ServiceRequestRepository();
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(DATA_DIR + "service_requests.csv"))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(",", -1);
                if (cols.length < 6) continue;
                ServiceRequest req = new ServiceRequest(0,
                    Integer.parseInt(cols[0].trim()),
                    Integer.parseInt(cols[1].trim()),
                    RequestType.valueOf(cols[2].trim()),
                    Integer.parseInt(cols[3].trim()),
                    RequestStatus.valueOf(cols[4].trim()),
                    LocalDateTime.parse(cols[5].trim())
                );
                repo.save(req);
                count++;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load service_requests CSV.", e);
        }
        System.out.println("  Loaded " + count + " service requests.");
    }
}
