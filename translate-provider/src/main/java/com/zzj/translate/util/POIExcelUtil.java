package com.zzj.translate.util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class POIExcelUtil {
    public static Workbook createWorkbook() {
        return new XSSFWorkbook();
    }

    public static void setCellValue(Sheet sheet, int rowIndex, int colIndex, String value) {
        rowIndex = rowIndex - 1;
        colIndex = colIndex - 1;
        Row row = sheet.getRow(rowIndex);
        if (row == null) row = sheet.createRow(rowIndex);
        row.createCell(colIndex).setCellValue(value);
    }
}