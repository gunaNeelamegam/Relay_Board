package com.mycompany.msexcel;

import java.io.FileInputStream;
import java.util.HashMap;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadData {

    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream("/home/user/NetBeansProjects/msexcel/excelSheet/sheet.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheet("Sheet1");

        CellStyle cellstyle = workbook.createCellStyle();
//        HashMap<Integer, ModelClass> map = new HashMap<>();

        int lastRow = sheet.getLastRowNum();

        for (int i = 0; i < lastRow; i++) {
            XSSFRow row = sheet.getRow(i);
            int cellnum = sheet.getRow(i).getLastCellNum();

            for (int j = 0; j < cellnum; j++) {
                XSSFCell cell = row.getCell(j);
                cellstyle.setFillBackgroundColor(IndexedColors.DARK_YELLOW.getIndex());
                cellstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                row.getCell(1).setCellStyle(cellstyle);
                switch (cell.getCellType()) {
                    case BOOLEAN:
                        System.out.print(cell.getBooleanCellValue() + " ");
                        break;
                    case NUMERIC:
                        System.out.print(cell.getNumericCellValue() + " ");
                        cellstyle.setFillBackgroundColor(IndexedColors.DARK_YELLOW.getIndex());
                        cellstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        cell.setCellStyle(cellstyle);
                        break;
                    case FORMULA:
                        System.out.print(cell.getCellFormula() + " ");
                        break;
                    case STRING:
                        System.out.print(cell.getStringCellValue() + " ");
                        cellstyle.setFillBackgroundColor(IndexedColors.INDIGO.getIndex());
                        cellstyle.setFillPattern(FillPatternType.LESS_DOTS);
                        cell.setCellStyle(cellstyle);
                        break;
                }
                System.out.print("  |   ");
            }
            System.out.println();
        }
    }
}
