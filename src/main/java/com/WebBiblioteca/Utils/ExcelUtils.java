package com.WebBiblioteca.Utils;

import com.WebBiblioteca.Model.Category;
import org.apache.poi.ss.usermodel.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
        }else if (val instanceof  Long){
            cell.setCellValue(((Long)val).intValue());
        }else if ( val instanceof LocalDate){
            cell.setCellValue((LocalDate) val);
            cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd-MM-yyyy"));
        } else if (val instanceof Category) {
            cell.setCellValue(((Category) val).name());
        }else if (val instanceof LocalTime){
            cell.setCellValue(((LocalTime) val).format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        }else if (val instanceof java.sql.Time){
            cell.setCellValue((java.sql.Time) val);
        }
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cell.setCellStyle(cellStyle);
    }
}
