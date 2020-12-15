package io.hardtke.adventofcode.days;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Day_13 extends Day {

    private static class Bus {

        private boolean valid = true;
        private long id;

        private Bus() {
            valid = false;
        }

        private Bus(long id) {
            this.id = id;
        }

        public long closesTimeAfter(long timestamp) {
            return timestamp % id == 0 ? timestamp : id * (((long) (timestamp / id)) + 1);
        }

        public boolean isValid() {
            return valid;
        }

        public long getId() {
            return id;
        }
    }

    private interface Callback {
        void resolved(long time);
        void finished(long startNumber, Callback cb, long duration);
    }

    @Override
    public void calculate() {

        int timeStamp = Integer.parseInt(getLines().get(0));

        List<Bus> lines = new ArrayList<>();
        Arrays.stream(getLines().get(1).split(",")).forEach(id -> {
            if (!id.equalsIgnoreCase("x")) {
                lines.add(new Bus(Integer.parseInt(id)));
            } else {
                lines.add(new Bus());
            }
        });

        Map<Long, Bus> closestTimes = new HashMap<>();
        lines.stream().filter(Bus::isValid).forEach(l -> closestTimes.put(l.closesTimeAfter(timeStamp), l));

        long closestTime = closestTimes.keySet().stream().sorted().findFirst().get();
        Bus closestBus = closestTimes.get(closestTime);

        System.out.printf("The closes bus is %d and leaves at %d%n", closestBus.id, closestTime);
        System.out.printf("Part 1 Answer: %d%n", closestBus.id * (closestTime - timeStamp));

        /*long time = 100000000000000L;
        while (true) {

            boolean inRow = true;

            for (int i = 0; i < lines.size(); i++) {
                Bus b = lines.get(i);

                if (b.isValid()) {
                    //System.out.printf("%3d: (%7d) ->  %7d%n", b.id, time + i, b.closesTimeAfter(time + i));
                    if (b.closesTimeAfter(time + i) != time + i) {
                        inRow = false;
                        break;
                    }
                }
            }

            //System.out.println("------------------");

            if (inRow) {
                break;
            } else {
                time++;
            }
        }

        long finalTime = time;
        lines.forEach(l -> {
            if (l.isValid()) {
                System.out.printf("%3d: %7d%n", l.id, l.closesTimeAfter(finalTime));
            } else {
                System.out.printf("%3s: %7s%n", "x", "x");
            }
        });

        System.out.printf("Part 2 Answer: %d%n", time);*/

        //100.000.000.000.000

/*        ExecutorService pool = Executors.newFixedThreadPool(20);

        long start = 143243000000000L;
        long steps = 1000000000;
        AtomicLong tasks = new AtomicLong();
        for (long i = 0; i < 20; i++) {
            pool.submit(getRunnable(start + steps * tasks.getAndIncrement(), steps, lines, new Callback() {
                @Override
                public void resolved(long time) {
                    pool.shutdown();
*//*                    lines.forEach(l -> {
                        if (l.isValid()) {
                            System.out.printf("%3d: %7d%n", l.id, l.closesTimeAfter(time));
                        } else {
                            System.out.printf("%3s: %7s%n", "x", "x");
                        }
                    });*//*

                    System.out.printf("Part 2 Answer: %d%n", time);
                }

                @Override
                public void finished(long startNumber, Callback cb, long duration) {
                    pool.submit(getRunnable(start + steps * tasks.getAndIncrement(), steps, lines, cb));
                    NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);
                    DecimalFormat df = (DecimalFormat)nf;
                    System.out.printf("Finished Testing from %30s -> %30s (%3ds)%n", nf.format(startNumber + steps), nf.format(startNumber + steps), (int) (duration / 1000));
                }
            }));
        }*/

        //System.out.println(moduloProduct(Arrays.asList(new Bus(3), new Bus(4))));


        part2(lines);

    }

    private long modInverse(long a, long m) {
        a = a % m;
        for (long x = 1; x < m; x++)
            if ((a * x) % m == 1)
                return x;
        return 1;
    }

    private long moduloProduct(List<Bus> lines) {
        long p = 1;
        for (Bus line : lines) {
            if (line.isValid()) p *= line.getId();
        }
        return p;
    }

    private long scf(long a, long b) {
        long i = 0;
        while(true) {
            if ((b * i + 1) % a == 0) break;
            i ++;
        }
        return b * i + 1;
    }

    private long[] gcdExtended(long p, long q) {
        if (q == 0)
            return new long[] { p, 1, 0 };

        long[] vals = gcdExtended(q, p % q);
        long d = vals[0];
        long a = vals[2];
        long b = vals[1] - (p / q) * vals[2];
        return new long[] { d, a, b };
    }

    private long gcd(long a, long b) {
        if (a == 0) return b;
        return gcd(b % a, a);
    }

    private void part2(List<Bus> lines) {

        long time = 0;
        long moduloProduct = moduloProduct(lines);

        for (long i = 0; i < lines.size(); i++) {
            Bus l = lines.get(Math.toIntExact(i));

            if (l.isValid()) {

                long a = l.getId() - i;
                long n = l.getId();
                long mp = moduloProduct / l.getId();

                //System.out.printf("a: %3d n: %3d, ModuloProduct: %4d%n", a, n, mp);
                //System.out.printf(" -> %3d * %3d * %3d = %5d%n", a, mp, modInverse(a, n), a * mp * modInverse(a, n));

                time += a * mp * modInverse(mp % n, n);

            }

        }

        System.out.println(time % moduloProduct);
        System.out.println(moduloProduct);
        System.out.println(time % moduloProduct + moduloProduct);

        time = time % moduloProduct + moduloProduct * 0;

        System.out.printf("mod: %d%n", moduloProduct);
        System.out.printf("Part 2 answer: %d", time);

    }

    public Runnable getRunnable(long startNumber, long steps, List<Bus> lines, Callback callback) {
        return () -> {
            long start = System.currentTimeMillis();
            long time = startNumber;
            while (true) {

                boolean inRow = true;

                for (int i = 0; i < lines.size(); i++) {
                    Bus b = lines.get(i);

                    if (b.isValid()) {
                        //System.out.printf("%3d: (%7d) ->  %7d%n", b.id, time + i, b.closesTimeAfter(time + i));
                        if (b.closesTimeAfter(time + i) != time + i) {
                            inRow = false;
                            break;
                        }
                    }
                }

                //System.out.println("------------------");

                if (inRow) {
                    callback.resolved(time);
                    break;
                } else {
                    if (time > startNumber + steps) {
                        callback.finished(startNumber, callback, System.currentTimeMillis() - start);
                        break;
                    };
                    time++;
                }
            }
        };
    }

}
