package pwydmuch.model;

import java.util.List;
import java.util.Set;

public record GameState(List<FieldDto> fieldDtos, Set<Point> minePoints,
                        GameStatus gameStatus, int remainingFlagsToSet) {

    public GameState {
        if (fieldDtos == null || minePoints == null || gameStatus == null) {
            throw new IllegalArgumentException("FieldDtos, minePoints and gameStatus cannot be null");
        }
        if (remainingFlagsToSet < 0) {
            throw new IllegalArgumentException("Remaining flags to set cannot be negative");
        }
    }
}
