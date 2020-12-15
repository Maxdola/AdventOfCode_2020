package io.hardtke.adventofcode.days;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Day_04 extends Day {

    private static interface Validator {
        boolean validate(String value);
    }

    private static String[] fields = new String[]{"byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"};

    private static Map<String, Validator> validators = new HashMap<>();

    @Override
    public void calculate() {

        List<Map<String, String>> documents = new ArrayList<>();
        Map<String, String> currentDocument = new HashMap<>();

        int valid = 0;

        getLines().add("");

        for (String l : getLines()) {

            if (l.strip().length() == 0) {

                List<String> fieldsToValidate = new ArrayList<>(Arrays.asList(fields));
                Map<String, String> finalCurrentDocument = currentDocument;
                fieldsToValidate.removeIf(finalCurrentDocument::containsKey);
                if (fieldsToValidate.size() == 0) valid++;

                documents.add(currentDocument);
                currentDocument = new HashMap<>();
            } else {
                for (String field : l.split("\\s+")) {
                    String[] fieldArr = field.split(":");
                    currentDocument.put(fieldArr[0], fieldArr[1]);
                }
            }

        }

        System.out.printf("Total Passports: %d valid: %d%n", documents.size(), valid);

        //Settings up Validators:

        validators.put("byr", value -> {
            try {
                int year = Integer.parseInt(value);
                return year >= 1920 && year <= 2002;
            } catch (NumberFormatException e) {
                return false;
            }
        });

        validators.put("iyr", value -> {
            try {
                int year = Integer.parseInt(value);
                return year >= 2010 && year <= 2020;
            } catch (NumberFormatException e) {
                return false;
            }
        });

        validators.put("eyr", value -> {
            try {
                int year = Integer.parseInt(value);
                return year >= 2020 && year <= 2030;
            } catch (NumberFormatException e) {
                return false;
            }
        });

        validators.put("hgt", value -> {
            try {
                int height = Integer.parseInt(value.substring(0, value.length() - 2));
                if (value.endsWith("cm")) {
                    return height >= 150 && height <= 193;
                } else if (value.endsWith("in")) {
                    return height >= 59 && height <= 76;
                }
                return false;
            } catch (NumberFormatException e) {
                return false;
            }
        });

        validators.put("hcl", value -> value.matches("#[a-f0-9]{6}"));

        validators.put("ecl", value -> value.matches("amb|blu|brn|gry|grn|hzl|oth"));

        validators.put("pid", value -> value.matches("[0-9]{9}"));

        AtomicInteger validV2 = new AtomicInteger();
        AtomicInteger i = new AtomicInteger();
        documents.forEach(d -> {
            AtomicBoolean b = new AtomicBoolean(true);
            AtomicInteger j = new AtomicInteger();
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> e : d.entrySet()) {
                Validator v = validators.get(e.getKey());
                if (v != null) {
                    j.getAndIncrement();
                    boolean res = v.validate(e.getValue());
                    sb.append(String.format("%s:%s -> %b%n", e.getKey(), e.getValue(), res));
                    if (!res) {
                        b.set(false);
                        return;
                    }
                }
            }
            if (b.get() && j.get() == 7) {
                validV2.getAndIncrement();
                System.out.println(sb.toString());
            }
        });

        System.out.printf("Total Valid documents: %d%n", validV2.get());

    }

}
