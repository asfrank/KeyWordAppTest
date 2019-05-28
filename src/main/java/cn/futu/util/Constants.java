package cn.futu.util;

public class Constants {
    /**
     *  定义Excel文件路径
     */
    public class ExcelPath {
        public static final String FilePath = "data/demo.xlsx";
    }
    /**
     *  测试调度文件列常量
     */
    public class TaskFile {
        public static final String Suite_sheet = "task";
        public static final int Suite_testCaseName = 0;
        public static final int Suite_testCaseDetail = 1;
        public static final int Suite_isRun = 2;
        public static final int Suite_result = 3;
    }

    /**
     *  测试用例文件列常量
     */
    public class CaseFile {
        public static final int Col_testCaseID = 0;
        public static final int Col_testStepID = 1;
        public static final int Col_inspector = 4;
        public static final int Col_actionStep = 5;
        public static final int Col_data = 6;
        public static final int Col_result = 7;
    }
}
