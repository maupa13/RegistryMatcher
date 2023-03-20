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
 * Find type of company in zakupki.gov.ru registry.
 */
public class CustomerTypeOfCompany {

    public static ArrayList resultCustomerType = new ArrayList();

    /**
     * Find company according to type of it.
     *
     * @param input path to Excel file.
     * @return Array list with type of company results.
     */
    public static ArrayList findMatches(String input) {
        SetIp setIp = new SetIp();
        Random r = new Random();

        System.out.println("\nSTARTED_CUSTOMER_TYPE_OF_COMPANY");

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
                    CustomerRegistration.errorList.add(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                i++;
            }

            if (success) {
                System.out.println("Customer type: " + 100 * urlCustomerList.indexOf(url)
                        / urlCustomerList.size() + "%");

                assert customer != null;

                Elements customerStatus = customer.getElementsByClass("d-flex lots-wrap-content__body__val");
                StringBuilder statusStringBuildCustomer = new StringBuilder();

                Matcher matcherClassesCustomer = Pattern.compile("d-flex lots-wrap-content__body__val")
                        .matcher(customerStatus.toString());

                int countOfReferenceCustomer = 0;
                while (matcherClassesCustomer.find()) {
                    statusStringBuildCustomer.append(matcherClassesCustomer.group());
                    statusStringBuildCustomer.append("d-flex lots-wrap-content__body__val");
                    countOfReferenceCustomer++;
                }

                String customerStatusWrite = customerStatus.toString()
                        .replace("<", "")
                        .replace(">", "")
                        .replace("/", "")
                        .replace("\"", "")
                        .replace("</div>", "")
                        .replace("div class=d-flex lots-wrap-content__body__val\n"
                                + " Заказчик\n" + "div\n" + "div class=d-flex lots-wrap-content__body__val", "")
                        .replace("class=d-flexlots-wrap-content__body__val", "")
                        .replace("div", "");

                if (countOfReferenceCustomer == 2) {
                    resultCustomerType.add(customerStatusWrite);
                } else if (countOfReferenceCustomer > 2) {
                    resultCustomerType.add(Result.massRegisteredType);
                } else {
                    resultCustomerType.add(Result.notRegisteredType);
                }
            }
        }
        return resultCustomerType;
    }
}
