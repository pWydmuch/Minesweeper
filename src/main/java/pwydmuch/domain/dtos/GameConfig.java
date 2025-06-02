package pwydmuch.domain.dtos;

public record GameConfig(int rows, int columns, int minesNumber) {
    public GameConfig {
        if (rows <= 0 || columns <= 0) {
            throw new IllegalArgumentException("Rows and columns must be greater than zero");
        }
        if (columns * rows < minesNumber) {
            throw new IllegalArgumentException("Too many mines for given board size");
        }
    }
}
