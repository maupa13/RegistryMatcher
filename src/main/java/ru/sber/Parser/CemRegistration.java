//package ru.sber.Parser;
//
//import com.gargoylesoftware.htmlunit.BrowserVersion;
//import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
//import com.gargoylesoftware.htmlunit.WebClient;
//import com.gargoylesoftware.htmlunit.html.HtmlButton;
//import com.gargoylesoftware.htmlunit.html.HtmlInput;
//import com.gargoylesoftware.htmlunit.html.HtmlPage;
//import com.gargoylesoftware.htmlunit.html.HtmlTable;
//import org.apache.commons.io.FileUtils;
//import ru.sber.DTO.Company;
//
//import javax.xml.xpath.XPath;
//import javax.xml.xpath.XPathFactory;
//import java.io.File;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import static com.poiji.bind.Poiji.*;
//
//public class CemRegistration {
//
////    public static ArrayList resultCem = new ArrayList();
//
//    public static ArrayList matcherCemRegistration(String input) {
//
//        System.out.println("STARTED MATCHING - CEM");
//
//        XPath xPath =  XPathFactory.newInstance().newXPath();
//
//        String cemResultTable = null;
//
//        File file = new File("C:\\Temp\\cemTemp.xml");
//
//        try {
//            cemResultTable = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        var statusStringBuildCem = new StringBuilder();
//
//        if (cemResultTable != null) {
//
//            List<Company> companies = fromExcel(new File(input), Company.class);
//
//            for (Company element : companies) {
//
////                Document document = Jsoup.parse(cemResultTable, "",  Parser.xmlParser());
////                Elements elementFromTable = document.select("tr");
//
//                int countOfReference = 0;
//
//                ArrayList indexList = new ArrayList();
//
//                System.out.println(element.getOgrn() + " " + 100 * element.getRowIndex() / companies.size() + "%");
//
//                Matcher matcherClasses = Pattern.compile(element.getOgrn()).matcher(cemResultTable);
//                while (matcherClasses.find()) {
//                    statusStringBuildCem.append(matcherClasses.group());
//                    countOfReference++;
//                }
////                resultCem.add(countOfReference);
//
////                indexList.add(cemResultTable.indexOf(element.getOgrn()));
////
////                System.out.println(indexList.toString());
//            }
//        }
//        return resultCem;
//    }
//}
