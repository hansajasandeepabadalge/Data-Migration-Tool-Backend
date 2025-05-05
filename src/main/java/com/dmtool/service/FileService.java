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
    public void uploadToInputFile(MultipartFile file) throws IOException {
        String INPUT_FILE_PATH = "../pre_convert/";
        Path inputpath = Paths.get(INPUT_FILE_PATH);
        Files.createDirectories(inputpath);
        Path filePath = inputpath.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        System.out.println("File uploaded successfully: " + filePath);
    }

    public void inputFileToOutput(MultipartFile file) throws IOException {
        int count = 0;

        String INPUT_FILE_PATH = "../pre_convert/";
        String OUTPUT_FILE_PATH = "../pre_migration/file_process/";

        Path outputPath = Paths.get(OUTPUT_FILE_PATH);
        Files.createDirectories(outputPath);

        Path inputFilePath = Paths.get(INPUT_FILE_PATH).resolve(file.getOriginalFilename());

        String outputFileName = "2_" + file.getOriginalFilename().replace(".xlsx", ".csv");
        Path csvOutputPath = outputPath.resolve(outputFileName);

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
        }

        System.out.println("File converted successfully: " + csvOutputPath);
    }

    public void convert(MultipartFile file) throws IOException {
        uploadToInputFile(file);
        inputFileToOutput(file);
    }


}