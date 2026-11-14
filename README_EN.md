<p align="center">
  <img src="assets/bootcamp-header.svg" alt="Software Testing Bootcamp Zero to Hero" width="800">
</p>

<p align="center">
  <a href="https://github.com/ergrato-dev/bc-testing/blob/main/LICENSE"><img src="https://img.shields.io/badge/license-CC_BY--NC--SA_4.0-green.svg" alt="License CC BY-NC-SA 4.0"></a>
  <a href="#"><img src="https://img.shields.io/badge/weeks-46-6366f1.svg" alt="46 Weeks"></a>
  <a href="#"><img src="https://img.shields.io/badge/hours-460-6366f1.svg" alt="460 Hours"></a>
  <a href="#"><img src="https://img.shields.io/badge/JavaScript-Jest-F7DF1E?logo=javascript&logoColor=black" alt="JavaScript Jest"></a>
  <a href="#"><img src="https://img.shields.io/badge/Python-pytest-3572A5?logo=python&logoColor=white" alt="Python pytest"></a>
  <a href="#"><img src="https://img.shields.io/badge/Java-JUnit6-F89820?logo=openjdk&logoColor=white" alt="Java JUnit 6"></a>
  <a href="#"><img src="https://img.shields.io/badge/Go-go_test-00ADD8?logo=go&logoColor=white" alt="Go go test"></a>
  <a href="#"><img src="https://img.shields.io/badge/Rust-cargo_test-DEA584?logo=rust&logoColor=black" alt="Rust cargo test"></a>
</p>

<p align="center">
  <a href="README.md"><img src="https://img.shields.io/badge/🇪🇸_Español-red?style=for-the-badge&logoColor=white" alt="Spanish Version"></a>
</p>

---

## 📋 Description

An intensive **46-week (~11.5 months)** bootcamp focused on mastering **automated software testing** across five languages: JavaScript, Python, Java, Go, and Rust. Designed to take students from zero experience to **QA Engineer / SDET Junior**, with emphasis on best practices, TDD, BDD, and testing in CI/CD pipelines.

> 🏛️ **Unique Domain Policy (Anti-Copy)**: Each student works on a unique business domain assigned by the instructor (Library, Pharmacy, Gym, etc.). This ensures original test suites and prevents copying between peers.

### 🎯 Learning Objectives

By the end of the bootcamp, students will be able to:

- ✅ Apply a quality mindset and design professional test cases
- ✅ Write unit tests in JavaScript (Jest), Python (pytest), Java (JUnit 6), Go (`go test`), and Rust (`cargo test`)
- ✅ Use mocks, stubs, and spies to isolate dependencies in all five languages
- ✅ Apply TDD (Red-Green-Refactor) in JavaScript, Python, and Java
- ✅ Implement BDD with Behave (Python) and Cucumber (Java)
- ✅ Write integration tests for REST APIs in all five languages
- ✅ Apply fuzzing, property-based testing, and benchmarks (Go and Rust)
- ✅ Automate E2E tests with Playwright
- ✅ Run performance tests with k6 and Locust
- ✅ Integrate testing into CI/CD pipelines with GitHub Actions
- ✅ Measure and maintain code coverage ≥85% across all five languages

---

## 🗓️ Bootcamp Structure

| Stage | Weeks | Hours | Main Topics |
| :---: | :---: | :---: | --- |
| **Stage 0** — Testing Fundamentals | 1–6 | 60h | Quality mindset, terminology, testing pyramid, first tests in all 3 languages in parallel |
| **Stage 1** — Testing with JavaScript | 7–15 | 90h | Advanced Jest, mocks/stubs/spies, async testing, TDD, Supertest, snapshot, fast-check |
| **Stage 2** — Testing with Python | 16–24 | 90h | pytest, fixtures, unittest.mock, TDD, Behave (BDD), httpx2, Testcontainers |
| **Stage 3** — Testing with Java | 25–31 | 70h | JUnit 6, AssertJ, Mockito, TDD, Spring Boot Test, Testcontainers, Cucumber |
| **Stage 4** — Testing with Go | 32–36 | 50h | `go test`, table-driven tests, interfaces and mockgen, httptest, testcontainers-go, `-race`, fuzzing, benchmarks |
| **Stage 5** — Testing with Rust | 37–41 | 50h | `cargo test`, doctests, mockall, rstest, Tokio/axum, `#[sqlx::test]`, proptest, cargo-llvm-cov |
| **Stage 6** — Advanced Transversal Testing | 42–46 | 50h | Playwright E2E, k6/Locust performance, OWASP and dependency auditing, CI/CD with GitHub Actions |

