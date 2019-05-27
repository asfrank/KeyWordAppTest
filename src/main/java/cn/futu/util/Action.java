package cn.futu.util;

import cn.futu.base.BaseTest;
import cn.futu.util.config.GlobalConfig;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.android.nativekey.AndroidKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Action {
    private static Logger logger = LoggerFactory.getLogger(Action.class);
    private static GlobalConfig config = GlobalConfig.load("/conf/globalConfig.yaml");
    private AndroidDriver<AndroidElement> driver;
    private static Action action;
    private Action(){};
    private Action(AndroidDriver<AndroidElement> driver) {
        this.driver = driver;
    }

    public static Action getInstance(AndroidDriver<AndroidElement> driver) {
        if (action == null) {
            action = new Action(driver);
        }
        return action;
    }
    /**
     *  单击操作
     */
    public void click(MobileElement mobileElement, String data) {
        try {
            mobileElement.click();
        }catch (Exception e) {
            try {
                Thread.sleep(config.getInspectConfig().getElementInspectInterval());
            } catch (InterruptedException e1) {
                BaseTest.testResult = false;
                e1.printStackTrace();
            }
            mobileElement.click();
        }
    }

    /**
     *  radio选择框
     */
    public void click_radio(MobileElement mobileElement, String data) {
        try {
            if (data.toLowerCase().equals("yes")) {
                if (!mobileElement.isSelected()) {
                    mobileElement.click();
                }
            }
        }catch (Exception e) {
            BaseTest.testResult = false;
            e.printStackTrace();
        }
    }

    /**
     * 输入框输入
     */
    public void input(MobileElement mobileElement, String data) {
        try {
            click(mobileElement, data);
            mobileElement.clear();
            mobileElement.sendKeys(data);
        }catch (Exception e) {
            BaseTest.testResult = false;
            e.printStackTrace();
        }
    }

    /**
     * 后退操作
     */
    public void back(MobileElement mobileElement, String data) {
        driver.pressKeyCode(AndroidKeyCode.BACK);
    }

    /**
     * 验证操作
     */
    public void verify(MobileElement mobileElement, String data) {
        String actualResult = "";
        try {
            actualResult = mobileElement.getAttribute("text");
            if (!actualResult.equals(data)) {
                BaseTest.testResult = false;
            }
        }catch (Exception e) {
            BaseTest.testResult = false;
        }
    }
    /**
     * 等待Activity跳转
     */
    public void waitForLoadingActivity(MobileElement mobileElement, String data) throws InterruptedException {
        Thread.sleep(3000);
        logger.info(driver.currentActivity());
        int activityInspectCount = config.getInspectConfig().getActivityInspectCount();
        int activityInspectInterval = config.getInspectConfig().getActivityInspectInterval();
        int i = 0;
        Thread.sleep(activityInspectInterval);
        while (i < activityInspectCount) {
            try {
                if (data.contains(driver.currentActivity())) {
                    logger.info(data + "出现！");
                    break;
                }else {
                    logger.info(data + "未出现，waiting......");
                    Thread.sleep(activityInspectInterval);
                    i++;
                }
            } catch (Exception e) {
                i++;
                logger.info("尝试" + activityInspectCount + "次，" + data + ",未出现");
                BaseTest.testResult = false;
            }
        }

    }
}
