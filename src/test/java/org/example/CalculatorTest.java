package org.example;

import org.junit.jupiter.api.Test;

import java.util.Random;

public class CalculatorTest {

    @Test
    public void whenTryToWorkWithCalculatorThenShowResult() {
        String expression = strRandomizer(5);
        System.out.println(expression);
//        Calculator test = new Calculator(expression);
        Calculator test = new Calculator("?J(M*7FCOB+O:B/54*H8E)OHL");
        System.out.println(test.act());
    }

    public String strRandomizer(int value) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value * 5; i++) {
            sb.append((char) (random.nextInt(40) + 40));
        }
        return sb.toString();
    }
}
