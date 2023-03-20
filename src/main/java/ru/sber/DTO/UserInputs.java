package ru.sber.dto;

/**
 * Data from user.
 */
public class UserInputs {
    private static String input;
    private static String output;

    public UserInputs(String input, String output) {
        this.input = input;
        this.output = output;
    }

    public static String getInput() {
        return input;
    }

    public static String getOutput() {
        return output;
    }

}
