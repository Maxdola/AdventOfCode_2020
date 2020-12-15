package io.hardtke.adventofcode.days;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day_06 extends Day {


    @Override
    public void calculate() {

        List<Set<Character>> groups = new ArrayList<>();

        getLines().add("");

        Set<Character> currentGroup = new HashSet<>();

        for (String l : getLines()) {
            if (l.length() == 0) {
                groups.add(currentGroup);
                currentGroup = new HashSet<>();
            } else {
                currentGroup.addAll(l.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
            }
        }

        int sum = groups.stream().mapToInt(Set::size).sum();

        System.out.printf("Sum of any Selected answers: %d%n", sum);

        partTwo();

    }

    private void partTwo() {

        List<List<Set<Character>>> groups = new ArrayList<>();

        List<Set<Character>> currentGroup = new ArrayList<>();

        for (String l : getLines()) {
            if (l.length() == 0) {
                groups.add(currentGroup);
                currentGroup = new ArrayList<>();
            } else {
                currentGroup.add(l.chars().mapToObj(c -> (char) c).collect(Collectors.toSet()));
            }
        }

        AtomicInteger i = new AtomicInteger();
        groups.forEach(g -> {
            Set<Character> allChars = g.stream().flatMap(Set::stream).collect(Collectors.toSet());
            allChars.removeIf(c -> g.size() != g.stream().filter(s -> s.contains(c)).count());
            //System.out.println(allChars.size());
            i.getAndAdd(allChars.size());
        });

        System.out.printf("Sum of unique Selected answers: %d", i.get());

    }


}
