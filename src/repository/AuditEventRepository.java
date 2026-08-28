package repository;

import db.DatabaseManager;
import model.AuditEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import datastructures.linear.LinkedList;

public class AuditEventRepository {
    private final Connection connection = DatabaseManager.getInstance().getConnection();

    public void save(AuditEvent event) {
        String sql = "INSERT INTO audit_events (event_type, description, created_at) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, event.getEventType());
            stmt.setString(2, event.getDescription());
            stmt.setString(3, event.getCreatedAt().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save audit event.", e);
        }
    }

    public LinkedList<AuditEvent> findAll() {
        LinkedList<AuditEvent> events = new LinkedList<>();
        String sql = "SELECT id, event_type, description, created_at FROM audit_events";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                events.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load audit events.", e);
        }
        return events;
    }

    public AuditEvent findById(int id) {
        String sql = "SELECT id, event_type, description, created_at FROM audit_events WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load audit event with id " + id, e);
        }
        return null;
    }

    private AuditEvent mapRow(ResultSet rs) throws SQLException {
        return new AuditEvent(
            rs.getInt("id"),
            rs.getString("event_type"),
            rs.getString("description"),
            LocalDateTime.parse(rs.getString("created_at"))
        );
    }
}
