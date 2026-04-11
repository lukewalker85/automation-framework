[![CI](https://github.com/lukewalker85/automation-framework/actions/workflows/ci.yml/badge.svg)](https://github.com/lukewalker85/automation-framework/actions)

# Automation Framework

A scalable test automation framework built from scratch using Java and Selenium 
WebDriver, demonstrating industry best practices in test architecture, 
BDD, API testing and CI/CD integration.

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
| CodeRabbit | Automated AI code reviews on pull requests |

## Project Structure

```
src/test/java/com/automation/
├── api/              # REST Assured IAM API tests
├── base/             # BaseTest - driver lifecycle management
├── pages/            # Page Object Model classes
├── runners/          # Cucumber test runners
├── stepdefinitions/  # Cucumber step definitions
├── tests/            # TestNG test classes
└── utils/            # ConfigReader and helper utilities
src/test/resources/
├── features/         # Cucumber BDD feature files
└── testdata/         # Data driven test data
```

## Running Tests

```bash
# Run full test suite
mvn test

# Run with specific browser (set in config.properties)
# Options: firefox, chrome, edge
BROWSER=chrome mvn test
```

## Configuration

All environment configuration lives in `src/test/resources/config.properties`.

> **Note:** In production, sensitive values such as passwords and URLs should
> be stored as environment variables or a secrets manager, never committed to Git.

## Git Convention

Commits follow the Conventional Commits standard:

| Prefix | Meaning |
|---|---|
| `feat:` | New feature or class |
| `fix:` | Bug fix |
| `refactor:` | Code restructure without behaviour change |
| `chore:` | Configuration or maintenance |

From v0.2.0 onwards commits reference GitHub issue numbers e.g. `closes #4`.
Earlier commits predate the project board setup.

## Git Workflow

From v0.2.0 onwards all changes are made on feature branches 
and merged to master via Pull Requests.
Earlier commits were made directly to master during initial setup.

Branch naming convention:
- `feature/` — new features e.g. `feature/1-cucumber-step-definitions`
- `fix/` — bug fixes e.g. `fix/18-login-test-coverage`
- `chore/` — maintenance e.g. `chore/update-dependencies`
- `refactor/` - Code restructure without behaviour change 

## Code Review

All pull requests are reviewed by [CodeRabbit](https://coderabbit.ai) before merging.
The review is configured in `.coderabbit.yaml` with an assertive profile
that provides inline comments and a high-level summary on every PR.

Pull request workflow:
1. Create feature branch from master
2. Push changes and open a pull request
3. CI pipeline runs tests automatically
4. CodeRabbit performs automated code review
5. Address any feedback from review
6. Merge to master once approved and CI is green

## Roadmap
- [x] Cucumber BDD feature files and step definitions
- [ ] TestNG DataProvider driven tests
- [ ] Screenshot on failure
- [ ] Log4j logging
- [ ] WireMock IAM API mocking with REST Assured
- [ ] Add CartPage and cart tests
- [ ] Add CheckoutPage and end to end checkout tests
- [ ] Add test grouping for smoke and regression suites
- [ ] Allure test reporting
- [x] Headless browser mode
- [ ] Environment switching
- [ ] Cross browser test matrix
- [ ] Docker + Selenium Grid
- [ ] Jenkins CI/CD pipeline
- [x] GitHub Actions workflow
- [ ] Retry failed tests
- [ ] Slack notifications
- [x] AI code reviews on pull requests
