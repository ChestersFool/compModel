package org.example;

import java.util.Scanner;

public class Main {

    public static final double eps = 0.0001;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        double k0;
        double[] x = new double[100];
        double[] y = new double[100];
        double[] h = new double[100];
        double[] xCorrect = new double[100];
        double[] yCorrect = new double[100];
        double[] xDelta = new double[100];
        double[] yDelta = new double[100];
        double[] pointsOfT = new double[100];
        x[0] = 1.;
        y[0] = 1.;

        System.out.println("Enter step, k0: ");
        h[0] = sc.nextDouble();
        k0 = sc.nextDouble();

        if (k0 <= 0) {
            k0 = 5;
        }
        // h opt

        // t start 0 to 1
        // ti = t0 + i * h opt

        // find 1,2

        // xn+1 = xn + h/2 * T2(T1+T2)
        // yn+1 = yn + h/2 * L2(L1+L2)
        // T1 = fn+1 = f (tn+1, xn+1, yn+1) = 2x+1 ....
        // T2 = f(tn,xn+1-h*T1, yn+1 - h*T1) = 2....

        // L1 = gn+1 = x!! + 2....
        // L2 = g(tn,xn+1-h*L1, yn+1 - h*L1)

        // search x1,x2

        double pointCur = 0;

        for (int i = 0; i < 2; i++) {
//            System.out.println(h[i]);
            double pointNext = pointCur + h[i];
            double xi1k = x[i];
            double yi1k = y[i];
            double xi1k1 = 0;
            double yi1k1 = 0;
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
                System.out.println("xi1k1: " + xi1k1 + "; yi1k1: " + yi1k1);
                System.out.println("xi1k1c: " + xFuncCorrect(pointNext) + "; yi1k1c: " + yFuncCorrect(pointNext));

                if (Math.abs(xi1k1 - xi1k) + Math.abs(yi1k1 - yi1k) <= eps) {
                    break;
                }

                xi1k = xi1k1;
                yi1k = yi1k1;

                j++;
            } //while (Math.abs()); //|x1k+1 - x1k| + |y1k+1-y1k| <= eps then x1 = x1k1+1 && k < k0

            if (j == k0) {
                h[i] /= 2;
                i--;
                continue;
            }

            x[i + 1] = xi1k1;
            y[i + 1] = yi1k1;
            h[i + 1] = h[i];
            pointCur = pointNext;
            /// IF |x1k+1 - x1k| + |y1k+1-y1k| <= eps then x1 = x1k1+1
            /// else k = k+1
            /// if k0 iterr -> hi/2

            /// заст метод простої ітерації
            /// Нехай х1 = х0, у1 = у0 або х0 + хаш * ф0
        }

        // search h opt = min(h) -> recalculate x1,x2,y1,y2

        // ГБРС
        // xn+3 = xn+1 + hopt/3 ( 7 * fn+2 - 2 fn+1 + fn)
    }

    public static double xFuncCorrect(double t) {
        return Math.exp(Math.pow(t, 2) / 2);
    }

    public static double yFuncCorrect(double t) {
        return Math.exp(-(Math.pow(t, 2) / 2));
    }

    public static double xFuncDerivative(double t, double x, double y) {
        return t / y;
    }

    public static double yFuncDerivative(double t, double x, double y) {
        return t / x;
    }
}