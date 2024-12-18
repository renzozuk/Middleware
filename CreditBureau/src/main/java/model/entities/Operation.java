package model.entities;

import exceptions.InvalidOperatorException;

public class Operation {

    private double firstNumber;
    private String operator;
    private double secondNumber;

    public Operation() {
    }

    public Operation(double firstNumber, String operator, double secondNumber) {
        this.firstNumber = firstNumber;
        this.operator = operator;
        this.secondNumber = secondNumber;
    }

    public double getFirstNumber() {
        return firstNumber;
    }

    public void setFirstNumber(double firstNumber) {
        this.firstNumber = firstNumber;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public double getSecondNumber() {
        return secondNumber;
    }

    public void setSecondNumber(double secondNumber) {
        this.secondNumber = secondNumber;
    }

    public double calculate() {
        return switch (operator) {
            case "+" -> firstNumber + secondNumber;
            case "-" -> firstNumber - secondNumber;
            case "*" -> firstNumber * secondNumber;
            case "/" -> firstNumber / secondNumber;
            case "+=" -> firstNumber += secondNumber;
            case "-=" -> firstNumber -= secondNumber;
            case "*=" -> firstNumber *= secondNumber;
            case "/=" -> firstNumber /= secondNumber;
            case "^" -> Math.pow(firstNumber, secondNumber);
            case "root" -> Math.pow(secondNumber, 1.0 / firstNumber);
            default -> throw new InvalidOperatorException(operator);
        };
    }
}
