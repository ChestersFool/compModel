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
        y[0] = 0.;

        System.out.println("Enter h0, k0: ");
        h[0] = sc.nextDouble();
        k0 = sc.nextDouble(); // 15

        // h opt

        // t start 0 to 1
        // ti = t0 + i * h opt

        // find 1,2
//        System.out.println(h0);
//        System.out.println(k0);

        // xn+1 = xn + h/2 * T2(T1+T2)
        // yn+1 = yn + h/2 * L2(L1+L2)
        // T1 = fn+1 = f (tn+1, xn+1, yn+1) = 2x+1 ....
        // T2 = f(tn,xn+1-h*T1, yn+1 - h*T1) = 2....

        // L1 = gn+1 = x!! + 2....
        // L2 = g(tn,xn+1-h*L1, yn+1 - h*L1)

        // search x1,x2

        double pointCur = 0;
        double pointNext = pointCur + h[0];

        for (int i = 0; i < 2; i++) {

            do {
                double xi1k = x[i];
                double yi1k = y[i];
                double t1 = xFuncDerivative(pointNext, xi1k, yi1k);
                double t2 = xFuncDerivative(pointCur, xi1k - h[i] * t1, yi1k - h[i] * t1);

                double l1 = yFuncDerivative(pointNext, xi1k, yi1k);
                double l2 = yFuncDerivative(pointCur, xi1k - h[i] * t1, yi1k - h[i] * t1);

                double xi1k1 = x[i] + h[i] / 2 * t2 * (t1 + t2); // x1k+1 = ...
                double yi1k1 = y[i] + h[i] / 2 * l2 *(l1 + l2);
            } while (); //|x1k+1 - x1k| + |y1k+1-y1k| <= eps then x1 = x1k1+1 && k < k0


            h[i + 1] = h[i];
            // IF |x1k+1 - x1k| + |y1k+1-y1k| <= eps then x1 = x1k1+1
            // else k = k+1
            // if k0 iterr -> hi/2

            // заст метод простої ітерації
            // Нехай х1 = х0, у1 = у0 або х0 + хаш * ф0
        }

        // search h opt = min(h) -> recalculate x1,x2,y1,y2

        // ГБРС
        // xn+3 = xn+1 + hopt/3 ( 7 * fn+2 - 2 fn+1 + fn)
    }

    public static double xFuncCorrect(double t) {
        return Math.sin(t) + 1;
    }

    public static double yFuncCorrect(double t) {
        return Math.pow(t, 2);
    }

    public static double xFuncDerivative(double t, double x, double y) {
        return 2 * x - y + Math.pow(t, 2) - 2 * (Math.sin(t) + 1) + Math.cos(t);
    }

    public static double yFuncDerivative(double t, double x, double y) {
        return x + 2 * y - Math.sin(t) - 2 * Math.pow(t, 2) + 2 * t - 1;
    }
}