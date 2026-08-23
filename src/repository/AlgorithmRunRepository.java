package repository;

import db.DatabaseManager;
import java.sql.Connection;

/** Repository for AlgorithmRun — JDBC operations against SQLite. */
public class AlgorithmRunRepository {
    protected final Connection connection = DatabaseManager.getInstance().getConnection();
    // TODO: implement save(), findAll(), findById() following LibraryRepository pattern
}
