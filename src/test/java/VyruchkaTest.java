import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VyruchkaTest {

    @Test
    public void vyruchkaMatcher() {

        String name = "АКЦИОНЕРНОЕ ОБЩЕСТВО \"123 АВИАЦИОННЫЙ РЕМОНТНЫЙ ЗАВОД\"";

        String nameWithPlus = name
                .replace(" ", "+")
                .replace("\"", "")
                .replace("/", "")
                .replace(">>", "")
                .replace("<<", "");


        String urlVyruchka = "https://zakupki.gov.ru/epz/revenue/search/results.html?searchString=" +
                nameWithPlus +
                "%22&morphology=on&search-filter=Дате+размещения&irrelevantInformation=on&sec_1=on&sec_2=" +
                "on&sec_3=on&reportingPeriodYearStartHidden=0&reportingPeriodQuarterStartHidden=DEFAULT&" +
                "reportingPeriodYearEndHidden=0&reportingPeriodQuarterEndHidden=DEFAULT&sortBy=REESTR_NAME" +
                "&pageNumber=1&sortDirection=true&recordsPerPage=_10&showLotsInfoHidden=false";

        Document vyruchka = null;

        try {
            vyruchka = Jsoup.connect(urlVyruchka)
                    .timeout(0)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder statusStringBuildVyruchka = new StringBuilder();

        Matcher matcherClassesVyruchka = Pattern.compile("2022 год").matcher(vyruchka.toString());

        int countOfReferenceVyruchka = 0;
        while (matcherClassesVyruchka.find()) {
            statusStringBuildVyruchka.append(matcherClassesVyruchka.group());
            countOfReferenceVyruchka++;
        }

        Assert.assertEquals(1, countOfReferenceVyruchka);
    }
}
