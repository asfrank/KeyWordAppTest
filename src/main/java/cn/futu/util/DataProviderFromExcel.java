package cn.futu.util;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class DataProviderFromExcel {
    private static File file = null;
    private static XSSFWorkbook workbook = null;
    private static XSSFSheet sheet = null;
    private static XSSFRow row = null;
    private static XSSFCell cell = null;

    /**
     *  设定待操作的文件路径，初始化workbook
     */
    public static void getExcel(String filePath) throws Exception {
        file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        workbook = new XSSFWorkbook(fis);
        if (workbook == null) {
            throw new Exception("Excel工作簿为空！");
        }
    }

    /**
     *  读取指定单元格的值
     */
    public static String getCellData(String sheetName, int rowNum, int colNum) throws Exception {
        sheet = workbook.getSheet(sheetName);
        cell = sheet.getRow(rowNum).getCell(colNum);
        String cellData = getCellValue(cell);
        return cellData;
    }

    /**
     *  根据Excel中格式的不同来读取不同格式的值
     */
    private static String getCellValue(XSSFCell cell) {
        String strCell = "";
        try {
            if (cell.getCellTypeEnum() == CellType.STRING) {
                strCell = cell.getStringCellValue();
            }else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                strCell = String.valueOf(cell.getNumericCellValue());
                strCell = strCell.split(".")[0];
            }else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
                strCell = String.valueOf(cell.getBooleanCellValue());
            }else if (cell.getCellTypeEnum() == CellType.BLANK) {
                strCell = "";
            }else {
                strCell = "";
            }
        }catch (Exception e) {
            strCell = "空";
        }
        return strCell;
    }

    /**
     *  向指定的单元格写入数据
     */
    public static void setCellData(int rowNum, int colNum, boolean result, String sheetName, String filePath) throws Exception {
        sheet = workbook.getSheet(sheetName);
        try {
            row = sheet.getRow(rowNum);
            cell = row.getCell(colNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(result);
            FileOutputStream fos = new FileOutputStream(filePath);
            workbook.write(fos);
            fos.close();
        } catch (Exception e) {
            throw new Exception("数据写入失败！");
        }
    }

    /**
     *  获取指定sheet页的单元格行数
     */
    public static int getAllRowNum(String sheetName) {
        sheet = workbook.getSheet(sheetName);
        return sheet.getLastRowNum();
    }
}
