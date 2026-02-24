package com.org.iopts.util;

import com.org.iopts.exception.CustomException;
import com.org.iopts.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Excel Utility Component
 *
 * Provides Excel file generation using Apache POI.
 * Generates .xlsx (XSSF) format files in memory and returns byte arrays.
 */
@Slf4j
@Component
public class ExcelUtil {

    /**
     * Create an Excel file (.xlsx) from headers and data rows.
     *
     * @param sheetName name of the worksheet
     * @param headers   column header labels
     * @param rows      data rows; each inner list represents one row of cell values
     * @return byte array of the generated Excel file
     */
    public byte[] createExcel(String sheetName, List<String> headers, List<List<Object>> rows) {
        log.debug("createExcel - sheetName: {}, headers: {}, rows: {}", sheetName, headers.size(), rows.size());

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet(sheetName);

            // Create header style
            CellStyle headerStyle = createHeaderStyle(workbook);

            // Create date style
            CellStyle dateStyle = workbook.createCellStyle();
            CreationHelper creationHelper = workbook.getCreationHelper();
            dateStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));

            // Write header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(headerStyle);
            }

            // Write data rows
            for (int rowIdx = 0; rowIdx < rows.size(); rowIdx++) {
                Row dataRow = sheet.createRow(rowIdx + 1);
                List<Object> rowData = rows.get(rowIdx);

                for (int colIdx = 0; colIdx < rowData.size(); colIdx++) {
                    Cell cell = dataRow.createCell(colIdx);
                    setCellValue(cell, rowData.get(colIdx), dateStyle);
                }
            }

            // Auto-size columns
            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
                // Ensure minimum width
                int currentWidth = sheet.getColumnWidth(i);
                if (currentWidth < 3000) {
                    sheet.setColumnWidth(i, 3000);
                }
            }

            workbook.write(outputStream);
            byte[] bytes = outputStream.toByteArray();
            log.info("createExcel success - sheetName: {}, size: {} bytes", sheetName, bytes.length);
            return bytes;

        } catch (IOException e) {
            log.error("Failed to create Excel file: {}", sheetName, e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to create Excel file: " + e.getMessage());
        }
    }

    /**
     * Create a header cell style (bold, background color, borders).
     */
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        // Font
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);

        // Background
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Borders
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        // Alignment
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    /**
     * Set a cell's value based on the object type.
     */
    private void setCellValue(Cell cell, Object value, CellStyle dateStyle) {
        if (value == null) {
            cell.setBlank();
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
            cell.setCellStyle(dateStyle);
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue(value.toString());
        } else if (value instanceof LocalDate) {
            cell.setCellValue(value.toString());
        } else {
            cell.setCellValue(value.toString());
        }
    }
}
