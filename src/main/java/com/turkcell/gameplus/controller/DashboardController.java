package com.turkcell.gameplus.controller;

import com.turkcell.gameplus.service.DataExportService;
import com.turkcell.gameplus.service.DataInitializationService;
import com.turkcell.gameplus.service.GamePlusOrchestrator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {

    private final GamePlusOrchestrator orchestrator;
    private final DataInitializationService dataInitializationService;
    private final DataExportService dataExportService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "redirect:/dashboard.html";
    }

    @PostMapping("/import")
    public ResponseEntity<Map<String, String>> importData() {
        try {
            dataInitializationService.importFromCsv();
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "CSV data imported successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error importing data", e);
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/export")
    public ResponseEntity<Map<String, String>> exportData() {
        try {
            dataExportService.exportToCsv();
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Data exported to CSV successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error exporting data", e);
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/process")
    public ResponseEntity<Map<String, String>> processDate(@RequestParam String date) {
        try {
            LocalDate asOfDate = LocalDate.parse(date);
            orchestrator.processDate(asOfDate);
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Date processed successfully: " + date);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing date", e);
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}

