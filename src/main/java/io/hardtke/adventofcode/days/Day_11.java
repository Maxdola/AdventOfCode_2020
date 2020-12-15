package io.hardtke.adventofcode.days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Project   » advent_of_code
 * You are not allowed to edit this resource!
 * Created by Maxdola on 12.12.2020 11:41.
 * © Copyright 2020, Max Tom Hardtke, Deutschland
 */
public class Day_11 extends Day {

    @Override
    public void calculate() {
        int yBound = getLines().size();
        int xBound = getLines().get(0).length();
        System.out.printf("xBound: %d yBound: %d%n", xBound, yBound);

        //System.out.println(countOccupiedChairs(getLines(), 4, 3));

        process(getLines(), false);
        process(getLines(), true);

    }

    private void process(List<String> lines, boolean lineOfSight) {
        List<String> previousLines = lines;
        int previousCount = countOccupiedChairs(previousLines);
        boolean run = true;
        int runs = 0;

        while(run) {
            List<String> newLines = run(previousLines, lineOfSight);
            int newCount = countOccupiedChairs(newLines);
            if (newCount == previousCount) {
                run = false;
            }
            previousCount = newCount;
            previousLines = newLines;
            runs++;
        }

        System.out.printf("After %d runs there are %d seats occupied%n", runs, previousCount);
    }

    private List<String> run(List<String> lines, boolean lineOfSight) {
        List<String> newLines = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String pLine = i - 1 >= 0 ? lines.get(i - 1) : null;
            String line = lines.get(i);
            String nLine = i + 1 < lines.size() ? lines.get(i + 1) : null;

            StringBuilder newLine = new StringBuilder();
            int j = 0;
            for (char c : line.toCharArray()) {
                if (c != '.') {
                    boolean occupied = c == '#';
                    int oc = lineOfSight ? countOccupiedChairs(lines, i, j) : countOccupiedChairs(j, pLine, line, nLine);
                    if (occupied && oc >= (lineOfSight ? 5 : 4)) {
                        occupied = false;
                    } else if (!occupied && oc == 0) {
                        occupied = true;
                    }
                    newLine.append(occupied ? '#' : 'L');
                } else {
                    newLine.append(c);
                }
                j++;
            }
            newLines.add(newLine.toString());

        }

        return newLines;
    }

    private int countOccupiedChairs(List<String> lines) {
        return Arrays.stream(String.join("", lines).split("")).mapToInt(s -> s.equals("#") ? 1 : 0).sum();
    }

    private int countOccupiedChairs(int index, String pLine, String line, String nLine) {
        int count = 0;
        if (pLine != null && pLine.charAt(index) == '#') count++;
        if (index - 1 >= 0) {
            if (pLine != null && pLine.charAt(index - 1) == '#') count++;
            if (line.charAt(index - 1) == '#') count++;
            if (nLine != null && nLine.charAt(index - 1) == '#') count++;
        }
        if (index + 1 < line.length()) {
            if (pLine != null && pLine.charAt(index + 1) == '#') count++;
            if (line.charAt(index + 1) == '#') count++;
            if (nLine != null && nLine.charAt(index + 1) == '#') count++;
        }
        if (nLine != null && nLine.charAt(index) == '#') count++;
        return count;
    }

    private int countOccupiedChairs(List<String> lines, int l, int i) {
        int count = 0;

        //left
        {
            String line = lines.get(l);
            for (int j = i - 1; j >= 0; j--) {
                char c = line.charAt(j);
                if (c != '.') {
                    if (c == '#') {
                        count++;
                    }
                    break;
                }
            }
        }

        //right
        {
            String line = lines.get(l);
            for (int j = i + 1; j < line.length(); j++) {
                char c = line.charAt(j);
                if (c != '.') {
                    if (c == '#') {
                        count++;
                    }
                    break;
                }
            }
        }

        //up
        {
            for (int j = l - 1; j >= 0; j--) {
                String line = lines.get(j);
                char c = line.charAt(i);
                if (c != '.') {
                    if (c == '#') {
                        count++;
                    }
                    break;
                }
            }
        }

        //down
        {
            for (int j = l + 1; j < lines.size(); j++) {
                String line = lines.get(j);
                char c = line.charAt(i);
                if (c != '.') {
                    if (c == '#') {
                        count++;
                    }
                    break;
                }
            }
        }

        //up - left
        {
            int k = 1;
            for (int j = l - 1; j >= 0; j--) {
                String line = lines.get(j);
                if (i - k < 0) break;
                char c = line.charAt(i - k);
                if (c != '.') {
                    if (c == '#') {
                        count++;
                    }
                    break;
                }
                k++;
            }
        }

        //up - right
        {
            int k = 1;
            for (int j = l - 1; j >= 0; j--) {
                String line = lines.get(j);
                if (i + k >= line.length()) break;
                char c = line.charAt(i + k);
                if (c != '.') {
                    if (c == '#') {
                        count++;
                    }
                    break;
                }
                k++;
            }
        }

        //down - left
        {
            int k = 1;
            for (int j = l + 1; j < lines.size(); j++) {
                String line = lines.get(j);
                if (i - k < 0) break;
                char c = line.charAt(i - k);
                if (c != '.') {
                    if (c == '#') {
                        count++;
                    }
                    break;
                }
                k++;
            }
        }

        //down - right
        {
            int k = 1;
            for (int j = l + 1; j < lines.size(); j++) {
                String line = lines.get(j);
                if (i + k >= line.length()) break;
                char c = line.charAt(i + k);
                if (c != '.') {
                    if (c == '#') {
                        count++;
                    }
                    break;
                }
                k++;
            }
        }

        return count;
    }

}
