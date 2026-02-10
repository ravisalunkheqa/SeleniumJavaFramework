# 🔬 Selenium Java Test Automation Framework

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://openjdk.org/)
[![Selenium](https://img.shields.io/badge/Selenium-4.27-green.svg)](https://selenium.dev/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> **A comprehensive Selenium Java test automation framework with Page Object Model, TestNG, and cloud platform integration.**

**Author:** [Pramod Dutta](https://thetestingacademy.com)  
**Website:** [The Testing Academy](https://thetestingacademy.com)

---

## 📋 Table of Contents

- [Features](#-features)
- [Architecture](#-architecture)
- [Prerequisites](#-prerequisites)
- [Quick Start](#-quick-start)
- [Project Structure](#-project-structure)
- [Step-by-Step Setup](#-step-by-step-setup)
- [Running Tests](#-running-tests)
- [Configuration](#-configuration)
- [Contributing](#-contributing)

---

## ✨ Features

### Test Automation Framework (Java)
- ✅ **Page Object Model (POM)** - Clean separation of test logic and page elements
- ✅ **Selenium 4.27** - Latest Selenium with built-in WebDriver Manager
- ✅ **TestNG** - Powerful test framework with parallel execution support
- ✅ **Allure Reporting** - Beautiful test reports with screenshots on failure
- ✅ **Automatic Screenshots** - Captures screenshots, page source, and URL on test failure
- ✅ **Structured Logging** - Comprehensive logging for debugging

### Cloud Testing Platforms
- ☁️ *OpenTelemetry Integration** - Comprehensive observability and tracing

### Cloud Testing Platforms
- ☁️ **BrowserStack Integration** - Run tests on 3000+ real browsers and devices
- ☁️ **LambdaTest Integration** - Scalable cross-browser testing in the cloud
- 🖥️ **Local Execution** - Run tests locally with Chrome, Firefox, or Edge
- 🔄 **Easy Switching** - Switch between local/cloud with a single parameter

### CI/CD Integration
- 🔧 **Jenkins Pipeline** - Ready-to-use Jenkinsfile with all stages
- 📦 **Parameterized Builds** - Configure browser, environment, and test suite
- 📊 **Allure Reports in Jenkins** - Integrated reporting with trend analysi
<img width="3600" height="3972" alt="image" src="https://github.com/user-attachments/assets/ee4079b7-f789-4c6a-9df0-fc58762fcbb3" />


## 🏗 Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                    TEST AUTOMATION FRAMEWORK                        │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐          │
│  │  Test Cases  │───▶│  Page Objects │───▶│   Selenium   │          │
│  │  (TestNG)    │    │    (POM)      │    │   WebDriver  │          │
│  └──────────────┘    └──────────────┘    └──────────────┘          │
│         │                                                           │
│         ▼                                                           │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐          │
│  │   Allure     │    │  OpenTelemetry│   │  Screenshots │          │
│  │   Reports    │    │   Tracing     │    │  on Failure  │          │
│  └──────────────┘    └──────────────┘    └──────────────┘          │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 📦 Prerequisites

| Requirement | Version | Purpose |
|-------------|---------|---------|
| **Java JDK** | 17+ | Running Selenium tests |
| **Maven** | 3.8+ | Build and dependency management |
| **Chrome/Firefox** | Latest | Browser for testing |

---

## 🚀 Quick Start

```bash
# 1. Clone the repository
git clone https://github.com/pramods12/AIATBSeleniumJavaFramework.git
cd AIATBSeleniumJavaFramework

# 2. Install Java (macOS)
brew install openjdk@17

# 3. Run tests
./mvnw clean test

# 4. View Allure report
./mvnw allure:serve
```

---

## 📁 Project Structure

```
AIATBSeleniumJavaFramework/
├── 📄 pom.xml                      # Maven configuration
├── 📄 testng.xml                   # TestNG suite configuration
├── 📄 mvnw                         # Maven wrapper
│
├── 📂 src/
│   ├── 📂 main/java/com/automation/
│   │   ├── 📂 pages/               # Page Object classes
│   │   │   ├── BasePage.java
│   │   │   ├── LoginPage.java
│   │   │   └── DashboardPage.java
│   │   ├── 📂 listeners/           # TestNG listeners
│   │   │   └── AllureScreenshotListener.java
│   │   └── 📂 analytics/           # Test analytics
│   │       └── TestAnalyticsLogger.java
│   │
│   └── 📂 test/java/com/automation/
│       ├── 📂 base/                # Base test class
│       │   └── BaseTest.java
│       └── 📂 tests/               # Test classes
│           └── LoginTest.java
│
└── 📂 target/                      # Build output
    └── 📂 allure-results/          # Allure report data
```

---

## 📝 Step-by-Step Setup

### Step 1: Install Java JDK 17+

**macOS (Homebrew):**
```bash
brew install openjdk@17
echo 'export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
java -version
```

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install openjdk-17-jdk
java -version
```

**Windows:**
1. Download from [Adoptium](https://adoptium.net/temurin/releases/)
2. Run installer
3. Add to PATH

### Step 2: Clone and Build Project

```bash
# Clone repository
git clone https://github.com/yourusername/AIATBSeleniumJavaFramework.git
cd AIATBSeleniumJavaFramework

# Make Maven wrapper executable
chmod +x mvnw

# Build project (downloads dependencies)
./mvnw clean compile
```

### Step 3: Run Tests

```bash
# Run all tests locally
./mvnw clean test

# Run specific test class
./mvnw test -Dtest=LoginTest

# Run with specific browser
./mvnw test -Dbrowser=firefox

# Run in headless mode (for CI)
./mvnw test -Dheadless=true
```

### Step 3.1: Run on BrowserStack

```bash
# Set credentials (or use execution.env file)
export BROWSERSTACK_USERNAME=your_username
export BROWSERSTACK_ACCESS_KEY=your_access_key

# Run tests on BrowserStack
./mvnw test -Dexecution.env=browserstack -Dbrowser=chrome
```

### Step 3.2: Run on LambdaTest

```bash
# Set credentials (or use execution.env file)
export LAMBDATEST_USERNAME=your_username
export LAMBDATEST_ACCESS_KEY=your_access_key

# Run tests on LambdaTest
./mvnw test -Dexecution.env=lambdatest -Dbrowser=chrome
```

### Step 4: View Allure Report

```bash
# Generate and open Allure report
./mvnw allure:serve
```

This opens a beautiful HTML report showing:
- ✅ Test results with pass/fail status
- 📸 Screenshots on failure
- 📄 Page source on failure
- 🔗 URLs where tests failed

---

## 🧪 Running Tests

### Basic Commands

```bash
# Run all tests
./mvnw clean test

# Run with Allure report generation
./mvnw clean test allure:serve

# Run specific test class
./mvnw test -Dtest=LoginTest

# Run specific test method
./mvnw test -Dtest=LoginTest#testSuccessfulLogin

# Run tests in parallel (configured in testng.xml)
./mvnw test -Dparallel=methods -DthreadCount=4
```

### Test Output Locations

| Output | Location | Description |
|--------|----------|-------------|
| Test Results | `target/surefire-reports/` | TestNG XML reports |
| Allure Data | `target/allure-results/` | Allure report data |
| Screenshots | `target/allure-results/*.png` | Failure screenshots |

---

## ⚙️ Configuration

### Java Framework Configuration

**testng.xml** - Test suite configuration:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Automation Test Suite" parallel="methods" thread-count="2">
    <listeners>
        <listener class-name="io.qameta.allure.testng.AllureTestNg"/>
        <listener class-name="com.automation.listeners.AllureScreenshotListener"/>
    </listeners>
    <test name="Login Tests">
        <classes>
            <class name="com.automation.tests.LoginTest"/>
        </classes>
    </test>
</suite>
```

---

## 🛠 Extending the Framework

### Adding New Page Objects

```java
// src/main/java/com/automation/pages/NewPage.java
public class NewPage extends BasePage {

    @FindBy(id = "element-id")
    private WebElement myElement;

    public NewPage(WebDriver driver) {
        super(driver);
    }

    public void clickElement() {
        click(myElement);
    }
}
```

### Adding New Tests

```java
// src/test/java/com/automation/tests/NewTest.java
public class NewTest extends BaseTest {

    @Test
    @Description("Test description for Allure")
    public void testNewFeature() {
        // Your test logic
    }
}
```

---

## 🔧 Jenkins CI/CD Integration

### Jenkinsfile Features

The included `Jenkinsfile` provides a complete CI/CD pipeline:

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│  Checkout   │───▶│    Build    │───▶│  Run Tests  │───▶│   Allure    │───▶│ AI Analysis │
│             │    │   Compile   │    │  (Params)   │    │   Report    │    │   Report    │
└─────────────┘    └─────────────┘    └─────────────┘    └─────────────┘    └─────────────┘
```

### Pipeline Parameters

| Parameter | Options | Description |
|-----------|---------|-------------|
| `BROWSER` | chrome, firefox, edge | Browser for test execution |
| `EXECUTION_ENV` | local, browserstack, lambdatest | Test execution environment |
| `TEST_SUITE` | testng.xml | TestNG suite file |

### Jenkins Setup

1. **Install Required Plugins:**
   - Allure Jenkins Plugin
   - Pipeline Plugin
   - Credentials Plugin

2. **Configure Credentials:**
   ```
   Jenkins → Manage Jenkins → Credentials → Add:
   - browserstack-username (Secret text)
   - browserstack-access-key (Secret text)
   - lambdatest-username (Secret text)
   - lambdatest-access-key (Secret text)
   ```

3. **Configure Tools:**
   ```
   Jenkins → Global Tool Configuration:
   - JDK17 (Java 17)
   - Maven3 (Maven 3.8+)
   ```

4. **Create Pipeline Job:**
   - New Item → Pipeline
   - Pipeline → Definition: Pipeline script from SCM
   - SCM: Git → Repository URL: your-repo-url
   - Script Path: Jenkinsfile

### Running Pipeline

```bash
# Trigger with default parameters
# Or use Jenkins UI to customize:
# - Browser: chrome/firefox/edge
# - Environment: local/browserstack/lambdatest
# - AI Report: enabled/disabled
```

---

## ☁️ Cloud Testing Configuration

### BrowserStack Setup

1. **Get Credentials:** https://www.browserstack.com/accounts/settings

2. **Set Environment Variables:**
   ```bash
   export BROWSERSTACK_USERNAME=your_username
   export BROWSERSTACK_ACCESS_KEY=your_access_key
   ```

3. **Run Tests:**
   ```bash
   ./mvnw test -Dexecution.env=browserstack -Dbrowser=chrome
   ```

4. **View Results:** https://automate.browserstack.com/dashboard

### LambdaTest Setup

1. **Get Credentials:** https://accounts.lambdatest.com/detail/profile

2. **Set Environment Variables:**
   ```bash
   export LAMBDATEST_USERNAME=your_username
   export LAMBDATEST_ACCESS_KEY=your_access_key
   ```

3. **Run Tests:**
   ```bash
   ./mvnw test -Dexecution.env=lambdatest -Dbrowser=firefox
   ```

4. **View Results:** https://automation.lambdatest.com/timeline

### Cloud Capabilities

Both platforms are configured with:
- Video recording of test execution
- Network logs capture
- Console logs capture
- Screenshot on failure
- Selenium 4.27 support

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request
- ✅ Passed test summary
- 🧠 AI analysis information

---

## 📋 Context YAML

The `context.yaml` file contains all the information needed to recreate this project. Use it as:

1. **Reference** - Understand the project structure and components
2. **AI Context** - Provide to AI assistants to understand the project
3. **Documentation** - Quick overview of all technologies and configurations

```yaml
# Key sections in context.yaml:
project:        # Project metadata
stack:          # Technology versions (Java, Python, libraries)
structure:      # File/folder organization
features:       # Detailed feature descriptions
commands:       # All CLI commands needed
test_site:      # Test application details
```

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Pramod Dutta**
- Website: [The Testing Academy](https://thetestingacademy.com)
- YouTube: [The Testing Academy](https://youtube.com/thetestingacademy)
- LinkedIn: [Pramod Dutta](https://linkedin.com/in/pramoddutta)

---

## ⭐ Star History

If you find this project useful, please consider giving it a star! ⭐

---

<p align="center">
  Made with ❤️ by <a href="https://thetestingacademy.com">The Testing Academy</a>
</p>

<img width="3600" height="2954" alt="image" src="https://github.com/user-attachments/assets/bec7ac92-225e-4592-a980-d07fc60878c5" />

