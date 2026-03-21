<p align="center">
  <img src="_assets/bootcamp-header.svg" alt="Software Testing Bootcamp Zero to Hero" width="800">
</p>

<p align="center">
  <a href="https://github.com/ergrato-dev/bc-testing/blob/main/LICENSE"><img src="https://img.shields.io/badge/license-MIT-blue.svg" alt="License MIT"></a>
  <a href="#"><img src="https://img.shields.io/badge/weeks-36-6366f1.svg" alt="36 Weeks"></a>
  <a href="#"><img src="https://img.shields.io/badge/hours-288-6366f1.svg" alt="288 Hours"></a>
  <a href="#"><img src="https://img.shields.io/badge/JavaScript-Jest-F7DF1E?logo=javascript&logoColor=black" alt="JavaScript Jest"></a>
  <a href="#"><img src="https://img.shields.io/badge/Python-pytest-3572A5?logo=python&logoColor=white" alt="Python pytest"></a>
  <a href="#"><img src="https://img.shields.io/badge/Java-JUnit5-F89820?logo=openjdk&logoColor=white" alt="Java JUnit 5"></a>
  <a href="CONTRIBUTING.md"><img src="https://img.shields.io/badge/PRs-Welcome-brightgreen?style=flat-square" alt="PRs Welcome"></a>
</p>

<p align="center">
  <a href="README.md"><img src="https://img.shields.io/badge/🇪🇸_Español-red?style=for-the-badge&logoColor=white" alt="Spanish Version"></a>
</p>

---

## 📋 Description

An intensive **36-week (~9 months)** bootcamp focused on mastering **automated software testing** across three languages: JavaScript, Python, and Java. Designed to take students from zero experience to **QA Engineer / SDET Junior**, with emphasis on best practices, TDD, BDD, and testing in CI/CD pipelines.

> 🏛️ **Unique Domain Policy (Anti-Copy)**: Each student works on a unique business domain assigned by the instructor (Library, Pharmacy, Gym, etc.). This ensures original test suites and prevents copying between peers.

### 🎯 Learning Objectives

By the end of the bootcamp, students will be able to:

- ✅ Apply a quality mindset and design professional test cases
- ✅ Write unit tests in JavaScript (Jest), Python (pytest), and Java (JUnit 5)
- ✅ Use mocks, stubs, and spies to isolate dependencies in all three languages
- ✅ Apply TDD (Red-Green-Refactor) in JavaScript, Python, and Java
- ✅ Implement BDD with Behave (Python) and Cucumber (Java)
- ✅ Write integration tests for REST APIs in all three languages
- ✅ Automate E2E tests with Playwright
- ✅ Run performance tests with k6 and Locust
- ✅ Integrate testing into CI/CD pipelines with GitHub Actions
- ✅ Measure and maintain code coverage ≥85% across all three languages

---

## 🗓️ Bootcamp Structure

| Stage | Weeks | Hours | Main Topics |
| :---: | :---: | :---: | --- |
| **Stage 0** — Testing Fundamentals | 1–6 | 48h | Quality mindset, terminology, testing pyramid, first tests in all 3 languages in parallel |
| **Stage 1** — Testing with JavaScript | 7–15 | 72h | Advanced Jest, mocks/stubs/spies, async testing, TDD, Supertest, snapshot, fast-check |
| **Stage 2** — Testing with Python | 16–24 | 72h | pytest, fixtures, unittest.mock, TDD, Behave (BDD), httpx, Testcontainers |
| **Stage 3** — Testing with Java | 25–31 | 56h | JUnit 5, AssertJ, Mockito, TDD, Spring Boot Test, Testcontainers, Cucumber |
| **Stage 4** — Advanced Transversal Testing | 32–36 | 40h | Playwright E2E, k6/Locust performance, OWASP security basics, CI/CD with GitHub Actions |

**Total: 36 weeks** | **~288 hours** of intensive training

---

## 📚 Weekly Content

Each week includes:

```
bootcamp/week-XX/
├── README.md                 # Week description and objectives
├── rubrica-evaluacion.md     # Assessment rubric
├── 0-assets/                 # SVG diagrams
├── 1-teoria/                 # Theory material
├── 2-practicas/              # Guided exercises (commented code to uncomment)
├── 3-proyecto/               # Weekly project (deliverable with TODOs)
├── 4-recursos/               # Additional resources
│   ├── ebooks-free/
│   ├── videografia/
│   └── webgrafia/
└── 5-glosario/               # Key terms
```

### 🔑 Key Components

- 📖 **Theory**: Testing concepts with cross-language comparisons
- 💻 **Practices**: Guided exercises — students learn by uncommenting code step by step
- 📝 **Project**: Test suite for the assigned business domain with TODOs to implement
- 📊 **Rubric**: Clear criteria with three evidence types (Knowledge · Performance · Product)

---

## 🛠️ Tools by Language

### JavaScript

| Tool | Version | Purpose |
| --- | --- | --- |
| Jest | 29+ | Unit and integration testing |
| Supertest | 6+ | Express API testing |
| fast-check | 3+ | Property-based testing |
| Playwright | 1.40+ | E2E testing |
| k6 | 0.50+ | Performance testing |

**Package manager**: `pnpm` or `yarn` (❌ NOT `npm`)

### Python

