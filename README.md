# Test Automation Framework for Petstore API

This project is a Test Automation Framework utilizing **TestNG** and **Spring** to validate the functionality of the Petstore API as defined in the [Swagger Petstore Specification](https://petstore.swagger.io/#/).

## Project Structure

- **Test Suites**:
  - `contractTestSuite.xml`: Contains tests to validate the API contracts.
  - `functionalTestSuite.xml`: Contains tests to validate the API's functional behavior.
- **Environment Support**:
  - `dev`, `qa`, and `prod` environments supported via system property `-Denvironment`.
    Please note that `dev` environment is default and will be used if no any environment is not explicitly specified 

## Prerequisites

- **Java Development Kit (JDK)**: Ensure JDK 18 or higher is installed.
- **Gradle**: This project uses the Gradle build system.

## Running Tests

Tests can be executed using Gradle with specific parameters to select the desired test suite.

### Running a Specific Test Suite

To run a specific test suite, use the `-Psuite` parameter:

```bash
./gradlew test -Psuite=contractTestSuite -Denvironment=dev
```
or for Windows
```cmd
.\gradlew test -Psuite=contractTestSuite -Denvironment=dev
```
Replace `contractTestSuite` with `functionalTestSuite` to run the functional tests.

### Running All Test Suites
To execute both test suites (all tests) sequentially:
```bash
./gradlew test -Denvironment=dev
```
or for Windows
```cmd
.\gradlew test -Denvironment=dev
```

## Test Reports
After test execution, reports are generated in the `build/reports/tests/test` directory. Open the `index.html` file in a browser to view detailed results.
