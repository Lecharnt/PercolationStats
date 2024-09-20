import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.util.HashSet;
import java.util.Set;

public class Percolation {
    private final int top = 0;
    private int bottom;
    private final int size;
    private final WeightedQuickUnionUF quickFind;
    private final boolean[][] opend;
    private int openPlaces;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("Grid size must be greater than 0");
        this.size = n;
        this.quickFind = new WeightedQuickUnionUF(n * n + 2);
        this.bottomIndex = n * n + 1;
        opend = new boolean[size][size];
        openPlaces = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validateIndices(row, col);
        gridObject site = new gridObject(row, col);
        if (openedObjects.contains(site)) return; // already open

        int index = getIndex(row, col);
        openedObjects.add(site);

        // Connect to adjacent open sites
        if (row == 1) {
            quickFind.union(index, topVirtualIndex);
        }
        if (row == size) {
            quickFind.union(index, bottomVirtualIndex);
        }

        if (row > 0 && isOpen(row - 1, col)) {
            quickFind.union(index, getIndex(row - 1, col));
        }
        if (row < size - 1 && isOpen(row + 1, col)) {
            quickFind.union(index, getIndex(row + 1, col));
        }
        if (col > 0 && isOpen(row, col - 1)) {
            quickFind.union(index, getIndex(row, col - 1));
        }
        if (col < size - 1 && isOpen(row, col + 1)) {
            quickFind.union(index, getIndex(row, col + 1));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateIndices(row, col);
        return openedObjects.contains(new gridObject(row, col));
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validateIndices(row, col);
        int index = getIndex(row, col);
        return quickFind.connected(index, topVirtualIndex);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openedObjects.size();
    }

    // does the system percolate?
    public boolean percolates() {
        return quickFind.connected(topVirtualIndex, bottomVirtualIndex);
    }

    // unit testing (required)
    public static void main(String[] args) {
        // Test cases can be added here.
    }

    private int getIndex(int row, int col) {
        return size*(row-1) + col;
    }

    private void validateIndices(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IllegalArgumentException("Index out of bounds");
        }
    }
}
