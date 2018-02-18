package io.github.riteshyadav.tests;

import de.sstoehr.harreader.model.HarEntry;
import de.sstoehr.harreader.model.HarQueryParam;
import io.github.riteshyadav.dao.DataProvider;
import io.github.riteshyadav.dao.PostDao;

import io.github.riteshyadav.pages.*;
import io.github.riteshyadav.utils.HarFileReader;
import io.github.riteshyadav.utils.ProxyServer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.Matchers.hasItem;

public class HomePageTest extends BaseTest {

    @Test
    @DisplayName("Check document title in google analytics.")
    public void checkPost() {

        HomePage homePage = new HomePage(driver);
        assertThat(homePage.getPageTitle(), equalTo(homePage.PAGE_TITLE));

        testReporter.info("Page title for home page matched.");

        PostDao latestPostDao = new DataProvider().postDetailsFor("mongodb");
        assertThat(homePage.getMostRecentPostTitle(), equalTo(latestPostDao.getTitle()));

        if (ProxyServer.enableProxy()) {

            HarFileReader harFileReader = new HarFileReader(saveHarFileIfRequired());
            List<HarEntry> harEntries = harFileReader.getHarEntries();

            List<HarEntry> harEntriesForCollect = harFileReader.getHarEntriesFor(harEntries, "https://www.google-analytics.com/r/collect");

            harEntriesForCollect.stream().forEach(harEntry -> {

                HarQueryParam queryParam = new HarQueryParam();
                queryParam.setName("dt");
                queryParam.setValue("Not Just A QA");

                assertThat("Matching document title in Google Analytics.", harEntry.getRequest().getQueryString(), hasItem(queryParam));
                testReporter.info("Document title in Google Analytics Matched to 'Not Just A QA'");

                harEntry.getRequest().getQueryString().stream()
                        .forEach( harQueryParam -> System.out.println(harQueryParam.getName() + "=" + harQueryParam.getValue()));
            });

            List<HarEntry> harEntriesForTags = harFileReader.getHarEntriesFor(harEntries,"https://www.googletagmanager.com/gtag/js");
            harEntriesForTags.stream().forEach(harEntry -> {
                System.out.println("----------------\n" + harEntry.getRequest().getUrl());
                harEntry.getRequest().getQueryString().stream().forEach(
                        harQueryParam -> System.out.println(harQueryParam.getName() + "=" + harQueryParam.getValue()));
            });

        }
    }
}
