package ru.sber.exporter;

import static ru.sber.exporter.WriteExcel.wb;
import static ru.sber.parser.CustomerRegistration.resultCustomer;
import static ru.sber.parser.CustomerTypeOfCompany.resultCustomerType;
import static ru.sber.parser.OrganizationRegistration.resultOrganization;
import static ru.sber.parser.RevenueRegistration.resultRevenue;
import static ru.sber.parser.SnmMatcher.resultSnm;

import com.poiji.bind.Poiji;
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
import ru.sber.dto.Company;

import java.io.File;
import java.util.List;

/**
 * Create page with result of matching.
 */
public class ResultInfo {

    static Font font0 = wb.createFont();
    static CellStyle style0 = wb.createCellStyle();

    static Sheet sheet0 = wb.createSheet("Данные реестров");
    static Row row0 = sheet0.createRow(0);

    /**
     * Write 1st page with result.
     */
    public static void resultInfoPage(String input) {
        final List<Company> companies = Poiji.fromExcel(new File(input), Company.class);

        style0.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN1.getIndex());
        style0.setAlignment(HorizontalAlignment.CENTER);
        style0.setVerticalAlignment(VerticalAlignment.CENTER);
        style0.setFillPattern(FillPatternType.THIN_BACKWARD_DIAG);
        style0.setFont(font0);

        row0.setHeight((short) 500);

        Cell cell = row0.createCell(0);
        cell.setCellValue("ОГРН");
        cell.setCellStyle(style0);

        cell = row0.createCell(1);
        cell.setCellValue("Наименование организации");
        cell.setCellStyle(style0);

        cell = row0.createCell(2);
        cell.setCellValue("Реестр заказчиков");
        cell.setCellStyle(style0);

        cell = row0.createCell(3);
        cell.setCellValue("Реестр заказчиков - вид ЮЛ");
        cell.setCellStyle(style0);

        cell = row0.createCell(4);
        cell.setCellValue("Реестр организаций");
        cell.setCellStyle(style0);

        cell = row0.createCell(5);
        cell.setCellValue("Сведения о размещенной выручке");
        cell.setCellStyle(style0);

        cell = row0.createCell(6);
        cell.setCellValue("Реестр СЕМ");
        cell.setCellStyle(style0);

        int i = 1;
        int m = 0;

        for (Company element : companies) {

            //Set value to psrn cell in excel file
            Row dataRow = sheet0.createRow(i);
            Cell dataCell = dataRow.createCell(0);
            dataCell.setCellValue(String.valueOf(element.getPsrn()));

            //Set value to NAME cell in excel file
            dataCell = dataRow.createCell(1);
            dataCell.setCellValue(String.valueOf(element.getName()));

            dataCell = dataRow.createCell(2);
            dataCell.setCellValue(String.valueOf(resultCustomer.get(m)));

            dataCell = dataRow.createCell(3);
            dataCell.setCellValue(String.valueOf(resultCustomerType.get(m)));

            dataCell = dataRow.createCell(4);
            dataCell.setCellValue(String.valueOf(resultOrganization.get(m)));

            dataCell = dataRow.createCell(5);
            dataCell.setCellValue(String.valueOf(resultRevenue.get(m)));

            dataCell = dataRow.createCell(6);
            dataCell.setCellValue(String.valueOf(resultSnm.get(m)));

            i++;
            m++;
        }

        sheet0.autoSizeColumn(0);
        sheet0.autoSizeColumn(1);
        sheet0.autoSizeColumn(2);
        sheet0.autoSizeColumn(3);
        sheet0.autoSizeColumn(4);
        sheet0.autoSizeColumn(5);
        sheet0.autoSizeColumn(6);

        sheet0.setAutoFilter(new CellRangeAddress(0, 0, 0, 6));
        sheet0.createFreezePane(0, 1);
    }
}
