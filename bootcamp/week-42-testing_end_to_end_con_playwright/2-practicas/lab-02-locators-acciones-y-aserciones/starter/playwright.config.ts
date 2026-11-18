import { defineConfig, devices } from '@playwright/test';

export default defineConfig({
  testDir: './tests',
  use: {
    // page.goto('/') va a esta dirección.
    baseURL: 'http://localhost:3000',
  },
  // Playwright arranca la aplicación antes de los tests y la para al terminar.
  webServer: {
    command: 'node app/server.mjs',
    url: 'http://localhost:3000',
    // En tu equipo reutiliza la aplicación si ya está arrancada; en CI exige arrancarla.
    reuseExistingServer: !process.env.CI,
  },
  projects: [{ name: 'chromium', use: { ...devices['Desktop Chrome'] } }],
});
