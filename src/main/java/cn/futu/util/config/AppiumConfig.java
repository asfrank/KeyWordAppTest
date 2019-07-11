package cn.futu.util.config;

import java.util.Map;

/**
 * appium的相关配置，对应src/main/resources/conf/globalConfig.yaml文件的appiumConfig
 */
public class AppiumConfig {
    private String app;
    private String url;
    private Integer wait;
    private Map<String,Object> capabilities;
    private String screenShotPath;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getWait() {
        return wait;
    }

    public void setWait(Integer wait) {
        this.wait = wait;
    }

    public Map<String, Object> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Map<String, Object> capabilities) {
        this.capabilities = capabilities;
    }

    public String getScreenShotPath() {
        return screenShotPath;
    }

    public void setScreenShotPath(String screenShotPath) {
        this.screenShotPath = screenShotPath;
    }
}
