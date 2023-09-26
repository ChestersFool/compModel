package org.example;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static final double eps = 0.0001;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        double from = 0;
        double k0;
        double[] x = new double[10];
        double[] y = new double[10];
        double[] h = new double[4];
        x[0] = 1.;
        y[0] = 1.;

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
        // search h opt = min(h) -> recalculate x1,x2,y1,y2

        // ГБРС
        double hOpt = h[2];
        int n = (int) (1 / hOpt);

        produceJAN3(from, x, y, hOpt);

        double[] xCorrect = calculateCorrectX(n, hOpt, from);
        double[] yCorrect = calculateCorrectY(n, hOpt, from);

        System.out.println("x: " + Arrays.toString(x));
        System.out.println("y: " + Arrays.toString(y));
        System.out.println("xCorrect: " + Arrays.toString(xCorrect));
        System.out.println("yCorrect: " + Arrays.toString(yCorrect));
        System.out.print("points: ");
        for (int i = 0; i < n; i++) {
            System.out.print((from + i * hOpt) + ", ");
        }
    }

    private static void produceNRK21(double from, double k0, double[] x, double[] y, double[] h) {
        double pointCur = from;

        // yn+1 = yn + h/2 * t2(t2 + t1)
        // t1 = f(tn+1, xn+1, yn+1)
        // t2 = f(tn, yn+1 - h * t1)
        for (int i = 0; i < 3; i++) {
//            System.out.println(h[i]);
            double pointNext = pointCur + h[i];
            double xi1k = x[i];
            double yi1k = y[i];
            double xi1k1 = 0; // init
            double yi1k1 = 0; // init
            int j = 0;

            while (j < k0) {
                double t1 = xFuncDerivative(pointNext, xi1k, yi1k);
                double t2 = xFuncDerivative(pointCur, xi1k - h[i] * t1, yi1k - h[i] * t1);
                double l1 = yFuncDerivative(pointNext, xi1k, yi1k);
                double l2 = yFuncDerivative(pointCur, xi1k - h[i] * l1, yi1k - h[i] * l1);

//                System.out.println(t1 + " " + t2 + " " + l1 + " " + l2);

                xi1k1 = x[i] + h[i] / 2 * t2 * (t1 + t2); // x1k+1 = ...
                yi1k1 = y[i] + h[i] / 2 * l2 * (l1 + l2);

//                System.out.println(i);
//                System.out.println("xi1k1: " + xi1k1 + "; yi1k1: " + yi1k1);
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

            x[i + 1] = xi1k1;
            y[i + 1] = yi1k1;
            h[i + 1] = h[i];
            pointCur = pointNext;
        }
    }

    public static void produceJAN3(double from, double[] x, double[] y, double hOpt) {
        double pointCur = from + 3 * hOpt;
        int n = (int) (1 / hOpt);

        for (int i = 3; i < n; i++) {
            double pointNext = pointCur + hOpt;
            // yn+3 = yn+1 + h/3 * (7 * f(xn+1, yn+1) - 2 * f(xn, yn) + f(xn-1, yn-1))
            x[i] = x[i - 2] + hOpt / 3 * (7 * xFuncDerivative(pointNext, x[i - 1], y[i - 1]) -
                    2 * xFuncDerivative(pointCur, x[i - 2], y[i - 2]) +
                    xFuncDerivative(pointCur, x[i - 3], y[i - 3]));
            y[i] = y[i - 2] + hOpt / 3 * (7 * yFuncDerivative(pointNext, x[i - 1], y[i - 1]) -
                    2 * yFuncDerivative(pointCur, x[i - 2], y[i - 2]) +
                    yFuncDerivative(pointCur, x[i - 3], y[i - 3]));
            pointCur = pointNext;
        }
    }

    public static double xFuncDerivative(double t, double x, double y) {
        return t / y;
    }

    public static double yFuncDerivative(double t, double x, double y) {
        return t / x;
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