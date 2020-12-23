package io.hardtke.adventofcode.days;

import java.util.concurrent.atomic.AtomicLong;

public class Day_18 extends Day {

/*    private interface BaseExpression {
        int importance();
        int evaluate();
    }

    private static class NumberExpression implements BaseExpression {

        private int value;

        @Override
        public int importance() {
            return 0;
        }

        @Override
        public int evaluate() {
            return value;
        }
    }

    private static class Parentheses implements BaseExpression {
        private MathExpression expression;

        @Override
        public int importance() {
            return 1;
        }

        @Override
        public int evaluate() {
            return expression.evaluate();
        }
    }

    private static class MathExpression implements BaseExpression {

        private BaseExpression expression;

        @Override
        public int importance() {
            return 0;
        }

        @Override
        public int evaluate() {
            return expression.evaluate();
        }
    }*/

    @Override
    public void calculate() {

        //solveExpression("(2+4*9)*(6+9*8+6)+6");
        //int a = solveExpression();

        //System.out.printf("Value of: %d%n", a);

        AtomicLong sum = new AtomicLong();

        getLines().forEach(l -> {
            long a = solveExpression(new StringBuilder(l).reverse().toString().replaceAll("\\(", "FRAC").replaceAll("\\)", "(").replaceAll("FRAC", ")"));
            sum.addAndGet(a);
            System.out.printf("%s = %d%n", l, a);
        });

        System.out.printf("Part 1 sum: %d%n", sum.get());

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

}
