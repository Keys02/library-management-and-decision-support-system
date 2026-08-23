package repository;

import db.DatabaseManager;
import java.sql.Connection;

/** Repository for Road — JDBC operations against SQLite. */
public class RoadRepository {
    protected final Connection connection = DatabaseManager.getInstance().getConnection();
    // TODO: implement save(), findAll(), findById() following LibraryRepository pattern
}
