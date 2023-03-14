package ru.sber;

import ru.sber.DTO.UserInputs;
import ru.sber.Exporter.WriteOutputExcelFile;
import ru.sber.Importer.DownloadCem;
import ru.sber.Parser.*;

import java.util.Scanner;

public class Main {

    static String input;
    static String output;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите путь к excel-файлу .xlsx (или .xls) (C:\\INPUTSHORT.xlsx):");
        input = scanner.next();

        System.out.println("Введите путь к выводимому excel-файлу .xlsx (или .xls) (C:\\OUTPUT.xlsx):");
        output = scanner.next();

        matcherProcess();
    }

    public static void matcherProcess() {

        UserInputs userInputs = new UserInputs(input, output);

        CemMatcher.makeInputInCemReestr(input);
//
////        DownloadCem.downloadCemTable();
//
////        CemRegistration.matcherCemRegistration(userInputs.getInput());
        ZakazchikiRegistration.findMatchesZakazchiki(userInputs.getInput());
        VyruchkaRegistration.findMatches(userInputs.getInput());
        ZakachikiTypeOfCompanyRegistration.findMatchesType(userInputs.getInput());
        OrganizaciiRegistration.findMatches(userInputs.getInput());

        new WriteOutputExcelFile(input, output);
    }
}
