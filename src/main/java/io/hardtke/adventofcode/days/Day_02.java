package io.hardtke.adventofcode.days;


import java.util.concurrent.atomic.AtomicInteger;

public class Day_02 extends Day {

    @Override
    public void calculate() {
        AtomicInteger valid = new AtomicInteger();
        AtomicInteger validV2 = new AtomicInteger();
        this.getLines().forEach(l -> {
            String[] arr = l.split("\\s+");
            String[] minMax = arr[0].split("-");
            char c = arr[1].substring(0, 1).toCharArray()[0];
            int min = Integer.parseInt(minMax[0]);
            int max = Integer.parseInt(minMax[1]);
            int count = 0;
            for (char c1 : arr[2].toCharArray()) {
                if (c1 == c) count++;
            }
            boolean passed = min <= count && count <= max;
            boolean passedV2 = false;
            if (passed) {
                valid.getAndIncrement();
            }

            if ((arr[2].toCharArray()[min - 1] == c || arr[2].toCharArray()[max - 1] == c) && !(arr[2].toCharArray()[min - 1] == c && arr[2].toCharArray()[max - 1] == c)) {
                validV2.getAndIncrement();
                passedV2 = true;
            }
            System.out.println(String.format("%30s -> %s (%2d/%2d) -> %2d -> %b %b", arr[2], c, min, max, count, passed, passedV2));
        });
        System.out.printf("Valid: %d%n", valid.get());
        System.out.printf("ValidV2: %d%n", validV2.get());
    }

}
