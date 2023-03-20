package ru.sber.parser;

import static ru.sber.converter.RegistryUrl.urlOrganizationList;
import static ru.sber.parser.CustomerRegistration.errorList;

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
 * Check company in zakupki.gov.ru for customer registry.
 */
public class OrganizationRegistration {

    public static ArrayList resultOrganization = new ArrayList();

    /**
     * Check company in customer registry.
     *
     * @param input path to Excel file.
     * @return Array list with registration results.
     */
    public static ArrayList findMatches(String input) {
        SetIp setIp = new SetIp();
        Random r = new Random();

        System.out.println("\nSTARTED_ORGANIZATION");

        boolean success = false;

        for (Object url : urlOrganizationList) {
            setIp.setOptionIp(r.nextInt());

            Document organization = null;

            int i = 0;

            while (i < 3) {
                try {
                    organization = Jsoup.connect(String.valueOf(url))
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
                System.out.println("Organization: " + 100 * urlOrganizationList.indexOf(url)
                        / urlOrganizationList.size() + "%");

                StringBuilder statusStringBuildOrganization = new StringBuilder();

                assert organization != null;

                Elements organizationStatus = organization
                        .getElementsByClass("registry-entry__header-top__title");


                Matcher matcherClasses = Pattern
                        .compile("registry-entry__header-top__title")
                        .matcher(organizationStatus.toString());

                int countOfReferenceOrganization = 0;
                while (matcherClasses.find()) {
                    statusStringBuildOrganization.append(matcherClasses.group());
                    statusStringBuildOrganization.append("registry-entry__header-top__title");
                    countOfReferenceOrganization++;
                }

                //do correct booleans with organizations, which blocked with 44-fz, but not with 223-fz
                boolean all = organizationStatus.toString()
                        .contains("44-ФЗ") && organizationStatus.toString()
                        .contains("223-ФЗ");
                boolean only44 = organizationStatus.toString().contains("44-ФЗ")
                        && !(organizationStatus.toString()
                        .contains("223-ФЗ"));
                boolean only223 = !(organizationStatus.toString()
                        .contains("44-ФЗ")) && organizationStatus.toString().contains("223-ФЗ");
                boolean blocked44 = organizationStatus.toString()
                        .contains("44-ФЗ Заблокирована");
                boolean blocked223 = organizationStatus.toString()
                        .contains("223-ФЗ Заблокирована");

                if (blocked223) {
                    resultOrganization.add(Result.blocked223Info);
                }

                if (blocked44) {
                    resultOrganization.add(Result.blocked44Info);
                }

                if (countOfReferenceOrganization == 1) {
                    if (all) {
                        resultOrganization.add(Result.registered44And223Info);
                    } else if (only223) {
                        resultOrganization.add(Result.registered223Info);
                    } else if (only44) {
                        resultOrganization.add(Result.registered44Info);
                    }
                } else if (countOfReferenceOrganization > 0) {
                    if (all) {
                        resultOrganization.add(Result.registered44And223MotherInfo);
                    } else if (only223) {
                        resultOrganization.add(Result.registered223MotherInfo);
                    } else if (only44) {
                        resultOrganization.add(Result.registered44MotherInfo);
                    }
                } else {
                    resultOrganization.add(Result.notRegisteredOrganizaciiInfo);
                }
            }
        }
        return resultOrganization;
    }
}
