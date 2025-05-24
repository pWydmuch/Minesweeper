package pwydmuch;

import pwydmuch.model.Board;
import pwydmuch.view.MainView;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Minesweeper implements Serializable {

    @Serial
    private static final long serialVersionUID = 1161474195722226307L;
    private static final Path SAVE_FILE = Path.of("game-state.ser");
    private static final String DB_URL = "jdbc:sqlite:minesweeper.db";


    public static void save(MainView sap) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try (var file = new ObjectOutputStream(Files.newOutputStream(SAVE_FILE))) {
                file.writeObject(sap);
            } catch (Exception ignored) {
            }
        }));
    }

    public static void main(String[] args) {
        connectToDatabase();
        try (var file = new ObjectInputStream(Files.newInputStream(SAVE_FILE))) {
            var gameState = (MainView) file.readObject();
            gameState.getFrame().setVisible(true);
        } catch (Exception e) {
            var board = new Board(13, 13, 25);
            new MainView(board);
        }
    }

    private static void connectToDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                System.out.println("Connected to minesweeper.db SQLite database successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to connect to database: " + e.getMessage());
        }
    }
}
