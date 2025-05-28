package pwydmuch.model;

import java.util.List;
import java.util.Set;

public record LeftClickResponse(GameStatus gameStatus, List<FieldDto> fields, Set<Point> minePoints) {
}
