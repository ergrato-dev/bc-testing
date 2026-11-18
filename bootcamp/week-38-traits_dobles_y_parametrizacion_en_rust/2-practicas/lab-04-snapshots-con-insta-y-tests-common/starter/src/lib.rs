//! Avisos de préstamo del museo: las condiciones que recibe la institución.

/// Condiciones de un préstamo aprobado.
#[derive(Debug, Clone, PartialEq)]
pub struct Notice {
    pub institution: String,
    pub code: String,
    pub days: u32,
    pub premium: u32,
}

/// Texto del aviso que se envía a la institución.
pub fn render_notice(n: &Notice) -> String {
    format!(
        "Préstamo de la pieza {}\nInstitución: {}\nDuración: {} días\nSeguro: {}\n",
        n.code, n.institution, n.days, n.premium
    )
}

/// Asunto del correo del aviso.
pub fn subject(n: &Notice) -> String {
    format!("[{}] Préstamo aprobado", n.code)
}
