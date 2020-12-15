package io.hardtke.adventofcode.days;

import io.hardtke.adventofcode.App;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Day_07 extends Day {

    static class Luggage {

        private String name;
        private int amount = 1;
        private List<Luggage> subLuggage = new ArrayList<>();

        public Luggage(String name) {
            this.name = name;
        }

        private Luggage(String name, int amount) {
            this.name = name;
            this.amount = amount;
        }

        public Luggage addSubLuggage(String name, int amount) {
            Luggage l = new Luggage(name, amount);
            this.subLuggage.add(l);
            return l;
        }

        public Luggage addSubLuggage(Luggage l, int amount) {
            l.amount = amount;
            this.subLuggage.add(l);
            return l;
        }

        public boolean canContain(String name) {
            if (this.subLuggage.size() == 0) return false;
            if (this.subLuggage.stream().map(Luggage::getName).anyMatch(lgName -> lgName.equalsIgnoreCase(name))) return true;
            for (Luggage lg : this.subLuggage) {
                if (lg.canContain(name)) {
                    return true;
                }
            }
            return false;
        }

        public int countSubLuggage() {
            if (this.subLuggage.size() == 0) return 0;
            int amount = 0;
            for (Luggage slg : this.subLuggage) {
                amount += slg.amount;
                amount += slg.amount * slg.countSubLuggage();
            }
            return amount;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return App.gson.toJson(this);
        }
    }

    @Override
    public void calculate() {

        String queryLuggage = "shiny gold";
        List<Luggage> luggages = new ArrayList<>();

        getLines().forEach(l -> {
            luggages.add(getLuggage(l));
        });

        AtomicInteger i = new AtomicInteger();

        luggages.forEach(lg -> {
            if (lg.canContain(queryLuggage)) i.getAndIncrement();
        });

        System.out.printf("%s can be stored in %d bags.%n", queryLuggage, i.get());

        Luggage qLuggage = luggages.stream().filter(lg -> lg.name.equalsIgnoreCase(queryLuggage)).findFirst().orElse(null);
        System.out.printf("%s contains %d other bags.%n", qLuggage.getName(), qLuggage.countSubLuggage());

    }

    public Luggage getLuggage(String line) {
        String nameArr[] = line.split("\\s+");
        String name = nameArr[0] + " " + nameArr[1];
        Luggage lg = new Luggage(name);

        String[] containsArr = line.split("contain")[1].split(",");
        if (containsArr.length == 1 && containsArr[0].strip().startsWith("no other bags")) return lg;
        for (String slg : containsArr) {
            String[] slgInfoArr = slg.strip().split("\\s+");
            int amount = Integer.parseInt(slgInfoArr[0]);
            String slgName = slgInfoArr[1] + " " + slgInfoArr[2];
            lg.addSubLuggage(getLuggage(Objects.requireNonNull(getLines().stream().filter(l -> l.startsWith(slgName)).findFirst().orElse(null))), amount);
        }

        return lg;
    }

}
