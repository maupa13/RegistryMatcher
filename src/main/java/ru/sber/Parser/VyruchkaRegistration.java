package ru.sber.parser;

import static com.poiji.bind.Poiji.fromExcel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.sber.connection.SetIp;
import ru.sber.dto.Company;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Check company in zakupki.gov.ru registry by vyruchka.
 */
public class VyruchkaRegistration {

    static String emptyVyruchka = "Отсутствуют сведения о выручке за 2022 г.";
    static String filledVyruchka = "Cведения о выручке за 2022 г. размещены";
    static String problemVyruchka = "Отсутствуют сведения либо "
            + "размещена квартальная выручка за 2022 г.";

    public static ArrayList resultVyruchka = new ArrayList();

    /**
     * Find company according to vyruchka.
     *
     * @param input path to Excel file.
     * @return Array list with registration results.
     */
    public static ArrayList findMatches(String input) {

        System.out.println("STARTED_VYRUCHKA");

        SetIp setIp = new SetIp();

        boolean success = false;

        Document vyruchka = null;

        String userAgent = "Mozilla/5.0 (X11; U; Linux i586; en-US; rv:1.7.3)"
                + " Gecko/20040924 Epiphany/1.4.4 (Ubuntu)";

        List<Company> companies = fromExcel(new File(input), Company.class);

        int i = 0;

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

            String urlVyruchka = "https://zakupki.gov.ru/epz/revenue/search/results.html?searchString="
                    + nameChanded
                    + "%22&morphology=on&search-filter=Дате+размещения&irrelevantInformation=on&sec_1=on&sec_2="
                    + "on&sec_3=on&reportingPeriodYearStartHidden=0&reportingPeriodQuarterStartHidden=DEFAULT&"
                    + "reportingPeriodYearEndHidden=0&reportingPeriodQuarterEndHidden=DEFAULT&sortBy=REESTR_NAME"
                    + "&pageNumber=1&sortDirection=true&recordsPerPage=_10&showLotsInfoHidden=false";

            while (i < 3) {
                try {
                    vyruchka = Jsoup.connect(urlVyruchka)
                            .userAgent(userAgent)
                            .timeout(0)
                            .ignoreHttpErrors(true)
                            .get();

                    success = true;
                    break;
                } catch (SocketTimeoutException ex) {
                    System.out.println("jsoup Timeout occurred " + i + " time(s)");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                i++;
            }

            if (success) {

                System.out.println(element.getName() + " " + 100 * element.getRowIndex() / companies.size() + "%");

                var statusStringBuildVyruchka = new StringBuilder();
                assert vyruchka != null;

                Matcher matcherClassesVyruchkaInfoYear = Pattern.compile("2022 год")
                        .matcher(vyruchka.toString());
                Matcher matcherClassesVyruchkaInfoName = Pattern.compile(element.getName())
                        .matcher(vyruchka.toString());

                int countOfReferenceVyruchka = 0;
                while (matcherClassesVyruchkaInfoYear.find() && matcherClassesVyruchkaInfoName.find()) {
                    statusStringBuildVyruchka.append(matcherClassesVyruchkaInfoYear.group());
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
        }
        return resultVyruchka;
    }
}
