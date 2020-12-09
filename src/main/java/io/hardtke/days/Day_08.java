package io.hardtke.days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Day_08 extends Day {

    private static class ProcessInfo {
        private boolean infinite;
        private int acc;

        public ProcessInfo(boolean infinite) {
            this.infinite = infinite;
        }

        public ProcessInfo(int acc) {
            this.acc = acc;
            this.infinite = false;
        }

        public boolean isInfinite() {
            return infinite;
        }

        public int getAcc() {
            return acc;
        }
    }

    @Override
    public void calculate() {

        List<Integer> executedLines = new ArrayList<>();

        int acc = 0;
        boolean execute = true;

        int currentLine = 0;
        String nextLine = getLines().get(0);

        while (execute) {

            if (!executedLines.contains(currentLine)) {
                executedLines.add(currentLine);

                String[] lineArr = nextLine.split("\\s+");
                Integer num = Integer.parseInt(lineArr[1]);
                switch (lineArr[0]) {
                    case "acc" -> {
                        acc += num;
                        currentLine++;
                    }
                    case "jmp" -> currentLine += num;
                    case "nop" -> currentLine++;
                }

                nextLine = getLines().get(currentLine);

            } else {
                execute = false;
                System.out.printf("Exited before Loop acc: %d%n", acc);
            }

        }

        int successfull = 0;
        int result = 0;

        for (int i = 0; i < getLines().size(); i++) {
            List<String> lines = new LinkedList<>(getLines());
            String[] lineArr = getLines().get(i).split("\\s+");
            String newLine = "";

            switch (lineArr[0]) {
                case "jmp" -> newLine = "nop" + " " + lineArr[1];
                case "nop" -> newLine = "jmp" + " " + lineArr[1];
            }

            if (newLine.length() > 0) {

                lines.set(i, newLine);

                ProcessInfo pi = evaluateProcess(lines);
                boolean infinite = pi.isInfinite();
                if (!infinite) {
                    successfull++;
                    result = pi.getAcc();
                };
                System.out.printf("%d: '%s' -> %b%n", i, newLine, !infinite);

            }

        }

        System.out.printf("Changing 'jmp' <-> 'nop' resulted in %d successful executions with result %d.%n", successfull, result);

    }

    public ProcessInfo evaluateProcess(List<String> lines) {
        List<Integer> executedLines = new ArrayList<>();

        int acc = 0;
        boolean execute = true;

        int currentLine = 0;
        String nextLine = lines.get(0);

        while (execute) {

            if (!executedLines.contains(currentLine)) {
                executedLines.add(currentLine);

                String[] lineArr = nextLine.split("\\s+");
                Integer num = Integer.parseInt(lineArr[1]);
                switch (lineArr[0]) {
                    case "acc" -> {
                        acc += num;
                        currentLine++;
                    }
                    case "jmp" -> currentLine += num;
                    case "nop" -> currentLine++;
                }

                if (currentLine == lines.size() - 1) return new ProcessInfo(acc);
                if (currentLine < 0 || currentLine >= lines.size()) {
                    System.out.printf("Exiting because line is: %d%n", currentLine);
                    return new ProcessInfo(true);
                };
                nextLine = lines.get(currentLine);

            } else {
                System.out.printf("Exiting because line got executed twice.%n");
                return new ProcessInfo(true);
            }

        }
        return new ProcessInfo(acc);
    }

}
