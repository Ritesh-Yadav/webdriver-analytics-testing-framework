# Webdriver Analytics Testing Framework

### Tech Stack

- Selenuim Webdriver
- JUnit 5
- ExtentReports
- Gson
- Gradle

### Running Tests

#### Firefox:
```./gradlew -Denv=SIT firefoxTest```

#### Chrome:
```./gradlew -Denv=SIT chromeTest```

**Different Environment:** INT, SIT, PROD

### Reports

After execution reports will be available in __projectDir__/build/reports