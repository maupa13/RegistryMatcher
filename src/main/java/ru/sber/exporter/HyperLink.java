package ru.sber.exporter;

import static ru.sber.exporter.WriteExcel.wb;

import com.poiji.bind.Poiji;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import ru.sber.dto.Company;

import java.io.File;
import java.util.List;

/**
 * Create page with hyperlink.
 */
public class HyperLink {

    static Sheet sheet2 = wb.createSheet("Перечень ссылок");
    static Row row2 = sheet2.createRow(0);

    /**
     * Write 3rd page with hyperlinks.
     */
    public static void hyperlinkPage(String input) {
        final List<Company> companies = Poiji.fromExcel(new File(input), Company.class);

        Font font2 = wb.createFont();
        CellStyle style2 = wb.createCellStyle();

        style2.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN1.getIndex());
        style2.setAlignment(HorizontalAlignment.CENTER);
        style2.setVerticalAlignment(VerticalAlignment.CENTER);
        style2.setFillPattern(FillPatternType.THIN_BACKWARD_DIAG);
        style2.setFont(font2);

        Font fontHyper = wb.createFont();
        CellStyle styleHyper = wb.createCellStyle();

        fontHyper.setColor(IndexedColors.BLUE.getIndex());
        fontHyper.setUnderline(Font.U_SINGLE);
        styleHyper.setAlignment(HorizontalAlignment.CENTER);
        styleHyper.setVerticalAlignment(VerticalAlignment.CENTER);
        styleHyper.setFont(fontHyper);

        row2.setHeight((short) 400);

        Cell cell = row2.createCell(0);
        cell.setCellValue("ОГРН");
        cell.setCellStyle(style2);

        cell = row2.createCell(1);
        cell.setCellValue("Наименование организации");
        cell.setCellStyle(style2);

        cell = row2.createCell(2);
        cell.setCellValue("Реестр заказчиков");
        cell.setCellStyle(style2);

        cell = row2.createCell(3);
        cell.setCellValue("Реестр организаций");
        cell.setCellStyle(style2);

        cell = row2.createCell(4);
        cell.setCellValue("Реестр сведений о размещенной выручке");
        cell.setCellStyle(style2);

        int p = 1;

        for (Company element : companies) {
            String urlCustomer = "https://zakupki.gov.ru/epz/customer223/search/results.html?searchString="
                    + element.getPsrn()
                    + "&morphology=on&search-filter=%D0%94%D0%B0%D1%82%D0%B5+"
                    + "%D1%80%D0%B0%D0%B7%D0%BC%D0%B5%D1%89%D0%B5%D0%BD%D0%B8%D1%8F&pageNumber=1&"
                    + "sortDirection=false&recordsPerPage=_10&showLotsInfoHidden=false&sortBy="
                    + "NAME&customer223Status_0=on&customer223Status=0&organizationRoleValueIdNameHidden=%7B%7D";

            String nameChanged = String.valueOf(element.getName())
                    .replace(" ", "+")
                    .replace("\"", "")
                    .replace("/", "")
                    .replace(">", "")
                    .replace("?", "")
                    .replace(",", "")
                    .replace("<", "");

            String urlRevenue = "https://zakupki.gov.ru/epz/revenue/search/results.html?searchString="
                    + nameChanged
                    + "%22&morphology=on&search-filter=Дате+размещения&irrelevantInformation=on&sec_1=on&sec_2="
                    + "on&sec_3=on&reportingPeriodYearStartHidden=0&reportingPeriodQuarterStartHidden=DEFAULT&"
                    + "reportingPeriodYearEndHidden=0&reportingPeriodQuarterEndHidden=DEFAULT&sortBy=REESTR_NAME"
                    + "&pageNumber=1&sortDirection=true&recordsPerPage=_10&showLotsInfoHidden=false";

            String urlOrganization = "https://zakupki.gov.ru/epz/organization/search/results.html?searchString="
                    + element.getPsrn()
                    + "&morphology=on&search-filter=%D0%94%D0%B0%D1%82%D0%B5+"
                    + "%D1%80%D0%B0%D0%B7%D0%BC%D0%B5%D1%89%D0%B5%D0%BD%D0%B8%D1%8F&fz94"
                    + "=on&fz223=on&F=on&S=on&M=on&NOT_FSM=on&registered94=on&notRegistered=on&sortBy="
                    + "NAME&pageNumber=1&sortDirection=false&recordsPerPage=_10&showLotsInfoHidden=false";

            Hyperlink linkZak = wb.getCreationHelper().createHyperlink(HyperlinkType.URL);
            linkZak.setAddress(urlCustomer);

            Hyperlink linkOrg = wb.getCreationHelper().createHyperlink(HyperlinkType.URL);
            linkOrg.setAddress(urlOrganization);

            Hyperlink linkVyr = wb.getCreationHelper().createHyperlink(HyperlinkType.URL);
            linkVyr.setAddress(urlRevenue);

            //Set value to psrn cell in excel file
            Row dataRow = sheet2.createRow(p);
            Cell dataCell = dataRow.createCell(0);
            dataCell.setCellValue(String.valueOf(element.getPsrn()));

            //Set value to NAME cell in excel file
            dataCell = dataRow.createCell(1);
            dataCell.setCellValue(String.valueOf(element.getName()));

            dataCell = dataRow.createCell(2);
            dataCell.setCellValue(p);
            dataCell.setHyperlink(linkZak);
            dataCell.setCellStyle(styleHyper);

            dataCell = dataRow.createCell(3);
            dataCell.setCellValue(p);
            dataCell.setHyperlink(linkOrg);
            dataCell.setCellStyle(styleHyper);

            dataCell = dataRow.createCell(4);
            dataCell.setCellValue(p);
            dataCell.setHyperlink(linkVyr);
            dataCell.setCellStyle(styleHyper);

            p++;
        }

        sheet2.autoSizeColumn(0);
        sheet2.autoSizeColumn(1);
        sheet2.autoSizeColumn(2);
        sheet2.autoSizeColumn(3);
        sheet2.autoSizeColumn(4);

        sheet2.setAutoFilter(new CellRangeAddress(0, 0, 0, 4));
    }
}
