package com.app.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.service.CSVProcessor;

@RestController
@RequestMapping("/api/csv")
public class CSVController {

    @Autowired
    private CSVProcessor csvProcessor;

    @PostMapping("/process")
    public String processCsvFile(@RequestParam("file") MultipartFile file) throws Exception {
    	 String inputDir = "uploads/";
         String outputDir = "outputs/";

         Files.createDirectories(Paths.get(inputDir));
         Files.createDirectories(Paths.get(outputDir));

         File inputFile = new File(inputDir + "input.csv");
         File outputFile = new File(outputDir + "output.csv");

         if (!inputFile.exists()) {
             inputFile.createNewFile();
         }

         if (!outputFile.exists()) {
             outputFile.createNewFile();
         }

          csvProcessor.processCsv(inputFile, outputFile);
        
        return "CSV processed. Output file saved as: " + outputFile.getAbsolutePath();
    }
}