// ============================================
// PASO 5: Configurar collectCoverageFrom y coverageThreshold
// ============================================
// module.exports = {
//   // Incluye todos los archivos fuente en el reporte, aunque ningun test los importe.
//   // Sin esta opcion, un archivo sin tests simplemente no aparece y el porcentaje miente.
//   collectCoverageFrom: ["*.js", "!*.test.js", "!jest.config.js"],
//   // Si alguna metrica queda por debajo del umbral, `pnpm test:coverage` termina con error.
//   // En un modulo pequeno y critico (precios) exigimos 100% de ramas.
//   coverageThreshold: {
//     global: {
//       branches: 100,
//       functions: 100,
//       lines: 100,
//       statements: 100,
//     },
//   },
// };
