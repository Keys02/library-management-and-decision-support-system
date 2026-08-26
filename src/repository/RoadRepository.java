package repository;

import db.DatabaseManager;
import model.Road;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import datastructures.linear.LinkedList;

public class RoadRepository {
    private final Connection connection = DatabaseManager.getInstance().getConnection();

    public void save(Road road) {
        String sql = "INSERT INTO roads (source_library_id, destination_library_id, distance, travel_time) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, road.getSourceLibraryId());
            stmt.setInt(2, road.getDestinationLibraryId());
            stmt.setDouble(3, road.getDistance());
            stmt.setDouble(4, road.getTravelTime());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save road.", e);
        }
    }

    public LinkedList<Road> findAll() {
        LinkedList<Road> roads = new LinkedList<>();
        String sql = "SELECT id, source_library_id, destination_library_id, distance, travel_time FROM roads";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                roads.addLast(new Road(
                    rs.getInt("id"),
                    rs.getInt("source_library_id"),
                    rs.getInt("destination_library_id"),
                    rs.getDouble("distance"),
                    rs.getDouble("travel_time")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load roads.", e);
        }
        return roads;
    }

    public Road findById(int id) {
        String sql = "SELECT id, source_library_id, destination_library_id, distance, travel_time FROM roads WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Road(
                        rs.getInt("id"),
                        rs.getInt("source_library_id"),
                        rs.getInt("destination_library_id"),
                        rs.getDouble("distance"),
                        rs.getDouble("travel_time")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load road with id " + id, e);
        }
        return null;
    }

    /** All roads touching a given library — convenience method for building the graph. */
    public LinkedList<Road> findByLibraryId(int libraryId) {
        LinkedList<Road> roads = new LinkedList<>();
        String sql = "SELECT id, source_library_id, destination_library_id, distance, travel_time "
            + "FROM roads WHERE source_library_id = ? OR destination_library_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, libraryId);
            stmt.setInt(2, libraryId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    roads.addLast(new Road(
                        rs.getInt("id"),
                        rs.getInt("source_library_id"),
                        rs.getInt("destination_library_id"),
                        rs.getDouble("distance"),
                        rs.getDouble("travel_time")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load roads for library " + libraryId, e);
        }
        return roads;
    }
}
