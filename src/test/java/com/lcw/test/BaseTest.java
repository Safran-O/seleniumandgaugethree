package com.lcw.test;

import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseTest {
    public static WebDriver driver;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private static final String DEFAULT_DIRECTORY_PATH = "elementValues";
    public static ConcurrentMap<String, Object> elementMapList = new ConcurrentHashMap<>();

    @BeforeScenario
    public void setUp(){
        System.setProperty("webdriver.chrome.driver","src/test/resources/chromedriver.exe");
        //2. kısım resources altında sağ click copy path deyip alıyoruz 3.su
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-notifications");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("disable-infobars");
        chromeOptions.addArguments("disable-plugins");
        chromeOptions.addArguments("disable-popup-blocking");
        chromeOptions.addArguments("ignore-certificate-errors");
        chromeOptions.addArguments("disable-translate");
        chromeOptions.addArguments("disable-extensions");

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        chromeOptions.merge(desiredCapabilities);
        //WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver(chromeOptions);
        driver.manage().window().maximize();

        driver.get("https://www.lcwaikiki.com/tr-TR/TR");
    }
    @AfterScenario
    public void tearDown(){
        if (driver != null){
            //quit driverı kapatır, close ise sayfayı kapar.
            driver.close();
            driver.quit();
        }
    }

    public void initMap(File[] fileList) {
        Type elementType = new TypeToken<List<ElementInfo>>() {}.getType();
        Gson gson = new Gson();
        List<ElementInfo> elementInfoList = null;
        for (File file : fileList) {
            try {
                elementInfoList = gson.fromJson(new FileReader(file), elementType);
                elementInfoList.parallelStream().forEach(elementInfo -> elementMapList.put(elementInfo.getKey(), elementInfo));
            } catch (FileNotFoundException e) {
                logger.warn("{} not found", e);
            }
        }
    }

    public File[] getFileList() {
        File[] fileList = new File(
                this.getClass().getClassLoader().getResource(DEFAULT_DIRECTORY_PATH).getFile())
                .listFiles(pathname -> !pathname.isDirectory() && pathname.getName().endsWith(".json"));
        if (fileList == null) {
            logger.warn(
                    "File Directory Is Not Found! Please Check Directory Location. Default Directory Path = {}",
                    DEFAULT_DIRECTORY_PATH);
            throw new NullPointerException();
        }
        return fileList;
    }

    public ElementInfo findElementInfoByKey(String key) {
        return (ElementInfo) elementMapList.get(key);
    }

    public void saveValue(String key, String value) {
        elementMapList.put(key, value);
    }

    public String getValue(String key) {
        return elementMapList.get(key).toString();
    }

}