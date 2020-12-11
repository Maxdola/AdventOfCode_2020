package io.hardtke.days;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day_10 extends Day {

    @Override
    public void calculate() {

        Map<Integer, Integer> differences = new HashMap<>();
        differences.put(1,0);
        differences.put(2,0);
        differences.put(3,1);

        List<Integer> intLines = getLines().stream().mapToInt(Integer::parseInt).sorted().boxed().collect(Collectors.toList());
        ServiceLoader.Provider<List<Integer>> provider = new ServiceLoader.Provider<>() {
            @Override
            public Class<? extends List<Integer>> type() {
                return null;
            }

            @Override
            public List<Integer> get() {
                return new ArrayList<>(intLines);
            }
        };

        List<Integer> nums = provider.get();

        AtomicInteger previousAdapter = new AtomicInteger(nums.get(0));
        differences.put(previousAdapter.get(), 1);
        nums.remove(0);

        nums.forEach(num -> {
            int diff = num - previousAdapter.get();
            differences.compute(diff, (k,v) -> v + 1);
            previousAdapter.set(num);
        });

        System.out.printf("Differences: 1:%2d 2:%2d 3:%2d%n", differences.get(1), differences.get(2), differences.get(3));
        System.out.printf("Part 1 Answer: %d%n" , differences.get(1) * differences.get(3));

        System.out.printf("Total possible arrangements: %d%n", getAllArrangements(provider.get(), null));
        //System.out.println(getAllArrangements(provider.get(), null));
    }

    public long getAllArrangements(List<Integer> nums, List<Integer> currentList) {

        long arrangements = 0;

        if (currentList == null) {
            currentList = new ArrayList<>();
            currentList.add(0);
        }

        if (nums.size() > 0) {
            int last = currentList.get(currentList.size() - 1);
            List<Integer> possibleNext = getPossibleNext(last, nums);
            //System.out.printf("Possible next for %2d -> %s%n", last, possibleNext);

            if (possibleNext.size() == 0) {
                arrangements++;
                return arrangements;
            }

            for (Integer next : possibleNext) {
                List<Integer> newCurrentList = new ArrayList<>(currentList);
                List<Integer> newNums = new ArrayList<>(nums);
                newNums.remove(next);
                newCurrentList.add(next);

                if (newNums.size() > 0) {
                    arrangements += getAllArrangements(newNums, newCurrentList);
                    System.out.print(arrangements + "\r");
                } else {
                    arrangements++;
                    System.out.print(arrangements + "\r");
                }
            }

        }

        return arrangements;
    }

    public List<Integer> getPossibleNext(int last, List<Integer> possible) {
        return possible.stream().filter(p -> last < p).filter(p -> p - last <= 3).collect(Collectors.toList());
    }

}
