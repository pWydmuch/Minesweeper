package pwydmuch.model;

public record RightClickResponse(GameStatus gameStatus, FieldDto field, int remainingFlagsToSet) {
}
