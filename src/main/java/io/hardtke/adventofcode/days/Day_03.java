package io.hardtke.adventofcode.days;

import java.awt.*;

public class Day_03 extends Day {

    @Override
    public void calculate() {
        System.out.println(countTrees(1, 1));
        System.out.println(countTrees(3, 1));
        System.out.println(countTrees(5, 1));
        System.out.println(countTrees(7, 1));
        System.out.println(countTrees(1, 2));
    }

    private int countTrees(int dx, int dy) {
        int trees = 0;
        Point location = new Point(0, 0);
        for (int i = 0; i < getLines().size(); i++) {
            if (location.y >= getLines().size()) break;
            if (getLines().get(location.y).toCharArray()[location.x] == '#') trees++;
            move(location, dx, dy);
        }
        return trees;
    }

    private void move(Point p, int dx, int dy) {
        p.translate(dx, dy);
        if (p.x >= getLines().get(0).length()) p.translate(- getLines().get(0).length(), 0);
    }

}
