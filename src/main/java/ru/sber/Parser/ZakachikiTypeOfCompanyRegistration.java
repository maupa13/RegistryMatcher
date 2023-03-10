package ru.sber.Parser;

import com.poiji.bind.Poiji;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import ru.sber.Connection.SetIp;
import ru.sber.DTO.Company;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZakachikiTypeOfCompanyRegistration {

    public static ArrayList resultZakazchikiType = new ArrayList();

    public static ArrayList findMatchesType(String input) {

        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36";

        List<Company> companies = Poiji.fromExcel(new File(input), Company.class);

        System.out.println("Starting Zakazchiki");

        SetIp setIp = new SetIp();

        for (Company element : companies) {

            setIp.setVariantIp(element.getRowIndex());

            String urlZakachicki = "https://zakupki.gov.ru/epz/customer223/search/results.html?searchString=" +
                    element.getOgrn() +
                    "&morphology=on&search-filter=%D0%94%D0%B0%D1%82%D0%B5+" +
                    "%D1%80%D0%B0%D0%B7%D0%BC%D0%B5%D1%89%D0%B5%D0%BD%D0%B8%D1%8F&pageNumber=1&" +
                    "sortDirection=false&recordsPerPage=_10&showLotsInfoHidden=false&sortBy=" +
                    "NAME&customer223Status_0=on&customer223Status=0&organizationRoleValueIdNameHidden=%7B%7D";

            Document zakazchiki = null;
            try {
                zakazchiki = Jsoup.connect(urlZakachicki)
                        .userAgent(userAgent)
                        .timeout(0)
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            assert zakazchiki != null;

            Elements zakazchikiStatus = zakazchiki.getElementsByClass("d-flex lots-wrap-content__body__val");
            StringBuilder statusStringBuildZakachiki = new StringBuilder();

            Matcher matcherClassesZakachiki = Pattern.compile("d-flex lots-wrap-content__body__val").matcher(zakazchikiStatus.toString());

            int countOfReferenceZakazchiki = 0;
            while (matcherClassesZakachiki.find()) {
                statusStringBuildZakachiki.append(matcherClassesZakachiki.group());
                statusStringBuildZakachiki.append("d-flex lots-wrap-content__body__val");
                countOfReferenceZakazchiki++;
            }

            String zakazchikiStatusWrite = zakazchikiStatus.toString()
                    .replace("<", "")
                    .replace(">", "")
                    .replace("/", "")
                    .replace("\"", "")
                    .replace("</div>", "")
                    .replace("div class=d-flex lots-wrap-content__body__val\n" +
                            " Заказчик\n" + "div\n" + "div class=d-flex lots-wrap-content__body__val", "")
                    .replace("class=d-flexlots-wrap-content__body__val", "")
                    .replace("div", "");

            String notRegisteredType = "Отсутствуют сведения о типе ЮЛ";
            String massRegisteredType = "Множество записей о типе ЮЛ: записи о дочерних организациях";

            if (countOfReferenceZakazchiki == 1) {
                resultZakazchikiType.add(zakazchikiStatusWrite);
            } else if (countOfReferenceZakazchiki > 1) {
                resultZakazchikiType.add(massRegisteredType);
            } else {
                resultZakazchikiType.add(notRegisteredType);
            }
        }
        return resultZakazchikiType;
    }
}
