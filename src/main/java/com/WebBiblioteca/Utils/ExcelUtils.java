package com.WebBiblioteca.Utils;

import org.apache.poi.ss.usermodel.*;

public class ExcelUtils {

    public static void createCell(Workbook wb, Row row,int column,Object val){
        CreationHelper creationHelper = wb.getCreationHelper();
        Cell cell = row.createCell(column);
        CellStyle cellStyle = wb.createCellStyle();
        if(val instanceof String){
            cell.setCellValue((String) val);
        }else if(val instanceof Integer){
            cell.setCellValue((Integer) val);
        } else if (val instanceof Double){
            cell.setCellValue((Double) val);
        } else if (val instanceof java.sql.Date) {
            cell.setCellValue((java.sql.Date) val);
            cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd/MM/yyyy"));
        }
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cell.setCellStyle(cellStyle);
    }
}
