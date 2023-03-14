package ru.sber.Exporter;

import com.poiji.bind.Poiji;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.sber.DTO.Company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

//import static ru.sber.Parser.CemRegistration.resultCem;
import static ru.sber.Parser.CemMatcher.*;
import static ru.sber.Parser.OrganizaciiRegistration.resultOrganizacii;
import static ru.sber.Parser.VyruchkaRegistration.resultVyruchka;
import static ru.sber.Parser.ZakachikiTypeOfCompanyRegistration.resultZakazchikiType;
import static ru.sber.Parser.ZakazchikiRegistration.resultZakazchiki;

public class WriteOutputExcelFile {

    XSSFWorkbook wb = new XSSFWorkbook();

    public WriteOutputExcelFile(String input, String output) {

        firstPage(input, output);
        secondPage(output);
        thirdPage(input, output);
    }

    public void firstPage(String input, String output){
        int i = 1;
        int m = 0;

        List<Company> companies = Poiji.fromExcel(new File(input), Company.class);

        Font font = wb.createFont();
        CellStyle style = wb.createCellStyle();

        style.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN1.getIndex());
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillPattern(FillPatternType.THIN_BACKWARD_DIAG);
        style.setFont(font);

        Sheet sheet = wb.createSheet("Реестры 223-ФЗ 44-ФЗ СЕМ");
        Row row = sheet.createRow(0);

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

    public void secondPage(String output){


        Sheet sheet1 = wb.createSheet("Данные реестра СЕМ");
        Row row1 = sheet1.createRow(0);

        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();

        style.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN1.getIndex());
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillPattern(FillPatternType.THIN_BACKWARD_DIAG);
        style.setFont(font);

        Cell cell1 = row1.createCell(0);
        cell1.setCellValue("Данные реестра");
        cell1.setCellStyle(style);

        for (int y = 1; y < resultCemType.size() + 1; y++) {

            Row dataRow1 = sheet1.createRow(y);
            Cell dataCell1 = dataRow1.createCell(0);
            dataCell1.setCellValue(String.valueOf(resultCemType.get(y - 1)));
        }

        sheet1.autoSizeColumn(0);

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

    public void thirdPage(String input, String output) {
        int p = 1;

        List<Company> companies = Poiji.fromExcel(new File(input), Company.class);

        Font font = wb.createFont();
        CellStyle style = wb.createCellStyle();

        style.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN1.getIndex());
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillPattern(FillPatternType.THIN_BACKWARD_DIAG);
        style.setFont(font);

        Sheet sheet = wb.createSheet("Перечень ссылок");
        Row row = sheet.createRow(0);

        Hyperlink link = (XSSFHyperlink)wb.getCreationHelper().createHyperlink(HyperlinkType.URL);

        row.setHeight((short) 400);

        Cell cell = row.createCell(0);
        cell.setCellValue("ОГРН");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("Наименование организации");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("Ссылка - Реестр заказчиков");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("Ссылка - Организаций");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("Ссылка - Сведения о размещенной выручке");
        cell.setCellStyle(style);

        for (Company element : companies) {

            String urlZakachicki = "https://zakupki.gov.ru/epz/customer223/search/results.html?searchString=" +
                    element.getOgrn() +
                    "&morphology=on&search-filter=%D0%94%D0%B0%D1%82%D0%B5+" +
                    "%D1%80%D0%B0%D0%B7%D0%BC%D0%B5%D1%89%D0%B5%D0%BD%D0%B8%D1%8F&pageNumber=1&" +
                    "sortDirection=false&recordsPerPage=_10&showLotsInfoHidden=false&sortBy=" +
                    "NAME&customer223Status_0=on&customer223Status=0&organizationRoleValueIdNameHidden=%7B%7D";

            String nameChanded = element.getName()
                    .replace(" ", "+")
                    .replace("\"", "")
                    .replace("/", "")
                    .replace(">", "")
                    .replace("?", "")
                    .replace(",", "")
                    .replace("<", "");

            String urlVyruchka = "https://zakupki.gov.ru/epz/revenue/search/results.html?searchString=" +
                    nameChanded +
                    "%22&morphology=on&search-filter=Дате+размещения&irrelevantInformation=on&sec_1=on&sec_2=" +
                    "on&sec_3=on&reportingPeriodYearStartHidden=0&reportingPeriodQuarterStartHidden=DEFAULT&" +
                    "reportingPeriodYearEndHidden=0&reportingPeriodQuarterEndHidden=DEFAULT&sortBy=REESTR_NAME" +
                    "&pageNumber=1&sortDirection=true&recordsPerPage=_10&showLotsInfoHidden=false";

            String urlOrganizacii = "https://zakupki.gov.ru/epz/organization/search/results.html?searchString=" +
                    element.getOgrn() +
                    "&morphology=on&search-filter=%D0%94%D0%B0%D1%82%D0%B5+" +
                    "%D1%80%D0%B0%D0%B7%D0%BC%D0%B5%D1%89%D0%B5%D0%BD%D0%B8%D1%8F&fz94" +
                    "=on&fz223=on&F=on&S=on&M=on&NOT_FSM=on&registered94=on&notRegistered=on&sortBy=" +
                    "NAME&pageNumber=1&sortDirection=false&recordsPerPage=_10&showLotsInfoHidden=false";

            //Set value to OGRN cell in excel file
            Row dataRow = sheet.createRow(p);
            Cell dataCell = dataRow.createCell(0);
            dataCell.setCellValue(element.getOgrn());

            //Set value to NAME cell in excel file
            dataCell = dataRow.createCell(1);
            dataCell.setCellValue(element.getName());

            dataCell = dataRow.createCell(2);
            dataCell.setCellValue(urlZakachicki);
            link.setAddress(urlZakachicki);
            dataCell.setHyperlink(link);

            dataCell = dataRow.createCell(3);
            dataCell.setCellValue(urlOrganizacii);
            link.setAddress(urlOrganizacii);
            dataCell.setHyperlink(link);

            dataCell = dataRow.createCell(4);
            dataCell.setCellValue(urlVyruchka);
            link.setAddress(urlVyruchka);
            dataCell.setHyperlink(link);

            p++;
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
//        sheet.autoSizeColumn(2);
//        sheet.autoSizeColumn(3);
//        sheet.autoSizeColumn(4);

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
