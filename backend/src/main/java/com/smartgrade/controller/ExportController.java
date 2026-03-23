package com.smartgrade.controller;

import com.smartgrade.service.ExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/export")
public class ExportController {

    private final ExportService exportService;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping("/report")
    @PreAuthorize("hasAnyRole('CC', 'HOD')")
    public ResponseEntity<byte[]> exportReport(
            @RequestParam(defaultValue = "pdf") String format,
            @RequestParam(required = false) String department) {
        try {
            byte[] data;
            String contentType;
            String filename;

            if ("docx".equalsIgnoreCase(format)) {
                data = exportService.generateDocxReport(department);
                contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                filename = "marks_report.docx";
            } else {
                data = exportService.generatePdfReport(department);
                contentType = "application/pdf";
                filename = "marks_report.pdf";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(data);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
