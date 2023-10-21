package org.example;

import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;

public class Lab2 extends JFrame {
    private double fi1(double etta) {
        return 3 * Math.cos(etta) - 3 * Math.cos(3 * etta);
    }

    private double fi2(double etta) {
        return 3 * Math.sin(etta) - 3 * Math.sin(3 * etta);
    }

    private double fi3(double etta) {
        return 7 * Math.cos(2 * etta) - 2 * Math.cos(etta) + 1;
    }

    private double fi4(double etta) {
        return 7 * Math.sin(2 * etta) - 2 * Math.sin(etta);
    }

    private double u(double etta) {
        return (fi1(etta) * fi3(etta) + fi2(etta) * fi4(etta)) /
                (Math.pow(fi3(etta), 2) + Math.pow(fi4(etta), 2));
    }

    private double v(double etta) {
        return (fi2(etta) * fi3(etta) - fi1(etta) * fi4(etta)) /
                (Math.pow(fi3(etta), 2) + Math.pow(fi4(etta), 2));
    }

    public void calculate() {
        int n = 1000;
        double h = 2 * Math.PI / n;

        for (int i = 0; i < n; i++) {
            ettaArray.add(i * h);
            uArray.add(u(ettaArray.get(i)));
            vArray.add(v(ettaArray.get(i)));
            System.out.println(ettaArray.get(i) + " " + uArray.get(i) + " " + vArray.get(i));

            double ro = 0 + (double) i / n;
            roArray.add((3 * (ro - Math.pow(ro, 3))) / (7 * Math.pow(ro, 2) - 2 * ro + 1));
        }
    }

    private ArrayList<Double> uArray = new ArrayList<>();
    private ArrayList<Double> vArray = new ArrayList<>();
    private ArrayList<Double> ettaArray = new ArrayList<>();
    private ArrayList<Double> roArray = new ArrayList<>();

    Lab2() {
        super("Lines Drawing Demo");

        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        calculate();
    }

    void drawLines(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawLine(this.getWidth() / 2, 0, this.getWidth() / 2, 500);
        g2d.drawLine(0, this.getHeight() / 2, 500, this.getHeight() / 2);

        int i = 0;

        for (Double aDouble : ettaArray) {
            int x = (int) (uArray.get(i) * 200 + this.getWidth() / 2);
            int y = (int) (vArray.get(i) * 200 + this.getHeight() / 2);
            System.out.println(x + " " + y);
            g2d.setPaint(Color.BLACK);
            g2d.fillOval(x - 1, y - 1, 3, 3);

            x = (int) (roArray.get(i) * 200 + this.getWidth() / 2);
            g2d.setPaint(Color.RED);
            g2d.fillOval(x - 1, this.getHeight() / 2 - 1, 3,  3);

            i++;
        }
    }

    public void paint(Graphics g) {
        drawLines(g);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Lab2().setVisible(true));
    }
}
