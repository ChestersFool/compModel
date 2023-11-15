package org.example;

import java.util.Scanner;

public class Lab4 {

    public static double func(double y, double t, double h, double eps, double beleberda) {
        return y + t / (eps * beleberda / h + 1 / 2.);
    }

    public static double correctFunc(double y, double t, double h, double eps) {
        double pow = -t / eps;
        if (pow < 10) {
            return t - eps + (1 + eps) * Math.exp(pow);
        }
        return t - eps;
    }

    public static double beleberda(double beleberda2) {
        return (beleberda2 * (1 / Math.tan(beleberda2 / 2))) / 2;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double t = 0;

        System.out.println("Enter eps, h: ");
        double eps = sc.nextDouble();
        double h = sc.nextDouble();
        int n = (int) (1 / h) + 1;
        double[] y = new double[n];
        y[0] = 1;
        double beleberda2 = h / eps;

        System.out.println("y\t\tcorr\tdelta\tt");
        for (int i = 1; i < n; i++) {
            t += h;
            y[i] = func(y[i - 1], t, h, eps, beleberda2);

            System.out.println(String.format("%.3f", y[i]) + "\t" +
                    String.format("%.3f", correctFunc(y[i - 1], t, h, eps)) + "\t" +
                    String.format("%.3f", (y[i] - correctFunc(y[i - 1], t, h, eps))) + "\t" +
                    String.format("%.3f", t));
        }
    }
}
