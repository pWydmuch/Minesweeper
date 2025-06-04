package pwydmuch;

import pwydmuch.domain.Board;
import pwydmuch.service.GameStateService;
import pwydmuch.view.MainView;

public class Minesweeper {
    private static final GameStateService gameStateService = new GameStateService();

    public static void save(MainView mainView) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                gameStateService.saveGameState(mainView.getBoard(), mainView.getTime());
            } catch (Exception ignored) {
                // Ignore exceptions during shutdown
            }
        }));
    }

    public static void main(String[] args) {
        Board board = gameStateService.loadLastGameState();
        if (board == null) {
            board = new Board(13, 13, 25);
        }
        new MainView(board);
    }
}
