package org.example;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to StringCalculator 1.0!");
        boolean toCont = true;
        while (toCont) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter you expression:");
            Calculator calculator = new Calculator(scanner.nextLine());
            scanner.reset();
            System.out.println(calculator.act());
            System.out.println("Do you wish to continue (y/n)?");
            if (!scanner.nextLine().equals("y")) {
                toCont = false;
            }
        }
        System.out.println("Thank you for using program");
    }
}