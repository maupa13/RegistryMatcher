package ru.sber.exporter;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * Write output Excel with all pages.
 */
public class WriteExcel {

    public static XSSFWorkbook wb = new XSSFWorkbook();

    /**
     * Load data from user, output file.
     *
     * @param input filepath from user file.
     * @param output filepath to Excel result file.
     */
    public WriteExcel(String input, String output) {
        ResultInfo.resultInfoPage(input);
        SnmInfo.snmPage();
        HyperLink.hyperlinkPage(input);
        ErrorInfo.errorPage();
        writeFile(output);
    }

    /**
     * Write result file.
     *
     * @param output path to file.
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
}
