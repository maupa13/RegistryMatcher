package ru.sber.converter;

import static com.poiji.bind.Poiji.fromExcel;

import ru.sber.dto.Company;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Convert Excel to list.
 */
public class CompanyList {

    public static ArrayList companyName = new ArrayList();
    public static ArrayList companyPsrn = new ArrayList();

    /**
     * Convert from Excel to name list.
     *
     * @return name array list.
     */
    public static List<Company> companyNameList(String input) {

        List<Company> companyNameList = fromExcel(new File(input), Company.class);

        for (Company element : companyNameList) {
            companyName.add(element.getName());
        }
        return companyName;
    }

    /**
     * Convert from Excel to psrn list.
     *
     * @return psrn array list.
     */
    public static List<Company> companyPsrnList(String input) {

        List<Company> companyPsrnList = fromExcel(new File(input), Company.class);

        for (Company element : companyPsrnList) {
            companyPsrn.add(element.getPsrn());
        }

        return companyPsrn;
    }
}

