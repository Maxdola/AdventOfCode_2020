package io.hardtke.adventofcode.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class FileToLines {

    private List<String> lines;

    public FileToLines(String fileName) {
        InputStreamReader reader = new ResourceLoader().loadResource(fileName, StandardCharsets.UTF_8);
        this.lines = new BufferedReader(reader).lines().collect(Collectors.toList());
    }

    public List<String> get() {
        return this.lines;
    }

}
