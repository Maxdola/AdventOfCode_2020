package io.hardtke.adventofcode.days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Day_16 extends Day{

    private static class TicketField {

        private String name;
        private List<ValueChecker> valueCheckers;

        public TicketField(String line) {
            String[] lineArr = line.split(":");
            this.name = lineArr[0];
            this.valueCheckers = new ArrayList<>();

            String[] checkerArr = lineArr[1].trim().split("or");
            for (String cs : checkerArr) {
                cs = cs.strip();
                String[] csArr = cs.split("-");
                int a = Integer.parseInt(csArr[0]);
                int b = Integer.parseInt(csArr[1]);
                this.valueCheckers.add(new ValueChecker(a, b));
            }
        }

        public boolean check(int n) {
            for (ValueChecker c : this.valueCheckers) {
                if (c.check(n)) return true;
            }
            return false;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "{" +
                    "name='" + name + '\'' +
                    ", valueCheckers=" + valueCheckers +
                    '}';
        }
    }

    private static class Ticket {
        private List<Integer> values = new ArrayList<>();

        public Ticket(String line) {
            for (String s : line.split(",")) {
                values.add(Integer.parseInt(s));
            }
        }

        public List<Integer> getValues() {
            return values;
        }

        @Override
        public String toString() {
            return "Ticket{" +
                    "values=" + values +
                    '}';
        }
    }

    private static class ValueChecker {
        private int min, max;

        public ValueChecker(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public boolean check(int n) {
            return min <= n && n <= max;
        }

        @Override
        public String toString() {
            return min + "-" + max;
        }
    }

    @Override
    public void calculate() {

        List<TicketField> ticketFields = new ArrayList<>();
        Ticket myTicket = null;
        List<Ticket> tickets = new ArrayList<>();

        int emptyLine = 0;

        for (int i = 0; i < getLines().size(); i++) {
            String l = getLines().get(i);
            if (l.length() == 0) {
                emptyLine++;
                i++;
                continue;
            }
            switch (emptyLine) {
                case 0 -> ticketFields.add(new TicketField(l));
                case 1 -> myTicket = new Ticket(l);
                case 2 -> tickets.add(new Ticket(l));
            }
        }

        AtomicInteger errorRate = new AtomicInteger();

        tickets.forEach(t -> {
            for (Integer v : t.values) {
                boolean valid = false;
                for (TicketField field : ticketFields) {
                    if (field.check(v)) {
                        valid = true;
                        break;
                    }
                }
                if (!valid) {
                    errorRate.getAndAdd(v);
                }
            }
        });

        System.out.printf("Error Rate: %d%n", errorRate.get());

        List<Ticket> validTickets = tickets.stream().filter(t -> {
            for (Integer v : t.values) {
                boolean valid = false;
                for (TicketField field : ticketFields) {
                    if (field.check(v)) {
                        valid = true;
                        break;
                    }
                }
                if (!valid) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());

        System.out.printf("Total Tickets: %d, invalid: %d, valid: %d%n", tickets.size(), tickets.size() - validTickets.size(), validTickets.size());

        Map<Integer, List<TicketField>> possibleFieldMap = new HashMap<>();

        for (int i = 0; i < myTicket.getValues().size(); i++) {
            int finalI = i;
            List<Integer> values = validTickets.stream().map(t -> t.getValues().get(finalI)).collect(Collectors.toList());
            values.add(myTicket.getValues().get(i));

            List<TicketField> validFields = new ArrayList<>();

            fl : for (TicketField f : ticketFields) {
                for (Integer v : values) {
                    if (!f.check(v)) continue fl;
                }
                validFields.add(f);
            }

/*            if (validFields.size() > 1) {
                throw new RuntimeException("More than one TicketField valid for Field: " + i);
            } else if (validFields.isEmpty()) {
                throw new RuntimeException("No TicketField valid for Field: " + i);
            } else {
                fieldMap.put(i, validFields.get(0));
            }*/

            if (validFields.size() == 1) {
                //throw new RuntimeException("More than one TicketField valid for Field: " + i);
                ticketFields.remove(validFields.get(0));
            }

            possibleFieldMap.put(i, validFields);
        }

        possibleFieldMap.forEach((k,v) -> {
            System.out.printf("%d: %s%n", k, v.stream().map(TicketField::getName).collect(Collectors.joining(",")));
        });

        boolean solved = true;
        while (solved) {
            List<TicketField> removeFromOthers = new ArrayList<>();

            possibleFieldMap.forEach((k,v) -> {
                if (v.size() == 1) {
                    removeFromOthers.add(v.get(0));
                }
            });

            removeFromOthers.forEach(tf -> {
                possibleFieldMap.keySet().forEach(k -> possibleFieldMap.compute(k, (kk, tfl) -> {
                    if (tfl.size() > 1) {
                        tfl.remove(tf);
                    }
                    return tfl;
                }));
            });
            solved = possibleFieldMap.values().stream().mapToInt(List::size).distinct().max().getAsInt() > 1;
        }

        Map<Integer, TicketField> fieldMap = new HashMap<>();

        possibleFieldMap.forEach((k,v) -> {
            System.out.printf("%d: %s%n", k, v.stream().map(TicketField::getName).collect(Collectors.joining(",")));
            fieldMap.put(k, v.get(0));
        });

        List<Integer> departureFields = new ArrayList<>();
        for (int i = 0; i < fieldMap.size(); i++) {
            if (fieldMap.get(i).getName().startsWith("departure")) departureFields.add(i);
        }

        AtomicLong sum = new AtomicLong(1);
        Ticket finalMyTicket = myTicket;
        departureFields.forEach(i -> {
            sum.getAndUpdate(s -> s * finalMyTicket.getValues().get(i));
        });

        System.out.printf("Departure sum from Fields: %s -> %d%n", departureFields, sum.get());
    }

}
