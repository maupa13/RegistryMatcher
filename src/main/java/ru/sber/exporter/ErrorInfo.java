package ru.sber.exporter;

import static ru.sber.exporter.WriteExcel.wb;
import static ru.sber.parser.CustomerRegistration.errorList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import ru.sber.parser.CustomerRegistration;

/**
 * Create page with error.
 */
public class ErrorInfo {

    static Sheet sheet3 = wb.createSheet("Ошибки");
    static Row row3 = sheet3.createRow(0);

    static Font font0 = wb.createFont();
    static CellStyle style0 = wb.createCellStyle();

    /**
     * Write 4th page with error info.
     */
    public static void errorPage() {
        XSSFCellStyle style3 = wb.createCellStyle();
        style3.setWrapText(true);

        style3.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN1.getIndex());
        style3.setAlignment(HorizontalAlignment.CENTER);
        style3.setVerticalAlignment(VerticalAlignment.CENTER);
        style3.setFillPattern(FillPatternType.THIN_BACKWARD_DIAG);
        style3.setFont(font0);

        Cell cell3 = row3.createCell(0);
        cell3.setCellValue("Организации с ошибкой проверки");
        cell3.setCellStyle(style3);

        if (errorList.size() > 0) {
            for (int y = 1; y < CustomerRegistration.errorList.size() + 1; y++) {
                Row dataRow4 = sheet3.createRow(y);
                Cell dataCell4 = dataRow4.createCell(0);
                dataCell4.setCellValue(String.valueOf(errorList.get(y - 1)));
            }
        } else {
            cell3 = row3.createCell(1);
            cell3.setCellValue("Ошибки отсутствуют");
            cell3.setCellStyle(style0);
        }

        sheet3.autoSizeColumn(0);
        sheet3.autoSizeColumn(1);
        sheet3.setAutoFilter(new CellRangeAddress(0, 0, 0, 0));
    }
}
