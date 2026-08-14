# Database Setup — SQLite + Plain JDBC

**Owners:** DB1 (schema + connection), DB2 (seed data + repository classes)
**Decision locked in:** SQLite, plain JDBC, hand-written SQL — no ORM, no DAO
interface abstraction. Keep it direct: a repository class per entity, each
method runs its own SQL string through JDBC. This is the fastest path given
the timeline, and the brief only requires JDBC/database support to be used,
not a particular architecture pattern.

---

## 0. Start here, right now (first 15 minutes)

1. Download the SQLite JDBC driver jar (`sqlite-jdbc-3.53.2.1.jar`) from Maven
   Central: https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/
2. In IntelliJ: `File > Project Structure > Libraries > +` → point it at the
   downloaded jar. This project has no Maven/Gradle setup, so the jar must
   be added manually to every team member's local project (or committed
   into a `lib/` folder in the repo so everyone gets it on pull).
3. Sanity check with a throwaway `Test.java`:
   ```java
   import java.sql.Connection;
   import java.sql.DriverManager;

   public class Test {
       public static void main(String[] args) throws Exception {
           Connection conn = DriverManager.getConnection("jdbc:sqlite:library.db");
           System.out.println("Connected: " + (conn != null));
           conn.close();
       }
   }
   ```
   If this prints `Connected: true` and a `library.db` file appears in your
   project root, the driver is wired up correctly. Delete `Test.java` once
   confirmed.

---

## 1. Schema — mapping the brief's tables to our library context

The brief's Section 4 uses generic terms ("locations", "roads"). Our context
is a library network, so terms map like this:

| Brief's generic term | Our table | Why |
| --- | --- | --- |
| locations (min 50) | `libraries` | Each row is a physical library branch — matches the existing `Library.java` model |
| roads (min 100) | `roads` | Connections between library branches, already modeled in `Road.java` |
| service_requests (min 300) | `service_requests` | Already modeled in `ServiceRequest.java` |
| resources (min 30) | `resources` | Already modeled in `Resource.java` |
| algorithm_runs (min 30) | `algorithm_runs` | Already modeled in `AlgorithmRun.java` |
| audit_events | `audit_events` | New — backs the stack-based undo/audit requirement (Section 4) |

We also have `books`, `librarians`, and `patrons` — these go beyond the
brief's minimum entity list but are needed since our context is a library,
not generic logistics. Keep them; more thorough data model, no downside.

### `schema.sql`

```sql
PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS libraries (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    library_name  TEXT NOT NULL,
    location      TEXT NOT NULL,
    open_hours    TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS roads (
    id                      INTEGER PRIMARY KEY AUTOINCREMENT,
    source_library_id       INTEGER NOT NULL REFERENCES libraries(id),
    destination_library_id  INTEGER NOT NULL REFERENCES libraries(id),
    distance                REAL NOT NULL,
    travel_time             REAL NOT NULL
);

CREATE TABLE IF NOT EXISTS librarians (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    name        TEXT NOT NULL,
    email       TEXT NOT NULL,
    library_id  INTEGER NOT NULL REFERENCES libraries(id)
);

CREATE TABLE IF NOT EXISTS patrons (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    name          TEXT NOT NULL,
    email         TEXT NOT NULL,
    phone_number  TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS books (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    title       TEXT NOT NULL,
    author      TEXT NOT NULL,
    isbn        TEXT NOT NULL,
    available   INTEGER NOT NULL DEFAULT 1,   -- SQLite has no boolean; 0/1
    library_id  INTEGER NOT NULL REFERENCES libraries(id)
);

CREATE TABLE IF NOT EXISTS service_requests (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    patron_id     INTEGER NOT NULL REFERENCES patrons(id),
    book_id       INTEGER NOT NULL REFERENCES books(id),
    request_type  TEXT NOT NULL,   -- BORROW / RETURN / RESERVE / RENEW
    urgency       INTEGER NOT NULL,
    status        TEXT NOT NULL,   -- PENDING / PROCESSING / COMPLETED / CANCELLED
    created_at    TEXT NOT NULL    -- ISO-8601 string; SQLite has no native datetime type
);

CREATE TABLE IF NOT EXISTS resources (
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    name      TEXT NOT NULL,
    type      TEXT NOT NULL,   -- BOOK / EQUIPMENT / ROOM / DEVICE
    cost      REAL NOT NULL,
    quantity  INTEGER NOT NULL,
    value     INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS algorithm_runs (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    algorithm_name  TEXT NOT NULL,
    input_size      INTEGER NOT NULL,
    time_ns         INTEGER NOT NULL,
    memory_kb       INTEGER NOT NULL,
    date_run        TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS audit_events (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    event_type  TEXT NOT NULL,
    description TEXT NOT NULL,
    created_at  TEXT NOT NULL
);
```

**Important:** `status`, `request_type`, and `resource.type` are stored as
`TEXT` in SQLite (it has no enum type), but the Java side uses the
`RequestStatus`, `RequestType`, `ResourceType` enums we already built. The
repository layer is responsible for converting `enum.name()` ↔ `TEXT` at
the JDBC boundary — see Section 4 below. This is exactly why we built those
enums before touching the database: the conversion point is now a single,
obvious spot instead of scattered string comparisons.

Run this once per fresh `library.db`:
```bash
sqlite3 library.db < schema.sql
```

---

## 2. Seed data — meeting the brief's minimums

