package io.github.riteshyadav.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends HeaderSectionOnPage{

    public static final String PAGE_TITLE = "Not Just A QA";

    @FindBy(css = "h2.archive__item-title>a:nth-child(1)")
    private WebElement MOST_RECENT_POST;


    public HomePage(WebDriver driver) {

        super(driver);
        initElements(driver, this);
    }

    @Override
    public String getPageTitle() {

        return driver.getTitle();
    }

    public void openMostRecentPost(){

        MOST_RECENT_POST.click();

    }

    public String getMostRecentPostTitle(){

        return MOST_RECENT_POST.getText();
    }

}
