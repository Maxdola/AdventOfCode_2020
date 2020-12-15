package io.hardtke.adventofcode.days;

import io.hardtke.adventofcode.utils.FileToLines;

import java.util.List;

public abstract class Day {

    private List<String> lines;

    public Day() {
        this.loadLines();
        this.calculate();
    }

    private void loadLines() {
        String[] nameArray = this.getClass().getCanonicalName().split("\\.");
        String fileName = nameArray[nameArray.length - 1].toLowerCase() + ".txt";
        this.lines = new FileToLines(fileName).get();
    }

    public List<String> getLines() {
        return lines;
    }

    public abstract void calculate();
}
