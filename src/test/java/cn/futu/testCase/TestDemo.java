package cn.futu.testCase;

import cn.futu.base.BaseTest;
import cn.futu.util.Constants;
import org.testng.annotations.Test;

public class TestDemo extends BaseTest {

    @Test
    public void test_Demo() throws Exception {
        //Excel文件路径
        String excelPath = Constants.ExcelPath.FilePath;
        base_Test(excelPath);
    }
}
