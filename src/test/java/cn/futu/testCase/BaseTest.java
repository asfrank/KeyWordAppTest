package cn.futu.testCase;

import cn.futu.base.Driver;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseTest {
    Logger logger = LoggerFactory.getLogger(getClass());
    protected AppiumDriver<AndroidElement> driver;

    @BeforeClass
    public void setUp() {
        logger.info("初始化，启动driver");
        driver = Driver.getInstance().getDriver();
    }

    @AfterClass
    public void tearDown() {
        driver.close();
    }
}
