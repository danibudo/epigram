CREATE TABLE epigrams (
    id         SERIAL       PRIMARY KEY,
    text       TEXT         NOT NULL UNIQUE,
    author     VARCHAR(255),
    source     VARCHAR(255),
    created_at TIMESTAMP    NOT NULL DEFAULT NOW()
);