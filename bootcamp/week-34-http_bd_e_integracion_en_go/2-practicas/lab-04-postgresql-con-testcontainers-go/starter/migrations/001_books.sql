-- El catálogo de libros. copies no puede bajar de cero: si el código lo
-- intenta, PostgreSQL rechaza el UPDATE.
CREATE TABLE books (
    isbn   text PRIMARY KEY,
    title  text NOT NULL,
    copies int  NOT NULL CHECK (copies >= 0)
);
