package repository;

import db.DatabaseManager;
import java.sql.Connection;

/** Repository for Book — JDBC operations against SQLite. */
public class BookRepository {
    protected final Connection connection = DatabaseManager.getInstance().getConnection();
    // TODO: implement save(), findAll(), findById() following LibraryRepository pattern
}
