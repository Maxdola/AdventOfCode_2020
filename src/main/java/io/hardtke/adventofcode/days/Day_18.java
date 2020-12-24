package io.hardtke.adventofcode.days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicLong;

public class Day_18 extends Day {

    private static final char[] importance = new char[]{'+', '*'};

    @Override
    public void calculate() {

        //solveExpression("(2+4*9)*(6+9*8+6)+6");
        //int a = solveExpression();

        //System.out.printf("Value of: %d%n", a);

        AtomicLong sum = new AtomicLong();
        AtomicLong sum2 = new AtomicLong();

        getLines().forEach(l -> {
            long a = solveExpression(new StringBuilder(l).reverse().toString().replaceAll("\\(", "FRAC").replaceAll("\\)", "(").replaceAll("FRAC", ")"));
            sum.addAndGet(a);
            //System.out.printf("%s = %d%n", l, a);

            long b = solveExpressionWithImportance(l);
            sum2.addAndGet(b);
            //System.out.printf("%s = %5d | %5d%n", l, b, bb);
        });

        System.out.printf("Part 1 sum: %d%n", sum.get());
        System.out.printf("Part 2 sum: %d%n", sum2.get());

    }


    private long solveExpression(String expression) {
        expression = expression.replaceAll("\\s+", "");

        long value = 0;

        if (expression.length() > 0) {
            if (expression.charAt(0) == '(') {

                StringBuilder subExpresion = new StringBuilder();
                int bracketScore = 1;
                int index = 1;
                while (bracketScore != 0) {
                    if (expression.charAt(index) == ')') {
                        bracketScore--;
                        if (bracketScore == 0) break;
                    } else if (expression.charAt(index) == '(') bracketScore++;
                    subExpresion.append(expression.charAt(index));
                    index++;
                }

                long subVal = solveExpression(subExpresion.toString());

                if (expression.length() > index + 1) {

                    String subExpression2 = expression.substring(index+2);
                    //System.out.println(expression.substring(index+2));

                    long subVal2 = solveExpression(subExpression2);

                    switch (expression.charAt(index+1)) {
                        case '+' -> value += (subVal + subVal2);
                        case '*' -> value += (subVal * subVal2);
                    }

                } else {
                    //System.out.printf("%s = %d%n", expression, subVal);
                    return subVal;
                }

            } else {
                int subVal = Integer.parseInt(String.valueOf(expression.charAt(0)));

                if (expression.length() > 1) {

                    String subExpression2 = expression.substring(2);
                    //System.out.println(expression.substring(2));

                    long subVal2 = solveExpression(subExpression2);

                    switch (expression.charAt(1)) {
                        case '+' -> value += (subVal + subVal2);
                        case '*' -> value += (subVal * subVal2);
                    }

                } else {
                    //System.out.printf("%s = %d%n", expression, subVal);
                    return subVal;
                }
            }
        }

        //System.out.printf("%s = %d%n", expression, value);
        return value;
    }

    private long solveExpressionWithImportance(String expression) {
        expression = expression.replaceAll("\\s+", "");
        long value = 0;

        //System.out.printf("solving= %s%n", expression);

        boolean onlyNumbers = true;
        for (char c : expression.toCharArray()) {
            if (!Character.isDigit(c)) {
                onlyNumbers = false;
                break;
            }
        }

        if (onlyNumbers) {
            return Long.parseLong(expression);
        }

        if (expression.contains("(")) {
            StringBuilder newExpression = new StringBuilder();
            StringBuilder bracketExpression = new StringBuilder();
            int bracketScore = 0;
            for (char c : expression.toCharArray()) {
                if (bracketScore == 0) {
                    if (c == '(') {
                        bracketScore--;
                    } else {
                        newExpression.append(c);
                    }
                } else {
                    if (c == '(') bracketScore--;
                    if (c == ')') {
                        bracketScore++;
                        if (bracketScore == 0) {
                            newExpression.append(solveExpressionWithImportance(bracketExpression.toString()));
                            bracketExpression.setLength(0);
                            continue;
                        }
                    }
                    bracketExpression.append(c);
                }
            }
            return solveExpressionWithImportance(newExpression.toString());
        }

        for (char operation : importance) {
            int index = expression.indexOf(operation);
            if (index != -1) {
                String subex = "";
                String as = "";
                for (int i = index - 1; i >= 0; i--) {
                    char c = expression.charAt(i);
                    if (Character.isDigit(c)) as = c + as;
                    else break;
                }
                long a = Long.parseLong(as);

                String bs = "";
                for (int i = index + 1; i < expression.length(); i++) {
                    char c = expression.charAt(i);
                    if (Character.isDigit(c)) bs += c;
                    else break;
                }
                long b = Long.parseLong(bs);


                switch (operation) {
                    case '*' -> value = a * b;
                    case '+' -> value = a + b;
                    case '-' -> value = a - b;
                    case '/' -> value = a / b;
                }

                subex = expression.replaceFirst(as + ("\\" + operation) + bs, String.valueOf(value));
                value = solveExpressionWithImportance(subex);
                break;
            }
        }

        //System.out.printf("%s = %d%n", expression, value);
        return value;
    }

}
