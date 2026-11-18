CREATE TABLE books (
    isbn   text PRIMARY KEY,
    title  text NOT NULL,
    copies int  NOT NULL CHECK (copies >= 0)
);
