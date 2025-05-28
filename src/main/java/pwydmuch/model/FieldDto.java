package pwydmuch.model;

public record FieldDto(int row, int column, Integer minesAround, FieldState fieldState) {
    public FieldDto {
        if (fieldState == FieldState.REVEALED && minesAround == null) {
            throw new IllegalArgumentException("Mines around must be provided when field is revealed");
        }
        if (fieldState != FieldState.REVEALED && minesAround != null) {
            throw new IllegalArgumentException("Mines around should not be provided when field is not revealed");
        }
        if (row < 0 || column < 0) {
            throw new IllegalArgumentException("Row, column must not be lesser than zero");
        }
    }

    static FieldDto fromField(Field field) {
        return new FieldDto(field.getRow(), field.getColumn(), getMinesAround(field), field.getState());
    }

    private static Integer getMinesAround(Field field) {
        return field.getState() == FieldState.REVEALED ? field.getMinesAroundNumber() : null;
    }

}