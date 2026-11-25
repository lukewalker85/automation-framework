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
| axe-core | Automated accessibility checks |

## Project Structure

```text
src/test/java/com/automation/
├── accessibility/    # axe-core accessibility checker, impact filter and allowlist
├── api/              # REST Assured API tests
├── base/             # Base test classes (driver, WireMock, live API)
├── listeners/        # TestNG retry analyzer and annotation transformer
├── pages/            # Page Object Model classes
├── reporting/        # Screenshot capture and storage
├── runners/          # Cucumber test runners
├── stepdefinitions/  # Cucumber step definitions
├── testdata/         # Test data records and constants
├── tests/            # TestNG integration tests
└── utils/            # ConfigReader, Environment, DriverFactory, helpers
src/test/resources/
├── features/         # Cucumber BDD feature files
├── testdata/         # Data-driven test data
├── a11y-allowlist.csv           # Accepted accessibility violations
├── config-dev.properties        # Dev environment config (default)
├── config-staging.properties    # Staging environment config
└── config-prod.properties       # Prod environment config
testng.xml                  # Full suite (unit + integration)
testng-smoke.xml            # Smoke suite (critical-path subset)
testng-regression.xml       # Regression suite (full coverage)
```

## Configuration

Non-sensitive configuration lives in `src/test/resources/config-<env>.properties`, one file per
environment (`dev`, `staging`, `prod`) — see [Environment Switching](#environment-switching) below.
Environment variables override file values for CI flexibility.

Test credentials are managed via environment variables (GitHub Secrets in CI). 
See [CONTRIBUTING.md](CONTRIBUTING.md) for local setup instructions.

## Environment Switching

Tests run against `dev` by default. Select a different environment via the `env` system property
on the command line, or the `TEST_ENV` environment variable:

```bash
# Run against staging
mvn verify -Denv=staging

# Run against prod
mvn verify -Denv=prod

# Equivalent using an environment variable
TEST_ENV=staging mvn verify
```

Each environment's configuration lives in its own file — `config-dev.properties`,
`config-staging.properties`, `config-prod.properties` — so environment-specific values (base
URLs, log level, etc.) stay independent. Add a new environment by creating a `config-<name>.properties`
file and a matching constant in `com.automation.utils.Environment`.

## Running Tests

> **Note:** Test credentials must be set before running tests. 
> See [Configuration](#configuration) above.

```bash
# Unit tests only
mvn test

# Full suite (unit + integration)
mvn verify

# Smoke suite (fast, critical-path subset)
mvn verify -Psmoke

# Regression suite (full coverage, includes smoke)
mvn verify -Pregression

# Specific browser
BROWSER=chrome mvn verify

# Specific environment
mvn verify -Denv=staging
```

Smoke and regression suites each write their Allure results to a dedicated subfolder (`target/allure-results/smoke` / `target/allure-results/regression`), so results from one suite never overwrite or mix with another. Run `mvn allure:serve` with the matching profile (e.g. `mvn allure:serve -Psmoke`) to inspect that suite's results.

## Logging

SLF4J with Log4j2 provides structured logging across all framework classes. Log level is configurable via the active environment's `config-<env>.properties` or the `LOG_LEVEL` environment variable:

```bash
LOG_LEVEL=DEBUG mvn verify
```

Logs are written to both the console and `target/logs/`, rotating at 10 MB. Valid levels: `DEBUG`, `INFO`, `WARN`, `ERROR`. Default: `INFO`.

## Retries

Failing tests are automatically retried, so transient failures (flaky UI timing, network blips) don't fail the build outright. Retry count is configurable via `RETRY_COUNT` in the active environment's `config-<env>.properties` (default: `2`):

```properties
RETRY_COUNT=2
```

Retries apply framework-wide via a TestNG `IAnnotationTransformer` (`com.automation.listeners.RetryTransformer`), so individual `@Test` methods don't need to opt in. Each retried attempt is logged and recorded as a failed step in the Allure report, with the failure reason, so retries are visible rather than appearing as a plain skip.

## Reporting

> **Note:** If the build fails before integration tests run (e.g. compilation error), the report will not be generated.

Allure generates interactive HTML test reports during `mvn verify`, after all tests have executed. Reports include pass/fail results with screenshots captured on failure.

Reports are written to `target/site/allure-maven-plugin/`. To view the report in a browser:

```bash
mvn allure:serve
```

This starts a temporary web server and opens the report automatically.

## Accessibility Checks

> **Automated checks don't replace manual testing.** axe-core catches roughly 30–40% of accessibility issues. Screen reader, keyboard-only and zoom testing are still needed before release.

The `@accessibility` scenarios in `accessibility.feature` scan the login, inventory, cart and checkout pages with [axe-core](https://github.com/dequelabs/axe-core-maven-html) against WCAG 2.2 AA (`wcag2a`, `wcag2aa`, `wcag21aa`, `wcag22aa`). They are also tagged `@regression`, so they run in the full and regression suites and in CI.

```bash
# As part of the full or regression suite
mvn verify
mvn verify -Pregression

# Accessibility scenarios only (the TestNG integration tests in the suite still run)
mvn verify -Pregression -Dcucumber.filter.tags=@accessibility
```

To check another page, navigate to it and add the step `Then the <page> page should have no blocking accessibility violations`.

### Fail level

Violations at or above `A11Y_FAIL_LEVEL` fail the scenario. Violations below it are reported but don't fail the build. Valid values are `minor`, `moderate`, `serious` and `critical`. The default is `serious`, so `critical` and `serious` violations fail the build and `moderate` and `minor` are reported only:

```properties
A11Y_FAIL_LEVEL=serious
```

As with other settings, an environment variable overrides the value in the active `config-<env>.properties`.

### Reading the results

Each scanned page adds an `Accessibility violations: <page>` attachment to the Allure report, grouped as **Blocking**, **Report only** and **Allowlisted**. Each violation lists its rule ID, impact, description, affected selectors and a help URL explaining how to fix it. When a scenario fails, the assertion message names the page and each blocking rule.

### Allowlisting known violations

Known, accepted violations go in `src/test/resources/a11y-allowlist.csv`, one per line:

```csv
ruleId,page,reason
color-contrast,checkout,Brand colours agreed with design, fix tracked in #123
```

- `ruleId` is the axe rule ID from the report, e.g. `color-contrast`.
- `page` must match the page name in the step, e.g. `checkout`. An entry covers that rule on that page only.
- `reason` is required. Give a ticket reference so the exclusion is reviewed rather than forgotten. It may contain commas.

Allowlisted violations still appear in the Allure report, so the suite stays green on purpose, not by accident. Remove the entry once the issue is fixed.

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
- [x] Allure test reporting
- [x] Automated accessibility checks (axe-core, WCAG 2.2 AA)
- [x] REST Assured + WireMock API testing
- [x] CartPage and cart tests
- [x] CheckoutPage and end-to-end checkout tests
- [x] Test grouping for smoke and regression suites
- [x] Retry failed tests
- [x] Environment switching
- [ ] Cross-browser test matrix
- [ ] Docker + Selenium Grid
