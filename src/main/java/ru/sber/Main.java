package ru.sber;

import ru.sber.converter.CompanyList;
import ru.sber.converter.RegistryUrl;
import ru.sber.dto.UserInputs;
import ru.sber.exporter.WriteExcel;
import ru.sber.parser.CustomerRegistration;
import ru.sber.parser.CustomerTypeOfCompany;
import ru.sber.parser.OrganizationRegistration;
import ru.sber.parser.RevenueRegistration;
import ru.sber.parser.SnmMatcher;

import java.util.Scanner;

/**
 * Main class to start and make inputs for users.
 */
public class Main {

    private static String input;
    private static String output;

    /**
     * Make input, load, write output.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите путь к excel-файлу .xlsx (или .xls) (C:\\INPUT.xlsx):");
        input = scanner.next();

        System.out.println("Введите путь к выводимому excel-файлу .xlsx (или .xls) (C:\\OUTPUT.xlsx):");
        output = scanner.next();

        matcherProcess();
    }

    /**
     * Load reader, matchers and writer.
     */
    static void matcherProcess() {

        UserInputs userInputs = new UserInputs(input, output);

        CompanyList.companyNameList(userInputs.getInput());
        CompanyList.companyPsrnList(userInputs.getInput());

        RegistryUrl.companyCustomerUrlList();
        RegistryUrl.companyOrganizationUrlList();
        RegistryUrl.companyRevenueUrlList();

        SnmMatcher.makeInputInSnmRegistry(userInputs.getInput());
        CustomerRegistration.findMatches();
        RevenueRegistration.findMatches(userInputs.getInput());
        CustomerTypeOfCompany.findMatches(userInputs.getInput());
        OrganizationRegistration.findMatches(userInputs.getInput());

        new WriteExcel(userInputs.getInput(), userInputs.getOutput());
    }
}
