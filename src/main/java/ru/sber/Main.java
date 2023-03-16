package ru.sber;

import ru.sber.dto.UserInputs;
import ru.sber.exporter.WriteOutputExcelFile;
import ru.sber.parser.CemMatcher;
import ru.sber.parser.OrganizaciiRegistration;
import ru.sber.parser.VyruchkaRegistration;
import ru.sber.parser.ZakachikiTypeOfCompanyRegistration;
import ru.sber.parser.ZakazchikiRegistration;

import java.util.Scanner;

/**
 * Main class to start and make inputs for users.
 */
public class Main {

    static String input;
    static String output;

    /**
     * Make inputs and load all classes.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        //C:\Users\18432282\Downloads\TABLES\INPUTSHORT.xls
        System.out.println("Введите путь к excel-файлу .xlsx (или .xls) (C:\\INPUT.xlsx):");
        input = scanner.next();

        System.out.println("Введите путь к выводимому excel-файлу .xlsx (или .xls) (C:\\OUTPUT.xlsx):");
        output = scanner.next();

        matcherProcess();
    }

    /**
     * Load reader, matchers and writer.
     */
    public static void matcherProcess() {

        UserInputs userInputs = new UserInputs(input, output);

        CemMatcher.makeInputInCemReestr(input);
        ZakazchikiRegistration.findMatchesZakazchiki(userInputs.getInput());
        VyruchkaRegistration.findMatches(userInputs.getInput());
        ZakachikiTypeOfCompanyRegistration.findMatchesType(userInputs.getInput());
        OrganizaciiRegistration.findMatches(userInputs.getInput());

        new WriteOutputExcelFile(input, output);
    }
}
