package repository;

import db.DatabaseManager;
import model.Library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LibraryRepository {
    private final Connection connection = DatabaseManager.getInstance().getConnection();

    public void save(Library library) {
        String sql = "INSERT INTO libraries (library_name, location, open_hours) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, library.getLibraryName());
            stmt.setString(2, library.getLocation());
            stmt.setString(3, library.getOpenHours());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save library.", e);
        }
    }

    public List<Library> findAll() {
        List<Library> libraries = new ArrayList<>();
        String sql = "SELECT id, library_name, location, open_hours FROM libraries";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                libraries.add(new Library(
                    rs.getInt("id"),
                    rs.getString("library_name"),
                    rs.getString("location"),
                    rs.getString("open_hours")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load libraries.", e);
        }
        return libraries;
    }

    public Library findById(int id) {
        String sql = "SELECT id, library_name, location, open_hours FROM libraries WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Library(
                        rs.getInt("id"),
                        rs.getString("library_name"),
                        rs.getString("location"),
                        rs.getString("open_hours")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load library with id " + id, e);
        }
        return null;
    }
}