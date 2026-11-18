//! Informe de préstamos del museo en CSV.

use std::fs;
use std::io;
use std::path::Path;

/// Escribe el informe en `path`: una cabecera y una línea `código,días` por préstamo.
pub fn write_report(path: &Path, loans: &[(&str, u32)]) -> io::Result<()> {
    let mut out = String::from("code,days\n");
    for (code, days) in loans {
        out.push_str(&format!("{code},{days}\n"));
    }
    fs::write(path, out)
}

/// Lee el informe de `path` y devuelve la suma de los días de todos los préstamos.
pub fn total_days(path: &Path) -> io::Result<u32> {
    let text = fs::read_to_string(path)?;
    let mut total = 0;
    for line in text.lines().skip(1) {
        let days = line.split(',').nth(1).unwrap_or("0");
        total += days.parse::<u32>().unwrap_or(0);
    }
    Ok(total)
}

#[cfg(test)]
mod tests {
    // PASO 1: un test que imprime.
    // use super::*;
    // use std::env;
    //
    // #[test]
    // fn report_has_a_header() -> io::Result<()> {
    //     let path = env::temp_dir().join("lab03-report.csv");
    //     write_report(&path, &[("MUS-001", 3)])?;
    //     let text = fs::read_to_string(&path)?;
    //     println!("report:\n{text}");
    //     assert!(text.starts_with("code,days\n"));
    //     Ok(())
    // }

    // PASO 2: dos tests más con el mismo archivo.
    // #[test]
    // fn total_of_two_loans() -> io::Result<()> {
    //     let path = env::temp_dir().join("lab03-report.csv");
    //     write_report(&path, &[("MUS-001", 3), ("MUS-002", 4)])?;
    //     assert_eq!(total_days(&path)?, 7);
    //     Ok(())
    // }
    //
    // #[test]
    // fn total_of_a_long_report() -> io::Result<()> {
    //     let path = env::temp_dir().join("lab03-report.csv");
    //     let loans = vec![("MUS-001", 1); 5000];
    //     write_report(&path, &loans)?;
    //     assert_eq!(total_days(&path)?, 5000);
    //     Ok(())
    // }

    // PASO 3: una ruta distinta para cada test.
    // fn temp_path(test: &str) -> std::path::PathBuf {
    //     env::temp_dir().join(format!("lab03-{test}.csv"))
    // }

    // PASO 4: un test lento que no se ejecuta siempre.
    // #[test]
    // #[ignore = "lento: escribe y lee un informe de 2 millones de líneas"]
    // fn total_of_a_huge_report() -> io::Result<()> {
    //     let path = temp_path("huge");
    //     let loans = vec![("MUS-001", 1); 2_000_000];
    //     write_report(&path, &loans)?;
    //     assert_eq!(total_days(&path)?, 2_000_000);
    //     Ok(())
    // }
}
