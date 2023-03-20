package ru.sber.parser;

import static ru.sber.converter.RegistryUrl.urlCustomerList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
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
 * Find registration of company in zakupki.gov.ru registry.
 */
public class CustomerRegistration {

    public static ArrayList resultCustomer = new ArrayList();
    public static ArrayList errorList = new ArrayList();

    /**
     * Find company according to match of it.
     *
     * @return Array list with matches results.
     */
    public static ArrayList findMatches() {
        SetIp setIp = new SetIp();
        Random r = new Random();

        System.out.println("\nSTARTED_CUSTOMER_REGISTRATION");

        boolean success = false;

        for (Object url : urlCustomerList) {
            setIp.setOptionIp(r.nextInt());
            Document customer = null;

            int i = 0;

            while (i < 3) {
                try {
                    customer = Jsoup.connect(String.valueOf(url))
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
                System.out.println("Customer: " + 100 * urlCustomerList.indexOf(url) / urlCustomerList.size() + "%");

                assert customer != null;

                Elements customerStatus = customer
                        .getElementsByClass("d-flex lots-wrap-content__body__val");
                StringBuilder statusStringBuildCustomer = new StringBuilder();

                Matcher matcherClassesCustomer = Pattern.compile("d-flex lots-wrap-content__body__val")
                        .matcher(customerStatus.toString());

                int countOfReferenceCustomer = 0;
                while (matcherClassesCustomer.find()) {
                    statusStringBuildCustomer.append(matcherClassesCustomer.group());
                    statusStringBuildCustomer.append("d-flex lots-wrap-content__body__val");
                    countOfReferenceCustomer++;
                }

                if (countOfReferenceCustomer == 2) {
                    resultCustomer.add(Result.registeredCompany);
                } else if (countOfReferenceCustomer > 2) {
                    resultCustomer.add(Result.massRegisteredCompany);
                } else {
                    resultCustomer.add(Result.notRegisteredCompany);
                }
            }
        }
        return resultCustomer;
    }
}
