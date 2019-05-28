package cn.futu.util;

import cn.futu.base.BaseTest;
import cn.futu.util.config.GlobalConfig;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FindElement {
    static Logger logger = LoggerFactory.getLogger(FindElement.class);
    static GlobalConfig config = GlobalConfig.load("/conf/globalConfig.yaml");

    public static AndroidElement findElementByType(AndroidDriver<?> driver, String locator) {
        MobileElement element = null;
        if (locator.startsWith("//")) {
            element = (MobileElement) driver.findElementByXPath(locator);
        }else if (locator.contains("id")) {
            element = (MobileElement) driver.findElementById(locator.split(":")[1]);
        }else {
            try {
                element = (MobileElement) driver.findElementByXPath("//*[@text='" + locator + "']");
            }catch (Exception e) {
                element = (MobileElement) driver.findElementByClassName(locator);
            }
        }
        return (AndroidElement) element;
    }

    public static MobileElement findElement(AndroidDriver<?> driver, String locator) throws Exception {
        int elementInspectCount = config.getInspectConfig().getElementInspectCount();
        int elementInspectInterval = config.getInspectConfig().getElementInspectInterval();
        MobileElement element = null;
        for (int i = 0;i<elementInspectCount;i++) {
            Thread.sleep(elementInspectInterval);
            try {
                element = findElementByType(driver, locator);
                logger.info("已经找到元素对象");
                return element;
            }catch (Exception e) {
                logger.info("控件未出现，waiting.........");
                try {
                    logger.info("尝试定位广告弹窗,waiting.........");
                    driver.findElementById("close_popup_ad_view").click();
                }catch (NoSuchElementException e1) {
                    logger.info("没有发现广告弹窗，retrying..........");
                }
            }
        }
        logger.info("元素定位信息："+locator+"，超出最大定位尝试次数");
        BaseTest.testResult = false;
        throw new IllegalArgumentException("在指定时间内未找到元素对象");
    }
}
