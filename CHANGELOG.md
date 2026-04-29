# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

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

[Unreleased]: https://github.com/lukewalker85/automation-framework/compare/v0.5.0...HEAD
[0.5.0]: https://github.com/lukewalker85/automation-framework/compare/v0.4.0...v0.5.0
[0.4.0]: https://github.com/lukewalker85/automation-framework/compare/v0.3.0...v0.4.0
[0.3.0]: https://github.com/lukewalker85/automation-framework/compare/v0.2.0...v0.3.0
[0.2.0]: https://github.com/lukewalker85/automation-framework/commit/005d7ec
