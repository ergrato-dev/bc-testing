import { defineConfig, devices } from '@playwright/test';

export default defineConfig({
  testDir: './tests',
  // TODO: el reporter HTML y las evidencias de cada fallo (screenshot, video y trace).
  use: {
    baseURL: 'http://localhost:3000',
  },
  webServer: {
    command: 'node app/server.mjs',
    url: 'http://localhost:3000',
    reuseExistingServer: !process.env.CI,
  },
  projects: [
    { name: 'chromium', use: { ...devices['Desktop Chrome'] } },
    // TODO: los proyectos firefox y webkit.
  ],
});
