// Prueba de carga de la API del dominio (semana 43).
// TODO: completa los pasos 3 y 4 de 3-proyecto/README.md.
import http from 'k6/http';
import { check, sleep } from 'k6';
import exec from 'k6/execution';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:18080';
const PIECES = 20000; // TODO: los elementos que crea perf/seed.sql
const SMOKE = __ENV.PROFILE === 'smoke';

// TODO: el código del elemento n, con el mismo formato que perf/seed.sql.
function code(n) {
  return `AA${String.fromCharCode(65 + Math.floor(n / 1000))}-${String(n % 1000).padStart(3, '0')}`;
}

export const options = {
  // TODO: un escenario de lectura con VUs y otro de escritura con ritmo fijo; con SMOKE,
  // unos segundos y 1 usuario.
  // TODO: los umbrales de tus criterios de aceptación, uno por petición.
};

export function setup() {
  const res = http.get(`${BASE_URL}/api/pieces/${code(0)}`); // TODO: tu ruta de lectura
  if (res.status !== 200) {
    throw new Error(`sin datos de prueba: GET ${code(0)} respondió ${res.status}`);
  }
}

export default function () {
  // TODO: sustituye esta función por una por escenario (exec), con etiqueta name y check.
  const res = http.get(`${BASE_URL}/api/pieces/${code(0)}`);
  check(res, { 'status is 200': (r) => r.status === 200 });
  sleep(1);
}
