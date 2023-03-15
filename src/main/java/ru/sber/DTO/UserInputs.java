package ru.sber.DTO;

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

//    public void setCounter(int counter) {
//        this.counter = counter;
//    }
//
//    public int getCounter(int counter) {
//        return counter;
//    }
}
