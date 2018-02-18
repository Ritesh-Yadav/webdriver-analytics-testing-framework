package io.github.riteshyadav.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PostsByTagPage extends HeaderSectionOnPage{

    public static final String PAGE_TITLE = "Posts by Tag - Not Just A QA";

    @FindBy(css = "h2.archive__item-title>a:nth-child(1)")
    private WebElement MOST_RECENT_POST;


    public PostsByTagPage(WebDriver driver) {

        super(driver);
        initElements(driver, this);
    }

    @Override
    public String getPageTitle() {

        return driver.getTitle();
    }
}
