-- Tablas y datos de la biblioteca. PostgreSQL ejecuta este archivo la primera vez que
-- arranca el contenedor (docker-entrypoint-initdb.d).

CREATE TABLE books (
    isbn   text PRIMARY KEY,
    title  text NOT NULL,
    author text NOT NULL,
    year   int  NOT NULL
);

-- Un préstamo abierto por libro: la clave primaria es el libro. Al devolverlo se borra la fila.
CREATE TABLE loans (
    isbn    text PRIMARY KEY REFERENCES books (isbn),
    member  text NOT NULL,
    lent_at timestamptz NOT NULL DEFAULT now()
);

-- 100 000 libros. El ISBN es 978 + el número de libro con 10 cifras: el libro 1 es
-- 9780000000001 y el 100 000, 9780000100000. Los títulos combinan un sustantivo, un
-- adjetivo invariable y un lugar, siempre en el mismo orden, así que los datos son los mismos en
-- todos los equipos.
INSERT INTO books (isbn, title, author, year)
SELECT
    '978' || lpad(n::text, 10, '0'),
    (ARRAY['El jardín', 'La casa', 'El viaje', 'La noche', 'El río', 'La ciudad', 'El silencio',
           'La memoria', 'El mapa', 'La isla', 'El faro', 'La carta', 'El bosque',
           'La sombra', 'El puente', 'La torre', 'El tren'])[1 + n % 17]
    || ' ' ||
    (ARRAY['gris', 'azul', 'verde', 'breve', 'final', 'invisible', 'imposible', 'feliz',
           'triste', 'salvaje', 'celeste'])[1 + n % 11]
    || ' de ' ||
    (ARRAY['Lima', 'Quito', 'Bogotá', 'Cusco', 'Arequipa', 'Valparaíso', 'Montevideo',
           'Cartagena', 'Oaxaca', 'Rosario', 'Cuenca', 'Granada', 'Mérida'])[1 + n % 13]
    || ' ' || n,
    (ARRAY['Ana Pérez', 'Luis Rojas', 'Marta Gil', 'Jorge Soto', 'Elena Ruiz', 'Pablo Vega',
           'Rosa Díaz', 'Iván Mora'])[1 + n % 8],
    1950 + n % 75
FROM generate_series(1, 100000) AS n;

ANALYZE books;
