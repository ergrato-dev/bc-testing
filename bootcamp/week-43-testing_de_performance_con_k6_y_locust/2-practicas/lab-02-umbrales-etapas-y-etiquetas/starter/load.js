// Lab 02 - Umbrales, etapas y etiquetas.
// Descomenta cada bloque PASO cuando el README lo pida.
import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE_URL = 'http://localhost:18043';
// Cinco títulos que existen, para que no todas las búsquedas sean la misma.
const TERMS = ['faro gris', 'isla azul', 'tren verde', 'carta breve', 'río final'];

export const options = {
  // PASO 1: los criterios de aceptación
  // thresholds: {
  //   http_req_failed: ['rate<0.01'],
  //   http_req_duration: ['p(95)<300'],
  //   checks: ['rate>0.99'],
  //   // PASO 3: un umbral por petición
  //   // 'http_req_duration{name:book}': ['p(95)<50'],
  //   // 'http_req_duration{name:search}': ['p(95)<100'],
  // },

  // PASO 2: el perfil de carga
  // stages: [
  //   { duration: '10s', target: 30 },
  //   { duration: '30s', target: 30 },
  //   { duration: '10s', target: 0 },
  // ],
};

// Un lector consulta un libro cualquiera de los 100 000 y después busca por título.
export default function () {
  const n = 1 + Math.floor(Math.random() * 100000);
  const isbn = `978${String(n).padStart(10, '0')}`;
  const book = http.get(`${BASE_URL}/api/books/${isbn}`, { tags: { name: 'book' } });
  check(book, { 'book is 200': (r) => r.status === 200 });
  sleep(1);

  const term = TERMS[Math.floor(Math.random() * TERMS.length)];
  const found = http.get(`${BASE_URL}/api/books?q=${encodeURIComponent(term)}`, {
    tags: { name: 'search' },
  });
  check(found, { 'search is 200': (r) => r.status === 200 });
  sleep(1);
}
