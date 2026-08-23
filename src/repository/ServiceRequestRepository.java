package repository;

import db.DatabaseManager;
import java.sql.Connection;

/** Repository for ServiceRequest — JDBC operations against SQLite. */
public class ServiceRequestRepository {
    protected final Connection connection = DatabaseManager.getInstance().getConnection();
    // TODO: implement save(), findAll(), findById() following LibraryRepository pattern
}
