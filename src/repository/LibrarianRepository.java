package repository;

import db.DatabaseManager;
import model.Librarian;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import datastructures.linear.LinkedList;

public class LibrarianRepository {
    private final Connection connection = DatabaseManager.getInstance().getConnection();

    public void save(Librarian librarian) {
        String sql = "INSERT INTO librarians (name, email, library_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, librarian.getName());
            stmt.setString(2, librarian.getEmail());
            stmt.setInt(3, librarian.getLibraryId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save librarian.", e);
        }
    }

    public LinkedList<Librarian> findAll() {
        LinkedList<Librarian> librarians = new LinkedList<>();
        String sql = "SELECT id, name, email, library_id FROM librarians";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                librarians.addLast(new Librarian(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getInt("library_id")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load librarians.", e);
        }
        return librarians;
    }

    public Librarian findById(int id) {
        String sql = "SELECT id, name, email, library_id FROM librarians WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Librarian(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getInt("library_id")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load librarian with id " + id, e);
        }
        return null;
    }
}
