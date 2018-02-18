package io.github.riteshyadav.utils;

import com.aventstack.extentreports.markuputils.Markup;


public class ImageMarkup implements Markup {

    private String markUpString;

    public ImageMarkup(String filename) {

        markUpString = imageMarkupString(filename);
    }

    public ImageMarkup(String filename, String title) {

        markUpString = String.format("<div>%s</div>%s", title, imageMarkupString(filename));
    }

    private String imageMarkupString(String filename) {

        return String.format("<img data-featherlight='%s' src='%s' data-src='%s' width='25%%'>",
                filename, filename, filename);
    }

    @Override
    public String getMarkup() {

        return markUpString;
    }
}

