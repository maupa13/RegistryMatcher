import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import ru.sber.connection.SetIp;

import java.io.IOException;

/**
 * Webclient.
 */
public class Chrome {

    /**
     * Chrome connection to registry.
     *
     * @param url connect registry.
     * @return page results.
     */
    public Page webClientChrome(String url) {

        SetIp setIp = new SetIp();

        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit")
                .setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http")
                .setLevel(java.util.logging.Level.OFF);

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

        webClient.waitForBackgroundJavaScript(10000);

        HtmlPage page = null;

        try {
            page = webClient.getPage(urlCem);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert page != null;

        HtmlInput inputBox = (HtmlInput) page.getElementById("oGRN");

        try {
            HtmlButton htmlButtonSearch = page
                    .getFirstByXPath("/html/body/div/div[2]/div[2]/button");
            htmlButtonSearch.click();
        } catch (IOException e) {
            e.printStackTrace();
        }

        webClient.waitForBackgroundJavaScript(10000);

        return page;
    }
}
