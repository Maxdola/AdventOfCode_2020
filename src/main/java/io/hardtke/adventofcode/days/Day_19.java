package io.hardtke.adventofcode.days;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Project   » advent_of_code
 * You are not allowed to edit this resource!
 * Created by Maxdola on 24.12.2020 14:22.
 * © Copyright 2020, Max Tom Hardtke, Deutschland
 */
public class Day_19 extends Day {

    private static class Rule {

        private int id;
        private List<List<Integer>> rawSubRules;
        private List<List<Rule>> subRules;
        private String sequence;

        public Rule(int id) {
            this.id = id;
        }

        public boolean isValid() {
            return this.sequence != null || ((this.subRules != null && this.subRules.size() > 0) && (this.rawSubRules.size() == this.subRules.size()));
        }

        public String getSequence() {
            return sequence;
        }

        public List<List<Rule>> getSubRules() {
            return subRules;
        }

        public List<List<Integer>> getRawSubRules() {
            return rawSubRules;
        }

        public int getId() {
            return id;
        }

        public String ruleToRegex() {
            if (this.getSequence() != null) return this.getSequence();
            StringBuilder regex = new StringBuilder();
            regex.append("(");
            //Below is too short for part2
            //regex.append(this.getSubRules().stream().map(l -> l.stream().map(Rule::ruleToRegex).collect(Collectors.joining())).collect(Collectors.joining("|")));
            String prefix = "";
            for (List<Rule> l : this.getSubRules()) {
                //String lRegex = l.stream().map(Rule::ruleToRegex).collect(Collectors.joining());
                StringBuilder lRegex = new StringBuilder();
                boolean checked = false;
                boolean print = l.contains(this);
                for (Rule sr : l) {
                    if (sr.getId() == this.getId()) {
                        checked = true;
                        //lRegex.append("+");
                        System.out.println(l.stream().map(Rule::getId).map(String::valueOf).collect(Collectors.joining()));
                        continue;
                    }
                    lRegex.append(sr.ruleToRegex());
                    if (print) lRegex.append("+");
                    if (checked) System.out.println("CHECKED!!!");
                    if (print) {
                        System.out.println(sr.getId() + " -> " + lRegex.toString());
                    }
                }
                regex.append(prefix).append(lRegex);
                if (checked && !regex.toString().endsWith("+")) regex.append("+");
                //if (regex.toString().endsWith("+") && this.getId() == 11) regex.setLength(regex.length() - 1);
                prefix = "|";
            }
            regex.append(")");
            return regex.toString();
        }

        @Override
        public String toString() {
            return "{" +
                    "id=" + id +
                    ", rawSubRules=" + rawSubRules +
                    ", subRules=" + subRules.size() +
                    ", sequence='" + sequence + '\'' +
                    '}';
        }
    }

