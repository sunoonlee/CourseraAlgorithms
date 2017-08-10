/**
 * Coursera Algorithms assg4: 8 puzzle
 * http://coursera.cs.princeton.edu/algs4/assignments/8puzzle.html
 */

package assg4;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * find the solution to 8-puzzle problem
 * using A* search algorithm and priority queue
 */
public class Solver {
    private final Stack<Board> sequence = new Stack<Board>();
    private final boolean solvable;
    private final int minMoves;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        MinPQ<Node> pq = new MinPQ<Node>();
        MinPQ<Node> pqTwin = new MinPQ<Node>();

        Node initNode = new Node(initial, null, 0);
        Node initNodeTwin = new Node(initial.twin(), null, 0);
        pq.insert(initNode);
        pqTwin.insert(initNodeTwin);

        Node curr, currTwin;
        while (true) {
            curr = pq.delMin();
            currTwin = pqTwin.delMin();

            if (curr.board.isGoal()) {
                solvable = true;
                break;
            }
            if (currTwin.board.isGoal()) {
                solvable = false;
                break;
            }

            // insert all neighbors
            for (Board b: curr.board.neighbors()) {
                if (curr.prev != null && b.equals(curr.prev.board)) continue;
                pq.insert(new Node(b, curr, curr.moves + 1));
            }

            for (Board b: currTwin.board.neighbors()) {
                if (currTwin.prev != null && b.equals(currTwin.prev.board)) continue;
                pqTwin.insert(new Node(b, currTwin, curr.moves + 1));
            }
        }

        if (solvable) {
            minMoves = curr.moves;
            for (Node node = curr; node != null; node = node.prev) {
                sequence.push(node.board);
            }
        }
        else {
            minMoves = -1;
        }
    }

    private class Node implements Comparable<Node> {
        private Board board;
        private Node prev;
        private int moves;
        private int priority;

        public Node(Board board, Node prev, int moves) {
            this.board = board;
            this.prev = prev;
            this.moves = moves;
            this.priority = manhattanPriority();
        }

        int manhattanPriority() { return board.manhattan() + moves; }

        public int compareTo(Node w) {
            if (this.priority < w.priority)
                return -1;
            else if (this.priority == w.priority)
                return 0;
            else return 1;
        }
    }

    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return minMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return sequence;
    }

    // solve a slider puzzle
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // int[][] a = {{2, 0, 3}, {1, 5, 6}, {4, 7, 8}};
        // Board initial = new Board(a);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
