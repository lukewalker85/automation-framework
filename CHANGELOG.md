# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.8.0] - 2026-11-25

### Added

- API test utilities: RequestSpecificationFactory, ResponseSpecificationFactory and AuthTokenManager, with unit tests (#96)
- `BASE_API_URI` config and `API_USERNAME` / `API_PASSWORD` environment variables, passed in from GitHub Secrets in CI (#96)
- WireMock-mocked IAM API tests (AuthTest, UserOperationsTest) with a BaseApiTest that manages the WireMock server lifecycle (#97)
- REST Assured tests against the live restful-booker API (BookingApiIT) with a BaseLiveApiTest base class (#98)
- Request and response logging for live API tests when `LOG_LEVEL` is `DEBUG` (#98)
- JSON schema validation for API responses using REST Assured's json-schema-validator, with a create-booking schema (#99)
- Smoke and regression test groups: `testng-smoke.xml` and `testng-regression.xml` suites, `smoke` and `regression` Maven profiles, Cucumber smoke and regression runners, and `@smoke` / `@regression` tags on feature files (#100)
- Separate Allure results folders per suite (`allure-results/smoke`, `allure-results/regression`) (#100)
- Configurable retries for flaky tests via RetryAnalyzer and RetryTransformer, with `RETRY_COUNT` in config (default: `2`) (#101)
- Retry attempts recorded as failed steps in the Allure report (#101)
- `ConfigReader.getInt` for integer settings with a default (#101)
- Environment switching between `dev`, `staging` and `prod` via `-Denv` or the `TEST_ENV` environment variable (default: `dev`) (#102)
- Environment enum and `ConfigReader.forEnvironment()` to load the matching config file (#102)
- Automated accessibility checks with axe-core (`com.deque.html.axe-core:selenium`) against WCAG 2.2 AA (`wcag2a`, `wcag2aa`, `wcag21aa`, `wcag22aa`)
- AccessibilityChecker to scan the current page and fail on blocking violations
- Impact filtering: violations at or above `A11Y_FAIL_LEVEL` fail the build, lower ones are reported only (default: `serious`)
- `A11Y_FAIL_LEVEL` setting in each environment config
- Allowlist for known, accepted violations in `a11y-allowlist.csv` (rule ID, page and reason)
- Allure attachment per scanned page listing each violation's rule ID, impact, description, selectors and help URL
- `@accessibility` scenarios for the login, inventory, cart and checkout pages, included in the regression suite
- Unit tests for impact filtering, the allowlist and the checker
- Accessibility Checks section in README
- README and CONTRIBUTING sections for API credentials, test suites, retries and environment switching

### Changed

- `config.properties` split into `config-dev.properties`, `config-staging.properties` and `config-prod.properties` (#102)

### Fixed

- An invalid environment name now fails with a clear error when RetryAnalyzer is created, instead of an `ExceptionInInitializerError` the first time the class is referenced (#102)
- Jackson modules aligned on one version via the Jackson BOM, fixing a `NoClassDefFoundError` after axe-core was added

## [0.7.0] - 2026-05-06

### Added

- Allure test reporting with interactive HTML reports
- allure-testng and allure-cucumber7-jvm dependencies for test result capture
- allure-maven plugin bound to post-integration-test phase for automatic report generation
- Screenshot attachments on test failure in Allure reports (Hooks and ScreenshotListener)
- allure.properties configuration file for results directory
- Reporting section in README with generation and viewing instructions

### Changed

- ScreenshotListener.onTestFailure refactored to separate screenshot capture from file storage
- CucumberRunnerIT plugin options updated to include AllureCucumber7Jvm
- .gitignore updated to exclude .allure/ directory

## [0.6.1] - 2026-05-06

### Added

- 15 minute timeout to CI workflow
- Maven dependency caching in CI
- MIT License
- CONTRIBUTING.md with branch naming, commit conventions, and PR process
- README badges for Java version and license
- Configurable explicit wait timeout via WAIT_TIMEOUT_SECONDS config or environment variable

### Changed
- Test credentials removed from config.properties and managed via environment variables (GitHub Secrets in CI)
- README restructured to reference CONTRIBUTING.md for contributor-focused content
- BasePage reads wait timeout from system property set by BaseTest

## [0.6.0] - 2026-05-04

### Added

- InventoryPage page object with sort, cart badge, product count and navigation methods
- ProductDetailsPage page object with product name, price and back button methods
- CartPage page object with remove item, item list and checkout button methods
- CheckoutStepOnePage, CheckoutStepTwoPage and CheckoutCompletePage page objects
- ScenarioContext for sharing state between Cucumber step definition classes
- Cucumber scenarios for inventory sorting, cart badge, product count
- Cucumber scenarios for product detail navigation and data verification
- Cucumber scenarios for logout and direct URL access
- Cucumber scenarios for cart add, remove and item count
- End-to-end checkout flow scenario with mid-flow product verification
- Checkout form validation scenario for missing shipping information
- Test user shipping details in config.properties

### Changed

- ProductDetailsSteps refactored to store product data in ScenarioContext
- InventorySteps add-to-cart step parameterised with product name

## [0.5.0] - 2026-04-28

### Added

- Screenshot capture on test failure for both TestNG and Cucumber tests
- ScreenshotStore pluggable interface for future storage backends (e.g. S3)
- LocalScreenshotStore saves screenshots to target/screenshots/
- ScreenshotListener registered via @Listeners for automatic TestNG coverage
- Cucumber screenshot capture via Hooks on scenario failure
- Path traversal protection in screenshot storage
- Filename sanitisation replacing illegal filesystem characters
- Millisecond precision timestamps to prevent filename collisions
- Unit tests for LocalScreenshotStore, ScreenshotStore filename building
- DriverFactory extracted from BaseTest as a pure factory class
- Unit test for DriverFactory unsupported browser exception

### Changed

- BaseTest delegates browser creation to DriverFactory
- Hooks refactored with extracted captureScreenshot method
- Screenshot capture uses broad exception handling to prevent resource leaks

## [0.4.0] - 2026-04-25

### Added

- SLF4J and Log4j2 structured logging across all framework classes
- Configurable log level via config.properties or LOG_LEVEL environment variable
- JUnit 5 unit tests for ConfigReader
- TestNG DataProvider driven login tests with LoginScenario record
- Spotless code formatting with Google Java Format
- Logging section in README

### Changed

- ConfigReader refactored for dependency injection and testability
- Login error messages centralised in LoginErrorMessages constants class
- Integration tests renamed to *IT convention
- Surefire runs JUnit 5 unit tests, Failsafe runs TestNG integration tests

### Fixed

- Log4j2 reconfigured at runtime to apply configured log level
- Driver ThreadLocal cleanup runs even if quit() throws
- BROWSER config defaults to Firefox when missing

## [0.3.0] - 2026-04-06

### Added

- Cucumber BDD with feature files, step definitions, hooks and runner
- GitHub Actions CI workflow
- Headless browser mode
- Parallel execution for Cucumber scenarios
- CodeRabbit AI code review integration
- Invalid password login test

### Fixed

- LoginPage thread safety for parallel execution

### Changed

- Branch protection requires CI and code review approval before merge to master

## [0.2.0] - 2026-04-02

Initial tagged release.

[Unreleased]: https://github.com/lukewalker85/automation-framework/compare/v0.8.0...HEAD
[0.8.0]: https://github.com/lukewalker85/automation-framework/compare/v0.7.0...v0.8.0
[0.7.0]: https://github.com/lukewalker85/automation-framework/compare/v0.6.1...v0.7.0
[0.6.1]: https://github.com/lukewalker85/automation-framework/compare/v0.6.0...v0.6.1
[0.6.0]: https://github.com/lukewalker85/automation-framework/compare/v0.5.0...v0.6.0
[0.5.0]: https://github.com/lukewalker85/automation-framework/compare/v0.4.0...v0.5.0
[0.4.0]: https://github.com/lukewalker85/automation-framework/compare/v0.3.0...v0.4.0
[0.3.0]: https://github.com/lukewalker85/automation-framework/compare/v0.2.0...v0.3.0
[0.2.0]: https://github.com/lukewalker85/automation-framework/commit/005d7ec
