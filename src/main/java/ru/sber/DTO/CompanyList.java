package ru.sber.dto;

import com.poiji.bind.Poiji;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Make list of companies.
 */
public class CompanyList {

    static String input;

    /**
     * Return list of companies.
     *
     * @return company array list.
     */
    public List<Company> companyList() {

        List<Company> companyList = (ArrayList<Company>) Poiji.fromExcel(new File(input), Company.class);

        return companyList;
    }
}

