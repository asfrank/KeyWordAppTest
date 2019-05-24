package cn.futu.util;

import cn.futu.util.config.GlobalConfig;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.android.nativekey.AndroidKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Action {
    static Logger logger = LoggerFactory.getLogger(Action.class);
    static GlobalConfig config = GlobalConfig.load("/conf/globalConfig.yaml");
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
                //todo:测试结果置为false
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
            //todo:false
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
            //todo:false
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
                //todo:false
            }
        }catch (Exception e) {
            //todo:false
        }
    }
    /**
     * 等待Activity跳转
     */
    public void waitForLoadingActivity(MobileElement mobileElement, String data) {

    }
}
