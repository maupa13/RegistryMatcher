import static ru.sber.parser.CustomerRegistration.errorList;
import static ru.sber.parser.CustomerRegistration.resultCustomer;
import static ru.sber.parser.CustomerTypeOfCompany.resultCustomerType;
import static ru.sber.parser.OrganizationRegistration.resultOrganization;
import static ru.sber.parser.RevenueRegistration.resultRevenue;
import static ru.sber.parser.SnmMatcher.resultSnm;
import static ru.sber.parser.SnmMatcher.resultSnmType;

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
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.sber.dto.Company;
import ru.sber.parser.CustomerRegistration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Write Excel result.
 */
public class OutputExcel {

    XSSFWorkbook wb = new XSSFWorkbook();

    Font font0 = wb.createFont();
    CellStyle style0 = wb.createCellStyle();

    Sheet sheet0 = wb.createSheet("Данные реестров");
    Row row0 = sheet0.createRow(0);

    Sheet sheet1 = wb.createSheet("Данные реестра СЕМ");
    Row row1 = sheet1.createRow((short) 0);

    Sheet sheet2 = wb.createSheet("Перечень ссылок");
    Row row2 = sheet2.createRow(0);

    Sheet sheet3 = wb.createSheet("Ошибки");
    Row row3 = sheet3.createRow(0);

    /**
     * Load data from user, output file.
     */
    public OutputExcel(String input, String output) {
        firstPage(input);
        secondPage();
        thirdPage(input);
        fourthPage();
        writeFile(output);
    }

    /**
     * Write result file.
     */
    public void writeFile(String output) {
        FileOutputStream fileOut = null;

        try {
            fileOut = new FileOutputStream(output);

            System.out.println("\nDone! Output: " + output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            wb.write(fileOut);
            Objects.requireNonNull(fileOut).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write 1st page with result.
     */
    public void firstPage(String input) {
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

    /**
     * Write 2nd page with snm data.
     */
    public void secondPage() {
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

    /**
     * Write 3rd page with hyperlinks.
     */
    public void thirdPage(String input) {
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

            //Set value to OGR cell in excel file
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

    /**
     * Write 4th page with error info.
     */
    public void fourthPage() {
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
                dataCell4.setCellValue(String.valueOf(resultSnmType.get(y - 1)));
                dataCell4.setCellStyle(style3);
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
