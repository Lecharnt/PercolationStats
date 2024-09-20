
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int gridSize;
    private final int rowColSize;
    private final WeightedQuickUnionUF quickFind;
    private final int topVirtualSite;
    private final int bottomVirtualSite;
    private final boolean[] isOpenSites;
    private final WeightedQuickUnionUF quickFind2;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("must be bigger than 0");
        }
        this.rowColSize = n;
        this.gridSize = n * n;
        this.quickFind = new WeightedQuickUnionUF(gridSize + 2);
        this.quickFind2 = new WeightedQuickUnionUF(gridSize + 1);
        this.topVirtualSite = gridSize;
        this.bottomVirtualSite = gridSize + 1;
        this.isOpenSites = new boolean[gridSize];
    }

    public void open(int row, int col) {
        proveIndex(row, col);
        if (isOpenSites[getIndex(row, col)]) return;

        isOpenSites[getIndex(row, col)] = true;

        if (row == 0) {
            quickFind.union(getIndex(row, col), topVirtualSite);
            quickFind2.union(getIndex(row, col), topVirtualSite);
        }
        if (row == rowColSize - 1) {
            quickFind.union(getIndex(row, col), bottomVirtualSite);
        }
        if (row > 0 && isOpen(row - 1, col)) {
            quickFind.union(getIndex(row, col), getIndex(row - 1, col));
            quickFind2.union(getIndex(row, col), getIndex(row - 1, col));
        }
        if (row < rowColSize - 1 && isOpen(row + 1, col)) {
            quickFind.union(getIndex(row, col), getIndex(row + 1, col));
            quickFind2.union(getIndex(row, col), getIndex(row + 1, col));
        }
        if (col > 0 && isOpen(row, col - 1)) {
            quickFind.union(getIndex(row, col), getIndex(row, col - 1));
            quickFind2.union(getIndex(row, col), getIndex(row, col - 1));
        }
        if (col < rowColSize - 1 && isOpen(row, col + 1)) {
            quickFind.union(getIndex(row, col), getIndex(row, col + 1));
            quickFind2.union(getIndex(row, col), getIndex(row, col + 1));
        }
    }

    public boolean isOpen(int row, int col) {
        proveIndex(row, col);
        return isOpenSites[getIndex(row, col)];
    }

    public boolean percolates() {
        return quickFind.connected(topVirtualSite, bottomVirtualSite);
    }

    public boolean isFull(int row, int col) {
        proveIndex(row, col);
        int index = getIndex(row, col);
        return quickFind2.connected(index, topVirtualSite);
    }

    public int numberOfOpenSites() {
        int count = 0;
        for (boolean isOpen : isOpenSites) {
            if (isOpen) count++;
        }
        return count;
    }

    private int getIndex(int row, int col) {
        return row * rowColSize + col;
    }

    private void proveIndex(int row, int col) {
        if (row < 0 || row >= rowColSize || col < 0 || col >= rowColSize) {
            throw new IllegalArgumentException("out of bounds");
        }
    }


    public static void main(String[] args) {
    }
}
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    double totNum;
    int totSum;
    int trials;
    int size;
    Percolation percolation;
    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials){
        if (n<=0 || trials <=0){
            throw new IllegalArgumentException("out of bounds");
        }
        this.trials = trials;
        for (int i = 1; i <= trials; i++) {
            percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int x = StdRandom.uniform(0, n);
                int y = StdRandom.uniform(0, n);
                percolation.open(x,y);
            }
            totNum += ((double) percolation.numberOfOpenSites() / (n*n));
        }
    }

    // sample mean of percolation threshold
    public double mean(){
        return (double) totNum / trials;
    }

    // sample standard deviation of percolation threshold
    public double stddev(){
        return Math.sqrt((totSum / trials) - Math.pow(mean(), 2));
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow(){
        double marginOfError = 1.96 * stddev() / Math.sqrt(trials);
        return mean() - marginOfError;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh(){
        double marginOfError = 1.96 * stddev() / Math.sqrt(trials);
        return mean() + marginOfError;
    }

    // test client (see below)
    public static void main(String[] args){
    }
}
