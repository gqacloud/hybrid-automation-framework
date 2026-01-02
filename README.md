# Hybrid Automation Framework

A scalable and maintainable **Hybrid Selenium Automation Framework** built using **Java, Selenium WebDriver, TestNG, and Maven**.

The framework follows **Page Object Model (POM)** principles, supports **data-driven testing using JSON and Excel**, enables **cross-browser execution**, and uses **thread-safe WebDriver management** for parallel execution.

It includes **centralized configuration**, **reusable utility classes**, **structured logging with Log4j2**, and **rich Extent Reports**, making it suitable for **real-world enterprise automation projects** and **CI/CD pipelines**.

---

## 🚀 Features

- 🧬 Hybrid Selenium Automation Framework using Java  
- 🧱 Page Object Model (POM) for clean and maintainable test design  
- 🧪 TestNG-based execution with parallel run support  
- 📊 Data-driven testing using JSON and Excel  
- 🌐 Cross-browser execution support  
- ⚙️ Centralized configuration via `config.properties`  
- 📈 Extent Reports with logs and screenshots  
- 📦 Maven build & dependency management  
- 🤝 CI/CD ready (Jenkins / GitHub Actions)  
- 🐳 Dockerized Selenium Grid support  

---

## ⚙️ Tech Stack

| Tool / Library | Purpose |
|---------------|---------|
| Java | Programming language |
| Selenium WebDriver | UI automation |
| TestNG | Test execution & management |
| Maven | Build & dependency management |
| Extent Reports | Reporting |
| Log4j2 | Logging |
| Apache POI | Excel handling |
| Jackson / Gson | JSON handling |
| Docker | Distributed test execution |

---

## 🌐 Supported Browsers

- Google Chrome  
- Mozilla Firefox  
- Microsoft Edge  

> Browser selection is configurable via `config.properties` or TestNG XML files.

---

## 📂 Test Data Management

- **JSON** – Structured test data  
- **Excel** – Data-driven testing  
- Centralized data readers for easy maintenance and scalability  

---

## 🚀 How to Run Tests

### ▶️ Run via TestNG XML (Recommended)

1. Navigate to `src/test/resources/testng`
2. Right-click on any TestNG XML file (e.g. `master.xml`)
3. Select **Run As → TestNG Suite**

---

### ▶️ Run via Maven

```bash
mvn clean test
```

---

## 🔄 CI/CD Integration

This framework is designed to run seamlessly in **CI/CD pipelines**.

### Jenkins
- Uses pipeline-based execution via `Jenkinsfile`
- Executes tests using Maven
- Archives Extent Reports and logs as build artifacts

```bash
mvn clean test
```

### GitHub Actions
- Can be executed using a workflow under `.github/workflows`
- Supports headless, parallel, and cross-browser execution

---

## 🐳 Docker Execution (Selenium Grid)

### Prerequisites
- Docker
- Docker Compose

### ▶️ Start Selenium Grid

```bash
docker-compose up -d
```

Verify Grid:
```
http://localhost:4444
```

### ▶️ Execute Tests on Grid

Update `config.properties`:
```properties
execution=remote
gridUrl=http://localhost:4444/wd/hub
browser=chrome
```

Run:
```bash
mvn clean test
```

### ▶️ Stop Selenium Grid

```bash
docker-compose down
```

---

## 🧬 Project Structure

```text
hybrid-automation-framework/
│
├── README.md
├── pom.xml
├── docker-compose.yaml
├── Jenkinsfile
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.framework
│   │   │       ├── base          # BaseTest, BasePage
│   │   │       ├── components    # Reusable UI components
│   │   │       ├── driver        # WebDriver factory & manager
│   │   │       ├── model         # POJOs for test data
│   │   │       ├── pages         # Page Object classes
│   │   │       └── utils
│   │   │           ├── config    # Configuration readers
│   │   │           ├── data      # Data providers & readers
│   │   │           ├── helpers   # Common utilities
│   │   │           └── reports   # Extent report listener
│   │   │
│   │   └── resources
│   │       ├── config
│   │       │   └── config.properties
│   │       └── log4j2.xml
│   │
│   └── test
│       ├── java
│       │   └── com.framework.tests
│       │       ├── login
│       │       ├── registration
│       │       ├── search
│       │       ├── cart
│       │       ├── checkout
│       │       └── regression
│       │
│       └── resources
│           ├── testdata          # JSON / Excel test data
│           └── testng            # TestNG suite XMLs
│
├── reports                       # Extent report output
├── logs                          # Execution logs
├── scripts                       # Utility scripts
└── target                        # Maven build output
```

---

## 🧠 Design Philosophy

- Clear separation of **framework code** and **test logic**
- Feature-based test organization for scalability
- Reusable utilities and centralized configuration
- Designed for **parallel execution**, **CI/CD**, and **enterprise usage**

---

## ✅ Final Notes

- Follows industry best practices
- Suitable for real-world automation projects
- Easy to scale, maintain, and integrate into CI/CD pipelines
