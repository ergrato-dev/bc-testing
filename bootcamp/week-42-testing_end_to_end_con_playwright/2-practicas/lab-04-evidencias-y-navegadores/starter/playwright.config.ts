import { defineConfig, devices } from '@playwright/test';

export default defineConfig({
  testDir: './tests',
  // PASO 1: el informe HTML y las evidencias de cada fallo
  // reporter: [['list'], ['html', { open: 'never' }]],

  use: {
    baseURL: 'http://localhost:3000',
    // PASO 1: el informe HTML y las evidencias de cada fallo
    // screenshot: 'only-on-failure',
    // video: 'retain-on-failure',
    // trace: 'retain-on-failure',
  },
  webServer: {
    command: 'node app/server.mjs',
    url: 'http://localhost:3000',
    reuseExistingServer: !process.env.CI,
  },
  projects: [
    { name: 'chromium', use: { ...devices['Desktop Chrome'] } },
    // PASO 3: Firefox y WebKit
    // { name: 'firefox', use: { ...devices['Desktop Firefox'] } },
    // { name: 'webkit', use: { ...devices['Desktop Safari'] } },
  ],
});
