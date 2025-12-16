# Hybrid Automation Framework

A well-structured and maintainable **Hybrid Selenium Automation Framework** built using **Java, Selenium WebDriver, TestNG, and Maven**.  
The framework follows **Page Object Model (POM)** principles, supports **data-driven testing using JSON and Excel**, enables **cross-browser execution**, and uses **Thread-safe WebDriver management** for parallel runs.  

It includes **centralized configuration**, **reusable utility classes**, **structured logging with Log4j2**, and **rich Extent Reports**, making it suitable for real-world automation projects and **CI/CD integration**.


---

## 🚀 Features


- 🧬 **Hybrid Selenium Automation Framework** using Java  
- 🧱 **Page Object Model (POM)** for clean and maintainable test design  
- 🧪 **TestNG-Based Execution** with parallel run support  
- 📊 **Data-Driven Testing** using JSON and Excel  
- 🌐 **Cross-Browser Execution** support  
- ⚙️ **Centralized Configuration** via `config.properties`  
- 📈 **Extent Reports** with logs and screenshots  
- 📦 **Maven Build Management** for dependencies and execution  
- 🤝 **CI/CD Ready** (Jenkins / GitHub Actions)



## ⚙️ Tech Stack

| Tool / Library | Purpose |
|---------------|---------|
| Java | Programming language |
| Selenium WebDriver | UI automation |
| TestNG | Test execution & management |
| Maven | Build & dependency management |
| Extent Reports | Test reporting |
| Log4j2 | Logging |
| Apache POI | Excel handling |
| Jackson / Gson | JSON handling |

---

## 🌐 Supported Browsers

- Google Chrome
- Mozilla Firefox
- Microsoft Edge

(Browser selection is configurable via `config.properties` or TestNG XML)

---

## 📂 Test Data Management

- **JSON** – Used for structured test data
- **Excel** – Used for data-driven testing
- Centralized readers for easy maintenance

---

## 🚀 How to Run Tests

### ▶️ Run via TestNG XML (Recommended)
1. Right-click on any `testng.xml`
2. Select **Run As → TestNG Suite**

---

### ▶️ Run via Maven
```bash
mvn clean test

---

## 🧬 Project Structure
hybrid-automation-framework/
│
├── README.md
├── pom.xml
├── .gitignore
│
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ └── com/framework/
│ │ │ ├── base/ # BaseTest & BasePage
│ │ │ ├── components/ # Reusable UI components
│ │ │ ├── driver/ # WebDriver factory & manager
│ │ │ ├── model/ # POJOs for test data
│ │ │ ├── pages/ # Page Object classes
│ │ │ └── utils/
│ │ │ ├── data/ # Data providers & readers
│ │ │ ├── helpers/ # Common utilities
│ │ │ └── reports/ # Extent report listener
│ │ │
│ │ └── resources/
│ │ ├── config.properties
│ │ └── log4j2.xml
│ │
│ └── test/
│ ├── java/
│ │ └── com/framework/tests/
│ │
│ └── resources/
│ ├── testdata/
│ │ ├── *.json
│ │ └── *.xlsx
│ └── testng/
│ ├── testng.xml
│ ├── testng-crossbrowser.xml
│ └── testng-groups.xml
│
├── reports/ # Extent reports output
├── logs/ # Execution logs
└── target/ # Maven build output
