package org.example;

public class Lab4 {

    public static double correctFunc(double y, double t, double h, double eps) {
        double pow = t / eps;
        if (pow < 10) {
            return t - eps + (1 + eps) * Math.exp(-pow);
        }
        return t - eps;
    }

    public static double func(double y, double t, double h, double eps, double sigma) {
        return t / (eps * sigma / h - 1 / 2.) + y;
    }

    public static double sigma(double ro) {
        return (ro * ctg(ro / 2.)) / 2.;
    }

    public static double ctg(double x) {
        return 1. / Math.tan(x);
    }

    public static void main(String[] args) {
        double eps = 0.01;
        calc(eps, eps, false);
    }

    public static void calc(double eps, double h, boolean toCalcSigma) {
        double t = 0;
        int n = (int) (1 / h) + 1;
        double[] y = new double[n];
        y[0] = 1;
        double sigma;

        if (toCalcSigma) {
            sigma = sigma(h / eps);
        } else {
            sigma = 1;
        }

        System.out.println("y\t\tcorr\tdelta\tt");
        for (int i = 1; i < n; i++) {
            t += h;
            y[i] = func(y[i - 1], t, h, eps, sigma);
            double correct = correctFunc(y[i - 1], t, h, eps);

            System.out.println(String.format("%.3f", y[i]) + "\t" +
                    String.format("%.3f", correct) + "\t" +
                    String.format("%.3f", (y[i] - correct)) + "\t" +
                    String.format("%.3f", t));
        }
    }
}
