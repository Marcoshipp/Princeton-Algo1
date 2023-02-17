import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // creates n-by-n grid, with all sites initially blocked
    private WeightedQuickUnionUF grid;
    private WeightedQuickUnionUF grid2;
    private int size;
    private boolean opened[][];
    private int virtualTop;
    private int virtualBot;
    private int opensites;

    public Percolation(int n) {
        // 0 is virtual top
        // n * n + 1 is virtual bottom
        if (n <= 0) throw new IllegalArgumentException();
        grid = new WeightedQuickUnionUF(n * n + 2);
        // no backwash
        grid2 = new WeightedQuickUnionUF(n * n + 1);
        size = n;
        virtualTop = 0;
        virtualBot = n * n + 1;
        opensites = 0;
        opened = new boolean[n][n];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                opened[i][j] = false;
            }
        }
    }

    private int indexOf(int row, int col) {
        if (((row < 1) || (row > size)) || ((col < 1) || (col > size))) throw new IllegalArgumentException();
        // 1 to 25
        return (row - 1) * size + col;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (((row < 1) || (row > size)) || ((col < 1) || (col > size))) throw new IllegalArgumentException();
        if (isOpen(row, col)) return;
        opened[row - 1][col - 1] = true;
        opensites++;
        // if the position is in the first row, connect it with the virtual top site
        if (row == 1) {
            // union with top virtual site
            grid.union(col, virtualTop);
            grid2.union(col, virtualTop);
            int i = indexOf(row, col);
            // union with left site if it's open, also check if left exists
            if (col != 1) {
                if (isOpen(row, col - 1)) {
                    grid.union(i, i - 1);
                    grid2.union(i, i - 1);
                }
            }
            // union with right site if it's open, also check if right exists
            if (col != size) {
                if (isOpen(row, col + 1)) {
                    grid.union(i, i + 1);
                    grid2.union(i, i + 1);
                }
            }
            // union with bottom site if it's open (done)
            if (isOpen(row + 1, col)) {
                grid.union(i, i + size);
                grid2.union(i, i + size);
            }
        }
        // if the position is in the last row, connect it with the virtual bottom site
        else if (row == size) {
            // union with top virtual site
            int i = indexOf(row, col);
            grid.union(i, virtualBot);
            // union with left site if it's open, also check if left exists
            if (col != 1) {
                if (isOpen(row, col - 1)) {
                    grid.union(i, i - 1);
                    grid2.union(i, i - 1);
                }
            }
            // union with right site if it's open, also check if right exists
            if (col != size) {
                if (isOpen(row, col + 1)) {
                    grid.union(i, i + 1);
                    grid2.union(i, i + 1);
                }
            }
            // union with top site if it's open (done)
            if (isOpen(row - 1, col)) {
                grid.union(i, i - size);
                grid2.union(i, i - size);
            }
        } else {
            int i = indexOf(row, col);
            // union with left site if it's open, also check if left exists
            if (col != 1) {
                if (isOpen(row, col - 1)) {
                    grid.union(i, i - 1);
                    grid2.union(i, i - 1);
                }
            }
            // union with right site if it's open, also check if right exists
            if (col != size) {
                if (isOpen(row, col + 1)) {
                    grid.union(i, i + 1);
                    grid2.union(i, i + 1);
                }
            }
            // connect with top if it's open
            if (isOpen(row - 1, col)) {
                grid.union(i, i - size);
                grid2.union(i, i - size);
            }
            // connect with bottom if it's open
            if (isOpen(row + 1, col)) {
                grid.union(i, i + size);
                grid2.union(i, i + size);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if ((row < 1 || row > size) || (col < 1 || col > size)) throw new IllegalArgumentException();
        return opened[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        // if any of the adjacent node is connected to virtualTop
        if ((row < 1 || row > size) || (col < 1 || col > size)) throw new IllegalArgumentException();
        int i = indexOf(row, col);
        return isOpen(row, col) && grid2.find(i) == grid2.find(virtualTop);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return opensites;
    }

    // does the system percolate?
    public boolean percolates() {
        return grid.find(virtualBot) == grid.find(virtualTop);
    }
}
