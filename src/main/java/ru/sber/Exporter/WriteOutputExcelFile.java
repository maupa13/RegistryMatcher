package ru.sber.exporter;

import static ru.sber.parser.CemMatcher.resultCem;
import static ru.sber.parser.CemMatcher.resultCemType;
import static ru.sber.parser.OrganizaciiRegistration.resultOrganizacii;
import static ru.sber.parser.VyruchkaRegistration.resultVyruchka;
import static ru.sber.parser.ZakachikiTypeOfCompanyRegistration.resultZakazchikiType;
import static ru.sber.parser.ZakazchikiRegistration.resultZakazchiki;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Write output Excel file with results of searching.
 */
public class WriteOutputExcelFile {

    static String input;
    static String output;

    XSSFWorkbook wb = new XSSFWorkbook();

    Sheet sheet = wb.createSheet("Данные реестров");
    Row row = sheet.createRow(0);

    Sheet sheet2 = wb.createSheet("Данные реестра СЕМ");
    Row row2 = sheet2.createRow((short) 0);

    CellStyle style = wb.createCellStyle();
    Font font = wb.createFont();

    /**
     * Write 3 pages with results.
     *
     * @param input path to read input file.
     * @param output path to write output file.
     */
    public WriteOutputExcelFile(String input, String output) {

        firstPage(input, output);
        secondPage(output);
        thirdPage(input, output);
    }

    /**
     * Write 1st page with registry info.
     *
     * @param input path to read input file and cells.
     * @param output path to write output file.
     */
    public void firstPage(String input, String output) {

        final ArrayList<Company> companies = (ArrayList<Company>) Poiji.fromExcel(new File(input), Company.class);

        Font font = wb.createFont();
        CellStyle style = wb.createCellStyle();

        style.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN1.getIndex());
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillPattern(FillPatternType.THIN_BACKWARD_DIAG);
        style.setFont(font);

        row.setHeight((short) 500);

        Cell cell = row.createCell(0);
        cell.setCellValue("ОГРН");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("Наименование организации");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("Реестр заказчиков");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("Реестр заказчиков - вид ЮЛ");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("Реестр организаций");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("Сведения о размещенной выручке");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("Реестр СЕМ");
        cell.setCellStyle(style);

        int i = 1;
        int m = 0;

        for (Company element : companies) {

            //Set value to OGRN cell in excel file
            Row dataRow = sheet.createRow(i);
            Cell dataCell = dataRow.createCell(0);
            dataCell.setCellValue(element.getOgrn());

            //Set value to NAME cell in excel file
            dataCell = dataRow.createCell(1);
            dataCell.setCellValue(element.getName());

            dataCell = dataRow.createCell(2);
            dataCell.setCellValue(String.valueOf(resultZakazchiki.get(m)));

            dataCell = dataRow.createCell(3);
            dataCell.setCellValue(String.valueOf(resultZakazchikiType.get(m)));

            dataCell = dataRow.createCell(4);
            dataCell.setCellValue(String.valueOf(resultOrganizacii.get(m)));

            dataCell = dataRow.createCell(5);
            dataCell.setCellValue(String.valueOf(resultVyruchka.get(m)));

            dataCell = dataRow.createCell(6);
            dataCell.setCellValue(String.valueOf(resultCem.get(m)));

            i++;
            m++;
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);

        sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, 6));
        sheet.createFreezePane(0, 1);

        FileOutputStream fileOut = null;

        try {
            fileOut = new FileOutputStream(output);

            System.out.println("FIRST PAGE WRITTEN: " + output);
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
     * Write 2nd page with fas.gov.ru info.
     *
     * @param output path to write output file.
     */
    public void secondPage(String output) {
        XSSFCellStyle style1 = wb.createCellStyle();
        style1.setWrapText(true);

        style.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN1.getIndex());
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillPattern(FillPatternType.THIN_BACKWARD_DIAG);
        style.setFont(font);

        Cell cell1 = row2.createCell(0);
        cell1.setCellValue("Данные реестра");
        cell1.setCellStyle(style);

        for (int y = 1; y < resultCemType.size() + 1; y++) {
            Row dataRow2 = sheet2.createRow(y);
            Cell dataCell2 = dataRow2.createCell(0);
            dataCell2.setCellValue(String.valueOf(resultCemType.get(y - 1))
                    .replaceAll("Реестр\tРаздел\tНомер"
                    + "\tРегион\tОрганизация\tРеквизиты\tАдрес"
                            + "\tНомер приказа о включении\tДата приказа о включении", ""));
            dataCell2.setCellStyle(style1);
        }

        sheet2.autoSizeColumn(0);

        sheet2.setAutoFilter(new CellRangeAddress(0, 0, 0, 0));

        FileOutputStream fileOut = null;

        try {
            fileOut = new FileOutputStream(output);

            System.out.println("SECOND PAGE WRITTEN: " + output);
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
     * Write 3rd page with hyperlinks info.
     *
     * @param input path to read cells from input file.
     * @param output path to write output file.
     */
    public void thirdPage(String input, String output) {

        final ArrayList<Company> companies = (ArrayList<Company>) Poiji.fromExcel(new File(input), Company.class);

        Font font3 = wb.createFont();
        CellStyle style = wb.createCellStyle();

        style.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN1.getIndex());
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillPattern(FillPatternType.THIN_BACKWARD_DIAG);
        style.setFont(font3);

        Font fontHyper = wb.createFont();
        CellStyle styleHyper = wb.createCellStyle();

        fontHyper.setColor(IndexedColors.BLUE.getIndex());
        fontHyper.setUnderline(Font.U_SINGLE);
        styleHyper.setAlignment(HorizontalAlignment.CENTER);
        styleHyper.setVerticalAlignment(VerticalAlignment.CENTER);
        styleHyper.setFont(fontHyper);

        Sheet sheet3 = wb.createSheet("Перечень ссылок");
        Row row3 = sheet3.createRow(0);

        row3.setHeight((short) 400);

        Cell cell3 = row3.createCell(0);
        cell3.setCellValue("ОГРН");
        cell3.setCellStyle(style);

        cell3 = row3.createCell(1);
        cell3.setCellValue("Наименование организации");
        cell3.setCellStyle(style);

        cell3 = row3.createCell(2);
        cell3.setCellValue("Реестр заказчиков");
        cell3.setCellStyle(style);

        cell3 = row3.createCell(3);
        cell3.setCellValue("Реестр организаций");
        cell3.setCellStyle(style);

        cell3 = row3.createCell(4);
        cell3.setCellValue("Реестр сведений о размещенной выручке");
        cell3.setCellStyle(style);

        int p = 1;

        for (Company element : companies) {
            String urlZakachicki = "https://zakupki.gov.ru/epz/customer223/search/results.html?searchString="
                    + element.getOgrn()
                    + "&morphology=on&search-filter=%D0%94%D0%B0%D1%82%D0%B5+"
                    + "%D1%80%D0%B0%D0%B7%D0%BC%D0%B5%D1%89%D0%B5%D0%BD%D0%B8%D1%8F&pageNumber=1&"
                    + "sortDirection=false&recordsPerPage=_10&showLotsInfoHidden=false&sortBy="
                    + "NAME&customer223Status_0=on&customer223Status=0&organizationRoleValueIdNameHidden=%7B%7D";

            String nameChanded = element.getName()
                    .replace(" ", "+")
                    .replace("\"", "")
                    .replace("/", "")
                    .replace(">", "")
                    .replace("?", "")
                    .replace(",", "")
                    .replace("<", "");

            String urlVyruchka = "https://zakupki.gov.ru/epz/revenue/search/results.html?searchString="
                    + nameChanded
                    + "%22&morphology=on&search-filter=Дате+размещения&irrelevantInformation=on&sec_1=on&sec_2="
                    + "on&sec_3=on&reportingPeriodYearStartHidden=0&reportingPeriodQuarterStartHidden=DEFAULT&"
                    + "reportingPeriodYearEndHidden=0&reportingPeriodQuarterEndHidden=DEFAULT&sortBy=REESTR_NAME"
                    + "&pageNumber=1&sortDirection=true&recordsPerPage=_10&showLotsInfoHidden=false";

            String urlOrganizacii = "https://zakupki.gov.ru/epz/organization/search/results.html?searchString="
                    + element.getOgrn()
                    + "&morphology=on&search-filter=%D0%94%D0%B0%D1%82%D0%B5+"
                    + "%D1%80%D0%B0%D0%B7%D0%BC%D0%B5%D1%89%D0%B5%D0%BD%D0%B8%D1%8F&fz94"
                    + "=on&fz223=on&F=on&S=on&M=on&NOT_FSM=on&registered94=on&notRegistered=on&sortBy="
                    + "NAME&pageNumber=1&sortDirection=false&recordsPerPage=_10&showLotsInfoHidden=false";


            Hyperlink linkZak = wb.getCreationHelper().createHyperlink(HyperlinkType.URL);
            linkZak.setAddress(urlZakachicki);

            Hyperlink linkOrg = wb.getCreationHelper().createHyperlink(HyperlinkType.URL);
            linkOrg.setAddress(urlOrganizacii);

            Hyperlink linkVyr = wb.getCreationHelper().createHyperlink(HyperlinkType.URL);
            linkVyr.setAddress(urlVyruchka);

            //Set value to OGRN cell in excel file
            Row dataRow = sheet3.createRow(p);
            Cell dataCell = dataRow.createCell(0);
            dataCell.setCellValue(element.getOgrn());

            //Set value to NAME cell in excel file
            dataCell = dataRow.createCell(1);
            dataCell.setCellValue(element.getName());

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

        sheet3.autoSizeColumn(0);
        sheet3.autoSizeColumn(1);
        sheet3.autoSizeColumn(2);
        sheet3.autoSizeColumn(3);
        sheet3.autoSizeColumn(4);

        sheet3.setAutoFilter(new CellRangeAddress(0, 0, 0, 4));

        FileOutputStream fileOut = null;

        try {
            fileOut = new FileOutputStream(output);

            System.out.println("THIRD PAGE WRITTEN: " + output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            wb.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
