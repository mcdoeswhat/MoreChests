CREATE TABLE IF NOT EXISTS 'chests' (
    ChestID    INTEGER         PRIMARY KEY AUTOINCREMENT,
    location   VARCHAR (255)   NOT NULL
                               UNIQUE ON CONFLICT REPLACE,
    chest_item VARCHAR (2550)  NOT NULL,
    items      VARCHAR (25500)
);
