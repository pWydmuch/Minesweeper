package pwydmuch.domain.dtos;

import java.io.Serializable;

public record Point(int x, int y) implements Serializable {
    public Point {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Coordinates must be non-negative");
        }
    }
}
