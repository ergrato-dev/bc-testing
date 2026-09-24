module.exports = {
  // Incluye todos los archivos fuente en el reporte, aunque ningún test los importe.
  // Sin esta opción, un archivo sin tests simplemente no aparece y el porcentaje miente.
  collectCoverageFrom: ["*.js", "!*.test.js", "!jest.config.js"],
  // Si alguna métrica queda por debajo del umbral, `pnpm test:coverage` termina con error.
  // En un módulo pequeño y crítico (precios) exigimos 100% de ramas.
  coverageThreshold: {
    global: {
      branches: 100,
      functions: 100,
      lines: 100,
      statements: 100,
    },
  },
};
