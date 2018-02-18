# Webdriver Analytics Testing Framework

### Tech Stack

- Selenuim Webdriver
- JUnit 5
- ExtentReports
- Gson
- Gradle

### Running Tests

_**Note:** If you run test from commandline it will automatically download drivers for you in __projectDir__/webdriver folder._

#### Firefox:

- Commandline ```./gradlew -Denv=SIT firefoxTest```
- If you want to run test from an IDE, you have to set environment variable manually ```-Denv=PROD -Dwebdriver.gecko.driver=./webdriver/PATH/TO/geckodriver -Dbrowser=firefox```

#### Chrome:
- ```./gradlew -Denv=SIT chromeTest```
- If you want to run test from an IDE, you have to set environment variable manually ```-Denv=PROD -Dwebdriver.chrome.driver=./webdriver/PATH/TO/chromedriver -Dbrowser=chrome```

**Different Environment:** SIT, STAGING, PROD

### Reports

After execution reports will be available in __projectDir__/build/reports