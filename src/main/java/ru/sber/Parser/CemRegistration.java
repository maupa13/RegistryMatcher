package ru.sber.Parser;

import com.poiji.bind.Poiji;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;
import ru.sber.DTO.Company;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CemRegistration {

    public static ArrayList resultCem = new ArrayList();

    public static ArrayList matcherCemRegistration(String input) {

        XPath xPath =  XPathFactory.newInstance().newXPath();

        String cemResultTable = null;

        try {
            cemResultTable = FileUtils.readFileToString(new File("C:\\Temp\\cemTemp.html"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        var statusStringBuildVyruchka = new StringBuilder();

        if (cemResultTable != null) {
            List<Company> companies = Poiji.fromExcel(new File(input), Company.class);

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
