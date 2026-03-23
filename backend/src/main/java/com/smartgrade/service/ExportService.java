package com.smartgrade.service;

import com.smartgrade.dto.AnalyticsResponse;
import com.smartgrade.dto.StudentRanking;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ExportService {

    private final AnalyticsService analyticsService;

    public ExportService(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    public byte[] generatePdfReport(String department) throws IOException {
        AnalyticsResponse analytics = analyticsService.getAnalytics(department);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        // Title
        doc.add(new Paragraph("INTERNAL MARKS REPORT")
                .setFontSize(20)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(new DeviceRgb(30, 58, 138)));

        doc.add(new Paragraph("Department: " + (department != null ? department : "All"))
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20));

        // Summary
        doc.add(new Paragraph("Summary")
                .setFontSize(16).setBold().setMarginTop(10));
        doc.add(new Paragraph(String.format(
                "Total Students: %d | Pass: %d | Fail: %d | Pass %%: %.1f%%",
                analytics.getTotalStudents(), analytics.getTotalPass(),
                analytics.getTotalFail(), analytics.getOverallPassPercentage())));

        // Rankings table
        doc.add(new Paragraph("Student Rankings")
                .setFontSize(16).setBold().setMarginTop(20));

        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 2, 3, 2, 2, 1}))
                .useAllAvailableWidth();
        table.addHeaderCell(new Cell().add(new Paragraph("Rank").setBold())
                .setBackgroundColor(new DeviceRgb(30, 58, 138))
                .setFontColor(ColorConstants.WHITE));
        table.addHeaderCell(new Cell().add(new Paragraph("Reg No").setBold())
                .setBackgroundColor(new DeviceRgb(30, 58, 138))
                .setFontColor(ColorConstants.WHITE));
        table.addHeaderCell(new Cell().add(new Paragraph("Name").setBold())
                .setBackgroundColor(new DeviceRgb(30, 58, 138))
                .setFontColor(ColorConstants.WHITE));
        table.addHeaderCell(new Cell().add(new Paragraph("Total").setBold())
                .setBackgroundColor(new DeviceRgb(30, 58, 138))
                .setFontColor(ColorConstants.WHITE));
        table.addHeaderCell(new Cell().add(new Paragraph("Percentage").setBold())
                .setBackgroundColor(new DeviceRgb(30, 58, 138))
                .setFontColor(ColorConstants.WHITE));
        table.addHeaderCell(new Cell().add(new Paragraph("Status").setBold())
                .setBackgroundColor(new DeviceRgb(30, 58, 138))
                .setFontColor(ColorConstants.WHITE));

        for (StudentRanking r : analytics.getRankings()) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(r.getRank()))));
            table.addCell(new Cell().add(new Paragraph(r.getRegNo())));
            table.addCell(new Cell().add(new Paragraph(r.getName())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(r.getTotalMarks()))));
            table.addCell(new Cell().add(new Paragraph(String.format("%.1f%%", r.getOverallPercentage()))));

            String status = r.getOverallPercentage() >= 50 ? "PASS" : "FAIL";
            Cell statusCell = new Cell().add(new Paragraph(status).setBold());
            if ("PASS".equals(status)) {
                statusCell.setFontColor(new DeviceRgb(22, 163, 74));
            } else {
                statusCell.setFontColor(new DeviceRgb(220, 38, 38));
            }
            table.addCell(statusCell);
        }
        doc.add(table);

        // Subject analytics
        doc.add(new Paragraph("Subject-wise Analysis")
                .setFontSize(16).setBold().setMarginTop(20));

        Table subTable = new Table(UnitValue.createPercentArray(new float[]{3, 2, 2, 2, 1, 1, 2}))
                .useAllAvailableWidth();
        String[] subHeaders = {"Subject", "Avg CT1", "Avg CT2", "Avg Total", "Pass", "Fail", "Pass %"};
        for (String h : subHeaders) {
            subTable.addHeaderCell(new Cell().add(new Paragraph(h).setBold())
                    .setBackgroundColor(new DeviceRgb(30, 58, 138))
                    .setFontColor(ColorConstants.WHITE));
        }

        analytics.getSubjectAnalytics().values().forEach(sa -> {
            subTable.addCell(sa.getSubjectName());
            subTable.addCell(String.format("%.1f", sa.getAverageCt1()));
            subTable.addCell(String.format("%.1f", sa.getAverageCt2()));
            subTable.addCell(String.format("%.1f", sa.getAverageTotal()));
            subTable.addCell(String.valueOf(sa.getPassCount()));
            subTable.addCell(String.valueOf(sa.getFailCount()));
            subTable.addCell(String.format("%.1f%%", sa.getPassPercentage()));
        });
        doc.add(subTable);

        doc.close();
        return baos.toByteArray();
    }

    public byte[] generateDocxReport(String department) throws IOException {
        AnalyticsResponse analytics = analyticsService.getAnalytics(department);

        try (XWPFDocument document = new XWPFDocument();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // Title
            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setText("INTERNAL MARKS REPORT");
            titleRun.setBold(true);
            titleRun.setFontSize(20);
            titleRun.setColor("1E3A8A");

            // Department
            XWPFParagraph deptPara = document.createParagraph();
            deptPara.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun deptRun = deptPara.createRun();
            deptRun.setText("Department: " + (department != null ? department : "All"));
            deptRun.setFontSize(12);

            // Summary
            XWPFParagraph summary = document.createParagraph();
            XWPFRun sumRun = summary.createRun();
            sumRun.setText(String.format(
                    "Total Students: %d | Pass: %d | Fail: %d | Pass %%: %.1f%%",
                    analytics.getTotalStudents(), analytics.getTotalPass(),
                    analytics.getTotalFail(), analytics.getOverallPassPercentage()));
            sumRun.addBreak();

            // Rankings table
            XWPFParagraph rankTitle = document.createParagraph();
            XWPFRun rankTitleRun = rankTitle.createRun();
            rankTitleRun.setText("Student Rankings");
            rankTitleRun.setBold(true);
            rankTitleRun.setFontSize(14);

            XWPFTable table = document.createTable(analytics.getRankings().size() + 1, 6);
            table.setWidth("100%");

            // Header
            String[] headers = {"Rank", "Reg No", "Name", "Total", "Percentage", "Status"};
            for (int i = 0; i < headers.length; i++) {
                XWPFRun run = table.getRow(0).getCell(i).getParagraphs().get(0).createRun();
                run.setText(headers[i]);
                run.setBold(true);
            }

            // Data rows
            for (int i = 0; i < analytics.getRankings().size(); i++) {
                StudentRanking r = analytics.getRankings().get(i);
                XWPFTableRow row = table.getRow(i + 1);
                row.getCell(0).setText(String.valueOf(r.getRank()));
                row.getCell(1).setText(r.getRegNo());
                row.getCell(2).setText(r.getName());
                row.getCell(3).setText(String.valueOf(r.getTotalMarks()));
                row.getCell(4).setText(String.format("%.1f%%", r.getOverallPercentage()));
                row.getCell(5).setText(r.getOverallPercentage() >= 50 ? "PASS" : "FAIL");
            }

            document.write(baos);
            return baos.toByteArray();
        }
    }
}
