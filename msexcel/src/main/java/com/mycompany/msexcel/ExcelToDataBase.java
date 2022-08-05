package com.mycompany.msexcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelToDataBase {

    FileInputStream fis = null;
    FileOutputStream stream = null;

    public ExcelToDataBase(String file) throws Exception {
        this.fileName = file;
    }

    String fileName = null;

    public FileInputStream read() throws Exception {
        File file = new File(this.fileName);

        if (file.exists()) {
            fis = new FileInputStream(file);
            System.out.println(" file Input stream created ");
            return fis;
        } else {
            file.createNewFile();
            System.out.println("file created Auomatically");
        }
        return fis;
    }

    public FileOutputStream write() throws Exception {
        stream = new FileOutputStream(fileName);
        File file = new File(this.fileName);
        if (file.exists()) {
            return stream;
        } else {
            file.createNewFile();
            return stream;
        }
    }

    public void createCell(long rownumber, long col, String sheetName, String value) throws Exception {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);
        XSSFRow row = sheet.createRow((int) rownumber);
        XSSFCell cell = row.createCell((int) col);
        cell.setCellValue(value);
        FileOutputStream s = write();
        workbook.write(s);
        workbook.close();

    }

    public ResultSet GetConnection(String url, String username, String pass, String query) throws Exception {
        Connection con = DriverManager.getConnection(url, username, pass);
        System.out.println("Connection is SucessFully created ");
        Statement statement = con.createStatement();
        ResultSet set = statement.executeQuery("'" + query + "'");
        return set;
    }

    public void redColour(XSSFWorkbook workbook, int row, int cell, int sheetIndex) throws IOException {
        CellStyle color = workbook.createCellStyle();
        color.setFillForegroundColor(IndexedColors.RED.getIndex());
        color.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
        XSSFCell c = sheet.getRow(row).getCell(cell);
        try {
            FileOutputStream out = write();
            workbook.write(out);
        } catch (Exception ex) {
            Logger.getLogger(ExcelToDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        c.setCellStyle(color);
    }

    public void yellowColour(XSSFWorkbook workbook, int row, int cell, int sheetIndex) throws IOException {
        CellStyle color = workbook.createCellStyle();
        color.setFillForegroundColor(IndexedColors.YELLOW1.getIndex());
        color.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
        XSSFCell c = sheet.getRow(row).getCell(cell);
        try {
            FileOutputStream out = write();
            workbook.write(out);
        } catch (Exception ex) {
            Logger.getLogger(ExcelToDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        c.setCellStyle(color);
    }

    public void greenColour(XSSFWorkbook workbook, int row, int cell, int sheetIndex) throws IOException {
        CellStyle color = workbook.createCellStyle();
        color.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        color.setFillPattern(FillPatternType.BIG_SPOTS);
        XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
        XSSFCell c = sheet.getRow(row).getCell(cell);
        try {
            FileOutputStream out = write();
            workbook.write(out);
        } catch (Exception ex) {
            Logger.getLogger(ExcelToDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        c.setCellStyle(color);
    }

    public void whiteColour(XSSFWorkbook workbook, int row, int cell, int sheetIndex) throws IOException {
        CellStyle color = workbook.createCellStyle();
        color.setFillForegroundColor(IndexedColors.WHITE1.getIndex());
        color.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
        XSSFCell c = sheet.getRow(row).getCell(cell);
        try {
            FileOutputStream out = write();
            workbook.write(out);
        } catch (Exception ex) {
            Logger.getLogger(ExcelToDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        c.setCellStyle(color);
    }
}
