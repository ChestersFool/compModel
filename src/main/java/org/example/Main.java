package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static final double eps = 0.00001;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        double from = 0;
        double k0;
        ArrayList<Double> x = new ArrayList<>(20);
        ArrayList<Double> y = new ArrayList<>(20);
//        double[] x = new double[1000];
//        double[] y = new double[1000];
        double[] h = new double[4];
        x.add(1.);
        y.add(1.);
//        x[0] = 1.;
//        y[0] = 1.;

        System.out.println("Enter step, k0: ");
        h[0] = sc.nextDouble();
        k0 = sc.nextDouble();

        if (k0 <= 0) {
            k0 = 5;
        }
        if (h[0] <= 0) {
            h[0] = 0.1;
        }

        produceNRK21(from, k0, x, y, h);
        double hOpt = h[2];

        while (h[0] != hOpt) {
//            System.out.println("h0,h1,h2: " + h[0] + " " + h[1] + " " + h[2]);
//            System.out.println("hOpt: " + hOpt);
//            System.out.println("RETRY");
            h[0] = h[2];
            produceNRK21(from, k0, x, y, h);
        }

        // ГБРС
        System.out.println("hOpt: " + hOpt);
        int n = (int) (1 / hOpt);

        produceJAN3(from, x, y, hOpt);

        double[] xCorrect = calculateCorrectX(n, hOpt, from);
        double[] yCorrect = calculateCorrectY(n, hOpt, from);

        System.out.println("x: " + x);
        System.out.println("y: " + y);
        System.out.println("xCorrect: " + Arrays.toString(xCorrect));
        System.out.println("yCorrect: " + Arrays.toString(yCorrect));
        System.out.print("points: ");
        for (int i = 0; i < n; i++) {
            System.out.print((from + i * hOpt) + ", ");
        }
    }

    private static void produceNRK21(double from, double k0, ArrayList<Double> x, ArrayList<Double> y, double[] h) {
        double pointCur = from;

        double temp = x.get(0);
        x.clear();
        x.add(temp);

        temp = y.get(0);
        y.clear();
        y.add(temp);

        // yn+1 = yn + h/2 * t2(t2 + t1)
        // t1 = f(tn+1, xn+1, yn+1)
        // t2 = f(tn, yn+1 - h * t1)
        for (int i = 0; i < 3; i++) {
//            System.out.println(h[i]);
            double pointNext = pointCur + h[i];
            double xi1k = x.get(i);
            double yi1k = y.get(i);
            double xi1k1 = 0; // init
            double yi1k1 = 0; // init
            int j = 0;

            while (j < k0) {
                double t1 = xFuncDerivative(pointNext, xi1k, yi1k);
                double t2 = xFuncDerivative(pointCur, xi1k - h[i] * t1, yi1k - h[i] * t1);
                double l1 = yFuncDerivative(pointNext, xi1k, yi1k);
                double l2 = yFuncDerivative(pointCur, xi1k - h[i] * l1, yi1k - h[i] * l1);

//                System.out.println(t1 + " " + t2 + " " + l1 + " " + l2);

                xi1k1 = x.get(i) + h[i] / 2 * t2 * (t1 + t2); // x1k+1 = ...
                yi1k1 = y.get(i) + h[i] / 2 * l2 * (l1 + l2);

//                System.out.println(j);
//                System.out.println("xi1k1: " + xi1k1 + "; yi1k1: " + yi1k1);
//                System.out.println("xi1k: " + xi1k + "; yi1k: " + yi1k);
//                System.out.println("xi1k1c: " + xFuncCorrect(pointNext) + "; yi1k1c: " + yFuncCorrect(pointNext));

                if (Math.abs(xi1k1 - xi1k) + Math.abs(yi1k1 - yi1k) <= eps) {
                    break;
                }

                xi1k = xi1k1;
                yi1k = yi1k1;
                j++;
            }

            if (j == k0) {
                h[i] /= 2;
                i--;
                continue;
            }

            x.add(xi1k1);
            y.add(yi1k1);
//            x[i + 1] = xi1k1;
//            y[i + 1] = yi1k1;
            h[i + 1] = h[i];
            pointCur = pointNext;
        }
    }

    public static void produceJAN3(double from, ArrayList<Double> x, ArrayList<Double> y, double hOpt) {
        double pointCur = from + 3 * hOpt;
        int n = (int) (1 / hOpt);

        for (int i = 3; i < n; i++) {
            double pointNext = pointCur + hOpt;
            // yn+3 = yn+1 + h/3 * (7 * f(xn+1, yn+1) - 2 * f(xn, yn) + f(xn-1, yn-1))
            double xi = x.get(i - 2) + hOpt / 3 * (7 * xFuncDerivative(pointNext, x.get(i - 1), y.get(i - 1)) -
                    2 * xFuncDerivative(pointCur, x.get(i - 2), y.get(i - 2)) +
                    xFuncDerivative(pointCur, x.get(i - 3), y.get(i - 3)));
            x.add(xi);
            double yi = y.get(i - 2) + hOpt / 3 * (7 * yFuncDerivative(pointNext, x.get(i - 1), y.get(i - 1)) -
                    2 * yFuncDerivative(pointCur, x.get(i - 2), y.get(i - 2)) +
                    yFuncDerivative(pointCur, x.get(i - 3), y.get(i - 3)));
            y.add(yi);
            pointCur = pointNext;
        }
    }

    public static double xFuncDerivative(double t, double x, double y) {
        return t / y;
    }

    public static double yFuncDerivative(double t, double x, double y) {
        return -(t / x);
    }

    public static double xFuncCorrect(double t) {
        return Math.exp(Math.pow(t, 2) / 2);
    }

    public static double yFuncCorrect(double t) {
        return Math.exp(-(Math.pow(t, 2) / 2));
    }

    private static double[] calculateCorrectX(int n, double h, double from) {
        double[] xCorrect = new double[n];

        for (int i = 0; i < n; i++) {
            xCorrect[i] = xFuncCorrect(i * h + from);
        }

        return xCorrect;
    }

    private static double[] calculateCorrectY(int n, double h, double from) {
        double[] yCorrect = new double[n];

        for (int i = 0; i < n; i++) {
            yCorrect[i] = yFuncCorrect(i * h + from);
        }

        return yCorrect;
    }
}