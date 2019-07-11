package cn.futu.util.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;

/**
 *  使用jackson-dataformat-yaml库读取yaml文件
 *  globalConfig中分为两部分：appiumConfig和inspectConfig
 *  库GitHub地址：https://github.com/FasterXML/jackson-dataformats-text/tree/master/yaml
 *  上面有具体的用法
 */
public class GlobalConfig {
    private AppiumConfig appiumConfig;
    private InspectConfig inspectConfig;

    public static GlobalConfig load(String path){
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        GlobalConfig globalConfig = new GlobalConfig();
        try {
            globalConfig = mapper.readValue(GlobalConfig.class.getResource(path),GlobalConfig.class);
            return globalConfig;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public AppiumConfig getAppiumConfig() {
        return appiumConfig;
    }

    public void setAppiumConfig(AppiumConfig appiumConfig) {
        this.appiumConfig = appiumConfig;
    }

    public InspectConfig getInspectConfig() {
        return inspectConfig;
    }

    public void setInspectConfig(InspectConfig inspectConfig) {
        this.inspectConfig = inspectConfig;
    }
}
