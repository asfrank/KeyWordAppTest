package cn.futu.base;

import cn.futu.base.Driver;
import cn.futu.util.Action;
import cn.futu.util.Constants;
import cn.futu.util.DataProviderFromExcel;
import cn.futu.util.FindElement;
import cn.futu.util.config.GlobalConfig;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BaseTest {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected AppiumDriver<AndroidElement> driver;
    public static boolean testResult;
    //获取action方法
    protected static Method[] methods;
    //步骤描述
    protected static String testStepDetail;
    //对象识别关键字
    protected static String inspector;
    //数据
    protected static String data;
    //操作
    protected static String actionStep;
    //定义类
    protected static Action action;
    protected static MobileElement mobileElement = null;
    //截图保存路径
    private static String screenShotPath;

    //所有的用例均继承此类，通过beforeclass注解初始化driver
    @BeforeClass
    public void setUp() {
        logger.info("初始化，启动driver");
        driver = Driver.getInstance().startDriver();
    }

    @AfterClass
    public void tearDown() {
        Driver.getInstance().closeDriver();
    }

    public void base_Test(String filePath) throws Exception{
        action = Action.getInstance((AndroidDriver<AndroidElement>) driver);
        methods = action.getClass().getMethods();
        DataProviderFromExcel.getExcel(filePath);
        String fileSheet = Constants.TaskFile.Suite_sheet;
        //获取测试集合中测试用例的总数
        int testSuiteAllNum = DataProviderFromExcel.getAllRowNum(fileSheet);
        //循环测试调度文件
        for (int testSuiteNum = 1;testSuiteNum <= testSuiteAllNum;testSuiteNum++) {
            //获取测试用例名，直接关联待测试用例所在的sheet名
            String testCaseName = DataProviderFromExcel.getCellData(Constants.TaskFile.Suite_sheet,
                    testSuiteNum, Constants.TaskFile.Suite_testCaseName).trim();
            //判断用例是否执行
            String isRun = DataProviderFromExcel.getCellData(Constants.TaskFile.Suite_sheet,
                    testSuiteNum, Constants.TaskFile.Suite_isRun);
            //获取测试用例详细的描述，只用来输出日志
            String testCaseDetail = DataProviderFromExcel.getCellData(Constants.TaskFile.Suite_sheet,
                    testSuiteNum, Constants.TaskFile.Suite_testCaseDetail).trim();
            //获取断言方式
            String testCaseAssertMethod = DataProviderFromExcel.getCellData(Constants.TaskFile.Suite_sheet,
                    testSuiteNum, Constants.TaskFile.Suite_assertMethod).trim();
            //获取断言数据
            String testCaseAssertData = DataProviderFromExcel.getCellData(Constants.TaskFile.Suite_sheet,
                    testSuiteNum, Constants.TaskFile.Suite_assertData).trim();
            //如果isRun的值为yes，则执行指定sheet页的测试步骤，sheet名与testCaseName相同
            if (isRun.equals("yes")) {
                logger.info("运行测试用例----测试用例名称：" +testCaseName+ "；测试用例详细描述：" +testCaseDetail);
                //先将结果置为true
                testResult = true;
                int testCaseAllNum = DataProviderFromExcel.getAllRowNum(testCaseName);
                logger.info("测试步骤数：" + testCaseAllNum);
                for (int testCaseNum =1; testCaseNum <= testCaseAllNum;testCaseNum++) {
                    //获取识别方式
                    inspector = DataProviderFromExcel.getCellData(testCaseName, testCaseNum, Constants.CaseFile.Col_inspector).trim();
                    //获取操作数据
                    data = DataProviderFromExcel.getCellData(testCaseName, testCaseNum, Constants.CaseFile.Col_data).trim();
                    //获取操作方式
                    actionStep = DataProviderFromExcel.getCellData(testCaseName, testCaseNum, Constants.CaseFile.Col_actionStep).trim();
                    //获取用例步骤描述
                    testStepDetail = DataProviderFromExcel.getCellData(testCaseName, testCaseNum, Constants.CaseFile.Col_testStep_detail).trim();

                    logger.info("执行测试步骤["+ testCaseNum +"]---步骤描述："+testStepDetail+"；元素定位符："+inspector+"；操作："+actionStep+"；测试数据："+data);
                    //识别元素
                    if (!inspector.isEmpty()) {
                        try {
                            mobileElement = FindElement.findElement((AndroidDriver<?>) driver, inspector);
                        }catch (IllegalArgumentException e) {
                            logger.info("有元素未定位到，测试步骤执行结果为false");
                            DataProviderFromExcel.setCellData(testCaseNum, Constants.CaseFile.Col_result,
                                    false, testCaseName, Constants.ExcelPath.FilePath);
                            logger.info("测试用例执行结果为false");
                            //截图
                            Calendar cal = Calendar.getInstance();
                            Date date = cal.getTime();
                            screenShotPath = getDateTime(date);
                            Driver.screenShot(driver, screenShotPath);
                            logger.info("截图中.......");
                            DataProviderFromExcel.setCellData(testSuiteNum, Constants.TaskFile.Suite_result,
                                    false, fileSheet, filePath);
                            Assert.fail("测试步骤未全部通过，测试用例执行失败！");
                            break;
                        }
                    }

                    execute_Actions(testCaseNum, testCaseName);
                    if (!testResult) {
                        logger.info("测试用例执行结果为false");
                        //截图
                        Calendar cal = Calendar.getInstance();
                        Date date = cal.getTime();
                        screenShotPath = getDateTime(date);
                        Driver.screenShot(driver, screenShotPath);
                        logger.info("截图中........");
                        DataProviderFromExcel.setCellData(testSuiteNum, Constants.TaskFile.Suite_result,
                                false, fileSheet, filePath);
                        Assert.fail("测试步骤未全部通过，测试用例执行失败！");
                        break;
                    }
                }
                //进行断言
                if (testResult) {
                    try {
                        //目前支持两种断言方式，这是第一种，查找当前页面是否存在某个元素信息，在用例编写时填入断言方式：查找元素
                        if (testCaseAssertMethod.equals("查找元素")) {
                            logger.info("断言中，waiting.............");
                            FindElement.findElement((AndroidDriver<?>) driver, testCaseAssertData);
                            //这是第二种断言方式，查找当前页面中是否包含某段文案，在用例编写时填入断言方式：内容包含
                            // TODO: 2019/7/11 还可以自定义更多的断言方式
                        }else if (testCaseAssertMethod.equals("内容包含")) {
                            logger.info("断言中，waiting.............");
                            String assertElement = testCaseAssertData.split(">")[0];
                            String assertElementData = testCaseAssertData.split(">")[1];
                            MobileElement element = FindElement.findElement((AndroidDriver<?>) driver, assertElement);
                            if (!element.getText().contains(assertElementData)) {
                                throw new Exception("未找到要断言的内容！");
                            }
                        }
                        logger.info("断言成功，测试用例执行结果为true");
                        DataProviderFromExcel.setCellData(testSuiteNum, Constants.TaskFile.Suite_result,
                                true, fileSheet, filePath);
                        Assert.assertTrue(true, "测试用例执行成功");

                    }catch (IllegalArgumentException e) {
                        logger.info("断言失败，测试用例执行结果为false");
                        //截图
                        Calendar cal = Calendar.getInstance();
                        Date date = cal.getTime();
                        screenShotPath = getDateTime(date);
                        Driver.screenShot(driver, screenShotPath);
                        logger.info("截图中........");
                        DataProviderFromExcel.setCellData(testSuiteNum, Constants.TaskFile.Suite_result,
                                false, fileSheet, filePath);
                        Assert.fail("测试步骤通过，断言失败，测试用例执行失败！");
                    }
                }

            }
        }
    }

    //用例执行的方法
    private void execute_Actions(int testcaseNum, String testCaseName) throws Exception {
        try {
            for (Method method : methods) {
                if (method.getName().equals(actionStep)) {
                    //根据方法名，调用action中对应的方法
                    method.invoke(action, mobileElement, data);
                    if (testResult) {
                        logger.info("测试步骤执行结果为true");
                        DataProviderFromExcel.setCellData(testcaseNum, Constants.CaseFile.Col_result,
                                true, testCaseName, Constants.ExcelPath.FilePath);
                        break;
                    } else {
                        logger.info("测试步骤执行结果为false");
                        DataProviderFromExcel.setCellData(testcaseNum, Constants.CaseFile.Col_result,
                                false, testCaseName, Constants.ExcelPath.FilePath);
                        break;
                    }
                }
            }
        }catch (Exception e) {
            logger.info("执行步骤出现异常"+ e.getMessage());
            //执行步骤有异常，自动截图
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            screenShotPath = getDateTime(date);
            Driver.screenShot(driver, screenShotPath);
            logger.info("截图中........");
        }
    }

    //用于获取当前的时间戳，拼接生成截图的文件名
    private String getDateTime(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String screenShotPath = GlobalConfig.load("/conf/globalConfig.yaml").getAppiumConfig().getScreenShotPath() + "/"+ df.format(date) + ".png";
        return screenShotPath;
    }
}
