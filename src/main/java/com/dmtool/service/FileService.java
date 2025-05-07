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
    private static final String INPUT_FILE_PATH = "../pre_convert/";
    private static final String OUTPUT_FILE_PATH = "../pre_migration/file_process/";
    
    public void uploadToInputFile(MultipartFile file) throws IOException {
        Path inputpath = Paths.get(INPUT_FILE_PATH);
        Files.createDirectories(inputpath);
        Path filePath = inputpath.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        System.out.println("File uploaded successfully: " + filePath);
        inputFileToOutput(file);
    }

    public void inputFileToOutput(MultipartFile file) throws IOException {
        Path outputPath = Paths.get(OUTPUT_FILE_PATH);
        Files.createDirectories(outputPath);

        // Count existing files to determine the next number
        long fileCount = Files.list(outputPath)
                .filter(path -> path.toString().endsWith(".csv"))
                .count() + 1; // Add 1 to start from 1 or continue from last number

        String outputFileName = String.format("2_%d_%s", fileCount, 
                              file.getOriginalFilename().replace(".xlsx", ".csv"));
        Path csvOutputPath = outputPath.resolve(outputFileName);

        Path inputFilePath = Paths.get(INPUT_FILE_PATH).resolve(file.getOriginalFilename());

        try (FileInputStream fis = new FileInputStream(inputFilePath.toFile());
             XSSFWorkbook workbook = new XSSFWorkbook(fis);
             FileWriter writer = new FileWriter(csvOutputPath.toFile())) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getLastRowNum();
            int columnCount = sheet.getRow(0).getLastCellNum();

            for (int i = 0; i <= rowCount; i++) {
                StringBuilder rowData = new StringBuilder();
                for (int j = 0; j < columnCount; j++) {
                    if (sheet.getRow(i) != null && sheet.getRow(i).getCell(j) != null) {
                        rowData.append(sheet.getRow(i).getCell(j).toString());
                    }
                    if (j < columnCount - 1) {
                        rowData.append("|");
                    }
                }
                rowData.append("\n");
                writer.write(rowData.toString());
            }
            System.out.println("File converted successfully: " + csvOutputPath);
        }
    }
}