| Table | Minimum required | Notes |
| --- | --- | --- |
| `libraries` | 50 | Ghana-localized branch names/areas — real or plausible Ghanaian towns/neighborhoods |
| `roads` | 100 | Distances/travel times between branch pairs; don't need every pair connected, just a realistic sparse network |
| `service_requests` | 300 | Mix of request types/urgency/status so filtering and sorting demos have real variety |
| `resources` | 30 | Mix of all 4 `ResourceType` values |
| `algorithm_runs` | 30+ | These get generated by your own timing harness later — don't hand-seed these, they're a byproduct of running the empirical experiments |

**Do not hand-type 300+ rows.** DB2 should write a small script (Python or
a throwaway Java program) that generates CSVs with randomized but
plausible data — realistic Ghanaian place names for `libraries`, varied
enough `urgency`/`status`/`request_type` combinations for `service_requests`
to make FIFO vs. priority-queue demos actually show a difference.

Output as CSVs (`libraries.csv`, `roads.csv`, `patrons.csv`, `books.csv`,
`service_requests.csv`, `resources.csv`) sitting in a `/data` folder in the
repo. Import with SQLite's built-in CSV import rather than writing a
custom Java CSV parser first — that's not what's being graded here:
```bash
sqlite3 library.db
.mode csv
.import data/libraries.csv libraries
.import data/roads.csv roads
.import data/patrons.csv patrons
.import data/books.csv books
.import data/service_requests.csv service_requests
.import data/resources.csv resources
```
(Watch out for header rows — either strip the header line from the CSV
first, or `DELETE` the resulting header-row record after import.)

The brief also wants CSV import to happen through the running Java program
(Module M2), not just the `sqlite3` CLI — see Section 5 below.

---

## 3. Connection management

One class, one shared connection, mirroring the existing `Application`
singleton pattern already in the codebase:

```java
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static DatabaseManager instance;
    private final Connection connection;

    private static final String DB_URL = "jdbc:sqlite:library.db";

    private DatabaseManager() {
        try {
            this.connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            throw new RuntimeException("Could not connect to database.", e);
        }
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
```

`db` is a new package, same level as `model` and `exception`.

---

## 4. Repository classes — hand-written SQL, one class per entity

No interfaces, no abstraction — a concrete class per table, each method
runs its own SQL. Example for `libraries`:

```java
package db;

import model.Library;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryRepository {
    private final Connection connection = DatabaseManager.getInstance().getConnection();

    public void save(Library library) {
        String sql = "INSERT INTO libraries (library_name, location, open_hours) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, library.getLibraryName());
            stmt.setString(2, library.getLocation());
            stmt.setString(3, library.getOpenHours());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save library.", e);
        }
    }

    public List<Library> findAll() {
        List<Library> libraries = new ArrayList<>();
        String sql = "SELECT id, library_name, location, open_hours FROM libraries";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                libraries.add(new Library(
                    rs.getInt("id"),
                    rs.getString("library_name"),
                    rs.getString("location"),
                    rs.getString("open_hours")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load libraries.", e);
        }
        return libraries;
    }
}
```

**Enum ↔ TEXT conversion example** (this is the one place enum values touch
SQL — for `ServiceRequestRepository`):

```java
// Writing: enum -> TEXT
stmt.setString(4, request.getRequestType().name());   // "BORROW"
stmt.setString(6, request.getStatus().name());         // "PENDING"

// Reading: TEXT -> enum
RequestType type = RequestType.valueOf(rs.getString("request_type"));
RequestStatus status = RequestStatus.valueOf(rs.getString("status"));
```

DB2 should build one repository per table following this exact pattern:
`LibraryRepository`, `RoadRepository`, `BookRepository`, `PatronRepository`,
`LibrarianRepository`, `ServiceRequestRepository`, `ResourceRepository`,
`AlgorithmRunRepository`, `AuditEventRepository`.

---

## 5. How this connects to the Java backend (the piece from the architecture diagram)

Recall the layered diagram: **UI → Service layer → (in-memory engine ↔ database access)**.
Concretely, at startup:

1. `ApplicationController` calls each repository's `findAll()` once.
2. The returned `List<Book>`, `List<Library>`, etc. get fed into the
   **custom data structures** (not `ArrayList` — once JP1–JP4 have built
   the custom linked list / hash table / graph, loading means "take this
   JDBC result and insert it into our own structure," not "use the JDBC
   result directly").
3. From then on, algorithms operate purely on the in-memory custom
   structures — the database is not touched again during a search/sort/
   graph run. This matters for the performance experiments: you're timing
   your own data structures and algorithms, not JDBC round-trips.
4. When a `ServiceRequest` status changes, or an `AlgorithmRun` completes,
   the service layer calls the relevant repository's `save()`/`update()`
   to persist it back.

This is also why DB and Java can build in parallel this week: JP1–JP4 can
develop and test their structures against **hardcoded or CSV-loaded test
data** without waiting for DB1/DB2 to finish the repositories — the
integration point is just "a `List<Book>` goes in." Once repositories
exist, that `List<Book>` source switches from a hardcoded list to
`bookRepository.findAll()` with no change to the data-structure code
itself.

---

## 6. This week's DB split

- **DB1:** `schema.sql`, `DatabaseManager`, and the first working
  end-to-end repository (`LibraryRepository`) as a proof of pattern.
- **DB2:** seed data generation scripts + CSVs, CSV import path, and the
  remaining repository classes following DB1's established pattern.

Sync point: DB1 should finish `LibraryRepository` and share the pattern
with DB2 on Day 1, so DB2 isn't guessing at conventions while writing the
other eight repositories.
