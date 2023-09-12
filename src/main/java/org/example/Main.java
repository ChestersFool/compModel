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
        x[0] = 1.;
        y[0] = 0.;

        System.out.println("Enter h0, k0: ");
        h[0] = sc.nextDouble();
        k0 = sc.nextDouble();

//        System.out.println(h0);
//        System.out.println(k0);
    }

    public static double xFunc(double t) {
        return Math.sin(t) + 1;
    }

    public static double yFunc(double t) {
        return Math.pow(t, 2);
    }

    public static double xFuncDerivative(double t) {
        return 2 * xFunc(t) - yFunc(t) + Math.pow(t, 2) - 2 * (Math.sin(t) + 1) + Math.cos(t);
    }

    public static double yFuncDerivative(double t) {
        return xFunc(t) + 2 * yFunc(t) - Math.sin(t) - 2 * Math.pow(t, 2) + 2 * t - 1;
    }
}