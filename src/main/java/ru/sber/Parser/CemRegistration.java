package ru.sber.Parser;

import org.apache.commons.io.FileUtils;
import ru.sber.DTO.Company;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.poiji.bind.Poiji.*;

public class CemRegistration {

    public static ArrayList resultCem = new ArrayList();

    public static ArrayList matcherCemRegistration(String input) {

        System.out.println("Started check - CEM 15%");

        XPath xPath =  XPathFactory.newInstance().newXPath();

        String cemResultTable = null;

        File file = new File("C:\\Temp\\cemTemp.xml");

        try {
            cemResultTable = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        var statusStringBuildVyruchka = new StringBuilder();

        if (cemResultTable != null) {

            List<Company> companies = fromExcel(new File(input), Company.class);

            for (Company element : companies) {

//                Document document = Jsoup.parse(cemResultTable, "",  Parser.xmlParser());
//                Elements elementFromTable = document.select("tr");

                int countOfReference = 0;

                Matcher matcherClasses = Pattern.compile(element.getOgrn()).matcher(cemResultTable);
                while (matcherClasses.find()) {
                    statusStringBuildVyruchka.append(matcherClasses.group());
                    countOfReference++;
//                    System.out.println("Number of Rows are : "+ elementFromTable.select(element.getOgrn()));
                }
                resultCem.add(countOfReference);
            }
        }
        return resultCem;
    }
}
