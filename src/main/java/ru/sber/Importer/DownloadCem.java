package ru.sber.Importer;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

public class DownloadCem {
    public static void downloadCemTable() {

        String outputPath = "C:\\Temp\\cemTemp.xml";

        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

        String urlCem = "http://apps.eias.fas.gov.ru/findcem/";

        WebClient webClient = new WebClient(BrowserVersion.CHROME);

//        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);

        HtmlPage page = null;

        try {
            page = webClient.getPage(urlCem);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert page != null;

        try {
            HtmlButton htmlButtonSearch = page.getFirstByXPath("/html/body/div/div[2]/div[2]/button");
            htmlButtonSearch.click();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        while (!page.asText().contains("<th>Номер приказа о включении</th>")) {
//            webClient.waitForBackgroundJavaScript(500);
//        }

        webClient.waitForBackgroundJavaScript(10000);

        HtmlTable table = page.getFirstByXPath("//*[@id=\"report-table\"]");
//
//        String cemTable = elementSuccessTable.asXml();

//        String result = page.asXml();

//        final HtmlTable table = (HtmlTable) page.getByXPath("//*[@id=\"SuccessTable\"]").get(0);

        try {
            Files.write(Paths.get(outputPath), Collections.singleton(String.valueOf(table.asXml())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
