package pwydmuch.service;

import pwydmuch.domain.Board;
import pwydmuch.domain.dtos.GameConfig;
import pwydmuch.domain.dtos.GameState;
import pwydmuch.domain.dtos.Point;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import org.flywaydb.core.Flyway;

public class GameStateService {
    private final String dbUrl;
    
    public GameStateService() {
        this("jdbc:sqlite:minesweeper.db");
    }
    
    public GameStateService(String dbUrl) {
        this.dbUrl = dbUrl;
        Flyway flyway = Flyway.configure().dataSource(dbUrl, null, null).load();
        flyway.migrate();
    }
    
    public void saveGameState(Board board, int time) {
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            conn.setAutoCommit(false);
            try {
                // Clear previous state
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("DELETE FROM fields");
                    stmt.execute("DELETE FROM game_state");
                }
                
                // Save game state
                try (PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO game_state (rows, columns, mines_number, game_status, time, remaining_flags) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                    
                    GameState gameState = board.getGameState();
                    GameConfig config = board.getGameConfig();
                    
                    pstmt.setInt(1, config.rows());
                    pstmt.setInt(2, config.columns());
                    pstmt.setInt(3, config.minesNumber());
                    pstmt.setString(4, gameState.gameStatus().name());
                    pstmt.setInt(5, time);
                    pstmt.setInt(6, gameState.remainingFlagsToSet());
                    
                    pstmt.executeUpdate();
                    
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            long gameId = rs.getLong(1);
                            saveFields(conn, gameId, gameState);
                        }
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save game state", e);
        }
    }
    
    private void saveFields(Connection conn, long gameId, GameState gameState) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(
            "INSERT INTO fields (game_id, row, column, state, has_mine, mines_around) VALUES (?, ?, ?, ?, ?, ?)")) {
            
            for (var field : gameState.fieldDtos()) {
                pstmt.setLong(1, gameId);
                pstmt.setInt(2, field.row());
                pstmt.setInt(3, field.column());
                pstmt.setString(4, field.fieldState().name());
                pstmt.setBoolean(5, gameState.minePoints().contains(new Point(field.row(), field.column())));
                pstmt.setObject(6, field.minesAround());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }
    
    public Board loadLastGameState() {
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            try (Statement stmt = conn.createStatement()) {
                // Get the game state
                try (ResultSet rs = stmt.executeQuery("SELECT * FROM game_state")) {
                    if (rs.next()) {
                        int rows = rs.getInt("rows");
                        int columns = rs.getInt("columns");
                        int minesNumber = rs.getInt("mines_number");

                        // Get mine points
                        Set<Point> minePoints = new HashSet<>();
                        try (PreparedStatement pstmt = conn.prepareStatement(
                            "SELECT row, column FROM fields WHERE has_mine = true")) {
                            try (ResultSet fieldsRs = pstmt.executeQuery()) {
                                while (fieldsRs.next()) {
                                    minePoints.add(new Point(
                                        fieldsRs.getInt("row"),
                                        fieldsRs.getInt("column")
                                    ));
                                }
                            }
                        }

                        // Create the board with mine points
                        Board board = new Board(rows, columns, minePoints);

                        // Restore field states
                        try (PreparedStatement pstmt = conn.prepareStatement(
                            "SELECT row, column, state FROM fields")) {
                            try (ResultSet fieldsRs = pstmt.executeQuery()) {
                                while (fieldsRs.next()) {
                                    int row = fieldsRs.getInt("row");
                                    int col = fieldsRs.getInt("column");
                                    String state = fieldsRs.getString("state");
                                    // Set the field state using right/left click simulation
                                    switch (state) {
                                        case "FLAG" -> board.clickRight(row, col);
                                        case "REVEALED" -> board.clickLeft(row, col);
                                        // NOT_MARKED and QUESTION_MARK are not handled for simplicity
                                    }
                                }
                            }
                        }
                        return board;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load game state", e);
        }
        return null;
    }
    
    public void clearSavedState() {
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("DELETE FROM fields");
                stmt.execute("DELETE FROM game_state");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clear saved state", e);
        }
    }
} 