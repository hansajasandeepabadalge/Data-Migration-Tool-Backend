package com.dmtool;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileWriter;

public class reading {
    public static void main(String[] args) throws Exception{
        // Reading Workbook
        FileWriter writer = new FileWriter("output.csv");

        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("D:\\Tools\\DM Tool\\DM Tool RD\\Sample Files\\1_CDGM.xlsx"));

        // Read Sheet
        XSSFSheet sheet = workbook.getSheetAt(0);

        // Get total rows and columns
        int rowCount = sheet.getLastRowNum();
        int columnCount = sheet.getRow(0).getLastCellNum();

        // Loop over rows and columns
        for (int i = 0; i <= rowCount; i++) {
            StringBuilder rowData = new StringBuilder();
            for (int j = 0; j < columnCount; j++) {
                if (sheet.getRow(i).getCell(j) != null) {
                    rowData.append(sheet.getRow(i).getCell(j));
                }
                if (j < columnCount - 1) {
                    rowData.append("|");
                }
            }
            rowData.append("\n");
            writer.write(rowData.toString());
        }

        writer.close();
        workbook.close();
    }
}
