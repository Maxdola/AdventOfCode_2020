package io.hardtke.days;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Day_05 extends Day {

    @Override
    public void calculate() {

        AtomicInteger maxSeatId = new AtomicInteger(Integer.MIN_VALUE);
        List<Integer> seatIds = new ArrayList<>();

        getLines().forEach(l -> {
            String rowPart = l.substring(0, 7);
            String columnPart = l.substring(7);

            int fRow = 0, lRow = 127;

            for (char c : rowPart.toCharArray()) {
                if (c == 'F') {
                    lRow = lRow - (int) Math.round(((double) lRow - fRow) / 2);
                } else {
                    fRow = fRow + (int) Math.round(((double) lRow - fRow) / 2);
                }
                //System.out.printf("%d - %d%n", fRow, lRow);
            }

            if (fRow != lRow) {
                throw new RuntimeException(String.format("%d != %d", fRow, lRow));
            }
            int row = fRow;

            fRow = 0;
            lRow = 7;
            for (char c : columnPart.toCharArray()) {
                if (c == 'L') {
                    lRow = lRow - (int) Math.round(((double) lRow - fRow) / 2);
                } else {
                    fRow = fRow + (int) Math.round(((double) lRow - fRow) / 2);
                }
            }

            if (fRow != lRow) {
                throw new RuntimeException(String.format("%d != %d", fRow, lRow));
            }
            int column = fRow;

            int seatId = row * 8 + column;
            if (maxSeatId.get() < seatId) maxSeatId.set(seatId);
            seatIds.add(seatId);

            System.out.printf("%s: row: %3d column: %1d seatID: %3d%n", l, row, column, seatId);

        });

        System.out.printf("Maximum seatID: %d%n", maxSeatId.get());

        List<Integer> missingSeatIds = new ArrayList<>();
        for (int i = 0; i < seatIds.size(); i++) {
            if (!seatIds.contains(i)) missingSeatIds.add(i);
        }
        missingSeatIds.stream().filter(sid -> seatIds.contains(sid - 1) && seatIds.contains(sid + 1)).forEach(sid -> {
            System.out.printf("Your seatId is: %d", sid);
        });

    }

}
