package cn.futu.base;

import cn.futu.base.Driver;
import cn.futu.util.Action;
import cn.futu.util.Constants;
import cn.futu.util.DataProviderFromExcel;
import cn.futu.util.FindElement;
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

public class BaseTest {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected AppiumDriver<AndroidElement> driver;
    public static boolean testResult;
    //获取action方法
    protected static Method[] method;

    //对象识别关键字
    public static String inspector;
    //数据
    public static String data;
    //操作
    public static String actionStep;
    //定义类
    public static Action action;
    public static MobileElement mobileElement = null;

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
        method = action.getClass().getMethods();
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
            //如果isRun的值为yes，则执行指定sheet页的测试步骤，sheet名与testCaseName相同
            if (isRun.equals("yes")) {
                logger.info("运行测试用例：测试用例名称" +testCaseName+ ";测试用例详细描述：" +testCaseDetail);
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
                    //识别元素
                    if (!inspector.isEmpty()) {
                        mobileElement = FindElement.findElement((AndroidDriver<?>) driver, inspector);
                    }
                    logger.info("执行测试步骤：识别方式："+inspector+"；操作："+actionStep+"；测试数据："+data);
                    execute_Actions(testCaseNum, testCaseName);
                    if (!testResult) {
                        logger.info("测试用例执行结果为false");
                        DataProviderFromExcel.setCellData(testSuiteNum, Constants.TaskFile.Suite_result,
                                false, fileSheet, filePath);
                        Assert.fail("测试步骤未全部通过，测试用例执行失败！");
                        break;
                    }
                    logger.info("测试用例执行结果为true");
                    DataProviderFromExcel.setCellData(testSuiteNum, Constants.TaskFile.Suite_result,
                            true, fileSheet, filePath);
                    Assert.assertTrue(true, "测试用例执行成功");
                }
            }
        }
    }

    private void execute_Actions(int testcaseNum, String testCaseName) throws Exception {
        try {
            for (int i = 0;i<method.length;i++) {
                if (method[i].getName().equals(actionStep)) {
                    method[i].invoke(action, mobileElement, data);
                    if (testResult) {
                        logger.info("测试步骤执行结果为true");
                        DataProviderFromExcel.setCellData(testcaseNum, Constants.CaseFile.Col_result,
                                true, testCaseName, Constants.ExcelPath.FilePath);
                        break;
                    }else {
                        logger.info("测试步骤执行结果为false");
                        DataProviderFromExcel.setCellData(testcaseNum, Constants.CaseFile.Col_result,
                                false, testCaseName, Constants.ExcelPath.FilePath);
                    }
                }
            }
        }catch (Exception e) {
            logger.info("执行步骤出现异常"+ e.getMessage());
        }
    }
}
