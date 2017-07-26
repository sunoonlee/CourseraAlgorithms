/**
 * Coursera Algorithms assg4: 8 puzzle
 * http://coursera.cs.princeton.edu/algs4/assignments/8puzzle.html
 */

package assg4;
import edu.princeton.cs.algs4.Stack;

/**
 * n x n board for 8-puzzle problem
 */
public class Board {

    private final int[][] blocks;
    private final int n;

    /**
     * construct a board from an n-by-n array of blocks
     * where blocks[i][j] = block in row i, column j
     */
    public Board(int[][] blocks)
    {
        this.blocks = copy2D(blocks);
        n = blocks.length;
    }

    // helper function for copying 2d arrays
    private int[][] copy2D(int[][] a) {
        int nrows = a.length;
        int ncols = a[0].length;
        int[][] copy = new int[nrows][ncols];
        for (int i = 0; i < nrows; i++)
            for (int j = 0; j < ncols; j++)
                copy[i][j] = a[i][j];
        return copy;
    }

    public int dimension() {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        int result = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (blocks[i][j] != 0 && blocks[i][j] != i * n + j + 1)
                    result += 1;
        return result;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int result = 0;
        int iGoal, jGoal;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0) continue;
                iGoal = (blocks[i][j] - 1) / n;
                jGoal = (blocks[i][j] - 1) % n;
                result += Math.abs(iGoal - i) + Math.abs(jGoal - j);
            }
        }
        return result;
    }

    public boolean isGoal() {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (blocks[i][j] != 0 && blocks[i][j] != i * n + j + 1)
                    return false;
        return true;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] blocksTwin = copy2D(blocks);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocksTwin[i][j] == 1) blocksTwin[i][j] = 2;
                else if (blocksTwin[i][j] == 2) blocksTwin[i][j] = 1;
            }
        }
        return new Board(blocksTwin);
    }

    public boolean equals(Board y) {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (blocks[i][j] != y.blocks[i][j])
                    return false;
        return true;
    }

    private int[][] swap(int[][] a, int i, int j, String direction) {
        int[][] swapped = copy2D(a);
        int temp;
        if (direction == "up") {
            temp = swapped[i-1][j];
            swapped[i-1][j] = swapped[i][j];
        }
        else if (direction == "down") {
            temp = swapped[i+1][j];
            swapped[i+1][j] = swapped[i][j];
        }
        else if (direction == "left") {
            temp = swapped[i][j-1];
            swapped[i][j-1] = swapped[i][j];
        }
        else if (direction == "right") {
            temp = swapped[i][j+1];
            swapped[i][j+1] = swapped[i][j];
        }
        else throw new IllegalArgumentException("Invalid value for direction: " + direction);
        swapped[i][j] = temp;
        return swapped;
    }

    // all neighboring boards
    public Iterable<Board> neighbors()
    {
        // locate the blank position
        int iBlank = -1;
        int jBlank = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0) {
                    iBlank = i;
                    jBlank = j;
                    break;
                }
            }
        }

        Stack<Board> stack = new Stack<Board>();
        if (iBlank == -1) return stack;

        if (iBlank > 0)
            stack.push(new Board(swap(blocks, iBlank, jBlank, "up")));
        if (iBlank < n-1)
            stack.push(new Board(swap(blocks, iBlank, jBlank, "down")));
        if (jBlank > 0)
            stack.push(new Board(swap(blocks, iBlank, jBlank, "left")));
        if (jBlank < n-1)
            stack.push(new Board(swap(blocks, iBlank, jBlank, "right")));

        return stack;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        /*
        int[][] a = {{0, 1, 3}, {4, 2, 5}, {6, 8, 7}};
        Board bd = new Board(a);
        System.out.println(bd.toString());

        System.out.println("** neighbors:");
        for (Board b: bd.neighbors()) {
            System.out.println(b.toString());
        }

        System.out.println("** twin:");
        System.out.println(bd.twin());

        assert bd.isGoal() == false;
        assert bd.hamming() == 5;
        assert bd.manhattan() == 8;
        */
    }
}

