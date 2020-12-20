package io.hardtke.adventofcode.days;

import java.awt.geom.Dimension2D;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class Day_17 extends Day {

    private interface ComputeAction<T> {
        T compute(T o);
    }

    private static class Point4D implements Cloneable {
        private int x, y, z, w;

        public Point4D(int x, int y, int z, int w) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
        }

        public Point4D add(int x, int y, int z, int w) {
            this.x = this.x + x;
            this.y = this.y + y;
            this.z = this.z + z;
            this.w = this.w + w;
            return this;
        }

        public Point4D add(Point4D p) {
            this.x = this.x + p.x;
            this.y = this.y + p.y;
            this.z = this.z + p.z;
            this.w = this.w + p.w;
            return this;
        }

        public int getY() {
            return y;
        }

        public int getX() {
            return x;
        }

        public int getZ() {
            return z;
        }

        public int getW() {
            return w;
        }

        public Point3D to3D() {
            return new Point3D(this.getX(), this.getY(), this.getZ());
        }

        public Point2D to2D() {
            return new Point2D(this.getX(), this.getY());
        }

        public List<Point4D> getSurrounding() {
            List<Point4D> points = new ArrayList<>();
            this.to3D().getSurrounding().forEach(p2 -> {
                points.add(p2.to4D(this.getW() - 1));
                points.add(p2.to4D(this.getW()));
                points.add(p2.to4D(this.getW() + 1));
            });
            points.add(this.clone().add(0,0,0,1));
            points.add(this.clone().add(0,0,0,-1));
            return points;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point4D point4D = (Point4D) o;
            return x == point4D.x &&
                    y == point4D.y &&
                    z == point4D.z &&
                    w == point4D.w;
        }

        @Override
        public Point4D clone() {
            return new Point4D(getX(), getY(), getZ(), getW());
        }

        @Override
        public String toString() {
            return "{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    ", w=" + w +
                    '}';
        }
    }

    private static class Point3D implements Cloneable {
        private int x, y, z;

        public Point3D(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Point3D add(int x, int y, int z) {
            this.x = this.x + x;
            this.y = this.y + y;
            this.z = this.z + z;
            return this;
        }

        public Point3D add(Point3D p) {
            this.x = this.x + p.x;
            this.y = this.y + p.y;
            this.z = this.z + p.z;
            return this;
        }

        public int getY() {
            return y;
        }

        public int getX() {
            return x;
        }

        public int getZ() {
            return z;
        }

        public Point2D to2D() {
            return new Point2D(this.getX(), this.getY());
        }

        public Point4D to4D(int w) {
            return new Point4D(this.getX(), this.getY(), this.getZ(), w);
        }

        public List<Point3D> getSurrounding() {
            List<Point3D> points = new ArrayList<>();
            this.to2D().getSurrounding().forEach(p2 -> {
                points.add(p2.to3D(this.getZ() - 1));
                points.add(p2.to3D(this.getZ()));
                points.add(p2.to3D(this.getZ() + 1));
            });
            points.add(this.clone().add(0,0,1));
            points.add(this.clone().add(0,0,-1));
            return points;
        }

        @Override
        public Point3D clone() {
            return new Point3D(getX(), getY(), getZ());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point3D point3D = (Point3D) o;
            return x == point3D.x &&
                    y == point3D.y &&
                    z == point3D.z;
        }

        @Override
        public String toString() {
            return "{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    '}';
        }
    }

    private static class Point2D implements Cloneable{
        private static final List<Point2D> surroundingOffset = Arrays.asList(
                new Point2D(-1, 1),     new Point2D(0, 1),     new Point2D(1, 1),
                new Point2D(-1, 0),                                  new Point2D(1, 0),
                new Point2D(-1, -1),    new Point2D(0, -1),    new Point2D(1, -1));
        private int x, y;

        public Point2D(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void add(int x, int y) {
            this.x = this.x + x;
            this.y = this.y + y;
        }

        public void add(Point2D p) {
            this.x = this.x + p.x;
            this.y = this.y + p.y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public List<Point2D> getSurrounding() {
            List<Point2D> points = new ArrayList<>();
            surroundingOffset.forEach(o -> {
                points.add(new Point2D(this.getX() + o.getX(), this.getY() + o.getY()));
            });
            return points;
        }

        public Point3D to3D(int z) {
            return new Point3D(this.getX(), this.getY(), z);
        }

        @Override
        public Point2D clone() {
            return new Point2D(getX(), getY());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point2D point2D = (Point2D) o;
            return x == point2D.x &&
                    y == point2D.y;
        }

        @Override
        public String toString() {
            return "{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    private static class Dimension4D {

        private Map<Integer, Dimension> dimension = new HashMap<>();

        public boolean isActive(Point4D p) {
            if (dimension.get(p.getW()) != null) {
                return dimension.get(p.getW()).isActive(p.to3D());
            }
            return false;
        }

        public void setPoint(Point4D p, boolean value) {
            this.dimension.compute(p.getW(), (k, d) -> {
                if (d == null) d = new Dimension(k);
                d.setPoint(p.to3D(), value);
                return d;
            });
        }

        public IntSummaryStatistics getMinMax() {
            return this.dimension.keySet().stream().mapToInt(Integer::intValue).summaryStatistics();
        }

        public void print() {
            if (dimension.size() == 0) {
                System.out.println("The dimension is Empty.");
                return;
            }
            IntSummaryStatistics minMax = getMinMax();
            IntSummaryStatistics dimensionMinMax = getDimensionMinMax();

            for (int i = minMax.getMin(); i < minMax.getMax() + 1; i++) {
                Dimension d = this.dimension.get(i);

                for (int j = dimensionMinMax.getMin(); j < dimensionMinMax.getMax() + 1; j++) {
                    System.out.println();
                    System.out.printf("z=%d w=%d%n", j, i);
                    if (d != null && d.dimension.get(j) != null) {
                        IntSummaryStatistics gridMinMax = d.getGridMinMax();
                        d.dimension.get(j).print(gridMinMax);
                    } else {
                        System.out.println("Layer not found");
                    }
                }
            }
        }

        public int countActive() {
            return this.dimension.values().stream().mapToInt(Dimension::countActive).sum();
        }

        public int countActiveAround(Point4D p) {
            AtomicInteger i = new AtomicInteger();
            p.getSurrounding().forEach(pp -> {
                if (isActive(pp)) i.getAndIncrement();
            });
            return i.get();
        }

        public IntSummaryStatistics getDimensionMinMax() {
            IntSummaryStatistics minMax = new IntSummaryStatistics();

            this.dimension.values().stream().map(Dimension::getMinMax).forEach(is -> {
                if (is.getMax() != Integer.MIN_VALUE) minMax.accept(is.getMax());
                if (is.getMin() != Integer.MAX_VALUE) minMax.accept(is.getMin());
            });

            //System.out.println(minMax);

            return minMax;
        }

        public IntSummaryStatistics getDimensionGridMinMax() {
            IntSummaryStatistics minMax = new IntSummaryStatistics();

            this.dimension.values().stream().map(Dimension::getGridMinMax).forEach(is -> {
                if (is.getMax() != Integer.MIN_VALUE) minMax.accept(is.getMax());
                if (is.getMin() != Integer.MAX_VALUE) minMax.accept(is.getMin());
            });

            //System.out.println(minMax);

            return minMax;
        }

        public Iterator<Dimension> iterator() {
            return new Iterator<>() {

                private int min = getMinMax().getMin() - 1;
                private int max = getMinMax().getMax() + 2;
                private int current = min;

                @Override
                public boolean hasNext() {
                    return max > current;
                }

                @Override
                public Dimension next() {
                    Dimension g = dimension.get(current);
                    if (g == null) {
                        g = new Dimension(current);
                        dimension.put(current, g);
                    }
                    current++;
                    return g;
                }
            };
        }

        public void forEach(Consumer<? super Dimension> action) {
            this.iterator().forEachRemaining(action);
        }

        public void compute(ComputeAction<Dimension> action) {
            Map<Integer, Dimension> dimension = new HashMap<>();
            this.iterator().forEachRemaining(g -> {
                dimension.put(g.getIndex(), action.compute(g));
            });
            this.dimension = dimension;
        }

    }

    private static class Dimension implements Cloneable {

        private int index;
        private Map<Integer, Grid> dimension = new HashMap<>();

        private Dimension(int index) {
            this.index = index;
        }

        public boolean isActive(Point3D p) {
            if (dimension.get(p.getZ()) != null) {
                return dimension.get(p.getZ()).isActive(p.to2D());
            }
            return false;
        }

        public void setPoint(Point3D p, boolean value) {
            this.dimension.compute(p.getZ(), (k, g) -> {
                if(g == null) g = new Grid(k);
                g.setPoint(p.to2D(), value);
                return g;
            });
        }

        public IntSummaryStatistics getMinMax() {
            return this.dimension.keySet().stream().mapToInt(Integer::intValue).summaryStatistics();
        }

        public void print() {
            if (dimension.size() == 0) {
                System.out.println("The dimension is Empty.");
                return;
            }
            IntSummaryStatistics minMax = getMinMax();
            IntSummaryStatistics gridMinMax = getGridMinMax();

            for (int i = minMax.getMin(); i < minMax.getMax() + 1; i++) {
                System.out.println();
                System.out.printf("z=%d%n", i);
                Grid g = this.dimension.get(i);
                if (g != null) {
                    g.print(gridMinMax);
                } else {
                    System.out.println("Layer not found");
                }
            }
        }

        public void print(IntSummaryStatistics minMax, IntSummaryStatistics gridMinMax) {
            if (dimension.size() == 0) {
                System.out.println("The dimension is Empty.");
                return;
            }
            for (int i = minMax.getMin(); i < minMax.getMax() + 1; i++) {
                System.out.println();
                System.out.printf("z=%d%n", i);
                Grid g = this.dimension.get(i);
                if (g != null) {
                    g.print(gridMinMax);
                } else {
                    System.out.println("Layer not found");
                }
            }
        }

        public void printHorizontal() {
            if (dimension.size() == 0) {
                System.out.println("The dimension is Empty.");
                return;
            }
            IntSummaryStatistics minMax = getMinMax();
            IntSummaryStatistics gridMinMax = getMinMax();
            int min = minMax.getMin();
            int max = minMax.getMax();

            Map<Integer, String> lines = new TreeMap<>();

            for (int i = min; i < max + 1; i++) {
                //System.out.println();
                //System.out.printf("z=%d%n", i);
                Grid g = this.dimension.get(i);
                if (g != null) {
                    g.printToList(gridMinMax).forEach((k, v) -> {
                        lines.compute(k, (kk,vv) -> {
                            if (vv == null) vv = "";
                            vv += v + "\t";
                            return vv;
                        });
                    });
                } else {
                    System.out.println("Layer not found");
                }
            }

            lines.forEach((k,v) -> {
                System.out.println(v);
            });

        }

        public int countActiveAround(Point3D p) {
            AtomicInteger i = new AtomicInteger();
            p.getSurrounding().forEach(pp -> {
                if (isActive(pp)) i.getAndIncrement();
            });
            return i.get();
        }

        public Iterator<Grid> iterator(IntSummaryStatistics minMax) {
            return new Iterator<>() {

                private int min = minMax.getMin() - 1;
                private int max = minMax.getMax() + 2;
                private int current = min;

                @Override
                public boolean hasNext() {
                    return max > current;
                }

                @Override
                public Grid next() {
                    Grid g = dimension.get(current);
                    if (g == null) {
                        g = new Grid(current);
                        dimension.put(current, g);
                    }
                    current++;
                    return g;
                }
            };
        }

        public int countActive() {
            return this.dimension.values().stream().mapToInt(Grid::countActive).sum();
        }

        public IntSummaryStatistics getGridMinMax() {
            IntSummaryStatistics minMax = new IntSummaryStatistics();

            this.dimension.values().stream().map(Grid::getMinMax).forEach(is -> {
                minMax.accept(is.getMax());
                minMax.accept(is.getMin());
            });

            //System.out.println(minMax);

            return minMax;
        }

        public void forEach(IntSummaryStatistics minMax, Consumer<? super Grid> action) {
            this.iterator(minMax).forEachRemaining(action);
        }

        public void compute(IntSummaryStatistics minMax, ComputeAction<Grid> action) {
            Map<Integer, Grid> dimension = new HashMap<>();
            this.iterator(minMax).forEachRemaining(g -> {
                dimension.put(g.getIndex(), action.compute(g));
            });
            this.dimension = dimension;
        }

        public int getIndex() {
            return index;
        }

        public Dimension clone() {
            Map<Integer, Grid> dimension = new HashMap<>();
            this.dimension.forEach((k, v) -> dimension.put(k, v.clone()));
            Dimension dd = new Dimension(this.getIndex());
            dd.dimension = dimension;
            return dd;
        }
    }

    private static class Grid implements Cloneable {

        private int index;
        private Map<Integer, Map<Integer, Boolean>> grid = new HashMap<>();

        private Grid(int index) {
            this.index = index;
            this.grid.put(0, new HashMap<>());
        }

        public boolean isActive(Point2D p) {
            if (grid.get(p.getX()) != null) {
                return grid.get(p.getX()).getOrDefault(p.getY(), false);
            }
            return false;
        }

        public void setPoint(Point2D p, boolean value) {
            //System.out.printf("Setting %s -> %b%n", p, value);
            grid.compute(p.getX(), (k, m) -> {
                if (m == null) m = new HashMap<>();
                m.put(p.getY(), value);
                return m;
            });
        }

        public IntSummaryStatistics getMinMax() {
            IntSummaryStatistics minMax = new IntSummaryStatistics(0,0,0,0);
            IntSummaryStatistics xMinMax = this.grid.keySet().stream()/*.flatMap(k -> this.grid.get(k).keySet().stream())*/.mapToInt(Integer::intValue).summaryStatistics();
            IntSummaryStatistics yMinMax = this.grid.keySet().stream().filter(k -> this.grid.get(k).size() > 0).flatMap(k -> this.grid.get(k).keySet().stream()).mapToInt(Integer::intValue).summaryStatistics();

            if (xMinMax.getMin() != Integer.MAX_VALUE && xMinMax.getMax() != Integer.MIN_VALUE) {
                minMax.accept(xMinMax.getMin());
                minMax.accept(xMinMax.getMax());
            }
            if (yMinMax.getMin() != Integer.MAX_VALUE && yMinMax.getMax() != Integer.MIN_VALUE) {
                minMax.accept(yMinMax.getMin());
                minMax.accept(yMinMax.getMax());
            }

/*            System.out.println(this.getIndex());
            System.out.println(xMinMax);
            System.out.println(yMinMax);
            System.out.println(minMax);*/

            return minMax;
        }

        public void print(IntSummaryStatistics minMax) {
            if (grid.size() > 0) {

                StringBuilder sb =  new StringBuilder();
                for (int i = minMax.getMin(); i < minMax.getMax() + 1; i++) {

                    for (int j = minMax.getMin(); j < minMax.getMax() + 1; j++) {
                        sb.append(this.isActive(new Point2D(i, j)) ? "#" : ".").append(" ");
                    }

                    System.out.println(sb.toString());
                    sb.setLength(0);
                }
            }
        }

        public Map<Integer, String> printToList(IntSummaryStatistics minMax) {
            Map<Integer, String> lines = new HashMap<>();
            StringBuilder sb =  new StringBuilder();
            lines.put(0, "i=" + this.getIndex() + "\t\t");
            for (int i = minMax.getMin(); i < minMax.getMax() + 1; i++) {

                for (int j = minMax.getMin(); j < minMax.getMax() + 1; j++) {
                    sb.append(this.isActive(new Point2D(i, j)) ? "#" : ".").append(" ");
                }

                lines.put(i+1, sb.toString());
                sb.setLength(0);
            }
            return lines;
        }

        public int countActiveAround(Point2D p, boolean includePoint) {
            AtomicInteger i = new AtomicInteger();
            p.getSurrounding().forEach(pp -> {
                if (isActive(pp)) i.getAndIncrement();
            });
            if (includePoint && isActive(p)) i.getAndIncrement();
            return i.get();
        }

        public int countActive() {
            AtomicInteger i = new AtomicInteger();
            this.forEach(getMinMax(), p -> {
                if (isActive(p)) i.getAndIncrement();
            });
            return i.get();
        }

        public Iterator<Point2D> iterator(IntSummaryStatistics minMax) {
            return new Iterator<>() {

                private int min = minMax.getMin() - 1;
                private int max = minMax.getMax() + 2;
                private int currentX = min;
                private int currentY = min;

                @Override
                public boolean hasNext() {
                    return max > currentX && currentY < max;
                }

                @Override
                public Point2D next() {
                    Point2D p = new Point2D(currentX, currentY);
                    currentY++;
                    if (currentY >= max) {
                        currentY = min;
                        currentX++;
                    }
                    return p;
                }
            };
        }

        public void forEach(IntSummaryStatistics minMax, Consumer<? super Point2D> action) {
            this.iterator(minMax).forEachRemaining(action);
        }

        public int getIndex() {
            return index;
        }

        public Grid clone() {
            Grid g = new Grid(this.getIndex());
            this.grid.forEach((k, v) -> {
                g.grid.put(k, new HashMap<>(v));
            });
            return g;
        }
    }

    @Override
    public void calculate() {

        Dimension d = new Dimension(0);
        Dimension4D d4 = new Dimension4D();

        for (int i = 0; i < getLines().size(); i++) {
            String l = getLines().get(i);

            for (int j = 0; j < l.length(); j++) {

                if (l.charAt(j) == '#') d.setPoint(new Point3D(i, j, 0), true);
                if (l.charAt(j) == '#') d4.setPoint(new Point4D(i, j, 0, 0), true);

            }
        }

        d.print();

        //IntSummaryStatistics minMax = d.getGridMinMax();
        //System.out.println(minMax);

/*        d.forEach(g -> {
            System.out.println(g.getIndex());
            g.forEach(minMax,p -> {
                boolean active = d.isActive(p.to3D(g.getIndex()));
                int around = d.countActive(p.to3D(g.getIndex()));
                if (active) {
                    if (!(around == 3 | around == 2)) {
                        d.setPoint(p.to3D(g.getIndex()), false);
                    }
                } else {
                    if (around == 3) d.setPoint(p.to3D(g.getIndex()), true);
                }
            });
        });*/

        for (int i = 0; i < 6; i++) {
            IntSummaryStatistics minMax = d.getGridMinMax();
            d.compute(d.getMinMax(), grid -> {
                Grid g = grid.clone();
                grid.forEach(minMax,p -> {
                    boolean active = d.isActive(p.to3D(g.getIndex()));
                    int around = d.countActiveAround(p.to3D(g.getIndex()));
                    if (active) {
                        if (!(around == 3 || around == 2)) {
                            g.setPoint(p, false);
                        }
                    } else {
                        if (around == 3) g.setPoint(p, true);
                    }
                });
                return g;
            });

            IntSummaryStatistics dimGridMinMax = d4.getDimensionGridMinMax(); //TODO GridMinMax for all Dimensions
            IntSummaryStatistics dimMinMax = d4.getDimensionMinMax(); //TODO GridMinMax for all Dimensions
            d4.compute(dimension -> {
                Dimension dim = dimension.clone();
                dim.compute(dimMinMax, grid -> {
                    Grid g = grid.clone();
                    grid.forEach(dimGridMinMax, p -> {
                        //System.out.printf("\tChecking x=%2d y=%2d z=%2d w=%2d%n", p.getX(), p.getY(), g.getIndex(), dim.getIndex());
                        boolean active = d4.isActive(p.to3D(g.getIndex()).to4D(dim.getIndex()));
                        int around = d4.countActiveAround(p.to3D(g.getIndex()).to4D(dim.getIndex()));
                        if (active) {
                            if (!(around == 3 || around == 2)) {
                                //dim.setPoint(p.to3D(g.getIndex()), false);
                                g.setPoint(p, false);
                            }
                        } else {
                            //if (around == 3) dim.setPoint(p.to3D(g.getIndex()), true);
                            if (around == 3) g.setPoint(p, true);
                        }
                    });
                    return g;
                });

                return dim;
            });


            System.out.printf("Run %d: Active(3D): %3d | Active(4D): %3d%n", i+1, d.countActive(), d4.countActive());
        }

        //d.print();

/*
        Dimension dd = new Dimension();

        dd.setPoint(new Point3D(4,4,4), true);

        dd.print();

        IntSummaryStatistics minMax = dd.getGridMinMax();
        dd.compute(grid -> {
            Grid g = grid.clone();
            grid.forEach(minMax, p -> {
                if (grid.isActive(p)) {
                    p.getSurrounding().forEach(pp -> {
                        g.setPoint(pp, true);
                    });
                }
            });
            return g;
        });

        dd.print();
*/

        //d4.print();

    }

}
