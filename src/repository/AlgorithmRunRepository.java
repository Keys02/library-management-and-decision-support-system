package repository;

import db.DatabaseManager;
import model.AlgorithmRun;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlgorithmRunRepository {
    private final Connection connection = DatabaseManager.getInstance().getConnection();

    public void save(AlgorithmRun run) {
        String sql = "INSERT INTO algorithm_runs (algorithm_name, input_size, time_ns, memory_kb, date_run) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, run.getAlgorithmName());
            stmt.setInt(2, run.getInputSize());
            stmt.setLong(3, run.getTimeNs());
            stmt.setLong(4, run.getMemoryKb());
            stmt.setString(5, run.getDateRun().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save algorithm run.", e);
        }
    }

    public List<AlgorithmRun> findAll() {
        List<AlgorithmRun> runs = new ArrayList<>();
        String sql = "SELECT id, algorithm_name, input_size, time_ns, memory_kb, date_run FROM algorithm_runs";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                runs.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load algorithm runs.", e);
        }
        return runs;
    }

    public AlgorithmRun findById(int id) {
        String sql = "SELECT id, algorithm_name, input_size, time_ns, memory_kb, date_run FROM algorithm_runs WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load algorithm run with id " + id, e);
        }
        return null;
    }

    /** All runs for a given algorithm — convenience method for building the performance graphs. */
    public List<AlgorithmRun> findByAlgorithmName(String algorithmName) {
        List<AlgorithmRun> runs = new ArrayList<>();
        String sql = "SELECT id, algorithm_name, input_size, time_ns, memory_kb, date_run "
            + "FROM algorithm_runs WHERE algorithm_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, algorithmName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    runs.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load runs for algorithm " + algorithmName, e);
        }
        return runs;
    }

    private AlgorithmRun mapRow(ResultSet rs) throws SQLException {
        return new AlgorithmRun(
            rs.getInt("id"),
            rs.getString("algorithm_name"),
            rs.getInt("input_size"),
            rs.getLong("time_ns"),
            rs.getLong("memory_kb"),
            LocalDateTime.parse(rs.getString("date_run"))
        );
    }
}
