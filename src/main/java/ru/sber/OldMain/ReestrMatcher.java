package ru.sber.OldMain;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.nonNull;

//NEED SSL V3 or CERTIFICATE SETTINGS
public class ReestrMatcher {
    public static void main(String[] args) {

        int exit = 1;
        int rowcount;

        String ogrn1;
        String ogrnString1;
        String ogrn;
        String ogrnString;
        String name;
        String nameWithPlus;
        String userAgent = "Mozilla/5.0 (X11; U; Linux i586; en-US; rv:1.7.3) Gecko/20040924 Epiphany/1.4.4 (Ubuntu)";

        boolean all;
        boolean only44;
        boolean only223;
        boolean blocked44;
        boolean blocked223;

        Document organizacii;
        Document zakazchiki;
        Document vyruchka;

        System.setProperty("http.proxyHost", "192.168.5.1");
        System.setProperty("http.proxyPort", "8182");

        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите путь к excel файлу (.xls) с форматом файла (например - c:\\input.xls):");
        String inputFile = scanner.next();

        System.out.println("Введите путь c наименованием и форматом excel файла (.xls) для выдачи результата проверки (например - c:\\output.xls):");
        String outputFile = scanner.next();

        System.out.println("УВЕДОМЛЕНИЕ: Закройте таблицу отправляемую на проверку. При ошибке программа перезапустится самостоятельно.");

        File fileIn = new File(inputFile);

        FileInputStream fisIn;

        Workbook wb = new HSSFWorkbook();
        Font font = wb.createFont();

        CellStyle style = wb.createCellStyle();

        style.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.THICK_BACKWARD_DIAG);
        style.setFont(font);

        Sheet sheet = wb.createSheet("Регистрация в Реестрах (223-44)");
        Row row = sheet.createRow(0);

        Cell cell = row.createCell(0);
        cell.setCellValue("ОГРН/ИНН проверяемой организации");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("Наименование организации");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("Статус - реестр заказчиков");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("Реестр организаций - вид юридического лица");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("Статус - реестр организаций");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("Сведения о размещенной выручке");
        cell.setCellStyle(style);


