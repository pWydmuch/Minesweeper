package pwydmuch.model;

public record RightClickResponse(GameStatus gameStatus, MyButton.State buttonState, int remainingFlagsToSet) {
}
