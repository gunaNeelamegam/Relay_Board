package com.mycompany.msexcel;

import java.io.FileInputStream;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MainClass {

    public static void main(String[] args) throws Exception {

        ExcelToDataBase ex = new ExcelToDataBase("/home/user/NetBeansProjects/msexcel/Writesheet.xlsx");
        FileInputStream fis = ex.read();
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        System.out.println("createing the workbook instance ");
        XSSFSheet sheet = workbook.getSheet("sheet1");
        System.out.println("creating the shhet instance ");
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            XSSFRow row = (XSSFRow) rowIterator.next();
            Iterator<Cell> cellIterator = row.iterator();
            while (cellIterator.hasNext()) {
                XSSFCell cell = (XSSFCell) cellIterator.next();
                switch (cell.getCellType()) {

                    case NUMERIC:
                        System.out.println(cell.getNumericCellValue());
                        break;
                    case BOOLEAN:
                        System.out.println(cell.getBooleanCellValue());
                        break;
                    case STRING:
                        System.out.println(cell.getStringCellValue());
                        ex.greenColour(workbook, cell.getRowIndex(), 0, 0);
                        ex.redColour(workbook, cell.getRowIndex(), 1, 0);
                        ex.yellowColour(workbook, cell.getRowIndex(), 2, 0);
                        break;
                }
            }
        }
        workbook.close();
        System.out.println("successfully close"
                + "");

    }

}
