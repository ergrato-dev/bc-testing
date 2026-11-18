-- Tablas y datos de la biblioteca insegura. Pocos datos: los labs comprueban qué
-- devuelve cada petición, no cuánto tarda.
CREATE TABLE books (
    id     int  PRIMARY KEY,
    title  text NOT NULL,
    author text NOT NULL
);

CREATE TABLE loans (
    id      int  PRIMARY KEY,
    book_id int  NOT NULL REFERENCES books (id),
    member  text NOT NULL
);

INSERT INTO books VALUES
    (1, 'The Go Programming Language', 'Alan Donovan'),
    (2, 'Learning Go', 'Jon Bodner'),
    (3, 'The Rust Programming Language', 'Steve Klabnik'),
    (4, 'Fluent Python', 'Luciano Ramalho'),
    (5, 'Effective Java', 'Joshua Bloch'),
    (6, 'Eloquent JavaScript', 'Marijn Haverbeke');

-- El préstamo 1 es de ana y el 2 de luis.
INSERT INTO loans VALUES
    (1, 1, 'ana'),
    (2, 3, 'luis');

-- Una tabla que la API nunca consulta: si un test la ve en una respuesta, hay inyección.
CREATE TABLE staff (
    username      text PRIMARY KEY,
    password_hash text NOT NULL
);

INSERT INTO staff VALUES ('admin', '$2a$10$Q7d1Jq2nZb1x0lJm7rC8eu8m1J1cZQyq0Kx3Vv4w1n7a8p5s2t6dK');
