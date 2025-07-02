package eu.nets.test.core.exceptions;

import eu.nets.test.core.enums.MpaLanguage;
import org.openqa.selenium.WebElement;

import static eu.nets.test.util.AllureUtil.logError;

public class LokaliseMismatchException extends RuntimeException {
    public LokaliseMismatchException(WebElement element, String lokaliseKey, String lokaliseText, MpaLanguage language) {
        super(logError(
                "Mismatch detected between actual UI text and expected lokalise text." +
                        "\nTarget language: " + language +
                        "\nUI Element Resource ID: " + element.getAttribute("resource-id") +
                        "\nUI Element Text: " + element.getText() +
                        "\nLokalise Key: " + lokaliseKey +
                        "\nLokalise Text: " + lokaliseText
        ));
    }
}
