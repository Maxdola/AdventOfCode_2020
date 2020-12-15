package io.hardtke.adventofcode.days;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Day_14 extends Day {

    private static class MemoryOperation {

        private Map<Integer, Integer> mask;
        private List<MemoryWrite> memoryWrites;
        private List<Integer> xLocations;

        public MemoryOperation(List<String> lines) {
            this.mask = new HashMap<>();
            this.xLocations = new ArrayList<>();
            String smask = lines.remove(0).split("=")[1].strip();
            for (int i = 0; i < smask.toCharArray().length; i++) {
                char c = smask.charAt(smask.length() - i - 1);
                if (c != 'X') {
                    int num = Integer.parseInt(String.valueOf(c));
                    mask.put(i, num);
                } else {
                    xLocations.add(i);
                }
            }
            memoryWrites = lines.stream().map(MemoryWrite::new).collect(Collectors.toList());
        }

        public String toBitString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mask.keySet().stream().mapToInt(Integer::intValue).max().getAsInt() + 1; i++) {
                sb.insert(0, mask.getOrDefault(i, -1) == -1 ? "X" : String.valueOf(mask.getOrDefault(i, -1)));
            }
            return sb.toString();
        }

    }

    private static class MemoryWrite {

        private int address;
        private int value;

        public MemoryWrite(String line) {
            String[] lineArr = line.split("=");
            this.address = Integer.parseInt(lineArr[0].split("\\[")[1].split("]")[0]);
            this.value = Integer.parseInt(lineArr[1].strip());

            //System.out.println(toBitString());
            //System.out.println(mapToInt(toBitMap()));
        }

        public List<Long> getAddressesWithMask(MemoryOperation mo) {
            Map<Integer, Integer> bitMap = modifyAddressWithMask(mo);

            //System.out.printf("AdressMap: %s%n", mapToString(bitMap));
            List<Map<Integer, Integer>> mutatedAddresses = mutateAddresses(new TreeMap<>(bitMap), null);
            //System.out.println(mutatedAddresses);

            /*mutatedAddresses.forEach(m -> {
                System.out.printf("%s: %d%n", mapToString(m), mapToInt(m));
            });*/

            return mutatedAddresses.stream().map(Day_14::mapToInt).collect(Collectors.toList());
        }

        public Map<Integer, Integer> modifyWithMask(Map<Integer, Integer> mask) {
            Map<Integer, Integer> bitMap = toBitMap();
            int max = mask.keySet().stream().mapToInt(Integer::intValue).max().getAsInt();
/*            mask.forEach((k,v) -> {
                bitMap.put(max - k, v);
            });*/
            //mask.forEach(bitMap::put);
            bitMap.putAll(mask);
            for (int i = 0; i < bitMap.keySet().stream().mapToInt(Integer::intValue).max().getAsInt() + 1; i++) {
                bitMap.putIfAbsent(i, 0);
            }
            return bitMap;
        }

        private Map<Integer, Integer> modifyAddressWithMask(MemoryOperation mo) {
            Map<Integer, Integer> bitMap = addressToBitMap();
            mo.mask.forEach((k,v) -> {
                if (v == 1) bitMap.put(k, v);
            });
            mo.xLocations.forEach(i -> bitMap.put(i, -1));
            for (int i = 0; i < bitMap.keySet().stream().mapToInt(Integer::intValue).max().getAsInt() + 1; i++) {
                bitMap.putIfAbsent(i, 0);
            }
            return bitMap;
        }

        public String toBitString() {
            StringBuilder sb = new StringBuilder();
            int am = this.value;
            while (am > 0) {
                sb.insert(0, am % 2);
                am = am / 2;
            }
            return sb.toString().length() == 0 ? "0" : sb.toString();
        }

        public Map<Integer, Integer> toBitMap() {
            List<Integer> bitList = new ArrayList<>();
            int am = this.value;
            return calculateBits(bitList, am);
        }

        public Map<Integer, Integer> addressToBitMap() {
            List<Integer> bitList = new ArrayList<>();
            int am = this.address;
            return calculateBits(bitList, am);
        }

        private Map<Integer, Integer> calculateBits(List<Integer> bitList, int am) {
            while (am > 0) {
                bitList.add(am % 2);
                am = am / 2;
            }
            Map<Integer, Integer> bitMap = new HashMap<>();
            for (int i = 0; i < bitList.size(); i++) {
                bitMap.put(i, bitList.get(i));
            }
            return bitMap;
        }

        public int getAddress() {
            return address;
        }

        public int getValue() {
            return value;
        }
    }

    private static List<Map<Integer, Integer>> mutateAddresses(TreeMap<Integer, Integer> bits, Map<Integer, Integer> current) {
        List<Map<Integer, Integer>> addresses = new ArrayList<>();

        if (current == null) current = new HashMap<>();

        Map.Entry<Integer, Integer> e = bits.descendingMap().lastEntry();
        bits.remove(e.getKey());

        if (e.getValue() == -1) {
            for (int i = 0; i < 2; i++) {
                Map<Integer, Integer> newCurrent = new HashMap<>(current);
                newCurrent.put(newCurrent.size(), i);
                if (bits.size() > 0) {
                    addresses.addAll(mutateAddresses(new TreeMap<>(bits), newCurrent));
                } else {
                    addresses.add(newCurrent);
                }
            }
        } else {
            Map<Integer, Integer> newCurrent = new HashMap<>(current);
            newCurrent.put(newCurrent.size(), e.getValue());
            if (bits.size() > 0) {
                addresses.addAll(mutateAddresses(new TreeMap<>(bits), newCurrent));
            } else {
                addresses.add(newCurrent);
            }
        }

        return addresses;
    }

    private static String mapToString(Map<Integer, Integer> intMap) {
        List<String> s = intMap.values().stream().map(i -> String.valueOf(i == -1 ? "X" : i)).collect(Collectors.toList());
        Collections.reverse(s);
        return String.join("", s);
    }

    private static long mapToInt(Map<Integer, Integer> intMap) {
        AtomicLong i = new AtomicLong();
        intMap.forEach((k, v) -> {
            if (v == 1) i.addAndGet(((long) Math.pow(2, k)));
        });
        return i.get();
    }

    @Override
    public void calculate() {

        List<MemoryOperation> operations = new ArrayList<>();

        List<String> lines = null;
        for (String line : getLines()) {
            if (line.startsWith("mask")) {
                if (lines != null) {
                    operations.add(new MemoryOperation(lines));
                }
                lines = new ArrayList<>();
                lines.add(line);
            } else {
                lines.add(line);
            }
        }
        operations.add(new MemoryOperation(lines));

        //System.out.println(operations);
        Map<Integer, Long> memory = new HashMap<>();
        Map<Long, Integer> memoryp2 = new HashMap<>();

        operations.forEach(o -> {

            //System.out.printf("Mask: %s%n", o.mask);
            //System.out.printf("Mask: %s%n", o.toBitString());

            o.memoryWrites.forEach(mw -> {

                //System.out.printf("%s -> %d%n", mw.toBitMap(), mapToInt(mw.toBitMap()));
                //System.out.printf("Value: %d -> %s => %s -> %s%n", mw.getValue(), mw.toBitString(), mapToString((mw.modifyWithMask(o.mask))), mapToInt(mw.modifyWithMask(o.mask)));
                //System.out.printf("Value: %d -> %s => %s%n", mw.getValue(), mw.toBitString(), mw.toBitMap().values());

                //System.out.printf("%36s : Mask \n%36s : Value \n%36s : Modified%n", o.toBitString(), mw.toBitString(), mapToString(mw.modifyWithMask(o.mask)));

                //System.out.println(mw.toBitMap());
                //System.out.println((mw.modifyWithMask(o.mask)));

                //System.out.println(mapToInt(mw.modifyWithMask(o.mask)));
                memory.put(mw.address, mapToInt(mw.modifyWithMask(o.mask)));

                mw.getAddressesWithMask(o).forEach(a -> {
                    memoryp2.put(a, mw.getValue());
                });
            });

        });

        System.out.printf("Part 1: Memorysum: %d%n", memory.values().stream().mapToLong(Long::longValue).sum());
        System.out.printf("Part 2: Memorysum: %d%n", memoryp2.values().stream().mapToLong(Integer::longValue).sum());




    }


}
