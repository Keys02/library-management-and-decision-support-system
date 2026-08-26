package repository;

import db.DatabaseManager;
import model.Patron;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import datastructures.linear.LinkedList;

public class PatronRepository {
    private final Connection connection = DatabaseManager.getInstance().getConnection();

    public void save(Patron patron) {
        String sql = "INSERT INTO patrons (name, email, phone_number) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, patron.getName());
            stmt.setString(2, patron.getEmail());
            stmt.setString(3, patron.getPhoneNumber());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save patron.", e);
        }
    }

    public LinkedList<Patron> findAll() {
        LinkedList<Patron> patrons = new LinkedList<>();
        String sql = "SELECT id, name, email, phone_number FROM patrons";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                patrons.addLast(new Patron(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone_number")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load patrons.", e);
        }
        return patrons;
    }

    public Patron findById(int id) {
        String sql = "SELECT id, name, email, phone_number FROM patrons WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Patron(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone_number")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load patron with id " + id, e);
        }
        return null;
    }
}
