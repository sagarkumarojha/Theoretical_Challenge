package com.app.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class CSVProcessor {

    public void processCsv(File inputFile, File outputFile) throws Exception {
        try (
            FileReader fileReader = new FileReader(inputFile);
            CSVReader csvReader = new CSVReader(fileReader);
            FileWriter fileWriter = new FileWriter(outputFile);
            CSVWriter csvWriter = new CSVWriter(fileWriter)
        ) {
            String[] nextLine;
          //  Map<String, Double> cellValues = new HashMap<>();
            
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sheet1");

            int rowNum = 0;
            while ((nextLine = csvReader.readNext()) != null) {
                Row row = sheet.createRow(rowNum);
                String[] outputRow = new String[nextLine.length];

                for (int colNum = 0; colNum < nextLine.length; colNum++) {
                    Cell cell = row.createCell(colNum);
                    String cellValue = nextLine[colNum];
                    
                    if (isFormula(cellValue)) {
                        // Set cell formula and evaluate it
                        cell.setCellFormula(cellValue.substring(1)); // Remove '=' before setting the formula
                        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                        CellValue evaluatedValue = evaluator.evaluate(cell);
                        outputRow[colNum] = String.valueOf(evaluatedValue.getNumberValue());
                    } else {
                        // Direct number value
                        double number = Double.parseDouble(cellValue);
                        cell.setCellValue(number);
                        outputRow[colNum] = cellValue;
                    }
                }
                csvWriter.writeNext(outputRow);
                rowNum++;
            }
            workbook.close();
        }
    }

    // Check if it's a formula (starts with '=')
    private boolean isFormula(String value) {
        return value.startsWith("=");
    }
}

