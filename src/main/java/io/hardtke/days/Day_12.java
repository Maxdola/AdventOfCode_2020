package io.hardtke.days;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Project   » advent_of_code
 * You are not allowed to edit this resource!
 * Created by Maxdola on 12.12.2020 14:43.
 * © Copyright 2020, Max Tom Hardtke, Deutschland
 */
public class Day_12 extends Day {

    private enum Type {
        N(true), E(true), S(true), W(true), R, L, F;

        static List<Type> directions = Arrays.asList(N, E, S, W);

        private boolean direction;

        Type() {
            this.direction = false;
        }

        Type(boolean direction) {
            this.direction = direction;
        }

        public boolean isDirection() {
            return direction;
        }

        public Type rotate(Type direction, int amount) {
            amount %= 360;
            amount /= 90;
            if (direction == L) amount *= -1;
            amount += directions.indexOf(this);
            if (amount < 0) amount += directions.size();
            if (amount >= directions.size()) amount -= directions.size();
            return directions.get(amount);
        }

        public Type opposite() {
            return rotate(R, 180);
        }
    }

    private class Operation {

        private Type type;
        private int amount;

        public Operation(Type type, int amount) {
            this.type = type;
            this.amount = amount;
        }

        public int getAmount() {
            return amount;
        }

        public Type getType() {
            return type;
        }
    }

    @Override
    public void calculate() {

        List<Operation> operations = new LinkedList<>();

        getLines().forEach(line -> {
            Type t = Type.valueOf(line.substring(0, 1));
            int amount = Integer.parseInt(line.substring(1));
            operations.add(new Operation(t, amount));
        });

        AtomicReference<Type> facing = new AtomicReference<>(Type.E);
        Map<Type, Integer> location = new HashMap<>();

        location.put(Type.N, 0);
        location.put(Type.E, 0);

        operations.forEach(o -> {
            //System.out.printf("Operation: %s:%d%n", o.type, o.amount);

            if (o.getType().isDirection() || o.getType() == Type.F) {
                //location.compute(o.getType(), (k, v) -> (v == null ? 0 : v) + o.getAmount());
                Type key = o.getType() == Type.F ? facing.get() : o.getType();
                Integer amount = location.getOrDefault(key, null);
                if (amount == null) {
                    key = (o.getType() == Type.F ? facing.get() : o.getType()).opposite();
                    amount = location.get(key);
                    amount -= o.getAmount();
                } else {
                    amount += o.getAmount();
                }
                if (amount > 0) {
                    location.put(key, amount);
                } else {
                    location.remove(key);
                    location.put(key.opposite(), amount * -1);
                }
            }/* else if (o.getType() == Type.F) {
                location.compute(facing.get(), (k, v) -> (v == null ? 0 : v) + o.getAmount());
            }*/ else {
                facing.set(facing.get().rotate(o.getType(), o.getAmount()));
            }

            //System.out.println(location);

        });

        System.out.println(location);

        int diff = location.values().stream().mapToInt(integer -> integer).sum();

        System.out.printf("The Manhattan differance is: %d%n", diff);


    }

}
