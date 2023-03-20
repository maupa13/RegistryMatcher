package ru.sber.exporter;

import static ru.sber.exporter.WriteExcel.wb;
import static ru.sber.parser.SnmMatcher.resultSnmType;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

/**
 * Create page with snm info.
 */
public class SnmInfo {

    static Sheet sheet1 = wb.createSheet("Данные реестра СЕМ");
    static Row row1 = sheet1.createRow((short) 0);

    /**
     * Write 2nd page with snm data.
     */
    public static void snmPage() {
        final Font font1 = wb.createFont();

        XSSFCellStyle styleHeight = wb.createCellStyle();
        styleHeight.setWrapText(true);

        XSSFCellStyle style1 = wb.createCellStyle();
        style1.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN1.getIndex());
        style1.setAlignment(HorizontalAlignment.CENTER);
        style1.setVerticalAlignment(VerticalAlignment.CENTER);
        style1.setFillPattern(FillPatternType.THIN_BACKWARD_DIAG);
        style1.setFont(font1);

        Cell cell1 = row1.createCell(0);
        cell1.setCellValue("Данные реестра");
        cell1.setCellStyle(style1);

        for (int y = 1; y < resultSnmType.size() + 1; y++) {
            Row dataRow1 = sheet1.createRow(y);
            Cell dataCell1 = dataRow1.createCell(0);
            dataCell1.setCellValue(String.valueOf(resultSnmType.get(y - 1)).replaceAll("Реестр\tРаздел\tНомер"
                    + "\tРегион\tОрганизация\tРеквизиты\tАдрес\tНомер приказа о включении"
                    + "\tДата приказа о включении", ""));
            dataCell1.setCellStyle(styleHeight);
        }

        sheet1.autoSizeColumn(0);

        sheet1.setAutoFilter(new CellRangeAddress(0, 0, 0, 0));
    }
}
