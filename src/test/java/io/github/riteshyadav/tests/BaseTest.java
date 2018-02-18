package io.github.riteshyadav.tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.Markup;
import io.github.riteshyadav.dao.DataProvider;
import io.github.riteshyadav.driver.BrowserClient;
import io.github.riteshyadav.utils.Extension;
import io.github.riteshyadav.utils.HtmlReporter;
import io.github.riteshyadav.utils.ImageMarkup;
import io.github.riteshyadav.utils.ProxyServer;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.core.har.Har;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@ExtendWith(Extension.class)
public class BaseTest {

    public static final String REPORT_FOLDER_NAME = new SimpleDateFormat("ddMMMyy_HHmmss").format(new Date());
    public WebDriver driver;
    public ExtentTest testReporter;
    public static final String REPORT_FOLDER_LOCATION = String.format("build/reports/%s", REPORT_FOLDER_NAME);
    public BrowserClient browserClient;
    public DataProvider dataProvider;

    private ExtentReports extentReports;
    private String keyForReporter;

    @BeforeEach
    private void init(TestInfo testInfo) {

        extentReports = HtmlReporter.getReporter(REPORT_FOLDER_LOCATION);
        testReporter = extentReports.createTest(testInfo.getDisplayName());

        keyForReporter = HtmlReporter.assembleKey(testInfo);
        HtmlReporter.REPORTER.put(keyForReporter, testReporter);

        browserClient = new BrowserClient();
        if (ProxyServer.enableProxy()) {
            BrowserMobProxy proxy = browserClient.getProxy();
            proxy.newHar("ritesh-yadav.github.io");
        }

        browserClient.openWebsite();
        driver = browserClient.getDriver();
        dataProvider = new DataProvider();


    }

    @AfterEach
    private void cleanup() {

        String screenShotName = String.format("%s_%s.png", keyForReporter,
                new SimpleDateFormat("ddmmyy_HHmmss").format(new Date()));
        String fileLocation = String.format("%s/%s", REPORT_FOLDER_LOCATION, screenShotName);
        browserClient.saveScreenShot(fileLocation);

        Markup mu = new ImageMarkup(screenShotName, "End of Execution.");
        testReporter.info(mu);

        saveHarFileIfRequired();
        if (ProxyServer.enableProxy()) {
            browserClient.getProxy().stop();
        }

        if (driver != null) {
            browserClient.quitBrowser();
        }

        extentReports.flush();
        HtmlReporter.REPORTER.remove(keyForReporter);

    }

    public File saveHarFileIfRequired() {

        if (ProxyServer.enableProxy()) {
            Har har = browserClient.getProxy().getHar();

            ExtentTest testReporter = HtmlReporter.REPORTER.get(keyForReporter);

            new File(REPORT_FOLDER_LOCATION).mkdirs();

            String fileName = String.format("%s/%s_%s.har", REPORT_FOLDER_LOCATION, keyForReporter,
                    new SimpleDateFormat("ddmmyy_HHmmss").format(new Date()));

            // Write HAR Data in a File
            File harFile = new File(fileName);

            try {
                if (har != null) {
                    har.writeTo(harFile);
                    testReporter.info("Har file saved in : " + fileName);
                }
            } catch (IOException ex) {
                testReporter.info(ex.toString());
                testReporter.info("Could not find file " + fileName);
            }

            return harFile;
        }

        return null;
    }
}
