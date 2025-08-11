# API Automation Framework with AI

A comprehensive API automation framework built with Java, RestAssured, TestNG, and Allure Reports, featuring AI integration capabilities.

## 🚀 Features

- **REST API Testing**: Full support for GET, POST, PUT, DELETE operations
- **AI Integration**: OpenAI integration for dynamic test data generation
- **Comprehensive Reporting**: Allure reports with detailed test execution logs
- **Environment Management**: Support for multiple environments (dev, qa, prod)
- **Advanced Assertions**: JSON Path validation and custom assertion utilities
- **Logging**: Structured logging with Logback
- **Performance Testing**: Response time validation and thresholds
- **Parallel Execution**: Configurable parallel test execution

## 🛠️ Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Internet connection for API testing

## 📦 Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd APIAutomationFrameworkWithAI
```

2. Install dependencies:
```bash
mvn clean install
```

## 🏃‍♂️ Running Tests

### Run All Tests
```bash
mvn clean test
```

### Run Specific Test Suite
```bash
mvn test -DsuiteXmlFile=testng.xml
```

### Run with Specific Environment
```bash
mvn test -Denv=qa
mvn test -Denv=prod
mvn test -Denv=dev
```

### Generate Allure Report
```bash
mvn allure:report
```

### View Allure Report
```bash
mvn allure:serve
```

## 📁 Project Structure

```
src/
├── test/
│   ├── java/
│   │   └── com/
│   │       └── apiautomation/
│   │           └── framework/
│   │               ├── BaseTest.java              # Base test class
│   │               ├── config/
│   │               │   └── ConfigManager.java     # Configuration management
│   │               ├── tests/
│   │               │   ├── WorkingAPITest.java    # Main API tests
│   │               │   ├── SimpleWorkingTest.java # Basic functionality tests
│   │               │   └── MinimalWorkingTest.java # Minimal test examples
│   │               └── utils/
│   │                   └── ApiUtils.java          # API utility methods
│   └── resources/
│       ├── config/
│       │   ├── dev.properties                     # Development environment
│       │   ├── qa.properties                      # QA environment
│       │   └── prod.properties                    # Production environment
│       ├── logback.xml                            # Logging configuration
│       └── schemas/
│           └── user-schema.json                   # JSON schemas for validation
```

## ⚙️ Configuration

### Environment Configuration

The framework supports multiple environments through property files:

- `dev.properties` - Development environment
- `qa.properties` - QA environment (default)
- `prod.properties` - Production environment

### Key Configuration Properties

```properties
# API Configuration
base.url=https://jsonplaceholder.typicode.com
api.timeout=30000
retry.count=3

# AI Configuration
ai.enabled=true
openai.api.key=your-api-key
openai.model=gpt-3.5-turbo

# Test Configuration
test.parallel.threads=4
test.suite.timeout=300000
test.method.timeout=60000

# Performance Thresholds
max.response.time=5000
max.throughput=100
```

### Setting Environment

```bash
# Set environment via system property
mvn test -Denv=prod

# Set environment via environment variable
export ENV=prod
mvn test
```

## 🧪 Writing Tests

### Basic Test Structure

```java
@Epic("API Testing Framework")
@Feature("User Management")
public class UserAPITest extends BaseTest {

    @Test
    @Story("Get User")
    @Description("Test GET /users/{id} endpoint")
    public void testGetUser() {
        Response response = io.restassured.RestAssured.given()
                .when()
                .get("/users/1")
                .then()
                .statusCode(200)
                .extract().response();

        // Verify response
        verifyStatusCode(response, 200);
        verifyResponseTime(response);
        logResponseDetails(response, "testGetUser");
        
        // Custom assertions
        ApiUtils.verifyJsonFieldExists(response, "$.id", "User ID");
        ApiUtils.verifyJsonFieldValue(response, "$.name", "Leanne Graham", "User Name");
    }
}
```

### Using BaseTest Features

The `BaseTest` class provides:

- **Automatic Setup**: Base URL and timeout configuration
- **Response Validation**: Status code and response time verification
- **Logging**: Structured logging with SLF4J
- **Configuration Access**: Environment-specific configuration values

### Using ApiUtils

```java
// Verify JSON structure
ApiUtils.verifyJsonStructure(response, "id", "name", "email");

// Extract values
String userId = ApiUtils.extractJsonValue(response, "$.id", String.class);

// Verify array size
ApiUtils.verifyJsonArraySize(response, "$.users", 10, "Users Array");
```

## 📊 Test Reports

### Allure Reports

The framework generates comprehensive Allure reports including:

- Test execution results
- Step-by-step test details
- Response data and validation
- Performance metrics
- Environment information

### Viewing Reports

1. Generate report:
```bash
mvn allure:report
```

2. View report:
```bash
mvn allure:serve
```

3. Open browser at: `http://localhost:12345`

### Surefire Reports

Maven Surefire generates HTML reports in `target/surefire-reports/`:

- Test execution summary
- Pass/fail statistics
- Detailed error logs

## 🔧 Customization

### Adding New Test Classes

1. Create new test class extending `BaseTest`
2. Add test methods with appropriate annotations
3. Update `testng.xml` to include new test class

### Custom Assertions

Extend `ApiUtils` class to add custom validation methods:

```java
public static void verifyCustomBusinessRule(Response response) {
    // Custom business logic validation
}
```

### Environment-Specific Logic

Use `ConfigManager` to access environment-specific values:

```java
String baseUrl = ConfigManager.getBaseUrl();
int timeout = ConfigManager.getApiTimeout();
boolean aiEnabled = ConfigManager.isAiEnabled();
```

## 🚨 Troubleshooting

### Common Issues

1. **Class Not Found**: Ensure test classes are in correct package structure
2. **Configuration Not Loaded**: Check property file names and paths
3. **API Timeout**: Adjust timeout values in configuration files
4. **Allure Report Issues**: Verify Allure plugin configuration in pom.xml

### Debug Mode

Enable debug logging:

```bash
mvn test -Dlogging.level=DEBUG
```

### Verbose Test Execution

```bash
mvn test -Dsuite.verbose=true
```

## 🤝 Contributing

1. Fork the repository
2. Create feature branch
3. Add tests for new functionality
4. Ensure all tests pass
5. Submit pull request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For issues and questions:

1. Check existing issues
2. Review documentation
3. Create new issue with detailed description
4. Include logs and error messages

## 🔄 Version History

- **v1.0.0**: Initial framework with basic API testing capabilities
- **v1.1.0**: Added AI integration and advanced utilities
- **v1.2.0**: Enhanced configuration management and reporting 