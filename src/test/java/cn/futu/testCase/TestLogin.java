package cn.futu.testCase;

import cn.futu.base.BaseTest;
import cn.futu.util.Constants;
import org.testng.annotations.Test;

public class TestLogin extends BaseTest {

    @Test
    public void test_Login() throws Exception {
        //Excel文件路径
        String excelPath = Constants.ExcelPath.FilePath;
        base_Test(excelPath);
    }


}
