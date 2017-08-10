/**
 * Coursera Algorithms assg5: Kd-trees
 * http://coursera.cs.princeton.edu/algs4/assignments/kdtree.html
 */

package assg5;
import edu.princeton.cs.algs4.*;

/**
 * KdTree: a 2d-tree to implement 2d range search and nearest-neighbor search
 */
public class KdTree {
    private Node root;
    private class Node {
        private Point2D p;
        private RectHV rect;
        private Node lb;  // left/bottom
        private Node rt;  // right/top
        Node(Point2D p) {
            this.p = p;
        }
    }
    private int size = 0;

    public KdTree() {}
    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("called insert() with a null point");
        if (root == null) {
            root = new Node(p);
            root.rect = new RectHV(0.0, 0.0, 1.0, 1.0);
            size = 1;
        }
        else root = insert(root, p, true);
    }

    private Node insert(Node x, Point2D p, boolean useX) {
        if (x == null) {
            size += 1;
            return new Node(p);
        }

        int cmp = useX ? ((Double) p.x()).compareTo((Double) x.p.x())
                       : ((Double) p.y()).compareTo((Double) x.p.y());

        if (cmp < 0) {
            x.lb = insert(x.lb, p, !useX);
            if (x.lb.rect == null) x.lb.rect = subRect(x.rect, x.p, useX, true);
        }
        else {
            if (!(cmp == 0 && x.p.equals(p))) {
                x.rt = insert(x.rt, p, !useX);
                if (x.rt.rect == null) x.rt.rect = subRect(x.rect, x.p, useX, false);
            }
        }
        return x;
    }

    private RectHV subRect(RectHV r, Point2D p, boolean useX, boolean less) {
        if (useX) {
            if (less) return new RectHV(r.xmin(), r.ymin(), p.x(), r.ymax());
            else return new RectHV(p.x(), r.ymin(), r.xmax(), r.ymax());
        }
        else {
            if (less) return new RectHV(r.xmin(), r.ymin(), r.xmax(), p.y());
            else return new RectHV(r.xmin(), p.y(), r.xmax(), r.ymax());
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("called insert() with a null point");
        return contains(root, p, true);
    }

    private boolean contains(Node x, Point2D p, boolean useX) {
        if (x == null) return false;
        int cmp = useX ? ((Double) p.x()).compareTo((Double) x.p.x())
                       : ((Double) p.y()).compareTo((Double) x.p.y());

        if (cmp < 0) return contains(x.lb, p, !useX);
        else if (cmp == 0) {
            if (p.equals(x.p)) return true;
            else return contains(x.rt, p, !useX);
        }
        else return contains(x.rt, p, !useX);
    }

    public void draw() { draw(root, true); }
    private void draw(Node x, boolean useX) {
        if (x == null) return;
        draw(x.lb, !useX);
        draw(x.rt, !useX);

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.p.draw();

        if (useX) StdDraw.setPenColor(StdDraw.RED);
        else StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius();
        subRect(x.rect, x.p, useX, true).draw();
        // 用这句取代 x.rect.draw(), 目的是当 x 为叶节点时能画出由 x.p 划分出的两个子矩阵
   }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> q = new Queue<Point2D>();
        range(root, rect, q);
        return q;
    }
    private void range(Node x, RectHV rect, Queue<Point2D> q) {
        if (rect == null) throw new IllegalArgumentException("called range() with a null rectangle");
        if (x == null) return;
        if (rect.contains(x.p)) q.enqueue(x.p);
        if (x.lb != null && rect.intersects(x.lb.rect)) range(x.lb, rect, q);
        if (x.rt != null && rect.intersects(x.rt.rect)) range(x.rt, rect, q);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("called nearest() with a null point");
        if (root == null) return null;
        return nearest(root, p, root.p, true);
    }

    private Point2D nearest(Node x, Point2D p, Point2D result, boolean useX) {
        if (x == null) return result;
        if (x.p.distanceSquaredTo(p) < result.distanceSquaredTo(p)) result = x.p;

        int cmp = useX ? ((Double) p.x()).compareTo((Double) x.p.x())
                       : ((Double) p.y()).compareTo((Double) x.p.y());

        if (x.rect.distanceSquaredTo(p) < result.distanceSquaredTo(p)) {
            // cmp < 0 时优先搜索 lb, 否则优先搜索 rt
            if (cmp < 0) {
                result = nearest(x.lb, p, result, !useX);
                result = nearest(x.rt, p, result, !useX);
            } else {
                result = nearest(x.rt, p, result, !useX);
                result = nearest(x.lb, p, result, !useX);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Point2D p1 = new Point2D(0.5, 0.5);
        Point2D p2 = new Point2D(0.1, 0.1);
        Point2D p3 = new Point2D(0.9, 0.7);
        Point2D p4 = new Point2D(0.8, 0.4);
        Point2D p5 = new Point2D(0.3, 0.3);
        Point2D p6 = new Point2D(0.8, 0.7);

        KdTree t = new KdTree();
        t.insert(p1);
        t.insert(p2);
        t.insert(p3);
        t.insert(p4);
        t.insert(p5);

        StdDraw.clear();
        t.draw();
        StdDraw.show();

        assert t.size() == 5;
        assert t.contains(p1);
        assert !t.contains(p6);
        System.out.println(t.nearest(p6));

        RectHV r = new RectHV(0.6, 0.3, 1.0, 1.0);
        Iterable<Point2D> pts = t.range(r);
        for (Point2D p: pts) System.out.println(p);
    }
}