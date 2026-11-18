//! Esperas del museo: reintentos con pausa, consultas con límite de tiempo,
//! reservas que caducan y un cálculo que bloquea el hilo.

use std::collections::HashMap;
use std::time::Duration;
use tokio::time::{Instant, sleep, timeout};

/// Ejecuta `op` hasta `attempts` veces. Entre un intento y el siguiente espera
/// 1 s, luego 2 s, luego 4 s: cada pausa es el doble de la anterior.
pub async fn with_retry<T, F, Fut>(attempts: u32, mut op: F) -> Result<T, String>
where
    F: FnMut() -> Fut,
    Fut: Future<Output = Result<T, String>>,
{
    let mut pause = Duration::from_secs(1);
    let mut last = String::new();
    for attempt in 1..=attempts {
        match op().await {
            Ok(value) => return Ok(value),
            Err(e) => last = e,
        }
        if attempt < attempts {
            sleep(pause).await;
            pause *= 2;
        }
    }
    Err(last)
}

/// Espera a `fut` como mucho `limit`. Si no termina a tiempo, devuelve `Err`.
pub async fn within<T>(limit: Duration, fut: impl Future<Output = T>) -> Result<T, String> {
    timeout(limit, fut)
        .await
        .map_err(|_| format!("sin respuesta en {} s", limit.as_secs()))
}

/// Simula la consulta de una ficha a un archivo externo que tarda `delay`.
pub async fn lookup(code: &str, delay: Duration) -> String {
    sleep(delay).await;
    format!("ficha de {code}")
}

/// Cuánto dura una reserva de una pieza para una exposición.
pub const HOLD: Duration = Duration::from_secs(15 * 60);

/// Reservas de piezas: cada una caduca `HOLD` después de hacerse.
#[derive(Default)]
pub struct Holds(HashMap<String, Instant>);

impl Holds {
    pub fn hold(&mut self, code: &str) {
        self.0.insert(code.to_string(), Instant::now());
    }

    pub fn is_held(&self, code: &str) -> bool {
        self.0.get(code).is_some_and(|since| since.elapsed() < HOLD)
    }
}

/// Suma de control de un inventario grande: un cálculo que ocupa el hilo.
/// `block_in_place` avisa a tokio para que mueva las otras tareas a otro hilo.
pub fn checksum(codes: &[&str]) -> u32 {
    tokio::task::block_in_place(|| {
        codes
            .iter()
            .flat_map(|c| c.bytes())
            .fold(0u32, |acc, b| acc.wrapping_mul(31).wrapping_add(b as u32))
    })
}

#[cfg(test)]
mod tests {
    // PASO 1: un test async.
    // use super::*;
    //
    // #[tokio::test]
    // async fn first_attempt_succeeds() {
    //     let got = with_retry(3, || async { Ok::<_, String>("ficha") }).await;
    //
    //     assert_eq!(got, Ok("ficha"));
    // }
    //
    // PASO 2: el reloj en pausa.
    // #[tokio::test(start_paused = true)]
    // async fn retries_wait_one_then_two_seconds() {
    //     let mut calls = 0;
    //     let start = Instant::now();
    //
    //     let got = with_retry(3, || {
    //         calls += 1;
    //         let n = calls;
    //         async move {
    //             if n < 3 {
    //                 Err(format!("fallo {n}"))
    //             } else {
    //                 Ok(n)
    //             }
    //         }
    //     })
    //     .await;
    //
    //     assert_eq!(got, Ok(3));
    //     assert_eq!(start.elapsed(), Duration::from_secs(3));
    // }
    //
    // PASO 3: un límite de tiempo.
    // #[tokio::test(start_paused = true)]
    // async fn slow_lookup_times_out() {
    //     let got = within(
    //         Duration::from_secs(5),
    //         lookup("MUS-001", Duration::from_secs(10)),
    //     )
    //     .await;
    //
    //     assert_eq!(got, Err("sin respuesta en 5 s".to_string()));
    // }
    //
    // #[tokio::test(start_paused = true)]
    // async fn lookups_run_concurrently() {
    //     let start = Instant::now();
    //
    //     let (a, b) = tokio::join!(
    //         lookup("MUS-001", Duration::from_secs(2)),
    //         lookup("MUS-002", Duration::from_secs(2)),
    //     );
    //
    //     assert_eq!(
    //         (a.as_str(), b.as_str()),
    //         ("ficha de MUS-001", "ficha de MUS-002")
    //     );
    //     assert_eq!(start.elapsed(), Duration::from_secs(2));
    // }
    //
    // PASO 4: mover el reloj a mano.
    // #[tokio::test(start_paused = true)]
    // async fn hold_expires_after_fifteen_minutes() {
    //     let mut holds = Holds::default();
    //     holds.hold("MUS-001");
    //
    //     tokio::time::advance(HOLD - Duration::from_secs(1)).await;
    //     assert!(holds.is_held("MUS-001"));
    //
    //     tokio::time::advance(Duration::from_secs(1)).await;
    //     assert!(!holds.is_held("MUS-001"));
    // }
    //
    // PASO 5: el runtime de varios hilos.
    // #[tokio::test(flavor = "multi_thread", worker_threads = 2)]
    // async fn checksum_needs_a_multi_thread_runtime() {
    //     assert_eq!(checksum(&["MUS-001"]), checksum(&["MUS-001"]));
    //     assert_ne!(checksum(&["MUS-001"]), checksum(&["MUS-002"]));
    // }
}
