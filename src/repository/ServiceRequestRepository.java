package repository;

import db.DatabaseManager;
import model.ServiceRequest;
import model.RequestType;
import model.RequestStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceRequestRepository {
    private final Connection connection = DatabaseManager.getInstance().getConnection();

    public void save(ServiceRequest request) {
        String sql = "INSERT INTO service_requests (patron_id, book_id, request_type, urgency, status, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, request.getPatronId());
            stmt.setInt(2, request.getBookId());
            // enum -> TEXT for both enum fields
            stmt.setString(3, request.getRequestType().name());
            stmt.setInt(4, request.getUrgency());
            stmt.setString(5, request.getStatus().name());
            // LocalDateTime -> TEXT: toString() produces ISO-8601, matching
            // what LocalDateTime.parse() expects when reading it back
            stmt.setString(6, request.getCreatedAt().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save service request.", e);
        }
    }

    public List<ServiceRequest> findAll() {
        List<ServiceRequest> requests = new ArrayList<>();
        String sql = "SELECT id, patron_id, book_id, request_type, urgency, status, created_at FROM service_requests";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                requests.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load service requests.", e);
        }
        return requests;
    }

    public ServiceRequest findById(int id) {
        String sql = "SELECT id, patron_id, book_id, request_type, urgency, status, created_at FROM service_requests WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load service request with id " + id, e);
        }
        return null;
    }

    /** Requests filtered by status — convenience method for the dispatch queue. */
    public List<ServiceRequest> findByStatus(RequestStatus status) {
        List<ServiceRequest> requests = new ArrayList<>();
        String sql = "SELECT id, patron_id, book_id, request_type, urgency, status, created_at "
            + "FROM service_requests WHERE status = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load service requests with status " + status, e);
        }
        return requests;
    }

    public void updateStatus(int id, RequestStatus status) {
        String sql = "UPDATE service_requests SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update status for service request " + id, e);
        }
    }

    private ServiceRequest mapRow(ResultSet rs) throws SQLException {
        return new ServiceRequest(
            rs.getInt("id"),
            rs.getInt("patron_id"),
            rs.getInt("book_id"),
            // TEXT -> enum, both fields
            RequestType.valueOf(rs.getString("request_type")),
            rs.getInt("urgency"),
            RequestStatus.valueOf(rs.getString("status")),
            // TEXT -> LocalDateTime
            LocalDateTime.parse(rs.getString("created_at"))
        );
    }
}
