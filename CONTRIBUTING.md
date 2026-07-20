# Contributing

Thanks for your interest in contributing to the Automation Framework.

## Prerequisites

- Java 21
- Maven
- Firefox (default browser)

## Getting Started

1. Fork and clone the repository
2. Run `mvn verify` to confirm all tests pass
3. Create a feature branch from `master`

## Test Credentials

Test credentials are not stored in the repository. Set these environment 
variables before running tests:

- `STANDARD_USER`
- `LOCKED_OUT_USER`
- `PASSWORD`
- `INVALID_PASSWORD`
- `API_USERNAME`
- `API_PASSWORD`

> `INVALID_PASSWORD` must be a value that does not match `PASSWORD` to ensure negative login tests fail for the correct reason.

Example (Git Bash):

```bash
STANDARD_USER=<value> LOCKED_OUT_USER=<value> PASSWORD=<value> INVALID_PASSWORD=<value> API_USERNAME=<value> API_PASSWORD=<value> mvn verify
```

API username and password are available on the [restful-booker](https://restful-booker.herokuapp.com/apidoc/index.html#api-Auth-CreateToken) help page.
Other values are available on the [SauceDemo](https://www.saucedemo.com/) login page.

In CI, these are passed via GitHub Secrets.

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

# Smoke suite (fast, critical-path subset)
mvn verify -Psmoke

# Regression suite (full coverage, includes smoke)
mvn verify -Pregression

# Specific browser
BROWSER=chrome mvn verify
```

Smoke and regression suites each write their Allure results to a dedicated subfolder (`target/allure-results/smoke` / `target/allure-results/regression`), so results from one suite never overwrite or mix with another. Run `mvn allure:serve` with the matching profile (e.g. `mvn allure:serve -Psmoke`) to inspect that suite's results.

## Branch Naming

| Prefix | Use |
|---|---|
| `feature/` | New features, e.g. `feature/1-cucumber-step-definitions` |
| `fix/` | Bug fixes, e.g. `fix/18-login-test-coverage` |
| `chore/` | Maintenance, e.g. `chore/40-spotless-enforcement` |
| `refactor/` | Code restructure without behaviour change |

Branches reference the GitHub issue number: `feature/N-short-description`.

## Commit Messages

Commits follow [Conventional Commits](https://www.conventionalcommits.org/):

```text
feat: add cart page object refs #12
fix: resolve stale element in inventory sort refs #18
chore: cache Maven dependencies in CI refs #45
```

- Use `refs #N` in commits, `closes #N` in the PR body
- Describe the behaviour, not the file
- Keep the subject line under 72 characters

## Pull Request Process

1. Create a feature branch from `master`
2. Make your changes and run `mvn verify` locally
3. Push and open a pull request against `master`
4. CI pipeline runs tests automatically
5. CodeRabbit performs automated code review
6. Address any feedback
7. Merge once CI is green and review is approved

## Code Formatting

Formatting is enforced by [Spotless](https://github.com/diffplug/spotless) using [Google Java Format](https://github.com/google/google-java-format). The check runs during Maven's `validate` phase — any violation fails the build.

To auto-fix formatting violations:

```bash
mvn spotless:apply
```

For automatic formatting on save, install a Google Java Format plugin:
- **IntelliJ IDEA:** [google-java-format plugin](https://plugins.jetbrains.com/plugin/8527-google-java-format)
- **VS Code:** [Language Support for Java by Red Hat](https://marketplace.visualstudio.com/items?itemName=redhat.java)

## Logging

All public classes should include SLF4J logging:
- `INFO` for business actions and lifecycle events
- `DEBUG` for low-level interactions
- Use parameterised message syntax: `LOG.info("Logging in with username: {}", username);`

## Git Blame

A mass reformat commit exists in the history. To skip it in local blame:

```bash
git config blame.ignoreRevsFile .git-blame-ignore-revs
```

GitHub's web UI handles this automatically.
