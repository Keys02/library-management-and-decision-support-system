package repository;

import db.DatabaseManager;
import java.sql.Connection;

/** Repository for Librarian — JDBC operations against SQLite. */
public class LibrarianRepository {
    protected final Connection connection = DatabaseManager.getInstance().getConnection();
    // TODO: implement save(), findAll(), findById() following LibraryRepository pattern
}