**Total: 46 weeks** | **~460 hours** of intensive training

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

| Language | Main framework | Package manager / build |
| --- | --- | --- |
| JavaScript | Jest | `pnpm` (❌ do NOT use `npm` or `yarn`) |
| Python | pytest | `uv` |
| Java | JUnit 6 | Maven |
| Go | `go test` (stdlib) | `go` |
| Rust | `cargo test` | `cargo` (`rustup`) |

Exact versions, where they are pinned and why: [`docs/stack-versions.md`](docs/stack-versions.md) (Spanish).

---

## 🚀 Quick Start

### Prerequisites

- **Node.js** 22 LTS + `pnpm` (for JavaScript weeks)
- **Python** 3.12+ + `uv` (for Python weeks)
- **JDK 21 LTS** + **Maven** (for Java weeks)
- **Go** (for Go weeks, version in the tools table)
- **Rust** via `rustup`, stable toolchain (+ nightly only for `cargo-fuzz`)
- **Git** 2.40+
- **VS Code** with recommended extensions (suggested automatically when opening the repo)
- **Docker** (for Testcontainers from S23 and OWASP ZAP in S44)

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

### Time Distribution (10h/week)

- **Theory**: 2–2.5 hours
- **Guided practices**: 4–4.5 hours
- **Weekly project**: 3 hours
- **Resources**: 0.5 hours

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

1. Fork the repository
2. Create your branch (`git checkout -b feature/new-feature`)
3. Commit using [Conventional Commits](https://www.conventionalcommits.org/) (`git commit -m 'feat: add new exercise'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Open a Pull Request

### 📋 Contribution Areas

- ✨ Additional exercises (in any of the 5 languages)
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

This project is licensed under **CC BY-NC-SA 4.0** (Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International).

- ✅ You may share and adapt the material with attribution
- ✅ Educational forks are permitted
- ❌ Commercial use is not allowed
- ❌ Derivative works must use the same license

See the [LICENSE](LICENSE) file for details.

---

## 🏆 Acknowledgements

- [Jest](https://jestjs.io/) · [pytest](https://docs.pytest.org/) · [JUnit 6](https://docs.junit.org/6.0.3/overview.html) — For the testing frameworks
- [Playwright](https://playwright.dev/) — For the best E2E framework
- [Testcontainers](https://testcontainers.com/) — For making integration tests realistic
- [OWASP](https://owasp.org/) — For security guidelines
- The Testing Community — For resources, examples and best practices
- All contributors

---

## 📚 Additional Documentation

- [📋 Study Plan](docs/plan-estudios.md)
- [📖 Content Development Guide](docs/guia-desarrollo-contenidos.md)
- [🧱 Stack Versions](docs/stack-versions.md)
- [📐 Duration and Overlap Decision](docs/decision-semanas.md)
- [🧵 Common-Thread Project](docs/proyecto-hilo-conductor.md)
- [✅ Autograding](docs/autograding.md)
- [🗂️ Week Index](bootcamp/README.md)
- [🤖 Copilot Instructions](.github/copilot-instructions.md)
- [🐛 Report Issue](https://github.com/ergrato-dev/bc-testing/issues)

---

## ⚠️ Disclaimer

This repository is an open-source educational resource provided **"as is"**, without warranties of any kind, either express or implied.

- The content is **strictly educational** in nature and does not constitute professional advice of any kind.
- The authors and contributors are **not liable** for any direct, indirect, incidental, or consequential damages arising from the use of this material.
- Code examples are included for didactic purposes. Their use in production environments is the **sole responsibility of the user**.
- Third-party tools, frameworks, and libraries mentioned are subject to their own licenses and terms of use.
- This project is not affiliated with or officially endorsed by any of the mentioned tools or frameworks (Jest, pytest, JUnit 6, Playwright, etc.).

See the [LICENSE](LICENSE) file for the CC BY-NC-SA 4.0 license terms applicable to this project.

---

<p align="center">
  <strong>🎓 Software Testing Bootcamp Zero to Hero</strong><br>
  <em>From zero to QA Engineer / SDET Junior in ~9 months</em>
</p>

<p align="center">
  <a href="bootcamp/week-01-que_es_testing_calidad_de_software">Start Week 1</a> •
  <a href="docs/plan-estudios.md">View Study Plan</a> •
  <a href="https://github.com/ergrato-dev/bc-testing/issues">Report Issue</a>
</p>

<p align="center">
  Made with ❤️ for the testing and developer community
</p>
