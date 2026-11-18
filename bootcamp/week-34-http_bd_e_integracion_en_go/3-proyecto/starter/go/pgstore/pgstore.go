// Package pgstore guarda los datos de tu dominio en PostgreSQL.
package pgstore

// TODO: el almacén real de las interfaces de tu servicio (paso 5).
//
//   - migrations/001_*.sql con el esquema de la semana 29, incrustado con
//     //go:embed migrations/*.sql, y una función Migrate.
//   - Una interfaz DBTX (Exec, QueryRow) que cumplan *pgxpool.Pool y pgx.Tx.
//   - Un Store que cumpla las interfaces del servicio y traduzca los errores
//     de PostgreSQL (pgx.ErrNoRows, códigos 23505 y 23503) a los del dominio.
