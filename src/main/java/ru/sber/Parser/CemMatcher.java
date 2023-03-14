package ru.sber.Parser;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import ru.sber.Connection.SetIp;
import ru.sber.DTO.Company;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.poiji.bind.Poiji.fromExcel;
import static java.util.Objects.nonNull;

public class CemMatcher {

    public static ArrayList resultCem = new ArrayList();
    public static ArrayList resultCemType = new ArrayList();

    public static ArrayList makeInputInCemReestr(String input) {

        SetIp setIp = new SetIp();

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

        HtmlInput inputBox = (HtmlInput) page.getElementById("oGRN");

        List<Company> companies = fromExcel(new File(input), Company.class);

        var stringCem = new StringBuilder();

        for (Company element : companies) {

            setIp.setVariantIp(element.getRowIndex());

            inputBox.setValueAttribute(element.getOgrn());

            try {
                HtmlButton htmlButtonSearch = page.getFirstByXPath("/html/body/div/div[2]/div[2]/button");
                htmlButtonSearch.click();
            } catch (IOException e) {
                e.printStackTrace();
            }

            webClient.waitForBackgroundJavaScript(10000);

            int countOfReference = 0;

//            ArrayList resultList = new ArrayList();

            System.out.println(element.getOgrn() + " " + 100 * element.getRowIndex() / companies.size() + "%");

            Matcher matcherClasses = Pattern.compile(element.getOgrn()).matcher(page.asXml());

            HtmlTable table = page.getFirstByXPath("//*[@id=\"report-table\"]");

            while (matcherClasses.find()) {

                stringCem.append(matcherClasses.group());
                countOfReference++;

                if (countOfReference > 1) {
                    resultCemType.add(table.asNormalizedText());
                }
            }
            resultCem.add(countOfReference - 1);
        }
        modifyMatchesInTable();

        return resultCem;
    }

    public static ArrayList modifyMatchesInTable() {

        String delete = "\"Реестр\\tРаздел\\tНомер\\tРегион\\tОрганизация\\tРеквизиты\\tАдрес\\tНомер приказа о включении\\tДата приказа о включении\"";

        if (nonNull(resultCemType) && resultCemType.contains(delete)) {
            resultCemType.remove(delete);
        }
        return resultCemType;
    }
}
