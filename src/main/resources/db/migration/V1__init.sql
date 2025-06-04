CREATE TABLE IF NOT EXISTS game_state (
    id INTEGER PRIMARY KEY,
    rows INTEGER NOT NULL,
    columns INTEGER NOT NULL,
    mines_number INTEGER NOT NULL,
    game_status TEXT NOT NULL,
    time INTEGER NOT NULL,
    remaining_flags INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS fields (
    game_id INTEGER,
    row INTEGER NOT NULL,
    column INTEGER NOT NULL,
    state TEXT NOT NULL,
    has_mine BOOLEAN NOT NULL,
    mines_around INTEGER,
    FOREIGN KEY (game_id) REFERENCES game_state(id),
    PRIMARY KEY (game_id, row, column)
); 