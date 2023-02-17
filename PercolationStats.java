import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] vals;
    private int T;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
        vals = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation model = new Percolation(n);
            while (!model.percolates()) {
                model.open(StdRandom.uniformInt(1, n + 1), StdRandom.uniformInt(1, n + 1));
            }
            vals[i] = (double) model.numberOfOpenSites() / (n * n);
        }
        T = trials;
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(vals);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(vals);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (1.96 * stddev()) / Math.sqrt(T);
    }


    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (1.96 * stddev()) / Math.sqrt(T);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(n, trials);
        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}
