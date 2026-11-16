CREATE TABLE tickets (
    id          bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    seat        text NOT NULL UNIQUE,
    price_cents int  NOT NULL CHECK (price_cents > 0)
);
