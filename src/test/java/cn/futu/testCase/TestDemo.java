package cn.futu.testCase;

import cn.futu.base.BaseTest;
import cn.futu.util.Constants;
import org.testng.annotations.Test;

public class TestDemo extends BaseTest {
    private static final String excelPath = Constants.ExcelPath.FilePath;

    @Test
    public void test_Demo() throws Exception {
        //Excel文件路径
        base_Test(excelPath);
    }
}
