package com.dmtool.service;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    private final String uploadDirectory = "../pre_migration/file_process/";

    public void processAndSaveFile(MultipartFile file) throws IOException {
        // Ensure upload directory exists
        Files.createDirectories(Paths.get(uploadDirectory));

        // Save the uploaded file
        Path filePath = Paths.get(uploadDirectory + file.getOriginalFilename());
        file.transferTo(filePath);

        // Convert Excel to CSV
        convertExcelToCsv(filePath.toString());
    }

    private void convertExcelToCsv(String excelFilePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(excelFilePath);
             XSSFWorkbook workbook = new XSSFWorkbook(fis);
             FileWriter writer = new FileWriter(excelFilePath.replace(".xlsx", ".csv"))) {

            XSSFSheet sheet = workbook.getSheetAt(0);

            // Get row and column counts
            int rowCount = sheet.getLastRowNum();
            int columnCount = sheet.getRow(0).getLastCellNum();

            // Loop through each row and column to build CSV
            for (int i = 0; i <= rowCount; i++) {
                StringBuilder rowData = new StringBuilder();
                for (int j = 0; j < columnCount; j++) {
                    if (sheet.getRow(i).getCell(j) != null) {
                        rowData.append(sheet.getRow(i).getCell(j).toString());
                    }
                    if (j < columnCount - 1) {
                        rowData.append("|"); // You can change this delimiter if needed
                    }
                }
                rowData.append("\n");
                writer.write(rowData.toString());
            }
        }
    }
}