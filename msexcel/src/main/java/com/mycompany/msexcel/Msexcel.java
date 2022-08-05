package com.mycompany.msexcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

public class Msexcel {
    
    HSSFWorkbook workbook = new HSSFWorkbook();
    HSSFSheet sheet = workbook.createSheet("sheet1");
    HSSFCellStyle cellStyle = workbook.createCellStyle();
    HSSFRow createRow = null;
    
    public void demo() throws FileNotFoundException {
        
        String[] text = {"ffbfhib", "gdsngj", "gdggd", "ggdggd", "fbf", "text", "vdvuddvuvd", "hvfdvfv", "fbfvfbf", "fbffbf"};
        for (int i = 0; i < text.length; i++) {
            System.out.println("createing the HSSFRow And HSSFCell ");
            createRow = sheet.createRow(i);
            for (int j = 0; j < text.length; j++) {
                cellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
                cellStyle.setFillPattern(FillPatternType.BIG_SPOTS);
                cellStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
                HSSFCell cell = createRow.createCell(j);
                cell.setCellValue(text[j]);
                cell.setCellStyle(cellStyle);
                
            }
            
        }
        File file = new File("/home/user/NetBeansProjects/msexcel/excelSheet/sheet1.xlsx");
        FileOutputStream fos = new FileOutputStream(file);
        try {
            workbook.write(fos);
            System.out.println(" SUccessfully created");
        } catch (IOException ex) {
            Logger.getLogger(Msexcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        Msexcel excel = new Msexcel();
        try {
            excel.demo();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Msexcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
