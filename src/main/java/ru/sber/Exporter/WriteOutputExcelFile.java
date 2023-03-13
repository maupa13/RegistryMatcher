package ru.sber.Exporter;

import com.poiji.bind.Poiji;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import ru.sber.DTO.Company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static ru.sber.Parser.CemRegistration.resultCem;
import static ru.sber.Parser.OrganizaciiRegistration.resultOrganizacii;
import static ru.sber.Parser.VyruchkaRegistration.resultVyruchka;
import static ru.sber.Parser.ZakachikiTypeOfCompanyRegistration.resultZakazchikiType;
import static ru.sber.Parser.ZakazchikiRegistration.resultZakazchiki;

public class WriteOutputExcelFile {

    public WriteOutputExcelFile(String input, String output) {

        int i = 1;
        int m = 0;

        List<Company> companies = Poiji.fromExcel(new File(input), Company.class);

        Workbook wb = new HSSFWorkbook();
        Font font = wb.createFont();

        CellStyle style = wb.createCellStyle();

        style.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.THICK_BACKWARD_DIAG);
        style.setFont(font);

        Sheet sheet = wb.createSheet("Регистрация в Реестрах (223-44)");
        Row row = sheet.createRow(0);

        Cell cell = row.createCell(0);
        cell.setCellValue("ОГРН организации");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("Наименование организации");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("Реестр заказчиков");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("Реестр организаций - вид ЮЛ");
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

        FileOutputStream fileOut = null;

        try {
            fileOut = new FileOutputStream(output);

            System.out.println("Done! 100% - output file: " + output);
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