    @Override
    public void calculate() {

        boolean addRules = true;
        List<Rule> rules = new ArrayList<>();
        List<String> words = new ArrayList<>();

        //final String fullLineRegex = "(\\d+\\s+\\d+)+";
        final String fullLineRegex = "^((\\\\d+[ ])+)\\\\|[ ]((\\\\d+[ ]?)+)$";
        final Pattern fullLinePattern = Pattern.compile(fullLineRegex);

        final String lineRegex = "(\\d+)\\D+(\\d+)";
        final Pattern linePattern = Pattern.compile(lineRegex);

        for (String l : getLines()) {
            if (l.length() == 0) {
                addRules = false;
                continue;
            }

            if (addRules) {
                String[] lArr = l.split(":\\s+");
                int id = Integer.parseInt(lArr[0]);
                String value = lArr[1];
                Rule r = new Rule(id);

                if (value.contains("\"")) {
                    value = value.replaceAll("\"", "");
                    r.sequence = value;
                } else {
                    /*Matcher matcher = fullLinePattern.matcher(value);

                    List<List<Integer>> rawSubRules = new ArrayList<>();
                    System.out.println(value + ": " + matcher.find());
                    /*while (matcher.find()) {
                        List<Integer> subSubRules = new ArrayList<>();
                        Matcher m = linePattern.matcher(matcher.group(0));
                        m.find();
                        for (int i = 1; i <= m.groupCount(); i++) {
                            subSubRules.add(Integer.parseInt(m.group(i)));
                        }
                        rawSubRules.add(subSubRules);
                    }*//*
                    for (int j = 1; j < 5; j++) {
                        List<Integer> subSubRules = new ArrayList<>();
                        System.out.println(matcher.group(j));
                        Matcher m = linePattern.matcher(matcher.group(j));
                        m.find();
                        for (int i = 1; i <= m.groupCount(); i++) {
                            subSubRules.add(Integer.parseInt(m.group(i)));
                        }
                        rawSubRules.add(subSubRules);
                        if (j == 1) j = 4;
                    }
                    r.rawSubRules = rawSubRules;*/

                    List<List<Integer>> rawSubRules = new ArrayList<>();

                    String[] vArr = value.split("\\|");
                    for (String v : vArr) {
                        List<Integer> subR = new ArrayList<>();
                        v = v.strip();
                        for (String n : v.split("\s")) {
                            subR.add(Integer.valueOf(n));
                        }
                        rawSubRules.add(subR);
                    }
                    r.rawSubRules = rawSubRules;

                    if (rawSubRules.size() == 0) {
                        String[] valueArr = value.replaceAll("\s+", "").split("\\|");
                        for (String val : valueArr) {
                            rawSubRules.add(Collections.singletonList(Integer.parseInt(val)));
                        }
                    }

                    if (rawSubRules.size() == 0) {
                        rawSubRules.add(Collections.singletonList(Integer.parseInt(value)));
                    }
                }

                rules.add(r);
            } else {
                words.add(l);
            }
        }

        for (Rule r : rules) {
            if (!r.isValid()) {
                //System.out.printf("Rule %d%n", r.getId());
                //System.out.println(r);
                r.getRawSubRules().forEach(l -> {
                    List<Rule> srs = new ArrayList<>();
                    l.forEach(id -> {
                        Rule sr = rules.stream().filter(rr -> rr.getId() == id).findFirst().orElse(null);
                        srs.add(sr);
                    });
                    if (r.getSubRules() == null) r.subRules = new ArrayList<>();
                    r.getSubRules().add(srs);
                });
                //System.out.println(r);
            }
        }

        AtomicBoolean valid = new AtomicBoolean(true);

        rules.forEach(r -> {
            //System.out.printf("%d: %b%n", r.getId(), r.isValid());
            if (!r.isValid()) valid.set(false);
        });
        System.out.printf("All Rules valid: %b%n", valid.get());

        //System.out.println(rules);

/*        try {
            allValidStrings(rules, null, "").forEach(s -> {
                System.out.println(s);
            });
        } catch (StackOverflowError e) {
            System.out.println("Upsidaysi");
        }
 */

        String rawRegex = "^" + (rules.stream().filter(r -> r.getId() == 0).findFirst().orElse(null).ruleToRegex()) + "$";
        Pattern p = Pattern.compile(rawRegex);

        System.out.println(rawRegex);

        AtomicInteger i = new AtomicInteger();
        words.forEach(w -> {
            Matcher m = p.matcher(w);
            System.out.printf("%70s: %b%n", w, m.matches());
            if (m.matches()) i.getAndIncrement();
        });
        System.out.printf("Total Matches: %d%n", i.get());

    }

    public List<String> allValidStrings(List<Rule> rules, Rule currentRule, String currentString) {
        List<String> values = new ArrayList<>();

        if (currentRule == null) currentRule = rules.stream().filter(r -> r.getId() == 0).findFirst().orElse(null);

        if (currentRule.getSubRules() != null) {
            Rule finalCurrentRule = currentRule;
            currentRule.getSubRules().forEach(pair -> {
                if (pair.size() == 0) System.out.println(finalCurrentRule);
                if (pair.size() == 0) System.out.println(finalCurrentRule.getSubRules());
                Rule rr  = pair.get(0);
                List<String> newValues = allValidStrings(rules, rr, currentString);
                if (pair.size() > 1) {
                    List<Rule> newPair = new ArrayList<>(pair);
                    //System.out.println(newPair.remove(rr));
                    //System.out.printf("[%s] -> [%s]%n", pair.stream().map(Rule::getId).map(String::valueOf).collect(Collectors.joining(", ")), newPair.stream().map(Rule::getId).map(String::valueOf).collect(Collectors.joining(", ")));
                    newPair.forEach(rrr -> {
                        List<String> newNewValues = new ArrayList<>();
                        newValues.forEach(nv -> {
                            newNewValues.addAll(allValidStrings(rules, rrr, nv));
                        });
                        newValues.clear();
                        newValues.addAll(newNewValues);
                    });
                }
                values.addAll(newValues);
            });
        } else {
            values.add(currentString + currentRule.getSequence());
        }

        //System.out.println(values);
        return values;
    }
}
