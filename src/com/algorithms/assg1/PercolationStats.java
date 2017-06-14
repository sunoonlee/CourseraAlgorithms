package com.algorithms.assg1;

/**
 * Created by lim on 14/06/2017.
 */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import com.algorithms.assg1.*;

public class PercolationStats {

    private int n;
    private int trials;
    private double[] fractions;  // results of experiments

    public PercolationStats(int n, int trials) {
        // perform trials independent experiments on an n-by-n grid
        this.n = n;
        this.trials = trials;
        fractions = new double[trials];

        for (int i=0; i<trials; i++) {
            Percolation pe = new Percolation(n);

            while (!pe.percolates()) {
                int row_rand = StdRandom.uniform(1, n+1);
                int col_rand = StdRandom.uniform(1, n+1);
                if(!pe.isOpen(row_rand, col_rand)) pe.open(row_rand, col_rand);
            }
            fractions[i] = (double) pe.numberOfOpenSites() / n / n;
            System.out.println(fractions[i]);
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
        PercolationStats ps = new PercolationStats(3, 5);
    }
}

