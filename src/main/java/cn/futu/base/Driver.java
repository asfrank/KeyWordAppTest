package cn.futu.base;

import cn.futu.util.config.GlobalConfig;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Driver {
    private static Driver driver;
    private AppiumDriver<AndroidElement> appiumDriver;

    private Driver(){};

    private static Logger logger = LoggerFactory.getLogger(Driver.class);

    //单例模式
    public static Driver getInstance() {
        if (driver == null) {
            driver = new Driver();
        }
        return driver;
    }

    //初始化driver
    private void initDriver() {
        final DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        //从配置文件中读取capabilities
        final GlobalConfig config = GlobalConfig.load("/conf/globalConfig.yaml");
        //读出后通过setCapability方法，设置capabilities
        config.getAppiumConfig().getCapabilities().keySet().forEach(key->{
            Object value = config.getAppiumConfig().getCapabilities().get(key);
            desiredCapabilities.setCapability(key, value);
        });

        try {
            //创建driver对象
            appiumDriver = new AndroidDriver<>(new URL(config.getAppiumConfig().getUrl()), desiredCapabilities);
        } catch (MalformedURLException e) {
            logger.warn("Driver初始化错误" + e.getStackTrace());
        }
        appiumDriver.manage().timeouts().implicitlyWait(config.getAppiumConfig().getWait(), TimeUnit.SECONDS);
    }

    //初始化并返回driver对象
    public AppiumDriver<AndroidElement> startDriver(){
        initDriver();
        return appiumDriver;
    }

    public AppiumDriver<AndroidElement> getDriver(){
        return appiumDriver;
    }

    public void closeDriver(){
        appiumDriver.quit();
    }

    //截图，通过调用appium自带的api：getScreenshotAs
    public static void screenShot(AppiumDriver<AndroidElement> appiumDriver, String path){
        try {
            FileUtils.copyFile(appiumDriver.getScreenshotAs(OutputType.FILE),new File(path));
        } catch (IOException e) {
            logger.info("截图保存失败" + e.getStackTrace());
        }
    }
}
