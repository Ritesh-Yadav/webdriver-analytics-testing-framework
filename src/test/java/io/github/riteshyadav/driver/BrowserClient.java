package io.github.riteshyadav.driver;

import com.google.gson.JsonObject;
import io.github.riteshyadav.utils.PropertiesReader;
import io.github.riteshyadav.utils.ProxyServer;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.client.ClientUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class BrowserClient {

    private WebDriver driver;
    private Properties configProps;

    public static final long DEFAULT_TIMEOUT_IN_SEC = 60;
    private BrowserMobProxy proxy;

    public BrowserClient() {

        if (ProxyServer.enableProxy()) {
            proxy = new ProxyServer().startProxyServer();
        }

        String environment = System.getProperty("env");

        configProps = new PropertiesReader().getProperties(environment);

        switch (System.getProperty("browser").toLowerCase()) {
            case "firefox":
                driver = getFirefoxDriver();
                break;
            case "chrome":
                driver = getChromeDriver();
                break;
        }

        driver.manage().timeouts().implicitlyWait(DEFAULT_TIMEOUT_IN_SEC, TimeUnit.SECONDS);
    }

    public WebDriver getDriver() {

        return driver;
    }

    public BrowserMobProxy getProxy() {

        return proxy;
    }

    private WebDriver getFirefoxDriver() {

        FirefoxOptions firefoxOptions = getFirefoxOptions();

        if (StringUtils.isNotBlank(System.getProperty("webdriver.gecko.driver"))) {
            driver = new FirefoxDriver(firefoxOptions);
            driver.manage().window().maximize();
        } else {
            System.out.println("Please specify webdriver.gecko.driver system property.");
        }
        return driver;
    }

    private WebDriver getSafariDriver() {

        SafariOptions safariOptions = new SafariOptions();
        safariOptions.setUseTechnologyPreview(true);
        driver = new SafariDriver(safariOptions);
        driver.manage().window().maximize();
        return driver;
    }

    private WebDriver getIERemoteDriver() {

        DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
        ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
                true);
        InternetExplorerOptions ieOptions = new InternetExplorerOptions();
        ieOptions.takeFullPageScreenshot();
        //ieOptions.setCapability("ignoreProtectedModeSettings", true);
        ieOptions.destructivelyEnsureCleanSession();
        ieOptions.merge(ieCapabilities);

        try {
            driver = new RemoteWebDriver(new URL("http://10.125.86.46:4444/wd/hub"), ieOptions);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return driver;
    }

    private FirefoxOptions getFirefoxOptions() {

        FirefoxOptions firefoxOptions = new FirefoxOptions();

        if (ProxyServer.enableProxy()) {
            int port = proxy.getPort();

            // configure it as a desired capability
            DesiredCapabilities capabilities = new DesiredCapabilities();
            JsonObject json = new JsonObject();
            json.addProperty("proxyType", "manual");
            json.addProperty("httpProxy", String.format("%s:%d","localhost", port));
            json.addProperty("ftpProxy", String.format("%s:%d","localhost", port));
            json.addProperty("sslProxy", String.format("%s:%d","localhost", port));
            capabilities.setCapability(CapabilityType.PROXY, json);
            capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            capabilities.setAcceptInsecureCerts(true);

            firefoxOptions.merge(capabilities);
        }
        return firefoxOptions;
    }


    private WebDriver getChromeDriver() {

        ChromeOptions chromeOptions = getChromeOptions();

        if (StringUtils.isNotBlank(System.getProperty("webdriver.chrome.driver"))) {
            driver = new ChromeDriver(chromeOptions);
            driver.manage().window().fullscreen();
        } else {
            System.out.println("Please specify webdriver.chrome.driver system property.");
        }
        return driver;
    }

    private ChromeOptions getChromeOptions() {

        DesiredCapabilities capabilities = new DesiredCapabilities();

        if (ProxyServer.enableProxy()) {
            capabilities.setCapability(CapabilityType.PROXY, ClientUtil.createSeleniumProxy(proxy));
            capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            capabilities.setAcceptInsecureCerts(true);
        }

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.merge(capabilities);

        return chromeOptions;
    }

    public void openWebsite() {

        driver.get(configProps.getProperty("WebsiteUrl"));
    }

    public void quitBrowser() {

        driver.quit();
    }

    public void saveScreenShot(String filename) {

        String browser = System.getProperty("browser");

        Screenshot ss;
        if ("firefox".equalsIgnoreCase(browser)) {
            ss = new AShot()
                    .takeScreenshot(driver);
        } else {
            ss = new AShot()
                    //.shootingStrategy(ShootingStrategies.viewportPasting(1200))
                    .takeScreenshot(driver);
        }
        File file = new File(filename);
        try {
            FileUtils.touch(file);
            ImageIO.write(ss.getImage(), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
