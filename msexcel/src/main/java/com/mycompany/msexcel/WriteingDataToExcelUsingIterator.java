/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.msexcel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Set;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author user
 */
public class WriteingDataToExcelUsingIterator {

    public static void main(String[] args) throws Exception {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sheet1");

        HashMap<String, Object[]> map = new HashMap<String, Object[]>();
        map.put("1", new Object[]{"EMP ID", "EMP NAME", "DESIGNATION"});
        map.put("2", new Object[]{"tp01", "Gopal", "Technical Manager"});
        map.put("3", new Object[]{"tp02", "Manisha", "Proof Reader"});
        map.put("4", new Object[]{"tp03", "Masthan", "Technical Writer"});
        map.put("5", new Object[]{"tp04", "Satish", "Technical Writer"});
        map.put("6", new Object[]{"tp05", "Krishna", "Technical Writer"});
        Set< String> keyid = map.keySet();
        int rowid = 0;
        XSSFRow row;
        for (String key : keyid) {
            row = sheet.createRow(rowid++);
            Object[] objectArr = map.get(key);
            int cellid = 0;

            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellid++);
                cell.setCellValue((String) obj);
            }
            FileOutputStream out = new FileOutputStream(new File("Writesheet.xlsx"));
            workbook.write(out);
            out.close();
            System.out.println("Writesheet.xlsx written successfully");
        }
    }
}
