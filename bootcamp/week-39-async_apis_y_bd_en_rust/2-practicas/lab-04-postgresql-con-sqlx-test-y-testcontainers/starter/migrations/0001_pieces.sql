-- Las reglas del dominio, repetidas en la base de datos.
CREATE TABLE pieces (
    code           text PRIMARY KEY,
    name           text NOT NULL,
    declared_value int  NOT NULL CHECK (declared_value > 0)
);
