package io.github.riteshyadav.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.HashMap;
import java.util.Map;

public class HtmlReporter {

    public static Map<String, ExtentTest> REPORTER = new HashMap<>();
    private static ExtentReports extentReports;

    private HtmlReporter() {
    }

    public static ExtentReports getReporter(String reportLocation) {

        if (extentReports == null) {
            ExtentHtmlReporter extentHtmlReporter = new ExtentHtmlReporter(String.format("%s/index.html", reportLocation));
            extentHtmlReporter.config().setTheme(Theme.STANDARD);
            extentHtmlReporter.config().setDocumentTitle("Automated Test Report");
            extentHtmlReporter.config().setEncoding("utf-8");

            extentHtmlReporter.setAppendExisting(true);
            extentReports = new ExtentReports();
            extentReports.attachReporter(extentHtmlReporter);
        }

        return extentReports;
    }

    public static String assembleKey(ExtensionContext context) {
        if (context.getTestClass().isPresent() && context.getTestMethod().isPresent()) {
            return String.format("%s-%s", context.getTestClass().get().getName(), context.getTestMethod().get().getName());
        }
        return null;
    }

    public static String assembleKey(TestInfo testInfo) {
        if (testInfo.getTestClass().isPresent() && testInfo.getTestMethod().isPresent()) {
            return String.format("%s-%s", testInfo.getTestClass().get().getName(), testInfo.getTestMethod().get().getName());
        }
        return null;
    }
}
