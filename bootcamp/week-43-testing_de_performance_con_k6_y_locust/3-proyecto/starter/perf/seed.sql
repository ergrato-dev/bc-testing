-- Datos de las pruebas de rendimiento: 20 000 piezas disponibles.
-- TODO: tu tabla y tus columnas. El código de cada elemento cumple las reglas de tu dominio,
-- y k6 y Locust tienen que poder calcularlo a partir de su número n.
-- perf/run.sh lo ejecuta en una base de datos recién creada, después de las migraciones.
-- El código de la pieza n (0 a 19 999) es AA + una letra (A + n / 1000) + guion + n % 1000
-- con tres cifras: AAA-000 ... AAT-999. k6 y Locust lo calculan igual.
INSERT INTO pieces (code, name, declared_value, status)
SELECT
    'AA' || chr(65 + n / 1000) || '-' || lpad((n % 1000)::text, 3, '0'),
    'Pieza ' || n,
    1000 + n,
    'AVAILABLE'
FROM generate_series(0, 19999) AS n;

ANALYZE pieces;
