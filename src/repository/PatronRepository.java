package repository;

import db.DatabaseManager;
import java.sql.Connection;

/** Repository for Patron — JDBC operations against SQLite. */
public class PatronRepository {
    protected final Connection connection = DatabaseManager.getInstance().getConnection();
    // TODO: implement save(), findAll(), findById() following LibraryRepository pattern
}
