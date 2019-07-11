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

    //元素的定位方式，目前支持的有xpath、id和文案
    //以//开头的自动识别为xpath，定位信息中含有id字样，则识别为id，否则按照文案处理
    // TODO: 2019/7/11 这是用的最多的三种定位方式，还可以自定义更多的方式
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

    //元素定位的流程
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
                    // TODO: 2019/7/11 这里是容错处理，尝试定位app非预期出现的各种弹框，可根据实际情况，加入更多弹窗处理
                    logger.info("尝试定位广告弹窗，waiting.........");
                    driver.findElementById("close_popup_ad_view").click();
                }catch (NoSuchElementException e1) {
                    logger.info("没有发现广告弹窗，retrying..........");
                    try {
                        logger.info("尝试定位同步弹窗，waiting.........");
                        driver.findElementByXPath("//*[@text='不同步']").click();
//                    driver.findElementById("button2").click();
                    } catch (NoSuchElementException e2) {
                        logger.info("没有发现同步弹窗，retrying..........");
                        try {
                            logger.info("尝试定位教程弹窗，waiting..........");
                            for (int a = 0;a<5;a++) {
                                driver.findElementById("nextBtn").click();
                            }
                        }catch (NoSuchElementException e3) {
                            logger.info("没有发现教程弹窗，retrying..........");
                        }
                    }
                }
            }
        }
        logger.info("元素定位信息："+locator+"，超出最大定位尝试次数");
        BaseTest.testResult = false;
        throw new IllegalArgumentException("在指定时间内未找到元素对象");
    }
}
