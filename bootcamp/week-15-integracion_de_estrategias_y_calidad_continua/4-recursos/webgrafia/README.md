# Webgrafia - Semana 15

| Recurso | URL | Por que leerlo |
|---|---|---|
| GitHub Docs - Building and testing Node.js | [docs.github.com/en/actions/tutorials/build-and-test-code/nodejs](https://docs.github.com/en/actions/tutorials/build-and-test-code/nodejs) | Workflow oficial para Node con setup-node e instalacion con pnpm. |
| GitHub Docs - Security hardening for GitHub Actions | [docs.github.com/en/actions/security-for-github-actions/security-guides/security-hardening-for-github-actions](https://docs.github.com/en/actions/security-for-github-actions/security-guides/security-hardening-for-github-actions) | Por que fijamos las actions por SHA completo y no por tag. |
| SonarQube Cloud - GitHub Actions | [docs.sonarsource.com/sonarqube-cloud/analyzing-source-code/ci-based-analysis/github-actions-for-sonarcloud/](https://docs.sonarsource.com/sonarqube-cloud/analyzing-source-code/ci-based-analysis/github-actions-for-sonarcloud/) | Configuracion oficial del scan y como fallar el workflow con `sonar.qualitygate.wait`. |
| SonarQube Cloud - JavaScript / TypeScript test coverage | [docs.sonarsource.com/sonarqube-cloud/analyzing-source-code/test-coverage/javascript-typescript-test-coverage/](https://docs.sonarsource.com/sonarqube-cloud/analyzing-source-code/test-coverage/javascript-typescript-test-coverage/) | Como importar `coverage/lcov.info` con `sonar.javascript.lcov.reportPaths`. |
| SonarQube Scan Action | [github.com/SonarSource/sonarqube-scan-action](https://github.com/SonarSource/sonarqube-scan-action) | Versiones, variables `SONAR_TOKEN`/`SONAR_HOST_URL` y paso de argumentos. |
| Supertest | [github.com/ladjs/supertest](https://github.com/ladjs/supertest) | API de Supertest para los tests de integracion HTTP del proyecto. |
| Express - Error Handling | [expressjs.com/en/guide/error-handling.html](https://expressjs.com/en/guide/error-handling.html) | Como traducir errores del servicio a codigos HTTP en Express 5. |

> Usa estas fuentes para validar cada paso del pipeline antes de adoptarlo en equipo.
