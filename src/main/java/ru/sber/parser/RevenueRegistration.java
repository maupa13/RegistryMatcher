package ru.sber.parser;

import static ru.sber.converter.RegistryUrl.urlRevenueList;
import static ru.sber.parser.CustomerRegistration.errorList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.sber.connection.SetIp;
import ru.sber.dto.Result;
import ru.sber.webclient.UserAgent;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.SSLException;

/**
 * Check company in zakupki.gov.ru registry by revenue.
 */
public class RevenueRegistration {

    public static ArrayList resultRevenue = new ArrayList();

    /**
     * Find company according to revenue.
     *
     * @param input path to Excel file.
     * @return Array list with registration results.
     */
    public static ArrayList findMatches(String input) {
        SetIp setIp = new SetIp();
        Random r = new Random();

        System.out.println("\nSTARTED_REVENUE");

        boolean success = false;

        Document revenue = null;

        for (Object url : urlRevenueList) {
            setIp.setOptionIp(r.nextInt());

            int i = 0;

            while (i < 3) {
                try {
                    revenue = Jsoup.connect(String.valueOf(url))
                            .userAgent(String.valueOf(UserAgent.getRandomUserAgent()))
                            .timeout(0)
                            .ignoreHttpErrors(true)
                            .get();

                    success = true;
                    break;
                } catch (SocketTimeoutException | SSLException ex) {
                    System.out.println("Connection Error: jsoup Timeout occurred "
                            + i + " time(s). Error field: " + url);
                    errorList.add(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                i++;
            }

            if (success) {
                System.out.println("Revenue: " + 100 * urlRevenueList.indexOf(url) / urlRevenueList.size() + "%");

                StringBuilder statusStringBuildRevenue = new StringBuilder();

                assert revenue != null;

                Matcher matcherClassesRevenueInfoYear = Pattern
                        .compile("2022 год")
                        .matcher(revenue.toString());

                int countOfReferenceRevenue = 0;

                while (matcherClassesRevenueInfoYear.find()) {
                    statusStringBuildRevenue.append(matcherClassesRevenueInfoYear.group());
                    countOfReferenceRevenue++;
                }

                if (countOfReferenceRevenue == 1) {
                    resultRevenue.add(String.valueOf(Result.filledRevenue));
                } else if (countOfReferenceRevenue > 1) {
                    resultRevenue.add(String.valueOf(Result.problemRevenue));
                } else {
                    resultRevenue.add(String.valueOf(Result.emptyRevenue));
                }
            }
        }
        return resultRevenue;
    }
}
