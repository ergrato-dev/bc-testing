// Lab 01 - Primer test de carga con k6.
// Descomenta cada bloque PASO cuando el README lo pida.
import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE_URL = 'http://localhost:18043';

export default function () {
  // PASO 1: una petición y sus comprobaciones
  // const res = http.get(`${BASE_URL}/api/books/9780000000001`);
  // check(res, {
  //   'status is 200': (r) => r.status === 200,
  //   'is the requested book': (r) => r.json('isbn') === '9780000000001',
  // });

  // PASO 2: el tiempo que un lector mira el resultado
  // sleep(1);

  // PASO 3: una búsqueda por título
  // const found = http.get(`${BASE_URL}/api/books?q=${encodeURIComponent('faro gris')}`);
  // check(found, {
  //   'search is 200': (r) => r.status === 200,
  //   'search finds 20 books': (r) => r.json().length === 20,
  // });
}
