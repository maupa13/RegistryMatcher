import org.apache.commons.io.FileUtils;
import org.junit.Test;
import ru.sber.dto.Company;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.poiji.bind.Poiji.fromExcel;

public class CemRegistration {

    @Test
    public void matcherCemRegistration(String input) {

        System.out.println("STARTED MATCHING - CEM");

        XPath xPath = XPathFactory.newInstance().newXPath();

        String cemResultTable = null;

        File file = new File("C:\\Temp\\cemTemp.xml");

        try {
            cemResultTable = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        var statusStringBuildCem = new StringBuilder();

        if (cemResultTable != null) {

            List<Company> companies = fromExcel(new File(input), Company.class);

            for (Company element : companies) {

                int countOfReference = 0;

                ArrayList indexList = new ArrayList();

                System.out.println(element.getOgrn() + " " + 100 * element.getRowIndex() / companies.size() + "%");

                Matcher matcherClasses = Pattern.compile(element.getOgrn()).matcher(cemResultTable);
                while (matcherClasses.find()) {
                    statusStringBuildCem.append(matcherClasses.group());
                    countOfReference++;
                }

                indexList.add(cemResultTable.indexOf(element.getOgrn()));

                System.out.println(indexList);
            }
        }
    }
}
