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

public class OrganizaciiRegistration {

    public static ArrayList resultOrganizacii = new ArrayList();

    public static ArrayList findMatches(String input) {

        SetIp setIp = new SetIp();

        List<Company> companies = Poiji.fromExcel(new File(input), Company.class);

        String userAgent = "Mozilla/5.1 (X11; U; Linux i586; en-US; rv:1.7.7) Gecko/20040924 Epiphany/1.4.4 (Ubuntu)";

        System.out.println("Starting Organizacii");

        for (Company element : companies) {

            setIp.setVariantIp(element.getRowIndex());

            String urlOrganizacii = "https://zakupki.gov.ru/epz/organization/search/results.html?searchString=" +
                    element.getOgrn() +
                    "&morphology=on&search-filter=%D0%94%D0%B0%D1%82%D0%B5+" +
                    "%D1%80%D0%B0%D0%B7%D0%BC%D0%B5%D1%89%D0%B5%D0%BD%D0%B8%D1%8F&fz94" +
                    "=on&fz223=on&F=on&S=on&M=on&NOT_FSM=on&registered94=on&notRegistered=on&sortBy=" +
                    "NAME&pageNumber=1&sortDirection=false&recordsPerPage=_10&showLotsInfoHidden=false";

            Document organizacii = null;

            try {
                organizacii = Jsoup.connect(urlOrganizacii)
                        .userAgent(userAgent)
                        .timeout(0)
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            assert organizacii != null;

            Elements organizaciiStatus = organizacii.getElementsByClass("registry-entry__header-top__title");
            StringBuilder statusStringBuild = new StringBuilder();
            Matcher matcherClasses = Pattern.compile("registry-entry__header-top__title").matcher(organizaciiStatus.toString());

            int countOfReference = 0;
            while (matcherClasses.find()) {
                statusStringBuild.append(matcherClasses.group());
                statusStringBuild.append("registry-entry__header-top__title");
                countOfReference++;
            }

            //do correct booleans with organizations, which blocked with 44-fz, but not with 223-fz
            boolean all = organizaciiStatus.toString().contains("44-ФЗ") && organizaciiStatus.toString().contains("223-ФЗ");
            boolean only44 = organizaciiStatus.toString().contains("44-ФЗ") && !(organizaciiStatus.toString().contains("223-ФЗ"));
            boolean only223 = !(organizaciiStatus.toString().contains("44-ФЗ")) && organizaciiStatus.toString().contains("223-ФЗ");
            boolean blocked44 = organizaciiStatus.toString().contains("44-ФЗ Заблокирована");
            boolean blocked223 = organizaciiStatus.toString().contains("223-ФЗ Заблокирована");

            String blocked223Info = "Запись о блокировке: 223-ФЗ. Рекомендуется проверить организацию вручную";
            String blocked44Info = "Запись о блокировке: 44-ФЗ. Рекомендуется проверить организацию вручную";
            String registered44And223Info = "Зарегистрирован: 223-ФЗ и 44-ФЗ. Записи о дочерних организациях отсутствуют";
            String registered223Info = "Зарегистрирован: 223-ФЗ. Записи о дочерних организациях отсутствуют";
            String registered44Info = "Зарегистрирован: 44-ФЗ. Записи о дочерних организациях отсутствуют";
            String registered44And223MotherInfo = "Материнская компания зарегистрирована: 223-ФЗ и 44-ФЗ";
            String registered223MotherInfo = "Зарегистрирован: 223-ФЗ. Записи о дочерних организациях в аналогичном реестре";
            String registered44MotherInfo = "Зарегистрирован: 44-ФЗ. Записи о дочерних организациях в аналогичном реестре";
            String notRegisteredOrganizaciiInfo = "Не зарегистрирован в реестре организаций";

            if (blocked223) {
                resultOrganizacii.add(blocked223Info);
            }

            if (blocked44) {
                resultOrganizacii.add(blocked44Info);
            }

            if (countOfReference == 1) {
                if (all) {
                    resultOrganizacii.add(registered44And223Info);
                } else if (only223) {
                    resultOrganizacii.add(registered223Info);
                } else if (only44) {
                    resultOrganizacii.add(registered44Info);
                }
            } else if (countOfReference > 0) {
                if (all) {
                    resultOrganizacii.add(registered44And223MotherInfo);
                } else if (only223) {
                    resultOrganizacii.add(registered223MotherInfo);
                } else if (only44) {
                    resultOrganizacii.add(registered44MotherInfo);
                }
            } else {
                resultOrganizacii.add(notRegisteredOrganizaciiInfo);
            }
        }
        return resultOrganizacii;
    }
}
