package ru.sber.Parser;

import com.poiji.bind.Poiji;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.sber.Connection.SetIp;
import ru.sber.DTO.Company;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VyruchkaRegistration {

    public static ArrayList resultVyruchka = new ArrayList();

    public static ArrayList findMatches(String input) {

        SetIp setIp = new SetIp();

        String emptyVyruchka = "Отсутствуют сведения о выручке за 2022 г.";
        String filledVyruchka = "Cведения о выручке за 2022 г. размещены";
        String problemVyruchka = "Отсутствуют сведения либо размещена квартальная выручка за 2022 г.";

        System.out.println("Starting Vyruchka");

        Document vyruchka = null;

        String userAgent = "Mozilla/5.0 (X11; U; Linux i586; en-US; rv:1.7.3) Gecko/20040924 Epiphany/1.4.4 (Ubuntu)";

        List<Company> companies = Poiji.fromExcel(new File(input), Company.class);

        for (Company element : companies) {

            setIp.setVariantIp(element.getRowIndex());

            String nameChanded = element.getName()
                    .replace(" ", "+")
                    .replace("\"", "")
                    .replace("/", "")
                    .replace(">", "")
                    .replace("?", "")
                    .replace(",", "")
                    .replace("<", "");

            String urlVyruchka = "https://zakupki.gov.ru/epz/revenue/search/results.html?searchString=" +
                    nameChanded +
                    "%22&morphology=on&search-filter=Дате+размещения&irrelevantInformation=on&sec_1=on&sec_2=" +
                    "on&sec_3=on&reportingPeriodYearStartHidden=0&reportingPeriodQuarterStartHidden=DEFAULT&" +
                    "reportingPeriodYearEndHidden=0&reportingPeriodQuarterEndHidden=DEFAULT&sortBy=REESTR_NAME" +
                    "&pageNumber=1&sortDirection=true&recordsPerPage=_10&showLotsInfoHidden=false";

            try {
                vyruchka = Jsoup.connect(urlVyruchka)
                        .userAgent(userAgent)
                        .timeout(0)
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            var statusStringBuildVyruchka = new StringBuilder();
            assert vyruchka != null;

            Matcher matcherClassesVyruchkaInfo = Pattern.compile("2022 год").matcher(vyruchka.toString());

            int countOfReferenceVyruchka = 0;
            while (matcherClassesVyruchkaInfo.find()) {
                statusStringBuildVyruchka.append(matcherClassesVyruchkaInfo.group());
                countOfReferenceVyruchka++;
            }

            if (countOfReferenceVyruchka == 1) {
                resultVyruchka.add(filledVyruchka);
            } else if (countOfReferenceVyruchka > 1) {
                resultVyruchka.add(problemVyruchka);
            } else {
                resultVyruchka.add(emptyVyruchka);
            }
        }
        return resultVyruchka;
    }
}
