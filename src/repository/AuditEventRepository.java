package repository;

import db.DatabaseManager;
import java.sql.Connection;

/** Repository for AuditEvent — JDBC operations against SQLite. */
public class AuditEventRepository {
    protected final Connection connection = DatabaseManager.getInstance().getConnection();
    // TODO: implement save(), findAll(), findById() following LibraryRepository pattern
}
