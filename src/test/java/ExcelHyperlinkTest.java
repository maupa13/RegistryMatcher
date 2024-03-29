import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelHyperlinkTest {

    @Test
    public void hyperLinkWithWritingExcelFile() {
        String url = "https://www.google.de/search?&q=Lorem+ipsum+dolor+sit+amet%2C+" +
                "consetetur+sadipscing+elitr%2C+sed+diam+nonumy+eirmod+tempor+invidunt+ut" +
                "+labore+et+dolore+magna+aliquyam+erat%2C+sed+diam+voluptua.+At+vero+eos+et" +
                "+accusam+et+justo+duo+dolores+et+ea+rebum.+Stet+clita+kasd+gubergren%2C+" +
                "+sea+takimata+sanctus+est+Lorem+ipsum+dolor+sit+amet.+Lorem+ipsum+dolor+sit" +
                "+amet%2C+consetetur+sadipscing+elitr%2C+sed+diam+nonumy+eirmod+tempor+invidunt" +
                "+ut+labore+et+dolore+magna+aliquyam+erat%2C+sed+diam+voluptua.+At+vero+eos+et" +
                "+accusam+et+justo+duo+dolores+et+ea+rebum.+Stet+clita+kasd+gubergren%2C+no+sea" +
                "+takimata+sanctus+est+Lorem+ipsum+dolor+sit+amet.&oq=Lorem+ipsum+dolor+sit+amet%2C" +
                "+consetetur+sadipscing+elitr%2C+sed+diam+nonumy+eirmod+tempor+invidunt+ut+labore" +
                "+et+dolore+magna+aliquyam+erat%2C+sed+diam+voluptua.+At+vero+eos+et+accusam+et+justo" +
                "+duo+dolores+et+ea+rebum.+Stet+clita+kasd+gubergren%2C+no+sea+takimata+sanctus+est" +
                "+Lorem+ipsum+dolor+sit+amet.+Lorem+ipsum+dolor+sit+amet%2C+consetetur+sadipscing+" +
                "elitr%2C+sed+diam+nonumy+eirmod+tempor+invidunt+ut+labore+et+dolore+magna+aliquyam" +
                "+erat%2C+sed+diam+voluptua.+At+vero+eos+et+accusam+et+justo+duo+dolores+et+ea+rebum." +
                "+Stet+clita+kasd+gubergren%2C+no+sea+takimata+sanctus+est+Lorem+ipsum+dolor+sit+amet." +
                "+Lorem+ipsum+dolor+sit+amet%2C+consetetur+sadipscing+elitr%2C+sed+diam+nonumy+eirmod" +
                "+tempor+invidunt+ut+labore+et+dolore+magna+aliquyam+erat%2C+sed+diam+voluptua." +
                "+At+vero+eos+et+accusam+et+justo+duo+dolores+et+ea+rebum.+Stet+clita+kasd+gubergren%2C" +
                "+no+sea+takimata+sanctus+est+Lorem+ipsum+dolor+sit+amet.";

        int urlLength = url.length();

        System.out.println(urlLength);

        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fileout = new FileOutputStream("src/test/resources/Excel.xlsx")) {

            Sheet sheet = workbook.createSheet();

            Hyperlink link = workbook.getCreationHelper().createHyperlink(HyperlinkType.URL);
            link.setAddress(url);

            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            String cellText = "Open link to long URL having length of " + urlLength + " characters.";
            cell.setCellValue(cellText);
            cell.setHyperlink(link);

            sheet.setColumnWidth(0, cellText.length() * 256);

            workbook.write(fileout);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
