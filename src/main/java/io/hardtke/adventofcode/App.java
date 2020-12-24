package io.hardtke.adventofcode;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.hardtke.adventofcode.days.*;

/**
 * Hello world!
 *
 */
public class App {

    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        //new Day_01();
        //new Day_02();
        //new Day_03();
        //new Day_04();
        //new Day_05();
        //new Day_06();
        //new Day_07();
        //new Day_08();
        //new Day_09();
        //new Day_10();
        //new Day_11();
        //new Day_12();
        //new Day_13();
        //new Day_14();
        //new Day_15();
        //new Day_16();
        //new Day_17();
        //new Day_18();
        new Day_19();

        System.out.printf("Time: %dms", System.currentTimeMillis() - startTime);

    }
}
