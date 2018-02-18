package io.github.riteshyadav.logics;


import io.github.riteshyadav.pages.HeaderSectionOnPage;
import io.github.riteshyadav.pages.PostsByTagPage;
import org.openqa.selenium.WebDriver;

public class PageNavigation {

    public PostsByTagPage openPostsByTagPage(WebDriver driver){

        HeaderSectionOnPage headerSectionOnPage = new HeaderSectionOnPage(driver);
        return headerSectionOnPage.openPostsByTagPage();
    }

}
