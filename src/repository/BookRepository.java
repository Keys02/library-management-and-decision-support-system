package repository;

import db.DatabaseManager;
import model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    private final Connection connection = DatabaseManager.getInstance().getConnection();

    public void save(Book book) {
        String sql = "INSERT INTO books (title, author, isbn, available, library_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getIsbn());
            stmt.setInt(4, book.isAvailable() ? 1 : 0);
            stmt.setInt(5, book.getLibraryId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save book.", e);
        }
    }

    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT id, title, author, isbn, available, library_id FROM books";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load books.", e);
        }
        return books;
    }

    public Book findById(int id) {
        String sql = "SELECT id, title, author, isbn, available, library_id FROM books WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load book with id " + id, e);
        }
        return null;
    }

    /** Updates only the availability flag — used by markBorrowed()/markReturned() flows. */
    public void updateAvailability(int id, boolean available) {
        String sql = "UPDATE books SET available = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, available ? 1 : 0);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update availability for book " + id, e);
        }
    }

    private Book mapRow(ResultSet rs) throws SQLException {
        return new Book(
            rs.getInt("id"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getString("isbn"),
            rs.getInt("available") == 1,
            rs.getInt("library_id")
        );
    }
}