        try {
            fisIn = new FileInputStream(fileIn);

            HSSFWorkbook wbIn = new HSSFWorkbook(fisIn);
            HSSFSheet sheet1 = wbIn.getSheetAt(0);
            rowcount = sheet1.getLastRowNum();

            System.out.println("Файл excel взят в работу. Количество ячеек на проверку: " + rowcount);

            for (int i = 1; i < rowcount + 1; i++) {

                Row dataRow = sheet.createRow(i + 1);
                Cell dataCell = dataRow.createCell(0);
                Cell cellOgrn = sheet1.getRow(i).getCell(0);

                dataCell.setCellValue(cellOgrn.getStringCellValue());

                dataCell = dataRow.createCell(1);
                Cell cellName = sheet1.getRow(i).getCell(1);
                dataCell.setCellValue(cellName.getStringCellValue());

                if (nonNull(cellOgrn)) {

                    ogrn = cellOgrn.getStringCellValue();

                    ogrnString = ogrn
                            .replace(" ", "")
                            .replace("\"", "")
                            .replace("/", "")
                            .replace(">", "")
                            .replace("<", "");

                    Thread.sleep(100);

                    String urlZakachicki = "https://zakupki.gov.ru/epz/customer223/search/results.html?searchString=" +
                            ogrnString +
                            "&morphology=on&search-filter=%D0%94%D0%B0%D1%82%D0%B5+" +
                            "%D1%80%D0%B0%D0%B7%D0%BC%D0%B5%D1%89%D0%B5%D0%BD%D0%B8%D1%8F&pageNumber=1&" +
                            "sortDirection=false&recordsPerPage=_10&showLotsInfoHidden=false&sortBy=" +
                            "NAME&customer223Status_0=on&customer223Status=0&organizationRoleValueIdNameHidden=%7B%7D";

                    zakazchiki = Jsoup.connect(urlZakachicki)
                            .userAgent(userAgent)
                            .timeout(0)
                            .get();

                    Thread.sleep(100);

//                    Matcher matcherZakazchiki = Pattern.compile(cellOgrn.getStringCellValue()).matcher(zakazchiki.toString());
//                    StringBuilder matchesZakazchiki = new StringBuilder();
//
//                    int matchCountZakazchiki = 0;
//
//                    while (matcherZakazchiki.find()) {
//                        matchesZakazchiki.append(matcherZakazchiki.group());
//                        matchesZakazchiki.append(",");
//                        matchCountZakazchiki++;
//                    }
//
//                    dataCell = dataRow.createCell(2);
//
//                    if (matchCountZakazchiki == 5) {
//                        dataCell.setCellValue("Зарегистрирован");
//                    } else if (matchCountZakazchiki > 5) {
//                        dataCell.setCellValue("Зарегистрирован. Записи о дочерних обществах");
//                    } else {
//                        dataCell.setCellValue("Не зарегистрирован");
//                    }

                    Elements zakazchikiStatus = zakazchiki.getElementsByClass("d-flex lots-wrap-content__body__val");
                    StringBuilder statusStringBuildZakachiki = new StringBuilder();

                    Matcher matcherClassesZakachiki = Pattern.compile("d-flex lots-wrap-content__body__val").matcher(zakazchikiStatus.toString());

                    int countOfReferenceZakazchiki = 0;
                    while (matcherClassesZakachiki.find()) {
                        statusStringBuildZakachiki.append(matcherClassesZakachiki.group());
                        statusStringBuildZakachiki.append("d-flex lots-wrap-content__body__val");
                        countOfReferenceZakazchiki++;
                    }

                    String zakazchikiStatusWrite = zakazchikiStatus.toString()
                            .replace("<", "")
                            .replace(">", "")
                            .replace("/", "")
                            .replace("\"", "")
                            .replace("</div>", "")
                            .replace("div class=d-flex lots-wrap-content__body__val\n" +
                                    " Заказчик\n" + "div\n" + "div class=d-flex lots-wrap-content__body__val", "")
                            .replace("class=d-flexlots-wrap-content__body__val", "")
                            .replace("div", "");

                    dataCell = dataRow.createCell(2);

                    if (countOfReferenceZakazchiki > 1) {
                        dataCell.setCellValue("Зарегистрирован");
                    } else {
                        dataCell.setCellValue("Не зарегистрирован");
                    }

                    Cell dataCell1 = dataRow.createCell(3);

                    if (countOfReferenceZakazchiki > 0) {
                        dataCell1.setCellValue(zakazchikiStatusWrite);
                    } else {
                        dataCell1.setCellValue("Не указан вид юридического лица");
                    }

                    FileOutputStream fileOut = new FileOutputStream(outputFile);
                    wb.write(fileOut);
                    fileOut.close();

                    System.out.println("\"Реестр заказчиков\": " + cellOgrn.getStringCellValue() + ". Выполнено: " + (100 * i / rowcount) + "%");

                    System.out.println("Результат проверки сохранен в следующий файл: " + outputFile);
                }
            }

            for (int n = 1; n < rowcount + 1; n++) {

                Cell cellOgrn1 = sheet1.getRow(n).getCell(0);

                if (nonNull(cellOgrn1)) {

                    ogrn1 = cellOgrn1.getStringCellValue();

                    ogrnString1 = ogrn1
                            .replace(" ", "")
                            .replace("\"", "")
                            .replace("/", "")
                            .replace(">", "")
                            .replace("<", "");

                    Thread.sleep(100);

                    String urlOrganizacii = "https://zakupki.gov.ru/epz/organization/search/results.html?searchString=" +
                            ogrnString1 +
                            "&morphology=on&search-filter=%D0%94%D0%B0%D1%82%D0%B5+" +
                            "%D1%80%D0%B0%D0%B7%D0%BC%D0%B5%D1%89%D0%B5%D0%BD%D0%B8%D1%8F&fz94" +
                            "=on&fz223=on&F=on&S=on&M=on&NOT_FSM=on&registered94=on&notRegistered=on&sortBy=" +
                            "NAME&pageNumber=1&sortDirection=false&recordsPerPage=_10&showLotsInfoHidden=false";

                    organizacii = Jsoup.connect(urlOrganizacii)
                            .userAgent(userAgent)
                            .timeout(0)
                            .get();

                    Thread.sleep(100);

                    Elements organizaciiStatus = organizacii.getElementsByClass("registry-entry__header-top__title");
                    StringBuilder statusStringBuild = new StringBuilder();
                    Matcher matcherClasses = Pattern.compile("registry-entry__header-top__title").matcher(organizaciiStatus.toString());

                    int countOfReference = 0;
                    while (matcherClasses.find()) {
                        statusStringBuild.append(matcherClasses.group());
                        statusStringBuild.append("registry-entry__header-top__title");
                        countOfReference++;
                    }

                    //do correct booleans with organizations, which blocked with 44-fz, but not with 223-fz
                    all = organizaciiStatus.toString().contains("44-ФЗ") && organizaciiStatus.toString().contains("223-ФЗ");
                    only44 = organizaciiStatus.toString().contains("44-ФЗ") && !(organizaciiStatus.toString().contains("223-ФЗ"));
                    only223 = !(organizaciiStatus.toString().contains("44-ФЗ")) && organizaciiStatus.toString().contains("223-ФЗ");
                    blocked44 = organizaciiStatus.toString().contains("44-ФЗ Заблокирована");
                    blocked223 = organizaciiStatus.toString().contains("223-ФЗ Заблокирована");

                    Row dataRow = sheet.getRow(n + 1);
                    Cell dataCell1 = dataRow.createCell(4);

                    if (blocked223) {
                        dataCell1.setCellValue("Запись о блокировке: 223-ФЗ. Рекомендуется проверить организацию вручную");
                    }

                    if (blocked44) {
                        dataCell1.setCellValue("Запись о блокировке: 44-ФЗ. Рекомендуется проверить организацию вручную");
                    }

                    if (countOfReference == 1) {
                        if (all && !(blocked44) && !(blocked223)) {
                            dataCell1.setCellValue("Зарегистрирован: 223-ФЗ и 44-ФЗ. Записи о дочерних организациях отсутствуют");
                        } else if (only223 && !(blocked223)) {
                            dataCell1.setCellValue("Зарегистрирован: 223-ФЗ. Записи о дочерних организациях отсутствуют");
                        } else if (only44 && !(blocked44)) {
                            dataCell1.setCellValue("Зарегистрирован: 44-ФЗ. Записи о дочерних организациях отсутствуют");
                        }
                    } else if (countOfReference > 0) {
                        if (all && !(blocked44) && !(blocked223)) {
                            dataCell1.setCellValue("Материнская компания зарегистрирована: 223-ФЗ и 44-ФЗ");
                        } else if (only223 && !(blocked223)) {
                            dataCell1.setCellValue("Зарегистрирован: 223-ФЗ. Записи о дочерних организациях в аналогичном реестре");
                        } else if (only44 && !(blocked44)) {
                            dataCell1.setCellValue("Зарегистрирован: 44-ФЗ. Записи о дочерних организациях в аналогичном реестре");
                        }
                    } else {
                        dataCell1.setCellValue("Не зарегистрирован в реестре организаций");
                    }

                    System.out.println("\"Реестр организаций\": " + cellOgrn1.getStringCellValue() + ". Выполнено: " + (100 * n / rowcount) + "%");

                    FileOutputStream fileOut = new FileOutputStream(outputFile);
                    wb.write(fileOut);
                    fileOut.close();

                    System.out.println("Результат проверки сохранен в следующий файл: " + outputFile);

                } else {
                    System.out.println("\"Реестр заказчиков\". Пустые ячейки в excel файле.");
                }
            }

            for (int l = 1; l < rowcount + 1; l++) {

                Cell cellName = sheet1.getRow(l).getCell(1);

                if (nonNull(cellName)) {

                    name = cellName.getStringCellValue();

                    nameWithPlus = name
                            .replace(" ", "+")
                            .replace("\"", "")
                            .replace("/", "")
                            .replace(">", "")
                            .replace("?", "")
                            .replace(",", "")
                            .replace("<", "");

                    Thread.sleep(100);

                    String urlVyruchka = "https://zakupki.gov.ru/epz/revenue/search/results.html?searchString=" +
                            nameWithPlus +
                            "%22&morphology=on&search-filter=Дате+размещения&irrelevantInformation=on&sec_1=on&sec_2=" +
                            "on&sec_3=on&reportingPeriodYearStartHidden=0&reportingPeriodQuarterStartHidden=DEFAULT&" +
                            "reportingPeriodYearEndHidden=0&reportingPeriodQuarterEndHidden=DEFAULT&sortBy=REESTR_NAME" +
                            "&pageNumber=1&sortDirection=true&recordsPerPage=_10&showLotsInfoHidden=false";

                    vyruchka = Jsoup.connect(urlVyruchka)
                            .userAgent(userAgent)
                            .timeout(0)
                            .get();

                    Thread.sleep(100);

                    StringBuilder statusStringBuildVyruchka = new StringBuilder();
                    Matcher matcherClassesVyruchka = Pattern.compile("2022 год").matcher(vyruchka.toString());

                    int countOfReferenceVyruchka = 0;
                    while (matcherClassesVyruchka.find()) {
                        statusStringBuildVyruchka.append(matcherClassesVyruchka.group());
                        countOfReferenceVyruchka++;
                    }

                    Row dataRow = sheet.getRow(l + 1);
                    Cell dataCell3 = dataRow.createCell(5);

                    if (countOfReferenceVyruchka > 0) {
                        dataCell3.setCellValue("Сведения о выручке за 2022 г. размещены");
                    } else {
                        dataCell3.setCellValue("Отсутствуют актуальные сведения о размещенной выручке");
                    }

                    System.out.println("\"Реестр сведений о выручке\": " + cellName.getStringCellValue() + ". Выполнено: " + (100 * l / rowcount) + "%");

                    FileOutputStream fileOut = new FileOutputStream(outputFile);
                    wb.write(fileOut);
                    fileOut.close();

                    System.out.println("Результат проверки сохранен в следующий файл: " + outputFile);

                } else {
                    System.out.println("\"Реестр сведений о выручке\". Пустые ячейки в excel файле.");
                }
            }

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);

            FileOutputStream fileOut = new FileOutputStream(outputFile);
            wb.write(fileOut);
            fileOut.close();

            System.out.println("Проверка завершена. Результат проверки: " + outputFile);

            System.exit(exit);

        } catch (SocketException ex) {
            throw new RuntimeException(ex);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
