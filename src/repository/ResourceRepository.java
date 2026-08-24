package repository;

import db.DatabaseManager;
import model.Resource;
import model.ResourceType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ResourceRepository {
    private final Connection connection = DatabaseManager.getInstance().getConnection();

    public void save(Resource resource) {
        String sql = "INSERT INTO resources (name, type, cost, quantity, value) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, resource.getName());
            // enum -> TEXT: store the enum constant's name, e.g. "BOOK"
            stmt.setString(2, resource.getType().name());
            stmt.setDouble(3, resource.getCost());
            stmt.setInt(4, resource.getQuantity());
            stmt.setInt(5, resource.getValue());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save resource.", e);
        }
    }

    public List<Resource> findAll() {
        List<Resource> resources = new ArrayList<>();
        String sql = "SELECT id, name, type, cost, quantity, value FROM resources";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                resources.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load resources.", e);
        }
        return resources;
    }

    public Resource findById(int id) {
        String sql = "SELECT id, name, type, cost, quantity, value FROM resources WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load resource with id " + id, e);
        }
        return null;
    }

    private Resource mapRow(ResultSet rs) throws SQLException {
        return new Resource(
            rs.getInt("id"),
            rs.getString("name"),
            // TEXT -> enum: ResourceType.valueOf() throws IllegalArgumentException
            // if the stored string doesn't match a known constant — that's a good
            // thing, it means bad data gets caught immediately rather than silently.
            ResourceType.valueOf(rs.getString("type")),
            rs.getDouble("cost"),
            rs.getInt("quantity"),
            rs.getInt("value")
        );
    }
}
