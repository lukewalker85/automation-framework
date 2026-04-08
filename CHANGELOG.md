# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

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

[Unreleased]: https://github.com/lukewalker85/automation-framework/compare/v0.3.0...HEAD
[0.3.0]: https://github.com/lukewalker85/automation-framework/compare/v0.2.0...v0.3.0
[0.2.0]: https://github.com/lukewalker85/automation-framework/commit/005d7ec
