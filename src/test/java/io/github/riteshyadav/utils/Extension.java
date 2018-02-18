package io.github.riteshyadav.utils;

import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Optional;

public class Extension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {

        Optional ex = context.getExecutionException();
        if (ex == Optional.empty()) {
            String key = HtmlReporter.assembleKey(context);
            HtmlReporter.REPORTER.get(key).pass("Test Completed Successfully...");
        } else {
            if (context.getExecutionException().isPresent()) {
                Throwable exception = context.getExecutionException().get();
                String stackTrace = ExceptionUtils.getStackTrace(exception);
                Markup mu = MarkupHelper.createCodeBlock(stackTrace);
                HtmlReporter.REPORTER.get(HtmlReporter.assembleKey(context)).fail(mu);
            } else {
                HtmlReporter.REPORTER.get(HtmlReporter.assembleKey(context)).skip("Test skipped for unknown reason.");
            }
        }
    }

}
