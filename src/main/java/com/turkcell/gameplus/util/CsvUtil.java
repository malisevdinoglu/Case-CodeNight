package com.turkcell.gameplus.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvUtil {

    public List<String[]> readCsv(String filePath) throws IOException, CsvException {
        List<String[]> records = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            records = reader.readAll();
            // Skip header row
            if (!records.isEmpty()) {
                records.remove(0);
            }
        }
        return records;
    }

    public void writeCsv(String filePath, List<String[]> data, String[] header) throws IOException {
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            if (header != null) {
                writer.writeNext(header);
            }
            writer.writeAll(data);
        }
    }

    public String[] readHeader(String filePath) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> records = reader.readAll();
            if (!records.isEmpty()) {
                return records.get(0);
            }
        }
        return new String[0];
    }
}

