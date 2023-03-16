import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

public class DownloadCem {

    @Test
    public void downloadCemTable() {

        String outputPath = "C:\\Temp\\cemTemp.xml";

        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

        String urlCem = "http://apps.eias.fas.gov.ru/findcem/";

        WebClient webClient = new WebClient(BrowserVersion.CHROME);

        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getCookieManager().setCookiesEnabled(true);

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

        HtmlInput inputBox = (HtmlInput)page.getElementById("oGRN");
        inputBox.setValueAttribute("text");

        webClient.waitForBackgroundJavaScript(10000);

        HtmlTable table = page.getFirstByXPath("//*[@id=\"report-table\"]");

        try {
            Files.write(Paths.get(outputPath), Collections.singleton(String.valueOf(table.asXml())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
