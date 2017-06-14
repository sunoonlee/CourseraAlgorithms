package com.algorithms.assg1;
/*
 * http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int n;
    private int numOpenSites;
    private boolean[][] sitesOpen;  // open or not
    private WeightedQuickUnionUF uf;

    public Percolation(int n) {
        // create n-by-n grid, with all sites blocked
        this.n = n;
        sitesOpen = new boolean[n][n];
        numOpenSites = 0;
        uf = new WeightedQuickUnionUF(n*n + 2);  // first and last: virtual-top, virtual-bottom
    }

    public void open(int row, int col) {
        // open site (row, col) if it is not open already
        if (!isOpen(row, col)) {
            sitesOpen[row - 1][col - 1] = true;
            numOpenSites += 1;
            int id = (row - 1) * n + col;  // index in uf

            // connect to neighboring sites
            if (row > 1 && isOpen(row - 1, col)) uf.union(id, id - n);
            if (row < n && isOpen(row + 1, col)) uf.union(id, id + n);
            if (col > 1 && isOpen(row, col - 1)) uf.union(id, id - 1);
            if (col < n && isOpen(row, col + 1)) uf.union(id, id + 1);

            // connect to virtual-top and virtual-bottom
            if (row == 1) uf.union(id, 0);
            if (row == n) uf.union(id, n * n + 1);
        }
    }

    public boolean isOpen(int row, int col)  {
        return sitesOpen[row-1][col-1];
    }

    public boolean isFull(int row, int col)  {
        int id = (row - 1) * n + col;  // index in uf
        return uf.connected(id, 0);
    }

    public int numberOfOpenSites() {
        return numOpenSites;
    }

    public boolean percolates() {
        return uf.connected(0, n*n + 1);
    }
}
