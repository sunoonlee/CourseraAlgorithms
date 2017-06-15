package com.algorithms.assg1;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/*
 * http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 * `PercolationStats` is for estimating the value of the percolation threshold
 * via Monte Carlo simulation
 */

public class PercolationStats {

    private int trials;
    private double[] fractions;  // results of experiments

    public PercolationStats(int n, int trials) {
        // perform trials independent experiments on an n-by-n grid
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
        this.trials = trials;
        fractions = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation pe = new Percolation(n);

            while (!pe.percolates()) {
                int rowRand = StdRandom.uniform(1, n+1);
                int colRand = StdRandom.uniform(1, n+1);
                if (!pe.isOpen(rowRand, colRand)) pe.open(rowRand, colRand);
            }
            fractions[i] = (double) pe.numberOfOpenSites() / n / n;
            // System.out.println(fractions[i]);
        }
    }
    public double mean() {
        // sample mean of percolation threshold
        return StdStats.mean(fractions);
    }
    public double stddev() {
        // sample standard deviation of percolation threshold
        return StdStats.stddev(fractions);
    }
    public double confidenceLo() {
        // low  endpoint of 95% confidence interval
        return mean() - 1.96 * stddev() / Math.sqrt(trials);
    }
    public double confidenceHi() {
        // high endpoint of 95% confidence interval
        return mean() + 1.96 * stddev() / Math.sqrt(trials);
    }

    public static void main(String[] args) {
        // test client (described below)
        int n = 10;
        int trials = 10;

        if (args.length > 1) {
            n = Integer.parseInt(args[0]);
            trials = Integer.parseInt(args[1]);
        }

        PercolationStats ps = new PercolationStats(n, trials);

        System.out.println("mean                    = " + ps.mean());
        System.out.println("stddev                  = " + ps.stddev());
        System.out.println("95% confidence interval = [" + ps.confidenceLo()
                           + ", " + ps.confidenceHi() + "]");
    }
}

