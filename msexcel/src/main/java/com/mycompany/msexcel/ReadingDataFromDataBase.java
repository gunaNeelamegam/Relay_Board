package com.mycompany.msexcel;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadingDataFromDataBase {

    public static void main(String[] args) throws Exception {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Data", "root", "");
        Statement smt = con.createStatement();
        ResultSet rs = smt.executeQuery("select * from Data");
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Database");
        int r = 1;
        while (rs.next()) {
            System.out.println(rs.getString("S.No") + " " + rs.getString("First Name") + " " + rs.getString("Last Name" + " " + rs.getDate("Date")));
            String SerialNo = rs.getString("S.No");
            String firstName = rs.getString("First Name");
            String LastName = rs.getString("Last Name");
            Date date = rs.getDate("Date");

            sheet.createRow(0).createCell(0).setCellValue(SerialNo);
            sheet.createRow(0).createCell(1).setCellValue(firstName);
            sheet.createRow(0).createCell(2).setCellValue(LastName);
            sheet.createRow(0).createCell(3).setCellValue(date);

            XSSFRow row = sheet.createRow(r++);

            row.createCell(r).setCellValue(SerialNo);
            row.createCell(r).setCellValue(firstName);
            row.createCell(r).setCellValue(LastName);
            row.createCell(r).setCellValue(date);
        }
        File file = new File("/home/user/NetBeansProjects/msexcel/excelSheet/sheet.xlsx");
        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);
        System.out.println("Writed Successfully");
    }

}
