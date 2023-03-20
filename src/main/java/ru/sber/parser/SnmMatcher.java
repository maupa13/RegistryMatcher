package ru.sber.parser;

import static com.poiji.bind.Poiji.fromExcel;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import ru.sber.connection.SetIp;
import ru.sber.dto.Company;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Check company in fas.gov.ru snm registry.
 */
public class SnmMatcher {

    public static ArrayList resultSnm = new ArrayList();
    public static ArrayList resultSnmType = new ArrayList();

    /**
     * Make input of cell in fas.gov.ru snm and find matches, info.
     *
     * @param input path to Excel file.
     * @return Array list with registration results.
     */
    public static ArrayList makeInputInSnmRegistry(String input) {

        System.out.println("\nSTARTED_SNM");

        SetIp setIp = new SetIp();

        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit")
                .setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http")
                .setLevel(java.util.logging.Level.OFF);

        String urlSnm = "http://apps.eias.fas.gov.ru/findcem/";

        WebClient webClient = new WebClient(BrowserVersion.CHROME);

        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getCookieManager().setCookiesEnabled(true);

        HtmlPage revenue = null;

        try {
            revenue = webClient.getPage(urlSnm);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert revenue != null;

        HtmlInput inputBox = (HtmlInput) revenue.getElementById("oGRN");

        List<Company> companies = fromExcel(new File(input), Company.class);

        var stringCem = new StringBuilder();

        for (Company element : companies) {

            setIp.setOptionIp(element.getRowIndex());

            inputBox.setValueAttribute(element.getPsrn());

            try {
                HtmlButton htmlButtonSearch = revenue
                        .getFirstByXPath("/html/body/div/div[2]/div[2]/button");
                htmlButtonSearch.click();
            } catch (IOException e) {
                e.printStackTrace();
            }

            webClient.waitForBackgroundJavaScript(10000);

            int countOfReference = 0;

            System.out.println("SNM: " + element.getPsrn() + " " + 100
                    * element.getRowIndex() / companies.size() + "%");

            Matcher matcherClassesSnm = Pattern.compile(element.getPsrn())
                    .matcher(revenue.asXml());

            HtmlTable table = revenue.getFirstByXPath("//*[@id=\"report-table\"]");

            while (matcherClassesSnm.find()) {

                stringCem.append(matcherClassesSnm.group());
                countOfReference++;

                if (countOfReference > 1) {
                    resultSnmType.add(table.asNormalizedText());
                }
            }
            resultSnm.add(countOfReference - 1);
        }
        modifyMatchesInTable(resultSnmType);

        return resultSnm;
    }

    /**
     * Edit results with info from registry.
     *
     * @param resultSnmType original list with info.
     * @return edited array list with info.
     */
    public static ArrayList modifyMatchesInTable(ArrayList resultSnmType) {
        return resultSnmType;
    }
}
