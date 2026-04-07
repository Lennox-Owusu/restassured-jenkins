
# REST Assured API Testing with Jenkins CI/CD

A REST API test automation framework built with **REST Assured** and **JUnit 5**, 
integrated with **Jenkins** for continuous integration and delivery.

---

## 🛠️ Tech Stack

| Tool | Version | Purpose |
|------|---------|---------|
| Java | 17 | Programming language |
| Maven | 3.9+ | Build and dependency management |
| REST Assured | 5.3.0 | API test automation |
| JUnit 5 | 5.10.0 | Test framework |
| Allure | 2.24.0 | Test reporting |
| Jenkins | LTS | CI/CD pipeline |
| Docker | Latest | Jenkins containerization |

---

## 📁 Project Structure

```
restassured-jenkins/
├── src/
│   └── test/
│       ├── java/
│       │   └── com/amalitech/
│       │       ├── config/
│       │       │   └── TestConfig.java          # Base test configuration
│       │       ├── testdata/
│       │       │   └── ProductTestData.java     # Test data and constants
│       │       └── tests/
│       │           └── productsEndpoint/
│       │               └── ProductsApiTest.java # Products API tests
│       └── resources/
│           ├── schemas/
│           │   ├── product-schema.json          # Single product JSON schema
│           │   └── products-list-schema.json    # Products list JSON schema
│           └── test.properties                  # API configuration
├── Jenkinsfile                                  # Jenkins pipeline definition
├── pom.xml                                      # Maven dependencies
└── README.md
```

---

## 🌐 API Under Test

**FakeStore API** — `https://fakestoreapi.com`

A free, open REST API for e-commerce testing.

### Endpoints Tested

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/products` | Get all products |
| GET | `/products?limit={n}` | Get products with limit |
| GET | `/products?sort=desc` | Get products sorted descending |
| GET | `/products?sort=asc` | Get products sorted ascending |
| GET | `/products/{id}` | Get single product |
| GET | `/products/categories` | Get all categories |
| GET | `/products/category/{category}` | Get products by category |
| POST | `/products` | Create a product |
| PUT | `/products/{id}` | Update a product |
| PATCH | `/products/{id}` | Partially update a product |
| DELETE | `/products/{id}` | Delete a product |

---

## ✅ Test Coverage

### Products Endpoint (17 tests)
- Get all products with schema validation
- Get products with limit query param
- Get products sorted ascending and descending
- Get single product with schema validation
- Get non-existent product
- Get product with invalid ID
- Get all categories
- Get products by each category (electronics, jewelery, men's clothing, women's clothing)
- Create a new product
- Full update a product with schema validation
- Partial update a product
- Delete a product
- Delete non-existent product

---

## ⚙️ Configuration

### `src/test/resources/test.properties`
```properties
api.baseUri=https://fakestoreapi.com
```

### JSON Schema Validation
Schema files are located in `src/test/resources/schemas/`:
- `product-schema.json` — validates single product response structure
- `products-list-schema.json` — validates array of products response structure

---

## 🚀 Running Tests Locally

### Prerequisites
- Java 17+
- Maven 3.9+

### Run all tests
```bash
mvn clean test
```

### Run a specific test class
```bash
mvn test -Dtest=ProductsApiTest
```

### Run a specific test method
```bash
mvn test -Dtest=ProductsApiTest#testGetAllProducts
```

### Generate Allure report
```bash
mvn allure:report
mvn allure:serve
```

---

## 🔁 Jenkins CI/CD Pipeline

### Prerequisites
- Docker installed
- Jenkins running in Docker

### Start Jenkins
```bash
docker run -d \
  --name jenkins \
  --network jenkins \
  -p 9090:8080 \
  -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  jenkins/jenkins:lts
```

Access Jenkins at: `http://localhost:9090`

### Pipeline Stages

```
Checkout → Build → Run Tests → Publish Reports
```

| Stage | Description |
|-------|-------------|
| Checkout | Pulls latest code from GitHub |
| Build | Compiles project with `mvn clean compile` |
| Run Tests | Executes all tests with `mvn test` |
| Publish Reports | Records JUnit results and generates Allure report |

### Jenkins Tools Configuration

| Tool | Name in Jenkins | Version |
|------|----------------|---------|
| JDK | JDK17 | Java 17 |
| Maven | Maven | 3.9.14 |

---

## 📊 Test Reports

### JUnit Report
Available in Jenkins under **Test Results** after each build.

### Allure Report
Available in Jenkins under **Allure Report** after each build.

To view locally:
```bash
mvn allure:serve
```

---

## 🔔 Notifications

The pipeline sends Slack notifications to `#jenkins-messages` with:
- ✅ Build status (PASSED / FAILED / UNSTABLE)
- 🌿 Branch name
- ⏱️ Build duration
- 📊 Test results (passed / failed / skipped count)
- ❌ List of failed test names (up to 10)
- 🔗 Link to build

### Notification Setup
1. Create a Slack App at https://api.slack.com/apps
2. Add `chat:write` OAuth scope
3. Install to workspace and copy `xoxb-` token
4. Add token as **Secret text** credential in Jenkins with ID `slack-token`
5. Configure in `Manage Jenkins` → `System` → **Slack** section

---

## 🔗 GitHub Repository

```
https://github.com/Lennox-Owusu/restassured-jenkins
```

### GitHub Webhook (Auto-trigger builds)
1. Go to GitHub repo → **Settings** → **Webhooks** → **Add webhook**
2. Payload URL: `http://<your-jenkins-url>/github-webhook/`
3. Content type: `application/json`
4. Event: **Just the push event**

---

## 👤 Author

**Lennox Owusu Afriyie**  
QA Automation Engineer  
Amalitech
```

