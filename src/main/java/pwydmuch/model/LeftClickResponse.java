package pwydmuch.model;

import java.util.Set;

public record LeftClickResponse(GameStatus gameStatus, Set<Point> minePoints) {
}
