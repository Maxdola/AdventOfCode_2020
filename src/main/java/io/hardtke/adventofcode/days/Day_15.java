package io.hardtke.adventofcode.days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day_15 extends Day {


    @Override
    public void calculate() {
        List<Integer> nums = Arrays.stream(getLines().get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());

        int lastNumber = nums.get(0);
        for (int i = 0; i < 30000000; i++) {
            int n = -1;
            if (nums.size() > i) {
                n = nums.get(i);
            } else {

                List<Integer> saidNums = new ArrayList<>(nums);
                saidNums.remove(i - 1);

                if (saidNums.contains(lastNumber)) {
                    n = i - 1 - saidNums.lastIndexOf(lastNumber);
                } else {
                    n = 0;
                }

                nums.add(n);

            }
            lastNumber = n;
            //System.out.println(n);
        }
        System.out.println(lastNumber);
    }


}
