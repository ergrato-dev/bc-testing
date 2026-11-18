// Lab 04 - La línea base de la búsqueda: 40 búsquedas por segundo durante 30 s.
// Descomenta el bloque PASO cuando el README lo pida.
import http from 'k6/http';
import { check } from 'k6';

const BASE_URL = 'http://localhost:18043';
const TERMS = ['faro gris', 'isla azul', 'tren verde', 'carta breve', 'río final'];

export const options = {
  // PASO 1: un ritmo fijo de llegadas, haga lo que haga la API
  // scenarios: {
  //   search: {
  //     executor: 'constant-arrival-rate',
  //     rate: 40,
  //     timeUnit: '1s',
  //     duration: '30s',
  //     preAllocatedVUs: 20,
  //     maxVUs: 200,
  //   },
  // },
  thresholds: {
    http_req_failed: ['rate<0.01'],
    http_req_duration: ['p(95)<100'],
  },
};

export default function () {
  const term = TERMS[Math.floor(Math.random() * TERMS.length)];
  const res = http.get(`${BASE_URL}/api/books?q=${encodeURIComponent(term)}`);
  check(res, { 'search is 200': (r) => r.status === 200 });
}
