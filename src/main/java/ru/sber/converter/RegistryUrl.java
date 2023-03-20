package ru.sber.converter;

import static ru.sber.converter.CompanyList.companyName;
import static ru.sber.converter.CompanyList.companyPsrn;

import ru.sber.dto.Company;

import java.util.ArrayList;
import java.util.List;

/**
 * Convert excel data to registry url.
 */
public class RegistryUrl {

    public static ArrayList urlCustomerList = new ArrayList();
    public static ArrayList urlOrganizationList = new ArrayList();
    public static ArrayList urlRevenueList = new ArrayList();

    /**
     * Change url to customer registry.
     *
     * @return customer url registry array list.
     */
    public static List<Company> companyCustomerUrlList() {

        for (Object code : companyPsrn) {
            String urlCustomer = "https://zakupki.gov.ru/epz/customer223/search/results.html?searchString="
                    + code
                    + "&morphology=on&search-filter=%D0%94%D0%B0%D1%82%D0%B5+"
                    + "%D1%80%D0%B0%D0%B7%D0%BC%D0%B5%D1%89%D0%B5%D0%BD%D0%B8%D1%8F&pageNumber=1&"
                    + "sortDirection=false&recordsPerPage=_10&showLotsInfoHidden=false&sortBy="
                    + "NAME&customer223Status_0=on&customer223Status=0&organizationRoleValueIdNameHidden=%7B%7D";

            urlCustomerList.add(urlCustomer);
        }
        return urlCustomerList;
    }

    /**
     * Change url to organization registry.
     *
     * @return organization url registry array list.
     */
    public static List<Company> companyOrganizationUrlList() {

        for (Object code : companyPsrn) {
            String urlOrganization = "https://zakupki.gov.ru/epz/organization/search/results.html?searchString="
                    + code
                    + "&morphology=on&search-filter=%D0%94%D0%B0%D1%82%D0%B5+"
                    + "%D1%80%D0%B0%D0%B7%D0%BC%D0%B5%D1%89%D0%B5%D0%BD%D0%B8%D1%8F&fz94"
                    + "=on&fz223=on&F=on&S=on&M=on&NOT_FSM=on&registered94=on&notRegistered=on&sortBy="
                    + "NAME&pageNumber=1&sortDirection=false&recordsPerPage=_10&showLotsInfoHidden=false";

            urlOrganizationList.add(urlOrganization);
        }
        return urlOrganizationList;
    }

    /**
     * Change url to revenue registry.
     *
     * @return revenue url registry array list.
     */
    public static List<Company> companyRevenueUrlList() {

        for (Object name : companyName) {
            String nameChanged = String.valueOf(name)
                    .replace(" ", "+")
                    .replace("\"", "")
                    .replace("/", "")
                    .replace(">", "")
                    .replace("?", "")
                    .replace(",", "")
                    .replace("<", "");


            String urlRevenue = "https://zakupki.gov.ru/epz/revenue/search/results.html?searchString="
                    + nameChanged
                    + "%22&morphology=on&search-filter=Дате+размещения&irrelevantInformation=on&sec_1=on&sec_2="
                    + "on&sec_3=on&reportingPeriodYearStartHidden=0&reportingPeriodQuarterStartHidden=DEFAULT&"
                    + "reportingPeriodYearEndHidden=0&reportingPeriodQuarterEndHidden=DEFAULT&sortBy=REESTR_NAME"
                    + "&pageNumber=1&sortDirection=true&recordsPerPage=_10&showLotsInfoHidden=false";

            urlRevenueList.add(urlRevenue);
        }

        return urlRevenueList;
    }
}
