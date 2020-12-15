package io.hardtke.adventofcode.days;

import java.util.*;
import java.util.stream.Collectors;

public class Day_09 extends Day {

    private static final int PREAMBLE = 25;

    @Override
    public void calculate() {

        List<Integer> preamble = new LinkedList<>();

        for (int i = 0; i < getLines().size(); i++) {
            int j = Integer.parseInt(getLines().get(i));

            if (i < PREAMBLE) {
                preamble.add(j);
            } else {

                if (!hasPair(preamble, j)) {
                    System.out.printf("%d cannot be formed with the current preamble.%n", j);
                    List<Long> nums = getLines().stream().map(Long::parseLong).collect(Collectors.toList());
                    List<Long> sumNum = findSum(nums, j);
                    System.out.println("SumNums: " + sumNum);
                    int max = sumNum.stream().mapToInt(Long::intValue).max().getAsInt();
                    int min = sumNum.stream().mapToInt(Long::intValue).min().getAsInt();
                    System.out.printf("min: %d, max: %d -> Secret: %d", min, max, min + max);
                    break;
                }

                preamble.remove(0);
                preamble.add(j);
            }

        }

    }

    public List<Long> findSum(List<Long> nums, int num) {

        List<Long> numbers = new ArrayList<>();
        int sum = 0;

        //System.out.print("Sum: ");
        for (int i = 0; i < nums.size(); i++) {
            numbers.add(nums.get(i));
            sum += nums.get(i);
            //System.out.printf("%d, ", sum);
            if (sum == num) return numbers;
            if (sum > num) {
                nums.remove(0);
                return findSum(nums, num);
            };
        }
        return null;
    }

    public boolean hasPair(List<Integer> preamble, int num) {
        Set<Integer> pset = new HashSet<>(preamble);

        for (Integer p : pset) {
            for (Integer pp : pset) {
                if (p + pp == num) return true;
            }
        }

        return false;
    }

}