| Tool | Version | Purpose |
| --- | --- | --- |
| pytest | 8+ | Main testing framework |
| pytest-cov | 4+ | Code coverage |
| pytest-asyncio | 0.23+ | Async testing |
| pytest-mock | 3+ | unittest.mock wrapper |
| httpx + respx | latest | Async API testing |
| Behave | 1.2+ | BDD (Gherkin) |
| hypothesis | 6+ | Property-based testing |
| Locust | 2+ | Performance testing |

**Package manager**: `uv` (recommended) or `pip` + `venv`

### Java

| Tool | Version | Purpose |
| --- | --- | --- |
| JUnit 5 | 5.10+ | Main testing framework |
| Mockito | 5+ | Mocking |
| AssertJ | 3+ | Fluent assertions |
| Spring Boot Test | 3+ | Spring Boot testing |
| Testcontainers | 1.19+ | Real databases in tests |
| Cucumber | 7+ | BDD (Gherkin) |
| JaCoCo | 0.8+ | Code coverage |

**Build tool**: Maven · **JDK**: 21 LTS

---

## 🚀 Quick Start

### Prerequisites

- **Node.js** 22 LTS + `pnpm` (for JavaScript weeks)
- **Python** 3.12+ + `uv` (for Python weeks)
- **JDK 21 LTS** + **Maven** (for Java weeks)
- **Git** 2.40+
- **VS Code** with recommended extensions (suggested automatically when opening the repo)
- **Docker** (for Testcontainers in weeks 20–31)

### 1. Clone the Repository

```bash
git clone https://github.com/ergrato-dev/bc-testing.git
cd bc-testing
```

### 2. Install VS Code Extensions

```bash
# Open in VS Code
code .

# Recommended extensions will appear automatically
# Or run: Ctrl+Shift+P → "Extensions: Show Recommended Extensions"
```

### 3. Navigate to the Current Week

```bash
cd bootcamp/week-01-que_es_testing_calidad_de_software
```

### 4. Follow the Instructions

Each week contains a `README.md` with detailed instructions and the 8-hour time breakdown.

---

## 📊 Learning Methodology

### Teaching Strategies

- 🎯 **Project-Based Learning (PBL)** — Weekly test suite per business domain
- 🔄 **Unique Domains** — Each student tests their own assigned business domain
- 🔁 **TDD as Discipline** — Dedicated weeks practicing the Red-Green-Refactor cycle
- 🌐 **Cross-Language Comparison** — Same concept shown in JS, Python, and Java side by side
- 👥 **Peer Code Review** — Students review each other's test suites
- 🎮 **Live Testing** — Live exploratory testing sessions

### Time Distribution (8h/week)

- **Theory**: 2–2.5 hours
- **Guided practices**: 3–3.5 hours
- **Weekly project**: 2–2.5 hours

### Assessment

Each week includes three types of evidence:

1. **Knowledge 🧠** (30%): Theory quizzes on testing concepts
2. **Performance 💪** (40%): Practical exercises with the week's framework
3. **Product 📦** (30%): Test suite for the assigned business domain project

**Passing criteria**: Minimum 70% on each evidence type

---

## 🤝 Contributing

Contributions are welcome! This is an open-source educational project.

### How to Contribute

1. Read the [Contributing Guide](CONTRIBUTING.md)
2. Review the [Code of Conduct](CODE_OF_CONDUCT.md)
3. Fork the repository
4. Create your branch (`git checkout -b feature/new-feature`)
5. Commit using [Conventional Commits](https://www.conventionalcommits.org/) (`git commit -m 'feat: add new exercise'`)
6. Push to the branch (`git push origin feature/new-feature`)
7. Open a Pull Request

### 📋 Contribution Areas

- ✨ Additional exercises (in any of the 3 languages)
- 📚 Documentation improvements
- 🐛 Bug fixes in practice code
- 🎨 Visual resources (SVG diagrams)
- 🌐 Translations
- 📹 Tutorial videos

---

## 📞 Support

- 💬 Discussions: [GitHub Discussions](https://github.com/ergrato-dev/bc-testing/discussions)
- 🐛 Issues: [GitHub Issues](https://github.com/ergrato-dev/bc-testing/issues)

---

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

---

## 🏆 Acknowledgements

- [Jest](https://jestjs.io/) · [pytest](https://docs.pytest.org/) · [JUnit 5](https://junit.org/junit5/) — For the testing frameworks
- [Playwright](https://playwright.dev/) — For the best E2E framework
- [Testcontainers](https://testcontainers.com/) — For making integration tests realistic
- [OWASP](https://owasp.org/) — For security guidelines
- The Testing Community — For resources, examples and best practices
- All contributors

---

## 📚 Additional Documentation

- [📋 Study Plan](_docs/plan-estudios.md)
- [📖 Content Development Guide](_docs/guia-desarrollo-contenidos.md)
- [🤖 Copilot Instructions](.github/copilot-instructions.md)
- [🤝 Contributing Guide](CONTRIBUTING.md)
- [📜 Code of Conduct](CODE_OF_CONDUCT.md)
- [🔒 Security Policy](SECURITY.md)

---

<p align="center">
  <strong>🎓 Software Testing Bootcamp Zero to Hero</strong><br>
  <em>From zero to QA Engineer / SDET Junior in ~9 months</em>
</p>

<p align="center">
  <a href="bootcamp/week-01-que_es_testing_calidad_de_software">Start Week 1</a> •
  <a href="_docs/plan-estudios.md">View Study Plan</a> •
  <a href="https://github.com/ergrato-dev/bc-testing/issues">Report Issue</a> •
  <a href="CONTRIBUTING.md">Contribute</a>
</p>

<p align="center">
  Made with ❤️ for the testing and developer community
</p>
