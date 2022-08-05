package com.mycompany.msexcel;

import java.io.File;
import java.io.FileOutputStream;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class createingFormula {

    public static void main(String[] args) throws Exception {
        File file = new File("/home/user/NetBeansProjects/msexcel/excelSheet/sheet.xlsx");
        FileOutputStream fos = new FileOutputStream(file);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        sheet.createRow(1001).createCell(7).setCellFormula("SUM(H2:H1001)");

        workbook.write(fos);
        System.out.println("Successfully");
    }
}
