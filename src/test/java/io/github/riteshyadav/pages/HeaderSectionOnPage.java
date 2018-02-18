package io.github.riteshyadav.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HeaderSectionOnPage extends BasePage {

    @FindBy(xpath = "//li[@class='masthead__menu-item']/a[text()='Posts By Tag']")
    private WebElement POSTS_BY_TAG_LINK;

    @FindBy(xpath = "//li[@class='masthead__menu-item']/a[text()='Posts By Category']")
    private WebElement POSTS_BY_CATEGORY_LINK;

    @Override
    public String getPageTitle() {
        return null;
    }

    public HeaderSectionOnPage(WebDriver driver) {

        super(driver);
        initElements(driver, this);
    }

    public PostsByTagPage openPostsByTagPage(){

        POSTS_BY_TAG_LINK.click();
        return new PostsByTagPage(driver);
    }

}
