[![CI](https://github.com/lukewalker85/automation-framework/actions/workflows/ci.yml/badge.svg)](https://github.com/lukewalker85/automation-framework/actions)
[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat&logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/21/)
[![License](https://img.shields.io/github/license/lukewalker85/automation-framework)](LICENSE)

# Automation Framework

A scalable test automation framework built from scratch using Java and Selenium WebDriver, demonstrating industry best practices in test architecture, BDD, API testing and CI/CD integration.

## Tech Stack

| Tool | Purpose |
|---|---|
| Java 21 | Core language |
| Selenium WebDriver 4 | Browser automation |
| TestNG | Test execution and parallel running |
| Cucumber | BDD feature files and step definitions |
| REST Assured | API test automation |
| WireMock | API mocking |
| AssertJ | Fluent assertions |
| WebDriverManager | Automatic driver management |
| Maven | Build and dependency management |
| GitHub Actions | Continuous integration |
| CodeRabbit | Automated AI code reviews |
| JUnit 5 | Unit test execution |
| SLF4J + Log4j2 | Structured logging |

## Project Structure

```text
src/test/java/com/automation/
├── api/              # REST Assured API tests
├── base/             # BaseTest — driver lifecycle management
├── pages/            # Page Object Model classes
├── reporting/        # Screenshot capture and storage
├── runners/          # Cucumber test runners
├── stepdefinitions/  # Cucumber step definitions
├── testdata/         # Test data records and constants
├── tests/            # TestNG integration tests
└── utils/            # ConfigReader, DriverFactory, helpers
src/test/resources/
├── features/         # Cucumber BDD feature files
└── testdata/         # Data-driven test data
```

## Configuration

Non-sensitive configuration lives in `src/test/resources/config.properties`. 
Environment variables override file values for CI flexibility.

Test credentials are managed via environment variables (GitHub Secrets in CI). 
See [CONTRIBUTING.md](CONTRIBUTING.md) for local setup instructions.

## Running Tests

> **Note:** Test credentials must be set before running tests. 
> See [Configuration](#configuration) above.

```bash
# Unit tests only
mvn test

# Full suite (unit + integration)
mvn verify

# Specific browser
BROWSER=chrome mvn verify
```

## Logging

SLF4J with Log4j2 provides structured logging across all framework classes. Log level is configurable via `config.properties` or the `LOG_LEVEL` environment variable:

```bash
LOG_LEVEL=DEBUG mvn verify
```

Logs are written to both the console and `target/logs/`, rotating at 10 MB. Valid levels: `DEBUG`, `INFO`, `WARN`, `ERROR`. Default: `INFO`.

## Reporting

Allure generates interactive HTML test reports automatically during `mvn verify`. Reports include pass/fail results with screenshots captured on failure.

Reports are written to `target/site/allure-maven-plugin/`. To view the report in a browser:

```bash
mvn allure:serve
```

This starts a temporary web server and opens the report automatically.

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for branch naming, commit conventions, PR process, and code formatting requirements.

## Licence

This project is licensed under the MIT License — see [LICENSE](LICENSE) for details.

## Roadmap

- [x] Cucumber BDD feature files and step definitions
- [x] TestNG DataProvider-driven tests
- [x] Screenshot on failure
- [x] Log4j logging
- [x] Headless browser mode
- [x] GitHub Actions workflow
- [x] AI code reviews on pull requests
- [ ] Allure test reporting
- [ ] REST Assured + WireMock API testing
- [ ] CartPage and cart tests
- [ ] CheckoutPage and end-to-end checkout tests
- [ ] Test grouping for smoke and regression suites
- [ ] Environment switching
- [ ] Cross-browser test matrix
- [ ] Docker + Selenium Grid
- [ ] Retry failed tests
