package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static DatabaseManager instance;
    private final Connection connection;

    private static final String DB_URL = "jdbc:sqlite:library.db";

    private DatabaseManager() {
        try {
            this.connection = DriverManager.getConnection(DB_URL);
            this.connection.createStatement().execute("PRAGMA foreign_keys = ON");
            createTables();
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

    private void createTables() throws SQLException {
        String[] statements = {
            """
            CREATE TABLE IF NOT EXISTS libraries (
                id            INTEGER PRIMARY KEY AUTOINCREMENT,
                library_name  TEXT NOT NULL,
                location      TEXT NOT NULL,
                open_hours    TEXT NOT NULL
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS roads (
                id                      INTEGER PRIMARY KEY AUTOINCREMENT,
                source_library_id       INTEGER NOT NULL REFERENCES libraries(id),
                destination_library_id  INTEGER NOT NULL REFERENCES libraries(id),
                distance                REAL NOT NULL,
                travel_time             REAL NOT NULL
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS librarians (
                id          INTEGER PRIMARY KEY AUTOINCREMENT,
                name        TEXT NOT NULL,
                email       TEXT NOT NULL,
                library_id  INTEGER NOT NULL REFERENCES libraries(id)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS patrons (
                id            INTEGER PRIMARY KEY AUTOINCREMENT,
                name          TEXT NOT NULL,
                email         TEXT NOT NULL,
                phone_number  TEXT NOT NULL
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS books (
                id          INTEGER PRIMARY KEY AUTOINCREMENT,
                title       TEXT NOT NULL,
                author      TEXT NOT NULL,
                isbn        TEXT NOT NULL,
                available   INTEGER NOT NULL DEFAULT 1,
                library_id  INTEGER NOT NULL REFERENCES libraries(id)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS service_requests (
                id            INTEGER PRIMARY KEY AUTOINCREMENT,
                patron_id     INTEGER NOT NULL REFERENCES patrons(id),
                book_id       INTEGER NOT NULL REFERENCES books(id),
                request_type  TEXT NOT NULL,
                urgency       INTEGER NOT NULL,
                status        TEXT NOT NULL,
                created_at    TEXT NOT NULL
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS resources (
                id        INTEGER PRIMARY KEY AUTOINCREMENT,
                name      TEXT NOT NULL,
                type      TEXT NOT NULL,
                cost      REAL NOT NULL,
                quantity  INTEGER NOT NULL,
                value     INTEGER NOT NULL
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS algorithm_runs (
                id              INTEGER PRIMARY KEY AUTOINCREMENT,
                algorithm_name  TEXT NOT NULL,
                input_size      INTEGER NOT NULL,
                time_ns         INTEGER NOT NULL,
                memory_kb       INTEGER NOT NULL,
                date_run        TEXT NOT NULL
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS audit_events (
                id          INTEGER PRIMARY KEY AUTOINCREMENT,
                event_type  TEXT NOT NULL,
                description TEXT NOT NULL,
                created_at  TEXT NOT NULL
            )
            """
        };

        try (Statement stmt = connection.createStatement()) {
            for (String sql : statements) {
                stmt.execute(sql);
            }
        }
    }
}