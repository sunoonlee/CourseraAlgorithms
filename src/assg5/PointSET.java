/**
 * Coursera Algorithms assg5: Kd-trees
 * http://coursera.cs.princeton.edu/algs4/assignments/kdtree.html
 */

package assg5;
import java.util.TreeSet;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

/**
 * PointSET: represents a set of points in the unit square
 * a brute-force implementation to support 2d range search and nearest-neighbor search
 * 一种支持 2d 范围查找和近邻查找的暴力解法
 */
public class PointSET {
    private TreeSet<Point2D> set;

    public PointSET() { set = new TreeSet<Point2D>(); }
    public boolean isEmpty() { return set.size() == 0; }
    public int size() { return set.size(); }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("called insert() with a null point");
        set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("called contains() with a null point");
        return set.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p: set)
            p.draw();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("called range() with a null rectangle");
        Queue<Point2D> q = new Queue<Point2D>();
        double xmin = rect.xmin();
        double ymin = rect.ymin();
        double xmax = rect.xmax();
        double ymax = rect.ymax();
        for (Point2D p: set) {
            if (p.x() >= xmin && p.x() <= xmax && p.y() >= ymin && p.y() <= ymax) {
                q.enqueue(p);
            }
        }
        return q;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("called nearest() with a null point");
        Point2D result = null;
        double minSqDist = 0.0;
        for (Point2D pt: set) {
            double sqDist = p.distanceSquaredTo(pt);
            if (result == null || sqDist < minSqDist) {
                result = pt;
                minSqDist = sqDist;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Point2D p1 = new Point2D(0.1, 0.1);
        Point2D p2 = new Point2D(0.5, 0.5);
        Point2D p3 = new Point2D(1.0, 0.5);
        Point2D p4 = new Point2D(0.8, 0.4);

        PointSET pset = new PointSET();
        pset.insert(p1);
        pset.insert(p2);
        pset.insert(p3);

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        pset.draw();
        StdDraw.show();

        assert pset.size() == 3;
        assert pset.contains(p1);
        assert !pset.contains(p4);
        assert pset.nearest(p4).equals(p3);
    }
}