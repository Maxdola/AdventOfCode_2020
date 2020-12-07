package io.hardtke;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.hardtke.days.*;

/**
 * Hello world!
 *
 */
public class App {

    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) {

        //new Day_01();
        //new Day_02();
        //new Day_03();
        //new Day_04();
        //new Day_05();
        //new Day_06();
        new Day_07();

    }
}
