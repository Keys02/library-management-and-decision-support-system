package repository;

import db.DatabaseManager;
import java.sql.Connection;

/** Repository for Resource — JDBC operations against SQLite. */
public class ResourceRepository {
    protected final Connection connection = DatabaseManager.getInstance().getConnection();
    // TODO: implement save(), findAll(), findById() following LibraryRepository pattern
}
