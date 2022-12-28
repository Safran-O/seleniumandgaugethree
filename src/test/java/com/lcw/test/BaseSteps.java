package com.lcw.test;

import com.thoughtworks.gauge.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Instant;
import java.util.List;
public class BaseSteps extends BaseTest {
    public BaseSteps() {
        initMap(getFileList());
    }

    WebElement findElement(String key) {
        By infoParam = getElementInfoToBy(findElementInfoByKey(key));
        WebDriverWait webDriverWait = new WebDriverWait(driver, 10);
        WebElement webElement = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(infoParam));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
        return webElement;
    }

    public By getElementInfoToBy(ElementInfo elementInfo) {
        By by = null;
        if (elementInfo.getType().equals("css")) {
            by = By.cssSelector(elementInfo.getValue());
        } else if (elementInfo.getType().equals(("name"))) {
            by = By.name(elementInfo.getValue());
        } else if (elementInfo.getType().equals("id")) {
            by = By.id(elementInfo.getValue());
        } else if (elementInfo.getType().equals("xpath")) {
            by = By.xpath(elementInfo.getValue());
        } else if (elementInfo.getType().equals("linkText")) {
            by = By.linkText(elementInfo.getValue());
        } else if (elementInfo.getType().equals(("partialLinkText"))) {
            by = By.partialLinkText(elementInfo.getValue());
        }
        return by;
    }
    @Step({"Write <text> values to key <key>",
            "<key> alanına <text> text değeri yazdır"})
    public void sendKeys(String text, String key){
        findElement(key).sendKeys(text);
        logger.info(key + " elementine " + text + " texti yazildi.");
    }

    @Step({"Click <key>", "<key> alanına tıkla"})
    public void click(String key) {
        findElement(key).click();
    }

    @Step("<Key> Saniye Bekle")
    public void waitElement(int Key) throws InterruptedException{
        //Gauge.captureScreenshot();
        //Gauge.writeMessage("Saniye Bekleniyor");
        Thread.sleep(Key*2500);
    }

    @Step({"Click to element <key>",
            "Elementine tıkla <key>"})
    public void clickElement(String key) {
            clickElement(findElement(key));
            logger.info(key + " elementine tiklandi.");
    }

    private void clickElement(WebElement element) {
        element.click();
    }

}
