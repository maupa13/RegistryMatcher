package ru.sber.dto;

/**
 * DTO Inputs from users.
 */
public class UserInputs {
    public String input;
    public String output;
    int counter;

    public UserInputs(String input, String output) {
        this.input = input;
        this.output = output;
    }

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }

}
