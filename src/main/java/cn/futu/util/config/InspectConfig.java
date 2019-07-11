package cn.futu.util.config;

/**
 * 元素查找的相关配置，对应src/main/resources/conf/globalConfig.yaml文件的inspectConfig
 */
public class InspectConfig {
    //元素尝试重复定位的次数
    private int elementInspectCount;
    //每两次尝试之间的时间间隔
    private int elementInspectInterval;
    //等待activity的尝试次数
    private int activityInspectCount;
    //每两次尝试之间的时间间隔
    private int activityInspectInterval;

    public int getElementInspectCount() {
        return elementInspectCount;
    }

    public void setElementInspectCount(int elementInspectCount) {
        this.elementInspectCount = elementInspectCount;
    }

    public int getElementInspectInterval() {
        return elementInspectInterval;
    }

    public void setElementInspectInterval(int elementInspectInterval) {
        this.elementInspectInterval = elementInspectInterval;
    }

    public int getActivityInspectCount() {
        return activityInspectCount;
    }

    public void setActivityInspectCount(int activityInspectCount) {
        this.activityInspectCount = activityInspectCount;
    }

    public int getActivityInspectInterval() {
        return activityInspectInterval;
    }

    public void setActivityInspectInterval(int activityInspectInterval) {
        this.activityInspectInterval = activityInspectInterval;
    }
}
